package net.geekstools.floatshort.PRO.Util.RemoteTask;


import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.annotation.Nullable;

import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClassDebug;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;

public class RecoveryFolders extends Service {

    FunctionsClass functionsClass;

    String categoryName;
    String[] categoryData;

    boolean runService = true;

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        PublicVariable.size = functionsClass.readDefaultPreference("floatingSize", 39);
        PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, this.getResources().getDisplayMetrics());

        if (!getApplicationContext().getFileStreamPath(".uCategory").exists()) {
            Toast.makeText(getApplicationContext(), "Press & Hold ", Toast.LENGTH_LONG).show();
            return START_NOT_STICKY;
        }
        try {
            categoryData = functionsClass.readFileLine(".uCategory");

            if (functionsClass.loadCustomIcons()) {
                LoadCustomIcons loadCustomIcons = new LoadCustomIcons(getApplicationContext(), functionsClass.customIconPackageName());
                loadCustomIcons.load();
                FunctionsClassDebug.Companion.PrintDebug("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIcons());
            }

            for (String aCategoryData : categoryData) {
                runService = true;
                if (PublicVariable.FloatingCategories != null) {
                    for (int check = 0; check < PublicVariable.FloatingCategories.size(); check++) {
                        if (aCategoryData.equals(PublicVariable.FloatingCategories.get(check))) {
                            runService = false;
                        }
                    }
                }

                if (runService == true) {
                    try {
                        categoryName = aCategoryData;
                        functionsClass.runUnlimitedCategoryService(categoryName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(Service.STOP_FOREGROUND_REMOVE);
                stopForeground(true);
            }
            stopSelf();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (PublicVariable.floatingCounter == 0) {
                if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .getBoolean("stable", false) == false) {
                    stopService(new Intent(getApplicationContext(), BindServices.class));
                }
            }
        }

        return functionsClass.serviceMode();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        functionsClass = new FunctionsClass(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(333, functionsClass.bindServiceNotification(), Service.STOP_FOREGROUND_REMOVE);
        } else {
            startForeground(333, functionsClass.bindServiceNotification());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(getApplicationContext(), BindServices.class));
        } else {
            startService(new Intent(getApplicationContext(), BindServices.class));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(Service.STOP_FOREGROUND_REMOVE);
            stopForeground(true);
        }
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
