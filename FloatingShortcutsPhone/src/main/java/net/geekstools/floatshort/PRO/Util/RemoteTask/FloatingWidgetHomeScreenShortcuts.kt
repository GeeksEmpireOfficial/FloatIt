package net.geekstools.floatshort.PRO.Util.RemoteTask

import android.app.Activity
import android.os.Bundle
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass

class FloatingWidgetHomeScreenShortcuts : Activity() {

    lateinit var functionsClass: FunctionsClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        functionsClass = FunctionsClass(applicationContext, this@FloatingWidgetHomeScreenShortcuts)
        val appWidgetId = intent.getIntExtra("ShortcutsId", -1)
        val widgetLabel = intent.getStringExtra("ShortcutLabel");
        functionsClass.runUnlimitedWidgetService(appWidgetId, widgetLabel)
        finish()
    }
}