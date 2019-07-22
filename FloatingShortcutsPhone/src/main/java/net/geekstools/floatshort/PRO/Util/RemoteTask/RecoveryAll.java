package net.geekstools.floatshort.PRO.Util.RemoteTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;

public class RecoveryAll extends Service {

    FunctionsClass functionsClass;

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        startService(new Intent(getApplicationContext(), RecoveryShortcuts.class));
        startService(new Intent(getApplicationContext(), RecoveryCategory.class));
        startService(new Intent(getApplicationContext(), RecoveryWidgets.class));

        stopSelf();
        return functionsClass.serviceMode();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        functionsClass = new FunctionsClass(getApplicationContext());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
