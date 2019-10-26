package net.geekstools.floatshort.PRO.Util.RemoteTask;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.TypedValue;

import androidx.annotation.Nullable;

import com.google.firebase.appindexing.FirebaseAppIndex;

import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClassDebug;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;

public class RecoveryShortcuts extends Service {

    FunctionsClass functionsClass;

    String packageName;
    String[] appData;

    boolean runService = true;

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        PublicVariable.size = functionsClass.readDefaultPreference("floatingSize", 39);
        PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, this.getResources().getDisplayMetrics());

        if (getApplicationContext().getFileStreamPath(".uFile").exists()) {
            try {
                appData = functionsClass.readFileLine(".uFile");
                FirebaseAppIndex.getInstance().removeAll();

                if (functionsClass.loadCustomIcons()) {
                    LoadCustomIcons loadCustomIcons = new LoadCustomIcons(getApplicationContext(), functionsClass.customIconPackageName());
                    loadCustomIcons.load();
                    FunctionsClassDebug.Companion.PrintDebug("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIcons());
                }

                for (String anAppData : appData) {
                    runService = true;
                    if (PublicVariable.FloatingShortcuts != null) {
                        for (int check = 0; check < PublicVariable.FloatingShortcuts.size(); check++) {
                            if (anAppData.equals(PublicVariable.FloatingShortcuts.get(check))) {
                                runService = false;
                            }
                        }
                    }

                    if (runService == true) {
                        try {
                            packageName = anAppData;
                            functionsClass.runUnlimitedShortcutsService(packageName);
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
        }
        if (PublicVariable.floatingCounter == 0) {
            if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .getBoolean("stable", false) == false) {
                stopService(new Intent(getApplicationContext(), BindServices.class));
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
