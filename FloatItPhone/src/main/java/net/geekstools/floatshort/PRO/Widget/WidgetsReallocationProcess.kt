package net.geekstools.floatshort.PRO.Widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.ActivityOptions
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.android.synthetic.main.reallocating.*
import net.geekstools.floatshort.PRO.R
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClassDebug
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable
import net.geekstools.floatshort.PRO.Widget.RoomDatabase.WidgetDataInterface
import net.geekstools.floatshort.PRO.Widget.RoomDatabase.WidgetDataModel

class WidgetsReallocationProcess : Activity() {

    lateinit var functionsClass: FunctionsClass

    lateinit var appWidgetHost: AppWidgetHost

    lateinit var widgetDataModelsReallocation: List<WidgetDataModel>
    var REALLOCATION_COUNTER: Int = 0
    val WIDGETS_REALLOCATION_REQUEST: Int = 777

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reallocating)

        functionsClass = FunctionsClass(applicationContext, this@WidgetsReallocationProcess)

        window.statusBarColor = PublicVariable.primaryColor
        window.navigationBarColor = PublicVariable.primaryColor

        fullViewAllocating.setBackgroundColor(PublicVariable.primaryColor)
        allocatingProgress.setColor(PublicVariable.primaryColorOpposite)

        if (getDatabasePath(PublicVariable.WIDGET_DATA_DATABASE_NAME).exists()) {
            appWidgetHost = AppWidgetHost(getApplicationContext(), System.currentTimeMillis().toInt())

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
                widgetDataModelsReallocation = widgetDataInterface.initDataAccessObject().getAllWidgetData()

                if (widgetDataModelsReallocation.isNotEmpty()) {
                    runOnUiThread {
                        Handler().postDelayed({
                            if (REALLOCATION_COUNTER < widgetDataModelsReallocation.size) {
                                widgetsReallocationProcess(widgetDataModelsReallocation[REALLOCATION_COUNTER], appWidgetHost)
                            }
                        }, 333)
                    }
                } else {
                    this@WidgetsReallocationProcess.finish()
                }

                widgetDataInterface.close()
            }).start()

            widgetInformation.setOnClickListener {
                startActivity(Intent(applicationContext, WidgetsReallocationProcess::class.java),
                        ActivityOptions.makeCustomAnimation(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out).toBundle())
            }
        } else {
            this@WidgetsReallocationProcess.finish()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                WIDGETS_REALLOCATION_REQUEST -> {
                    FunctionsClassDebug.PrintDebug("*** WIDGETS_REALLOCATION_REQUEST | ${REALLOCATION_COUNTER} ***")

                    if (REALLOCATION_COUNTER < widgetDataModelsReallocation.size) {
                        Handler().postDelayed({
                            widgetsReallocationProcess(widgetDataModelsReallocation[REALLOCATION_COUNTER], appWidgetHost)
                        }, 333)
                    } else if (REALLOCATION_COUNTER >= widgetDataModelsReallocation.size) {
                        functionsClass.savePreference("WidgetsInformation", "Reallocated", true)

                        startActivity(Intent(applicationContext, WidgetConfigurations::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                                ActivityOptions.makeCustomAnimation(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out).toBundle())

                        this@WidgetsReallocationProcess.finish()
                    }
                }
            }
        }
    }

    private fun widgetsReallocationProcess(widgetDataModel: WidgetDataModel, appWidgetHost: AppWidgetHost) {
        val widgetId = appWidgetHost.allocateAppWidgetId()

        runOnUiThread {
            widgetInformation.setBackgroundColor(PublicVariable.colorLightDark)
            widgetInformation.alpha = 0.77F

            val valueAnimatorScaleWidgetInformation =
                    ValueAnimator.ofInt(
                            widgetInformation.width,
                            functionsClass.DpToInteger(51)
                    )
            valueAnimatorScaleWidgetInformation.duration = 1000
            valueAnimatorScaleWidgetInformation.addUpdateListener { animator ->
                widgetInformation.layoutParams.width = (animator.animatedValue as Int)
                widgetInformation.requestLayout()
                if ((animator.animatedValue as Int) < functionsClass.DpToInteger(300)) {
                    widgetInformation.text = null
                    widgetInformation.icon = null

                    Handler().postDelayed({

                        widgetInformation.icon = functionsClass.appIcon(widgetDataModel.PackageName)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            widgetInformation.text = Html.fromHtml("<big><b>" + widgetDataModel.AppName + "</b></big><br/>"
                                    + widgetDataModel.WidgetLabel + "<br/>"
                                    + "<small>" + getString(R.string.reallocatingWidgets) + "</small>", Html.FROM_HTML_MODE_LEGACY)
                        } else {
                            widgetInformation.text = Html.fromHtml("<big><b>" + widgetDataModel.AppName + "</b></big><br/>"
                                    + widgetDataModel.WidgetLabel + "<br/>"
                                    + "<small>" + getString(R.string.reallocatingWidgets) + "</small>")
                        }
                    }, 577)
                }
            }
            valueAnimatorScaleWidgetInformation.start()
            valueAnimatorScaleWidgetInformation.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    val valueAnimatorScaleWidgetInformationRevert =
                            ValueAnimator.ofInt(
                                    functionsClass.DpToInteger(51),
                                    functionsClass.DpToInteger(313)
                            )
                    valueAnimatorScaleWidgetInformationRevert.duration = 500
                    valueAnimatorScaleWidgetInformationRevert.addUpdateListener { animator ->
                        widgetInformation.layoutParams.width = (animator.animatedValue as Int)
                        widgetInformation.requestLayout()
                    }
                    valueAnimatorScaleWidgetInformationRevert.start()
                    valueAnimatorScaleWidgetInformationRevert.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {

                        }

                        override fun onAnimationEnd(animation: Animator?) {

                        }

                        override fun onAnimationCancel(animation: Animator?) {

                        }

                        override fun onAnimationStart(animation: Animator?) {

                        }
                    })
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {

                }
            })

            val provider = ComponentName.createRelative(widgetDataModel.PackageName, widgetDataModel.ClassNameProvider)
            FunctionsClassDebug.PrintDebug("*** ${widgetId}. Provider Widget = ${provider} ***")

            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, provider)
            startActivityForResult(intent, WIDGETS_REALLOCATION_REQUEST)

            if (widgetDataModel.ConfigClassName != null) {
                val configure = ComponentName.createRelative(widgetDataModel.PackageName, widgetDataModel.ConfigClassName!!)
                FunctionsClassDebug.PrintDebug("*** Configure Widget = $configure")

                val intentWidgetConfiguration = Intent()
                intentWidgetConfiguration.action = AppWidgetManager.ACTION_APPWIDGET_CONFIGURE
                intentWidgetConfiguration.component = configure
                intentWidgetConfiguration.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                intentWidgetConfiguration.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, provider)
                intentWidgetConfiguration.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                startActivityForResult(intentWidgetConfiguration, WIDGETS_REALLOCATION_REQUEST)
            }
        }

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
            val widgetDataDAO = widgetDataInterface.initDataAccessObject()
            widgetDataDAO.updateWidgetIdByPackageNameClassName(widgetDataModel.PackageName, widgetDataModel.ClassNameProvider, widgetId)
        }).start()

        REALLOCATION_COUNTER++
    }
}