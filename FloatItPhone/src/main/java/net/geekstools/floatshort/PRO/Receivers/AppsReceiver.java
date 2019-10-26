package net.geekstools.floatshort.PRO.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;

public class AppsReceiver extends BroadcastReceiver {

    FunctionsClass functionsClass;

    @Override
    public void onReceive(Context context, Intent intent) {
        functionsClass = new FunctionsClass(context);
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_FULLY_REMOVED)) {
            try {
                String packageName = intent.getData().getEncodedSchemeSpecificPart();

                functionsClass.removeLine(".uFile", packageName);
                functionsClass.addAppShortcuts();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
