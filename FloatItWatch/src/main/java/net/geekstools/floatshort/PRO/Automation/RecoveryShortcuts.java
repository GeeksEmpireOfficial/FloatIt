package net.geekstools.floatshort.PRO.Automation;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.TypedValue;

import androidx.annotation.Nullable;

import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.Configurations;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;

public class RecoveryShortcuts extends Service {

    String PackageName;
    String[] appData;

    boolean runService = true;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        FunctionsClass functionsClass = new FunctionsClass(getApplicationContext());
        functionsClass.loadSavedColor();

        PublicVariable.alpha = 133;
        PublicVariable.opacity = 255;
        if (sharedPrefs.getBoolean("hide", false) == false) {
            PublicVariable.hide = false;
        } else if (sharedPrefs.getBoolean("hide", false) == true) {
            PublicVariable.hide = true;
        }
        PublicVariable.size = 33;
        PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, this.getResources().getDisplayMetrics());

        if (getApplicationContext().getFileStreamPath(".uFile").exists()) {
            try {
                appData = functionsClass.readFileLine(".uFile");
                for (int navItem = 0; navItem < appData.length; navItem++) {
                    runService = true;
                    if (PublicVariable.FloatingShortcuts != null) {
                        for (int check = 0; check < PublicVariable.FloatingShortcuts.size(); check++) {
                            if (appData[navItem].equals(PublicVariable.FloatingShortcuts.get(check))) {
                                runService = false;
                            }
                        }
                    }

                    if (runService == true) {
                        try {
                            PackageName = appData[navItem];
                            functionsClass.runUnlimitedShortcutsService(PackageName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (PublicVariable.floatingCounter == 0) {
                    if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .getBoolean("stable", true) == false) {
                        stopService(new Intent(getApplicationContext(), BindServices.class));
                    }
                }
            }
        } else {
            startActivity(new Intent(getApplicationContext(), Configurations.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        if (PublicVariable.floatingCounter == 0) {
            if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .getBoolean("stable", true) == false) {
                stopService(new Intent(getApplicationContext(), BindServices.class));
            }
        }
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
