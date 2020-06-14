package net.geekstools.floatshort.PRO;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.TypedValue;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.geekstools.floatshort.PRO.Folders.FoldersHandler;
import net.geekstools.floatshort.PRO.Shortcuts.HybridViewOff;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryAll;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryFolders;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryShortcuts;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Configurations extends Activity {

    FunctionsClass functionsClass;
    String[] freqAppsArray;
    int numArray;

    String setAppIndex;

    @Override
    protected void onCreate(Bundle Saved) {
        super.onCreate(Saved);
        FirebaseApp.initializeApp(getApplicationContext());

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());

        PublicVariable.contextStatic = getApplicationContext();

        functionsClass = new FunctionsClass(getApplicationContext(), Configurations.this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            functionsClass.savePreference(".UserInformation", "isBetaTester", functionsClass.appVersionName(getPackageName()).contains("[BETA]") ? true : false);
            functionsClass.savePreference(".UserInformation", "installedVersionCode", functionsClass.appVersionCode(getPackageName()));
            functionsClass.savePreference(".UserInformation", "installedVersionName", functionsClass.appVersionName(getPackageName()));
            functionsClass.savePreference(".UserInformation", "deviceModel", functionsClass.getDeviceName());
            functionsClass.savePreference(".UserInformation", "userRegion", functionsClass.getCountryIso());

            if (functionsClass.appVersionName(getPackageName()).contains("[BETA]")) {
                functionsClass.saveDefaultPreference("JoinedBetaProgrammer", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            PublicVariable.actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        PublicVariable.statusBarHeight = result;
        PublicVariable.navigationBarHeight = getResources().getDimensionPixelSize(getResources().getIdentifier("navigation_bar_height", "dimen", "android"));

        if (functionsClass.returnAPI() >= 26) {
            functionsClass.extractWallpaperColor();
            functionsClass.loadSavedColor();
        } else if (!getFileStreamPath(".colors").exists()) {
            functionsClass.extractWallpaperColor();
            functionsClass.saveFile(".colors", "Theme Color Extracted");
            functionsClass.loadSavedColor();
        } else {
            functionsClass.loadSavedColor();
        }

        functionsClass.checkLightDarkTheme();

        if (sharedPreferences.getBoolean("stable", false) == true) {
            PublicVariable.Stable = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(getApplicationContext(), BindServices.class));
            } else {
                startService(new Intent(getApplicationContext(), BindServices.class));
            }
        } else if (sharedPreferences.getBoolean("stable", false) == false) {
            PublicVariable.Stable = false;
        }

        PublicVariable.size = functionsClass.readDefaultPreference("floatingSize", 39);
        PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, this.getResources().getDisplayMetrics());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getFileStreamPath(".uFile").exists()) {
                    String[] recoveryApps = functionsClass.readFileLine(".uFile");
                    for (String packageName : recoveryApps) {
                        try {
                            setAppIndex = functionsClass.appName(packageName) + " | " + packageName;
                            functionsClass.IndexAppInfoShortcuts(setAppIndex);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }, 200);

        try {
            if (functionsClass.UsageStatsEnabled()) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("smart", true);
                editor.apply();

                UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
                List<UsageStats> queryUsageStats = usageStatsManager
                        .queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                                System.currentTimeMillis() - 1000 * 60,            //begin
                                System.currentTimeMillis());                    //end
                Collections.sort(queryUsageStats, new LastTimeLaunchedComparator());
                try {
                    freqAppsArray = retrieveFreqUsedApp();
                    String previousAppPack = queryUsageStats.get(1).getPackageName();
                    if (previousAppPack.contains("com.google.android.googlequicksearchbox")) {
                        if (sharedPreferences.getString("boot", "1").equals("0")) {
                        } else if (sharedPreferences.getString("boot", "1").equals("1")) {
                            Intent r = new Intent(getApplicationContext(), RecoveryShortcuts.class);
                            r.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startService(r);
                            finish();
                            return;
                        } else if (sharedPreferences.getString("boot", "1").equals("2")) {
                            Intent r = new Intent(getApplicationContext(), RecoveryFolders.class);
                            r.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startService(r);
                            finish();
                            return;
                        } else if (sharedPreferences.getString("boot", "1").equals("3")) {
                            Intent r = new Intent(getApplicationContext(), RecoveryAll.class);
                            r.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startService(r);
                            finish();
                            return;
                        }
                    }
                } catch (Exception e) {

                    finish();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getApplicationContext(), Configurations.class));
                        }
                    }, 10);
                }
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("smart", false);
                editor.apply();

                if (getFileStreamPath("Frequently").exists()) {
                    String[] freqDelete = functionsClass.readFileLine("Frequently");
                    for (String deleteFreq : freqDelete) {
                        deleteFile(deleteFreq + "Frequently");
                    }
                    functionsClass.removeLine(".categoryInfo", "Frequently");
                    deleteFile("Frequently");
                }
            }
        } catch (SecurityException ignored) {
            ignored.printStackTrace();
        }

        if (functionsClass.returnAPI() >= 23) {
            if (functionsClass.returnAPI() >= 26) {
                if (!Settings.canDrawOverlays(getApplicationContext())
                        || !getSharedPreferences(".Configuration", Context.MODE_PRIVATE).getBoolean("Permissions", false)) {
                    startActivity(new Intent(getApplicationContext(), CheckPoint.class));
                    finish();
                    return;
                }
            } else {
                if (!Settings.canDrawOverlays(getApplicationContext())) {
                    startActivity(new Intent(getApplicationContext(), CheckPoint.class));
                    finish();
                    return;
                }
            }
        }
        functionsClass.updateRecoverShortcuts();
        if (functionsClass.readPreference("OpenMode", "openClassName", HybridViewOff.class.getSimpleName()).equals(FoldersHandler.class.getSimpleName())) {
            try {
                if (functionsClass.UsageStatsEnabled()) {
                    if (getFileStreamPath("Frequently").exists()) {
                        functionsClass.removeLine(".categoryInfo", "Frequently");
                        deleteFile("Frequently");
                    }
                    String[] frequentApps = retrieveFreqUsedApp();
                    PublicVariable.freqApps = frequentApps;
                    PublicVariable.freqLength = frequentApps.length;

                    for (String frequentApp : frequentApps) {
                        functionsClass.saveFileAppendLine("Frequently", frequentApp);
                        functionsClass.saveFile(frequentApp + "Frequently", frequentApp);
                    }
                    functionsClass.saveFileAppendLine(".categoryInfo", "Frequently");
                    functionsClass.addAppShortcuts();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent categoryIntent = new Intent(getApplicationContext(), FoldersHandler.class);
            startActivity(categoryIntent);
        } else {
            if (functionsClass.UsageStatsEnabled()) {
                try {
                    PublicVariable.freqApps = retrieveFreqUsedApp();
                    PublicVariable.freqLength = PublicVariable.freqApps.length;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Intent AppIntent = new Intent(getApplicationContext(), HybridViewOff.class);
            try {
                Intent goHome = getIntent();
                if (goHome.hasExtra("goHome")) {
                    if (goHome.getBooleanExtra("goHome", false)) {
                        AppIntent.putExtra("goHome", true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            AppIntent.putExtra("freq", freqAppsArray);
            AppIntent.putExtra("num", numArray);
            startActivity(AppIntent);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    functionsClass.addAppShortcuts();
                }
            }, 333);
        }

        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        functionsClass.endIndexAppInfo();
    }

    public String[] retrieveFreqUsedApp() throws Exception {
        List<String> freqApps = functionsClass.letMeKnow(Configurations.this, (25), (86400000 * 7), System.currentTimeMillis());
        numArray = freqApps.size();
        return freqApps.toArray(new String[numArray]);
    }

    private class LastTimeLaunchedComparator implements Comparator<UsageStats> {
        @Override
        public int compare(UsageStats left, UsageStats right) {
            return Long.compare(right.getLastTimeUsed(), left.getLastTimeUsed());
        }
    }
}
