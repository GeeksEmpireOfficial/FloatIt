package net.geekstools.floatshort.PRO.Util.RemoteTask;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.annotation.Nullable;

import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;

public class RecoveryCategory extends Service {

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
                FunctionsClass.println("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIcons());
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
        stopSelf();
        return functionsClass.serviceMode();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        functionsClass = new FunctionsClass(getApplicationContext());
        if (functionsClass.returnAPI() >= 26) {
            startForeground(333, functionsClass.bindServiceNotification());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
