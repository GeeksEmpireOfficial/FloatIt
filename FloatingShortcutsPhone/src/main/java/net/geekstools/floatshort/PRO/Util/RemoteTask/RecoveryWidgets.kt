package net.geekstools.floatshort.PRO.Util.RemoteTask

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import net.geekstools.floatshort.PRO.BindServices
import net.geekstools.floatshort.PRO.R
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons
import net.geekstools.floatshort.PRO.Widget.RoomDatabase.WidgetDataInterface
import net.geekstools.floatshort.PRO.Widget.RoomDatabase.WidgetDataModel

class RecoveryWidgets : Service() {

    lateinit var functionsClass: FunctionsClass

    var noRecovery = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread(Runnable {
            try {
                if (getDatabasePath(PublicVariable.WIDGET_DATA_DATABASE_NAME).exists()) {

                    if (functionsClass.loadCustomIcons()) {
                        val loadCustomIcons = LoadCustomIcons(applicationContext, functionsClass.customIconPackageName())
                        loadCustomIcons.load()
                        println("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIcons())
                    }

                    val widgetDataInterface: WidgetDataInterface = Room.databaseBuilder(applicationContext, WidgetDataInterface::class.java, PublicVariable.WIDGET_DATA_DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(object : RoomDatabase.Callback() {
                                override fun onCreate(sqLiteDatabase: SupportSQLiteDatabase) {
                                    super.onCreate(sqLiteDatabase)
                                }

                                override fun onOpen(sqLiteDatabase: SupportSQLiteDatabase) {
                                    super.onOpen(sqLiteDatabase)
                                }
                            })
                            .build()
                    val widgetDataModels: List<WidgetDataModel> = widgetDataInterface.initDataAccessObject().getAllWidgetData()

                    AllWidgetData@ for (widgetDataModel in widgetDataModels) {
                        if (widgetDataModel.Recovery) {
                            noRecovery = false

                            FloatingWidgetCheck@ for (floatingWidgetCheck in PublicVariable.FloatingWidgets) {
                                if (widgetDataModel.WidgetId == floatingWidgetCheck) {
                                    continue@AllWidgetData
                                }
                            }

                            try {
                                functionsClass.runUnlimitedWidgetService(widgetDataModel.WidgetId, widgetDataModel.WidgetLabel)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            noRecovery = true
                        }
                    }

                    if (noRecovery) {
                        Toast.makeText(applicationContext, getString(R.string.recoveryErrorWidget), Toast.LENGTH_LONG).show()
                    }

                    widgetDataInterface.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (PublicVariable.floatingCounter == 0) {
                    if (PreferenceManager.getDefaultSharedPreferences(applicationContext)
                                    .getBoolean("stable", true) == false) {
                        stopService(Intent(applicationContext, BindServices::class.java))
                    }
                }
            }
        }).start()

        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        functionsClass = FunctionsClass(applicationContext)
    }
}