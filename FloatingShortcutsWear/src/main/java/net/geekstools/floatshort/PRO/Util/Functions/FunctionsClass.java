package net.geekstools.floatshort.PRO.Util.Functions;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.palette.graphics.Palette;

import net.geekstools.floatshort.PRO.App_Unlimited_HIS;
import net.geekstools.floatshort.PRO.App_Unlimited_Shortcuts;
import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.Category_Unlimited_Category;
import net.geekstools.floatshort.PRO.Configurations;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.OpenApplications;
import net.geekstools.floatshort.PRO.Util.UI.FloatingSplash;
import net.geekstools.floatshort.PRO.Util.UI.PopupOptionsFloatingCategory;
import net.geekstools.floatshort.PRO.Util.UI.PopupOptionsFloatingShortcuts;
import net.geekstools.imageview.customshapes.ShapesImage;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FunctionsClass {
    int API;
    Activity activity;
    Context context;
    PackageManager packageManager;
    BroadcastReceiver.PendingResult pendingResult;

    public FunctionsClass(Context context) {
        this.context = context;

        API = Build.VERSION.SDK_INT;

        loadSavedColor();
    }

    public FunctionsClass(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

        API = Build.VERSION.SDK_INT;

        loadSavedColor();
    }

    /*Check Point Function*/
    public void dialogueLicense(final Activity activity) {
        context = activity.getApplicationContext();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        alertDialog.setTitle(Html.fromHtml(context.getResources().getString(R.string.license_title)));
        alertDialog.setMessage(Html.fromHtml(context.getResources().getString(R.string.license_msg)));
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton(context.getString(R.string.buy), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent r = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.play_store_link) + context.getPackageName() + ".PRO"));
                activity.startActivity(r);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        uninstallApp(context.getPackageName());
                    }
                }, 2333);
            }
        });
        alertDialog.setNegativeButton(context.getString(R.string.free), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent r = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.play_store_link) + context.getPackageName()));
                activity.startActivity(r);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        uninstallApp(context.getPackageName());
                    }
                }, 2333);
            }
        });
        alertDialog.setNeutralButton(context.getString(R.string.contact), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //dialog.dismiss();
                String[] contactOption = new String[]{
                        "Send an Email",
                        "Contact via Forum"};
                AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
                builder.setTitle(context.getString(R.string.supportTitle));
                builder.setSingleChoiceItems(contactOption, 0, null);
                builder.setCancelable(false);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        if (selectedPosition == 0) {
                            String textMsg = "\n\n\n\n\n"
                                    + "[Essential Information]" + "\n"
                                    + getDeviceName() + " | " + "API " + Build.VERSION.SDK_INT + " | " + getCountryIso().toUpperCase();
                            Intent email = new Intent(Intent.ACTION_SEND);
                            email.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.support)});
                            email.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.feedback_tag) + " [" + appVersion(context.getPackageName()) + "] ");
                            email.putExtra(Intent.EXTRA_TEXT, textMsg);
                            email.setType("message/*");
                            email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(Intent.createChooser(email, context.getString(R.string.feedback_tag)));
                        } else if (selectedPosition == 1) {
                            Intent r = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.link_xda)));
                            r.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(r);
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        activity.finish();
                    }
                });
                builder.show();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        try {
            alertDialog.show();
        } catch (Exception e) {
            activity.finish();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activity.startActivity(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }, 300);
        }
    }

    public boolean networkConnection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public int returnAPI() {
        return API;
    }

    public void overrideBackPressToClass(Class returnClass) throws Exception {
        activity.startActivity(new Intent(context, returnClass));
    }

    /*Unlimited Shortcuts*/
    public int serviceMode() {
        int ReturnValue = Service.START_NOT_STICKY;
        if (PublicVariable.Return == false) {
            ReturnValue = Service.START_NOT_STICKY;
        } else if (PublicVariable.Return == true) {
            ReturnValue = Service.START_REDELIVER_INTENT;
        }
        return ReturnValue;
    }

    public void runUnlimitedShortcutsService(String packageName) {
        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, Configurations.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        PublicVariable.floatingCounter++;
        PublicVariable.shortcutsCounter++;
        PublicVariable.FloatingShortcuts.add(PublicVariable.shortcutsCounter, packageName);

        Intent u = new Intent(context, App_Unlimited_Shortcuts.class);
        u.putExtra("pack", packageName);
        u.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(u);
        if (PublicVariable.floatingCounter == 1) {
            context.startService(new Intent(context, BindServices.class));
        }
    }

    public void saveUnlimitedShortcutsService(String packageName) {
        boolean duplicated = false;
        String fileName = ".uFile";
        File uFile = context.getFileStreamPath(".uFile");
        if (!uFile.exists()) {
            saveFileAppendLine(fileName, packageName);
        } else if (uFile.exists()) {
            int countLine = countLine(fileName);
            String[] contentLine = new String[countLine];
            contentLine = readFileLine(fileName);
            for (String aContentLine : contentLine) {
                if (aContentLine.equals(packageName)) {
                    duplicated = true;
                    break;
                }
            }
            if (duplicated == false) {
                saveFileAppendLine(fileName, packageName);
            }
        }
    }

    public void runUnlimitedShortcutsServiceHIS(String packageName, String className) {
        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, Configurations.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        PublicVariable.floatingCounter++;

        Intent u = new Intent(context, App_Unlimited_HIS.class);
        u.putExtra("packageName", packageName);
        u.putExtra("className", className);
        u.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(u);
        if (PublicVariable.floatingCounter == 1) {
            if (API < 26) {
                context.startService(new Intent(context, BindServices.class));
            } else {
                context.startForegroundService(new Intent(context, BindServices.class));
            }
        }
    }

    public void openApplication(String packageName) {
        if (appInstalledOrNot(packageName) == true) {
            try {
                Toast.makeText(context,
                        appName(packageName), Toast.LENGTH_SHORT).show();

                Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(packageName);
                activity.startActivity(launchIntentForPackage);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, context.getString(R.string.not_install), Toast.LENGTH_LONG).show();
                Intent playStore = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(context.getString(R.string.play_store_link) + packageName));
                playStore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(playStore);
            }
        } else {
            Toast.makeText(context, context.getString(R.string.not_install), Toast.LENGTH_LONG).show();
            Intent playStore = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(context.getString(R.string.play_store_link) + packageName));
            playStore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(playStore);
        }
    }

    public void openApplication(String packageName, String className) {
        if (appInstalledOrNot(packageName) == true) {
            try {
                Toast.makeText(context,
                        context.getPackageManager().getActivityInfo(new ComponentName(packageName, className), 0).loadLabel(context.getPackageManager()),
                        Toast.LENGTH_SHORT).show();

                Intent openAlias = new Intent();
                openAlias.setClassName(packageName, className);
                context.startActivity(openAlias);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, context.getString(R.string.not_install), Toast.LENGTH_LONG).show();
                Intent playStore = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(context.getString(R.string.play_store_link) + packageName));
                playStore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(playStore);
            }
        } else {
            Toast.makeText(context, context.getString(R.string.not_install), Toast.LENGTH_LONG).show();
            Intent playStore = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(context.getString(R.string.play_store_link) + packageName));
            playStore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(playStore);
        }
    }

    public void appsLaunchPad(String packageName) {
        Intent intent = new Intent(context, OpenApplications.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", packageName);
        context.startActivity(intent);
    }

    public void appsLaunchPad(String packageName, String className) {
        Intent intent = new Intent(context, OpenApplications.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", packageName);
        intent.putExtra("className", className);
        context.startActivity(intent);
    }

    public void PopupShortcuts(View anchorView, final String packageName, String classNameCommand, int startId, int X, int Y) {
        try {
            Intent popupOptionsShortcuts = new Intent(context, PopupOptionsFloatingShortcuts.class);
            popupOptionsShortcuts.putExtra("packageName", packageName);
            popupOptionsShortcuts.putExtra("classNameCommand", classNameCommand);
            popupOptionsShortcuts.putExtra("startIdCommand", startId);
            popupOptionsShortcuts.putExtra("X", X);
            popupOptionsShortcuts.putExtra("Y", Y);
            popupOptionsShortcuts.putExtra("HW", anchorView.getWidth());
            context.startService(popupOptionsShortcuts);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("Hide_PopupListView_Shortcuts");
            final BroadcastReceiver counterReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals("Hide_PopupListView_Shortcuts")) {
                        context.stopService(new Intent(context, PopupOptionsFloatingShortcuts.class));
                    }
                }
            };
            try {
                context.unregisterReceiver(counterReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            context.registerReceiver(counterReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Unlimited Categories*/
    public void runUnlimitedCategoryService(String categoryName, String[] categoryNamePackages) {
        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, Configurations.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        PublicVariable.floatingCounter++;
        PublicVariable.floatingCategoryCounter_category++;
        PublicVariable.categoriesCounter++;
        PublicVariable.FloatingCategories.add(PublicVariable.categoriesCounter, categoryName);

        Intent c = new Intent(context, Category_Unlimited_Category.class);
        c.putExtra("categoryName", categoryName);
        c.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(c);
        if (PublicVariable.floatingCounter == 1) {
            context.startService(new Intent(context, BindServices.class));
        }
    }

    public void PopupListCategory(View anchorView,
                                  String categoryName, String[] packagesName, String classNameCommand, int startIdCommand, int X, int Y, int HW) {
        if (!context.getFileStreamPath(categoryName).exists() && !context.getFileStreamPath(categoryName).isFile()) {
            return;
        }

        try {
            Intent popupOptionsFloatingCategory = new Intent(context, PopupOptionsFloatingCategory.class);
            popupOptionsFloatingCategory.putExtra("MODE", "AppsList");
            popupOptionsFloatingCategory.putExtra("categoryName", categoryName);
            popupOptionsFloatingCategory.putExtra("PackagesNames", packagesName);
            popupOptionsFloatingCategory.putExtra("classNameCommand", classNameCommand);
            popupOptionsFloatingCategory.putExtra("startIdCommand", startIdCommand);
            popupOptionsFloatingCategory.putExtra("X", X);
            popupOptionsFloatingCategory.putExtra("Y", Y);
            popupOptionsFloatingCategory.putExtra("HW", HW);
            context.startService(popupOptionsFloatingCategory);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("Hide_PopupListView_Category");
            final BroadcastReceiver counterReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals("Hide_PopupListView_Category")) {
                        context.stopService(new Intent(context, PopupOptionsFloatingCategory.class));
                    }
                }
            };
            try {
                context.unregisterReceiver(counterReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            context.registerReceiver(counterReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PopupOptionCategory(View anchorView,
                                    String categoryName, String classNameCommand, int startIdCommand, int X, int Y) {
        if (!context.getFileStreamPath(categoryName).exists() && !context.getFileStreamPath(categoryName).isFile()) {
            return;
        }
        try {
            Intent popupOptionsFloatingCategory = new Intent(context, PopupOptionsFloatingCategory.class);
            popupOptionsFloatingCategory.putExtra("MODE", "Options");
            popupOptionsFloatingCategory.putExtra("categoryName", categoryName);
            popupOptionsFloatingCategory.putExtra("classNameCommand", classNameCommand);
            popupOptionsFloatingCategory.putExtra("startIdCommand", startIdCommand);
            popupOptionsFloatingCategory.putExtra("X", X);
            popupOptionsFloatingCategory.putExtra("Y", Y);
            popupOptionsFloatingCategory.putExtra("HW", anchorView.getWidth());
            context.startService(popupOptionsFloatingCategory);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("Hide_PopupListView_Category");
            final BroadcastReceiver counterReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals("Hide_PopupListView_Category")) {
                        context.stopService(new Intent(context, PopupOptionsFloatingCategory.class));
                    }
                }
            };
            try {
                context.unregisterReceiver(counterReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            context.registerReceiver(counterReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*File Functions*/
    public void removeLine(String fileName, String lineToRemove) {
        try {
            FileInputStream fin = context.openFileInput(fileName);
            BufferedReader myDIS = new BufferedReader(new InputStreamReader(fin));
            OutputStreamWriter fOut = new OutputStreamWriter(context.openFileOutput(fileName + ".tmp", Context.MODE_APPEND));

            String tmp = "";
            while ((tmp = myDIS.readLine()) != null) {
                if (!tmp.trim().equals(lineToRemove)) {
                    fOut.write(tmp);
                    fOut.write("\n");
                }
            }
            fOut.close();
            myDIS.close();
            fin.close();

            File tmpD = context.getFileStreamPath(fileName + ".tmp");
            File New = context.getFileStreamPath(fileName);

            if (tmpD.isFile()) {
            }
            context.deleteFile(fileName);
            tmpD.renameTo(New);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (API > 23) {
                    context.deleteSharedPreferences(lineToRemove);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFile(String fileName, String content) {
        try {
            FileOutputStream fOut = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fOut.write((content).getBytes());

            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFileAppendLine(String fileName, String content) {
        try {
            FileOutputStream fOut = context.openFileOutput(fileName, Context.MODE_APPEND);
            fOut.write((content + "\n").getBytes());

            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] readFileLine(String fileName) {
        String[] contentLine = null;
        if (context.getFileStreamPath(fileName).exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(context.getFileStreamPath(fileName));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

                int count = countLine(fileName);
                contentLine = new String[count];
                String line = "";
                int i = 0;
                while ((line = bufferedReader.readLine()) != null) {
                    contentLine[i] = line;
                    i++;
                }

                fileInputStream.close();
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return contentLine;
    }

    public String readFile(String fileName) {
        String temp = "0";

        File G = context.getFileStreamPath(fileName);
        if (!G.exists()) {
            temp = "0";
        } else {
            try {
                FileInputStream fileInputStream = context.openFileInput(fileName);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"), 1024);

                int c;
                temp = "";
                while ((c = bufferedReader.read()) != -1) {
                    temp = temp + Character.toString((char) c);
                }

                fileInputStream.close();
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return temp;
    }

    public int countLine(String fileName) {
        int nLines = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(context.getFileStreamPath(fileName)));

            while (reader.readLine() != null) {
                nLines++;
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            nLines = 0;
        }
        return nLines;
    }

    public void savePreference(String PreferenceName, String KEY, String VALUE) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
        editorSharedPreferences.putString(KEY, VALUE);
        editorSharedPreferences.apply();
    }

    public void savePreference(String PreferenceName, String KEY, int VALUE) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
        editorSharedPreferences.putInt(KEY, VALUE);
        editorSharedPreferences.apply();
    }

    public void savePreference(String PreferenceName, String KEY, boolean VALUE) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
        editorSharedPreferences.putBoolean(KEY, VALUE);
        editorSharedPreferences.apply();
    }

    public void saveDefaultPreference(String KEY, boolean VALUE) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
        editorSharedPreferences.putBoolean(KEY, VALUE);
        editorSharedPreferences.apply();
    }

    public String readPreference(String PreferenceName, String KEY, String defaultVALUE) {
        return context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE).getString(KEY, defaultVALUE);
    }

    public int readPreference(String PreferenceName, String KEY, int defaultVALUE) {
        return context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE).getInt(KEY, defaultVALUE);
    }

    public boolean readPreference(String PreferenceName, String KEY, boolean defaultVALUE) {
        return context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE).getBoolean(KEY, defaultVALUE);
    }

    public boolean readDefaultPreference(String KEY, boolean defaultVALUE) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY, defaultVALUE);
    }

    /*CheckPoint*/
    public String appName(String packageName) {
        String Name = null;

        try {
            PackageManager packManager = context.getPackageManager();
            ApplicationInfo app = context.getPackageManager().getApplicationInfo(packageName, 0);
            Name = packManager.getApplicationLabel(app).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Name;
    }

    public String appVersion(String packageName) {
        String Version = "0";

        try {
            PackageInfo packInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            Version = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Version;
    }

    public String appVersionName(String packageName) {
        String Version = "0";
        try {
            PackageInfo packInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            Version = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Version;
    }

    public int appVersionCode(String packageName) {
        int VersionCode = 0;

        try {
            PackageInfo packInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            VersionCode = packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return VersionCode;
    }

    public boolean appInstalledOrNot(String packName) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(packName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void setSizeBack() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String s = sharedPrefs.getString("sizes", "NULL");
        if (s.equals("1")) {
            PublicVariable.size = 24;
        } else if (s.equals("2")) {
            PublicVariable.size = 36;
        } else if (s.equals("3")) {
            PublicVariable.size = 48;
        }
        PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, context.getResources().getDisplayMetrics());
    }

    public void uninstallApp(String pack) {
        Uri packageUri = Uri.parse("package:" + pack);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
        uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(uninstallIntent);
    }

    public boolean ifSystem(String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            // Get packageinfo for target application
            PackageInfo targetPkgInfo = packageManager.getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
            // Get packageinfo for system package
            PackageInfo sys = packageManager.getPackageInfo(
                    "android", PackageManager.GET_SIGNATURES);
            // Match both packageinfo for there signatures
            return (targetPkgInfo != null && targetPkgInfo.signatures != null && sys.signatures[0]
                    .equals(targetPkgInfo.signatures[0]));
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public boolean ifDefaultLauncher(String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo defaultLauncher = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String defaultLauncherStr = defaultLauncher.activityInfo.packageName;
        if (defaultLauncherStr.equals(packageName)) {
            return true;
        }
        return false;
    }

    public void updateRecoverShortcuts() {
        try {
            if (context.getFileStreamPath(".uFile").exists()) {
                PublicVariable.RecoveryShortcuts = new ArrayList<String>();

                FileInputStream fileInputStream = new FileInputStream(context.getFileStreamPath(".uFile"));
                DataInputStream dataInputStream = new DataInputStream(fileInputStream);

                String line = "";
                int u = 0;
                while ((line = dataInputStream.readLine()) != null) {
                    PublicVariable.RecoveryShortcuts.add(u, line);
                    u++;
                }

                fileInputStream.close();
                dataInputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean loadRecoveryIndicator(String packageName) {
        boolean inRecovery = false;
        try {
            if (PublicVariable.RecoveryShortcuts != null) {
                for (String anAppNameArrayRecovery : PublicVariable.RecoveryShortcuts) {
                    if (packageName.equals(anAppNameArrayRecovery)) {
                        inRecovery = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inRecovery;
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public String getCountryIso() {
        String countryISO = "Undefined";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            countryISO = telephonyManager.getSimCountryIso();
            if (countryISO.length() < 2) {
                countryISO = "Undefined";
            }
        } catch (Exception e) {
            e.printStackTrace();
            countryISO = "Undefined";
        }
        return countryISO;
    }

    public void appInfoSetting(String packageName) {
        Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + packageName));
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public boolean bootReceiverEnabled() {
        return readPreference("SmartFeature", "remoteRecovery", false);
    }

    /*GUI Functions*/
    public WindowManager.LayoutParams normalLayoutParams(int HW, int X, int Y) {
        int marginClear = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
        WindowManager.LayoutParams layoutParams = null;
        if (API > 25) {
            layoutParams = new WindowManager.LayoutParams(
                    HW + marginClear,
                    HW + marginClear,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            layoutParams = new WindowManager.LayoutParams(
                    HW + marginClear,
                    HW + marginClear,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        layoutParams.gravity = Gravity.TOP | Gravity.START;
        layoutParams.x = X;
        layoutParams.y = Y;
        layoutParams.windowAnimations = android.R.style.Animation_Dialog;
        return layoutParams;
    }

    public WindowManager.LayoutParams splashRevealParams(int X, int Y) {
        WindowManager.LayoutParams layoutParams = null;
        if (API > 25) {
            layoutParams = new WindowManager.LayoutParams(
                    displayX(),
                    displayY(),
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            layoutParams = new WindowManager.LayoutParams(
                    displayX(),
                    displayY(),
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        layoutParams.gravity = Gravity.TOP | Gravity.START;
        layoutParams.x = X;
        layoutParams.y = Y;
        return layoutParams;
    }

    public WindowManager.LayoutParams setLayoutParams(int HW, int X, int Y) {
        WindowManager.LayoutParams layoutParams = null;
        if (API > 25) {
            layoutParams = new WindowManager.LayoutParams(
                    HW,
                    HW,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            layoutParams = new WindowManager.LayoutParams(
                    HW,
                    HW,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }
        layoutParams.gravity = Gravity.TOP | Gravity.START;
        layoutParams.x = X;
        layoutParams.y = Y;
        layoutParams.windowAnimations = android.R.style.Animation_Dialog;
        return layoutParams;
    }

    public int extractDominantColor(Drawable drawable) {
        int VibrantColor = context.getResources().getColor(R.color.default_color);
        Bitmap bitmap = null;
        if (returnAPI() >= 26) {
            if (drawable instanceof VectorDrawable) {
                bitmap = drawableToBitmap(drawable);
            } else if (drawable instanceof AdaptiveIconDrawable) {
                try {
                    bitmap = ((BitmapDrawable) ((AdaptiveIconDrawable) drawable).getBackground()).getBitmap();
                } catch (Exception e) {
                    bitmap = ((BitmapDrawable) ((AdaptiveIconDrawable) drawable).getForeground()).getBitmap();
                }
            } else {
                bitmap = drawableToBitmap(drawable);
            }
        } else {
            bitmap = drawableToBitmap(drawable);
        }
        Palette currentColor;
        try {
            if (bitmap != null && !bitmap.isRecycled()) {
                currentColor = Palette.from(bitmap).generate();

                int defaultColor = context.getResources().getColor(R.color.default_color);
                VibrantColor = currentColor.getDominantColor(defaultColor);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (bitmap != null && !bitmap.isRecycled()) {
                    currentColor = Palette.from(bitmap).generate();

                    int defaultColor = context.getResources().getColor(R.color.default_color);
                    VibrantColor = currentColor.getMutedColor(defaultColor);
                }
            } catch (Exception e1) {
                e.printStackTrace();
            }
        }
        return VibrantColor;
    }

    public int extractVibrantColor(Drawable drawable) {
        int VibrantColor = context.getResources().getColor(R.color.default_color);
        Bitmap bitmap;
        if (returnAPI() >= 26) {
            if (drawable instanceof VectorDrawable) {
                bitmap = drawableToBitmap(drawable);
            } else if (drawable instanceof AdaptiveIconDrawable) {
                try {
                    bitmap = ((BitmapDrawable) ((AdaptiveIconDrawable) drawable).getBackground()).getBitmap();
                } catch (Exception e) {
                    try {
                        bitmap = ((BitmapDrawable) ((AdaptiveIconDrawable) drawable).getForeground()).getBitmap();
                    } catch (Exception e1) {
                        bitmap = drawableToBitmap(drawable);
                    }
                }
            } else {
                bitmap = drawableToBitmap(drawable);
            }
        } else {
            bitmap = drawableToBitmap(drawable);
        }
        Palette currentColor;
        try {
            if (bitmap != null && !bitmap.isRecycled()) {
                currentColor = Palette.from(bitmap).generate();

                int defaultColor = context.getResources().getColor(R.color.default_color);
                VibrantColor = currentColor.getVibrantColor(defaultColor);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (bitmap != null && !bitmap.isRecycled()) {
                    currentColor = Palette.from(bitmap).generate();

                    int defaultColor = context.getResources().getColor(R.color.default_color);
                    VibrantColor = currentColor.getMutedColor(defaultColor);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return VibrantColor;
    }

    public void loadSavedColor() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("themeColor", false) == true) {
            PublicVariable.primaryColor = context.getColor(R.color.default_color);
            PublicVariable.primaryColorOpposite = context.getColor(R.color.default_color_darker);
            PublicVariable.colorLightDark = context.getResources().getColor(R.color.light);
            PublicVariable.colorLightDarkOpposite = context.getResources().getColor(R.color.dark);
        } else if (sharedPreferences.getBoolean("themeColor", false) == false) {
            PublicVariable.primaryColor = context.getColor(R.color.default_color_darker);
            PublicVariable.primaryColorOpposite = context.getColor(R.color.default_color);
            PublicVariable.colorLightDark = context.getResources().getColor(R.color.dark);
            PublicVariable.colorLightDarkOpposite = context.getResources().getColor(R.color.light);
        }
    }

    public int setColorAlpha(int color, float alphaPercent) {
        int alpha = Math.round(Color.alpha(color) * alphaPercent);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public int manipulateColor(int color, float aFactor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * aFactor);
        int g = Math.round(Color.green(color) * aFactor);
        int b = Math.round(Color.blue(color) * aFactor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    public int mixColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    public boolean setAppThemeBlur() {
        boolean blurEffect = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("blur", false);
        if (blurEffect == true) {
            try {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                BitmapDrawable wallpaper = (BitmapDrawable) wallpaperManager.getDrawable();

                Bitmap bitmapWallpaper = wallpaper.getBitmap();
                Bitmap inputBitmap = Bitmap.createBitmap(
                        bitmapWallpaper,
                        (bitmapWallpaper.getWidth() / 2) - (displayX() / 2),
                        (bitmapWallpaper.getHeight() / 2) - (displayY() / 2),
                        displayX(),
                        displayY()
                );
                Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

                RenderScript rs = RenderScript.create(context);
                ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
                Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

                intrinsicBlur.setRadius(25);
                intrinsicBlur.setInput(tmpIn);
                intrinsicBlur.forEach(tmpOut);
                tmpOut.copyTo(outputBitmap);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), outputBitmap);
                activity.getWindow().getDecorView().setBackground(new ColorDrawable(Color.RED));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (blurEffect == false) {
        }
        return blurEffect;
    }

    public boolean checkThemeLightDark() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
        setAppThemeBlur();
        return sharedPreferences.getBoolean("themeColor", false);
    }

    public boolean setAppTransparency() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean("hide", false);
    }

    public int DpToInteger(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public int displayX() {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public int displayY() {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public int displaySection(int X, int Y) {
        int section = 0;
        if (X < displayX() / 2 && Y < displayY() / 2) {
            section = 1;
        } else if (X > displayX() / 2 && Y < displayY() / 2) {
            section = 2;
        } else if (X < displayX() / 2 && Y > displayY() / 2) {
            section = 3;
        } else if (X > displayX() / 2 && Y > displayY() / 2) {
            section = 4;
        }
        return section;
    }

    public void circularRevealSplashScreenRemoval(final View view,
                                                  final int xPosition, final int yPosition) {
        int startRadius = 0;
        int endRadius = (int) Math.hypot(displayX(), displayY());
        Animator animator = ViewAnimationUtils.createCircularReveal(
                view,
                (xPosition),
                (yPosition),
                startRadius,
                endRadius
        );
        animator.setInterpolator(new FastOutLinearInInterpolator());
        animator.setDuration(555);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    public void circularRevealSplashScreen(final View view, final View childView,
                                           final int xPosition, final int yPosition, final int iconColor,
                                           final String packageName, final String className,
                                           boolean circularExpandCollapse) {
        if (circularExpandCollapse) {
            view.setBackgroundColor(iconColor);
            int startRadius = 0;
            int endRadius = (int) Math.hypot(displayX(), displayY());
            Animator animator = ViewAnimationUtils.createCircularReveal(
                    view,
                    (xPosition + (childView.getWidth() / 2)),
                    (yPosition + (childView.getHeight() / 2)),
                    startRadius,
                    endRadius
            );
            animator.setInterpolator(new FastOutLinearInInterpolator());
            animator.setDuration(777);
            animator.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new HeartBeat(packageName, childView);
                    if (className != null) {
                        appsLaunchPad(packageName, className);
                    } else {
                        appsLaunchPad(packageName);
                    }
                }
            }, 13);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    context.stopService(new Intent(context, FloatingSplash.class));
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        } else {
            view.setBackgroundColor(iconColor);
            int startRadius = (int) Math.hypot(displayX(), displayY());
            int endRadius = 0;
            Animator animator = ViewAnimationUtils.createCircularReveal(
                    view,
                    (xPosition + (childView.getWidth() / 2)),
                    (yPosition + (childView.getHeight() / 2)),
                    startRadius,
                    endRadius
            );
            animator.setInterpolator(new FastOutLinearInInterpolator());
            animator.setDuration(123);
            animator.start();
        }
    }

    public void circularRevealViewScreen(final View view,
                                         final int xPosition, final int yPosition,
                                         boolean circularExpandCollapse) {
        if (circularExpandCollapse) {
            int startRadius = 0;
            int endRadius = (int) Math.hypot(displayX(), displayY());
            Animator animator = ViewAnimationUtils.createCircularReveal(
                    view,
                    (xPosition),
                    (yPosition),
                    startRadius,
                    endRadius
            );
            animator.setInterpolator(new FastOutLinearInInterpolator());
            animator.setDuration(555);
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        } else {
            int startRadius = (int) Math.hypot(displayX(), displayY());
            int endRadius = 0;
            Animator animator = ViewAnimationUtils.createCircularReveal(
                    view,
                    (xPosition),
                    (yPosition),
                    startRadius,
                    endRadius
            );
            animator.setInterpolator(new FastOutLinearInInterpolator());
            animator.setDuration(333);
            animator.start();
        }
    }

    public boolean splashReveal() {
        return context.getSharedPreferences("theme", Context.MODE_PRIVATE)
                .getBoolean("floatingSplash", true);
    }

    /*Shaping Functions*/
    public Drawable appIcon(String packageName) {
        Drawable icon = null;
        try {
            PackageManager packManager = context.getPackageManager();
            icon = packManager.getApplicationIcon(packageName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (icon == null) {
                try {
                    PackageManager packManager = context.getPackageManager();
                    icon = packManager.getDefaultActivityIcon();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return icon;
    }

    public int shapesImageId() {
        int shapesImageId = 0;
        SharedPreferences sharedPreferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
        switch (sharedPreferences.getInt("iconShape", 0)) {
            case 1:
                shapesImageId = 1;
                break;
            case 2:
                shapesImageId = 2;
                break;
            case 3:
                shapesImageId = 3;
                break;
            case 4:
                shapesImageId = 4;
                break;
            case 5:
                shapesImageId = 5;
                break;
            case 0:
                shapesImageId = 0;
                break;
        }
        return shapesImageId;
    }

    public Drawable shapesDrawables() {
        Drawable shapeDrawable = null;
        switch (shapesImageId()) {
            case 1:
                shapeDrawable = context.getDrawable(R.drawable.droplet_icon);
                break;
            case 2:
                shapeDrawable = context.getDrawable(R.drawable.circle_icon);
                break;
            case 3:
                shapeDrawable = context.getDrawable(R.drawable.square_icon);
                break;
            case 4:
                shapeDrawable = context.getDrawable(R.drawable.squircle_icon);
                break;
            case 5:
                shapeDrawable = context.getDrawable(R.drawable.cut_circle_icon);
                break;
            case 0:
                shapeDrawable = null;
                break;
        }
        return shapeDrawable;
    }

    public ShapesImage initShapesImage(ViewGroup viewGroup, int viewId) {
        ShapesImage shapesImage = (ShapesImage) viewGroup.findViewById(viewId);
        SharedPreferences sharedPreferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
        switch (sharedPreferences.getInt("iconShape", 0)) {
            case 1:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.droplet_icon));
                break;
            case 2:
                shapesImage.setShapeDrawable(ShapesImage.CIRCLE);
                break;
            case 3:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.square_icon));
                break;
            case 4:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.squircle_icon));
                break;
            case 5:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.cut_circle_icon));
                break;
            case 0:
                shapesImage.setShapeDrawable(null);
                break;
        }
        return shapesImage;
    }

    public ShapesImage initShapesImage(View view, int viewId) {
        ShapesImage shapesImage = (ShapesImage) view.findViewById(viewId);
        SharedPreferences sharedPreferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
        switch (sharedPreferences.getInt("iconShape", 0)) {
            case 1:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.droplet_icon));
                break;
            case 2:
                shapesImage.setShapeDrawable(ShapesImage.CIRCLE);
                break;
            case 3:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.square_icon));
                break;
            case 4:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.squircle_icon));
                break;
            case 5:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.cut_circle_icon));
                break;
            case 0:
                shapesImage.setShapeDrawable(null);
                break;
        }
        return shapesImage;
    }

    public ShapesImage initShapesImage(Activity activity, int viewId) {
        ShapesImage shapesImage = (ShapesImage) activity.findViewById(viewId);
        SharedPreferences sharedPreferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
        switch (sharedPreferences.getInt("iconShape", 0)) {
            case 1:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.droplet_icon));
                break;
            case 2:
                shapesImage.setShapeDrawable(ShapesImage.CIRCLE);
                break;
            case 3:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.square_icon));
                break;
            case 4:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.squircle_icon));
                break;
            case 5:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.cut_circle_icon));
                break;
            case 0:
                shapesImage.setShapeDrawable(null);
                break;
        }
        return shapesImage;
    }

    public Drawable shapedAppIcon(String packageName) {
        Drawable appIconDrawable = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("iconShape", 0) == 1
                || sharedPreferences.getInt("iconShape", 0) == 2
                || sharedPreferences.getInt("iconShape", 0) == 3
                || sharedPreferences.getInt("iconShape", 0) == 4
                || sharedPreferences.getInt("iconShape", 0) == 5) {
            Drawable drawableBack = null;
            Drawable drawableFront = null;
            LayerDrawable layerDrawable = null;
            if (returnAPI() >= 26) {
                AdaptiveIconDrawable adaptiveIconDrawable = null;
                try {
                    Drawable tempAppIcon = appIcon(packageName);
                    if (tempAppIcon instanceof AdaptiveIconDrawable) {
                        adaptiveIconDrawable = (AdaptiveIconDrawable) tempAppIcon;
                        drawableBack = adaptiveIconDrawable.getBackground();
                        drawableFront = adaptiveIconDrawable.getForeground();
                        layerDrawable = new LayerDrawable(new Drawable[]{drawableBack, drawableFront});
                        appIconDrawable = layerDrawable;
                    } else {
                        drawableBack = new ColorDrawable(extractDominantColor(tempAppIcon));
                        drawableFront = tempAppIcon;
                        layerDrawable = new LayerDrawable(new Drawable[]{drawableBack, drawableFront});
                        layerDrawable.setPaddingMode(LayerDrawable.PADDING_MODE_NEST);
                        layerDrawable.setLayerInset(1, 35, 35, 35, 35);
                        appIconDrawable = layerDrawable;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Drawable tempAppIcon = appIcon(packageName);
                drawableBack = new ColorDrawable(extractDominantColor(tempAppIcon));
                drawableFront = tempAppIcon;
                layerDrawable = new LayerDrawable(new Drawable[]{drawableBack, drawableFront});
                layerDrawable.setPaddingMode(LayerDrawable.PADDING_MODE_NEST);
                layerDrawable.setLayerInset(1, 35, 35, 35, 35);
                appIconDrawable = layerDrawable;
            }
        } else if (sharedPreferences.getInt("iconShape", 0) == 0) {
            appIconDrawable = appIcon(packageName);
        }
        return appIconDrawable;
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof VectorDrawable) {
            VectorDrawable vectorDrawable = (VectorDrawable) drawable;
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        } else if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                bitmap = bitmapDrawable.getBitmap();
            }
        } else if (drawable instanceof LayerDrawable) {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return bitmap;
    }

    public Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public Drawable shapedAppIcon(ActivityInfo activityInfo) {
        Drawable appIconDrawable = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.getInt("iconShape", 0) == 1
                || sharedPreferences.getInt("iconShape", 0) == 2
                || sharedPreferences.getInt("iconShape", 0) == 3
                || sharedPreferences.getInt("iconShape", 0) == 4
                || sharedPreferences.getInt("iconShape", 0) == 4) {
            Drawable drawableBack = null;
            Drawable drawableFront = null;
            LayerDrawable layerDrawable = null;
            if (returnAPI() >= 26) {
                AdaptiveIconDrawable adaptiveIconDrawable = null;
                try {
                    Drawable tempAppIcon = appIcon(activityInfo);
                    if (tempAppIcon instanceof AdaptiveIconDrawable) {
                        adaptiveIconDrawable = (AdaptiveIconDrawable) tempAppIcon;
                        drawableBack = adaptiveIconDrawable.getBackground();
                        drawableFront = adaptiveIconDrawable.getForeground();
                        layerDrawable = new LayerDrawable(new Drawable[]{drawableBack, drawableFront});
                        appIconDrawable = layerDrawable;
                    } else {
                        drawableBack = new ColorDrawable(extractDominantColor(tempAppIcon));
                        drawableFront = tempAppIcon;
                        layerDrawable = new LayerDrawable(new Drawable[]{drawableBack, drawableFront});
                        layerDrawable.setPaddingMode(LayerDrawable.PADDING_MODE_NEST);
                        layerDrawable.setLayerInset(1, 35, 35, 35, 35);
                        appIconDrawable = layerDrawable;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Drawable tempAppIcon = appIcon(activityInfo);
                drawableBack = new ColorDrawable(extractDominantColor(tempAppIcon));
                drawableFront = tempAppIcon;
                layerDrawable = new LayerDrawable(new Drawable[]{drawableBack, drawableFront});
                layerDrawable.setPaddingMode(LayerDrawable.PADDING_MODE_NEST);
                layerDrawable.setLayerInset(1, 35, 35, 35, 35);
                appIconDrawable = layerDrawable;
            }
        } else if (sharedPreferences.getInt("iconShape", 0) == 0) {
            appIconDrawable = appIcon(activityInfo);
        }
        return appIconDrawable;
    }

    public Drawable appIcon(ActivityInfo activityInfo) {
        Drawable icon = null;
        try {
            icon = activityInfo.loadIcon(context.getPackageManager());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (icon == null) {
                try {
                    PackageManager packManager = context.getPackageManager();
                    icon = packManager.getDefaultActivityIcon();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return icon;
    }

    public static class DisplaySection {
        public static final int TopLeft = 1;
        public static final int TopRight = 2;
        public static final int BottomLeft = 3;
        public static final int BottomRight = 4;
    }

    public class HeartBeat {
        String packageName;
        View viewToBeat;
        Animator.AnimatorListener scaleDownListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewToBeat.animate().scaleXBy(0.33f).scaleYBy(0.33f).setDuration(133).setListener(scaleUpListener);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        };
        Animator.AnimatorListener scaleUpListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewToBeat.animate().scaleXBy(-0.33f).scaleYBy(-0.33f).setDuration(233).setListener(scaleDownListener);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        };

        public HeartBeat(String packageName, View viewToBeat) {
            this.packageName = packageName;
            this.viewToBeat = viewToBeat;
            this.viewToBeat.animate().scaleXBy(0.33f).scaleYBy(0.33f).setDuration(133).setListener(scaleUpListener);
        }
    }

    /*Firebase Remote Config*/
    public String versionCodeRemoteConfigKey() {
        String versionCodeKey = null;
        if (readDefaultPreference("JoinedBetaProgrammer", false)) {
            versionCodeKey = context.getString(R.string.BETAintegerVersionCodeNewUpdateWear);
        } else {
            versionCodeKey = context.getString(R.string.integerVersionCodeNewUpdateWear);
        }
        return versionCodeKey;
    }
}