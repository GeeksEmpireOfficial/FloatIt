package net.geekstools.floatshort.PRO.Util.RemoteTask;

import android.app.Activity;
import android.os.Bundle;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;

public class CreateFloatingShortcuts extends Activity {

    FunctionsClass functionsClass;

    String packageName, className;

    @Override
    protected void onCreate(Bundle Saved) {
        super.onCreate(Saved);
        functionsClass = new FunctionsClass(getApplicationContext(), CreateFloatingShortcuts.this);

        packageName = getIntent().getStringExtra("PackageName");
        className = getIntent().getStringExtra("ClassName");

        functionsClass.runUnlimitedShortcutsServiceHIS(packageName, className);

        finish();
    }
}
