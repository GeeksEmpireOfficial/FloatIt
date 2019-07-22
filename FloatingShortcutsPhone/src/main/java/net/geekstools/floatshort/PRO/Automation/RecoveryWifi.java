package net.geekstools.floatshort.PRO.Automation;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.TypedValue;

import androidx.annotation.Nullable;

import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;

public class RecoveryWifi extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        FunctionsClass functionsClass = new FunctionsClass(getApplicationContext());
        PublicVariable.size = functionsClass.readDefaultPreference("floatingSize", 39);
        PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, this.getResources().getDisplayMetrics());

        if (getFileStreamPath(".auto" + getClass().getSimpleName().replace("Recovery", "")).exists()
                && getFileStreamPath(".auto" + getClass().getSimpleName().replace("Recovery", "")).isFile()) {
            try {
                String[] PackageNames = functionsClass.readFileLine(
                        ".auto" + getClass().getSimpleName().replace("Recovery", ""));
                if (PackageNames.length > 0) {
                    for (String PackageName : PackageNames) {
                        functionsClass.runUnlimitedWifi(PackageName);
                    }

                    Intent steady = new Intent(getApplicationContext(), BindServices.class);
                    steady.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (functionsClass.returnAPI() < 26) {
                        startService(steady);
                    } else {
                        startForegroundService(steady);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (getFileStreamPath(".auto" + getClass().getSimpleName().replace("Recovery", "") + "Category").exists()
                && getFileStreamPath(".auto" + getClass().getSimpleName().replace("Recovery", "") + "Category").isFile()) {
            try {
                String[] CategoryNames = functionsClass.readFileLine(
                        ".auto" + getClass().getSimpleName().replace("Recovery", "") + "Category");
                if (CategoryNames.length > 0) {
                    for (String CategoryName : CategoryNames) {
                        functionsClass.runUnlimitedCategoryWifi(CategoryName, functionsClass.readFileLine(CategoryName));
                    }

                    Intent steady = new Intent(getApplicationContext(), BindServices.class);
                    steady.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (functionsClass.returnAPI() < 26) {
                        startService(steady);
                    } else {
                        startForegroundService(steady);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        stopSelf();
        return functionsClass.serviceMode();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
