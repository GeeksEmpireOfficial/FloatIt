package net.geekstools.floatshort.PRO.Util.RemoteTask

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import net.geekstools.floatshort.PRO.R
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable

class RecoveryWidgetActivity : Activity() {

    lateinit var functionsClass: FunctionsClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        functionsClass = FunctionsClass(applicationContext, this@RecoveryWidgetActivity)

        try {
            if (intent.action == Intent.ACTION_CREATE_SHORTCUT) {
                val shapeTempDrawable = functionsClass.shapesDrawables()
                shapeTempDrawable?.setTint(PublicVariable.primaryColor)
                val drawWidget = getDrawable(R.drawable.draw_widget_widgets) as LayerDrawable
                drawWidget.setDrawableByLayerId(R.id.backtemp, shapeTempDrawable)
                val shortcutApp = Bitmap
                        .createBitmap(drawWidget.intrinsicWidth, drawWidget.intrinsicHeight, Bitmap.Config.ARGB_8888)
                drawWidget.setBounds(0, 0, drawWidget.intrinsicWidth, drawWidget.intrinsicHeight)
                drawWidget.draw(Canvas(shortcutApp))

                val setWidgetRecovery = Intent(applicationContext, RecoveryWidgetActivity::class.java)
                setWidgetRecovery.action = "Remote_Recover_Widgets"
                setWidgetRecovery.addCategory(Intent.CATEGORY_DEFAULT)
                val intent = Intent()
                intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, setWidgetRecovery)
                intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.recover_widgets))
                intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, shortcutApp)
                setResult(RESULT_OK, intent)
            } else if (intent.action == Intent.ACTION_MAIN || intent.action == Intent.ACTION_VIEW
                    || intent.action == "Remote_Recover_Widgets") {

                println(">>> To Recovery Widgets to Service <<<")

                val intent = Intent(applicationContext, RecoveryWidgets::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startService(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()

            val intent = Intent(applicationContext, RecoveryWidgets::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startService(intent)
        }

        finish()
    }
}