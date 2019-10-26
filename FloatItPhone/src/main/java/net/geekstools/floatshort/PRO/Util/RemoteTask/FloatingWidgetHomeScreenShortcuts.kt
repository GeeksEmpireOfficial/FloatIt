package net.geekstools.floatshort.PRO.Util.RemoteTask

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable
import net.geekstools.floatshort.PRO.Widget.RoomDatabase.WidgetDataInterface
import net.geekstools.floatshort.PRO.Widget.WidgetsReallocationProcess

class FloatingWidgetHomeScreenShortcuts : Activity() {

    lateinit var functionsClass: FunctionsClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        functionsClass = FunctionsClass(applicationContext, this@FloatingWidgetHomeScreenShortcuts)

        if (!functionsClass.readPreference("WidgetsInformation", "Reallocated", true) && getDatabasePath(PublicVariable.WIDGET_DATA_DATABASE_NAME).exists()) {
            startActivity(Intent(applicationContext, WidgetsReallocationProcess::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                    ActivityOptions.makeCustomAnimation(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out).toBundle())

            this@FloatingWidgetHomeScreenShortcuts.finish()
            return
        }

        val packageName = intent.getStringExtra("PackageName")
        val providerClassName = intent.getStringExtra("ProviderClassName")
        val widgetLabel = intent.getStringExtra("ShortcutLabel")

        Thread(Runnable {
            val widgetDataInterface = Room.databaseBuilder(applicationContext, WidgetDataInterface::class.java, PublicVariable.WIDGET_DATA_DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(supportSQLiteDatabase: SupportSQLiteDatabase) {
                            super.onCreate(supportSQLiteDatabase)
                        }

                        override fun onOpen(supportSQLiteDatabase: SupportSQLiteDatabase) {
                            super.onOpen(supportSQLiteDatabase)

                        }
                    })
                    .build()
            val widgetDataModelsReallocation = widgetDataInterface.initDataAccessObject().loadWidgetByClassNameProviderWidget(packageName, providerClassName)
            val appWidgetId = widgetDataModelsReallocation.WidgetId

            runOnUiThread {
                functionsClass.runUnlimitedWidgetService(appWidgetId, widgetLabel)
            }

            widgetDataInterface.close()
        }).start()



        finish()
    }
}