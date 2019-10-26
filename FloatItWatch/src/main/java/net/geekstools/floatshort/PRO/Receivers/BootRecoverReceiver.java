package net.geekstools.floatshort.PRO.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import net.geekstools.floatshort.PRO.Automation.RecoveryShortcuts;
import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;

public class BootRecoverReceiver extends BroadcastReceiver {

    FunctionsClass functionsClass;

    @Override
    public void onReceive(final Context context, Intent intent) {
        functionsClass = new FunctionsClass(context);
        try {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                if (functionsClass.bootReceiverEnabled()) {
                    Intent bindServices = new Intent(context, BindServices.class);
                    bindServices.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (functionsClass.returnAPI() < 26) {
                        context.startService(bindServices);
                    } else {
                        context.startForegroundService(bindServices);
                    }

                    if (functionsClass.bootReceiverEnabled()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent recoveryShortcuts = new Intent(context, RecoveryShortcuts.class);
                                recoveryShortcuts.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (functionsClass.returnAPI() < 26) {
                                    context.startService(recoveryShortcuts);
                                } else {
                                    context.startForegroundService(recoveryShortcuts);
                                }
                            }
                        }, 3000);
                    } else {
                        context.stopService(new Intent(context, BindServices.class));
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
