package net.geekstools.floatshort.PRO.Util.Functions;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.webkit.DownloadListener;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.ColorUtils;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.palette.graphics.Palette;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Actions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.geekstools.floatshort.PRO.App_Unlimited_Bluetooth;
import net.geekstools.floatshort.PRO.App_Unlimited_Gps;
import net.geekstools.floatshort.PRO.App_Unlimited_HIS;
import net.geekstools.floatshort.PRO.App_Unlimited_Nfc;
import net.geekstools.floatshort.PRO.App_Unlimited_Shortcuts;
import net.geekstools.floatshort.PRO.App_Unlimited_Time;
import net.geekstools.floatshort.PRO.App_Unlimited_Wifi;
import net.geekstools.floatshort.PRO.Automation.Alarms.AlarmAlertBroadcastReceiver;
import net.geekstools.floatshort.PRO.Automation.Alarms.SetupAlarms;
import net.geekstools.floatshort.PRO.Automation.Apps.AppAutoFeatures;
import net.geekstools.floatshort.PRO.Automation.Categories.CategoryAutoFeatures;
import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.BuildConfig;
import net.geekstools.floatshort.PRO.Category.FoldersHandler;
import net.geekstools.floatshort.PRO.Category_Unlimited_Bluetooth;
import net.geekstools.floatshort.PRO.Category_Unlimited_Floating;
import net.geekstools.floatshort.PRO.Category_Unlimited_Gps;
import net.geekstools.floatshort.PRO.Category_Unlimited_Nfc;
import net.geekstools.floatshort.PRO.Category_Unlimited_Time;
import net.geekstools.floatshort.PRO.Category_Unlimited_Wifi;
import net.geekstools.floatshort.PRO.CheckPoint;
import net.geekstools.floatshort.PRO.Configurations;
import net.geekstools.floatshort.PRO.Notifications.NavAdapter.PopupShortcutsNotification;
import net.geekstools.floatshort.PRO.Notifications.NotificationListener;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Shortcuts.HybridViewOff;
import net.geekstools.floatshort.PRO.Util.IAP.InAppBilling;
import net.geekstools.floatshort.PRO.Util.InteractionObserver.InteractionObserver;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.OpenApplications;
import net.geekstools.floatshort.PRO.Util.RemoteTask.FloatingWidgetHomeScreenShortcuts;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RemoteController;
import net.geekstools.floatshort.PRO.Util.SettingGUI.SettingGUIDark;
import net.geekstools.floatshort.PRO.Util.SettingGUI.SettingGUILight;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;
import net.geekstools.floatshort.PRO.Util.UI.FloatingSplash;
import net.geekstools.floatshort.PRO.Util.UI.PopupOptionsFloatingCategory;
import net.geekstools.floatshort.PRO.Util.UI.PopupOptionsFloatingShortcuts;
import net.geekstools.floatshort.PRO.Widget.RoomDatabase.WidgetDataInterface;
import net.geekstools.floatshort.PRO.Widget.WidgetConfigurations;
import net.geekstools.floatshort.PRO.Widget_Unlimited_Floating;
import net.geekstools.imageview.customshapes.ShapesImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.ACCESSIBILITY_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

public class FunctionsClass {
    int API;
    Activity activity;
    Context context;
    ArrayList<NavDrawerItem> navDrawerItems;

    /*Free Functions*/
    FirebaseRemoteConfig firebaseRemoteConfig;

    public static InterstitialAd interstitialAdApps, interstitialAdWidgetShortcuts,
            interstitialAdSettingsGUI;

    /*Free Floating Widgets*/
    public static String packageNameWidget, widgetProviderClassName, shortcutNameWidget;
    public static Drawable widgetPreviewDrawable;

    public FunctionsClass(final Context context, final Activity activity) {
        this.context = context;
        this.activity = activity;
        API = Build.VERSION.SDK_INT;

        loadSavedColor();
        checkLightDarkTheme();

        String ClassName = activity.getComponentName().getClassName();

        if (ClassName.contains(HybridViewOff.class.getSimpleName())
                || ClassName.contains(FoldersHandler.class.getSimpleName())
                || ClassName.contains(CategoryAutoFeatures.class.getSimpleName())
                || ClassName.contains(AppAutoFeatures.class.getSimpleName())) {
            if (LoadAds()) {
                if (returnAPI() >= 23) {
                    adBlockerDetection(activity);
                }

                if (interstitialAdSettingsGUI == null) {
                    interstitialAdSettingsGUI = new InterstitialAd(context);
                    interstitialAdSettingsGUI.setImmersiveMode(true);
                    interstitialAdSettingsGUI.setAdUnitId(context.getString(R.string.AdUnitActivities));
                }
                if (interstitialAdSettingsGUI.isLoaded()) {

                } else {
                    interstitialAdSettingsGUI.loadAd(new AdRequest.Builder()
                            .addTestDevice("CDCAA1F20B5C9C948119E886B31681DE")
                            .addTestDevice("D101234A6C1CF51023EE5815ABC285BD")
                            .addTestDevice("65B5827710CBE90F4A99CE63099E524C")
                            .addTestDevice("5901E5EE74F9B6652E05621140664A54")
                            .addTestDevice("DD428143B4772EC7AA87D1E2F9DA787C")
                            .addTestDevice("F54D998BCE077711A17272B899B44798")
                            .build());
                }

                FunctionsClassDebug.Companion.PrintDebug("*** ClassName == " + ClassName + " ***");
                MobileAds.initialize(context, context.getString(R.string.AdAppId));
                final AdRequest adRequestinterstitialAd = new AdRequest.Builder()
                        .addTestDevice("CDCAA1F20B5C9C948119E886B31681DE")
                        .addTestDevice("D101234A6C1CF51023EE5815ABC285BD")
                        .addTestDevice("65B5827710CBE90F4A99CE63099E524C")
                        .addTestDevice("5901E5EE74F9B6652E05621140664A54")
                        .addTestDevice("DD428143B4772EC7AA87D1E2F9DA787C")
                        .addTestDevice("F54D998BCE077711A17272B899B44798")
                        .build();
                if (interstitialAdApps == null) {
                    interstitialAdApps = new InterstitialAd(context);
                    interstitialAdApps.setImmersiveMode(true);
                    interstitialAdApps.setAdUnitId(context.getString(R.string.AdUnitActivities));
                }
                if (interstitialAdApps.isLoaded()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            interstitialAdApps.show();
                        }
                    }, 313);
                } else {
                    interstitialAdApps.loadAd(adRequestinterstitialAd);
                }
                interstitialAdApps.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        FunctionsClassDebug.Companion.PrintDebug("*** InterstitialAd Loaded | Activities ***");

                        PublicVariable.interstitialAdLoaded = true;
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCodeAdRequest) {
                        FunctionsClassDebug.Companion.PrintDebug("*** AdFailedToLoad " + errorCodeAdRequest + " | Activities ***");

                        if (PublicVariable.eligibleLoadShowAds) {
                            interstitialAdApps.loadAd(adRequestinterstitialAd);
                        }
                    }

                    @Override
                    public void onAdOpened() {
                        FunctionsClassDebug.Companion.PrintDebug("*** OnAdOpened | Activities ***");

                    }

                    @Override
                    public void onAdLeftApplication() {

                    }

                    @Override
                    public void onAdClosed() {
                        try {
                            PublicVariable.interstitialAdLoaded = false;

                            if (PublicVariable.targetClassAfterAds != null) {
                                Intent intentTarget = new Intent();
                                if (PublicVariable.targetClassAfterAds.equals(FoldersHandler.class.getSimpleName())) {
                                    intentTarget.setClass(context, FoldersHandler.class);
                                } else if (PublicVariable.targetClassAfterAds.equals(SettingGUIDark.class.getSimpleName())) {
                                    intentTarget.setClass(context, SettingGUIDark.class);
                                } else if (PublicVariable.targetClassAfterAds.equals(SettingGUILight.class.getSimpleName())) {
                                    intentTarget.setClass(context, SettingGUILight.class);
                                } else {
                                    Intent hybridViewOff = new Intent(context, HybridViewOff.class);
                                    hybridViewOff.putExtra("freq", PublicVariable.freqApps);
                                    hybridViewOff.putExtra("num", PublicVariable.freqLength);
                                    hybridViewOff.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                                    activity.startActivity(hybridViewOff);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            activity.finish();
                                        }
                                    }, 313);
                                }
                                intentTarget.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intentTarget);
                            } else {
                                interstitialAdApps.loadAd(adRequestinterstitialAd);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            context.startActivity(new Intent(context, Configurations.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }
                });
            }
        }

        if (ClassName.contains(SettingGUILight.class.getSimpleName())
                || ClassName.contains(SettingGUIDark.class.getSimpleName())) {
            final PreferenceActivity preferenceActivity = ((PreferenceActivity) activity);

            firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_default);
            firebaseRemoteConfig.fetch(0)
                    .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                firebaseRemoteConfig.activate().addOnSuccessListener(new OnSuccessListener<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        if (firebaseRemoteConfig.getBoolean("boolean_new_data")) {
                                            LayerDrawable layerDrawableLoadLogo = (LayerDrawable) context.getDrawable(R.drawable.ic_launcher_layer_logo);
                                            BitmapDrawable gradientDrawableLoadLogo = (BitmapDrawable) layerDrawableLoadLogo.findDrawableByLayerId(R.id.ic_launcher_back_layer);
                                            BitmapDrawable drawableLoadLogo = (BitmapDrawable) layerDrawableLoadLogo.findDrawableByLayerId(R.id.ic_launcher_back_layer);
                                            gradientDrawableLoadLogo.setTint(PublicVariable.primaryColorOpposite);
                                            layerDrawableLoadLogo.setDrawableByLayerId(R.id.ic_launcher_front_layer, context.getDrawable(R.drawable.ic_launcher_new));

                                            Preference proUpgrade = (Preference) preferenceActivity.findPreference("proUpgrade");
                                            proUpgrade.setIcon(layerDrawableLoadLogo);
                                            proUpgrade.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                                                @Override
                                                public boolean onPreferenceClick(Preference preference) {
                                                    proVersionDialogue(preferenceActivity);

                                                    return true;
                                                }
                                            });
                                            proUpgrade.setIcon(layerDrawableLoadLogo);
                                            proUpgrade.setSummary(Html.fromHtml(firebaseRemoteConfig.getString("string_new_upgrade_info_summary")));
                                        } else {

                                        }
                                    }
                                });
                            } else {

                            }
                        }
                    });

            if (LoadAds()) {
                FunctionsClassDebug.Companion.PrintDebug("*** ClassName == " + ClassName + " ***");
                MobileAds.initialize(context, context.getString(R.string.AdAppId));

                final AdRequest adRequestinterstitialAd = new AdRequest.Builder()
                        .addTestDevice("CDCAA1F20B5C9C948119E886B31681DE")
                        .addTestDevice("D101234A6C1CF51023EE5815ABC285BD")
                        .addTestDevice("65B5827710CBE90F4A99CE63099E524C")
                        .addTestDevice("5901E5EE74F9B6652E05621140664A54")
                        .addTestDevice("DD428143B4772EC7AA87D1E2F9DA787C")
                        .addTestDevice("F54D998BCE077711A17272B899B44798")
                        .build();

                if (interstitialAdSettingsGUI == null) {
                    interstitialAdSettingsGUI = new InterstitialAd(context);
                    interstitialAdSettingsGUI.setImmersiveMode(true);
                    interstitialAdSettingsGUI.setAdUnitId(context.getString(R.string.AdUnitActivities));
                }
                if (interstitialAdSettingsGUI.isLoaded()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            interstitialAdSettingsGUI.show();
                        }
                    }, 313);
                } else {
                    interstitialAdSettingsGUI.loadAd(adRequestinterstitialAd);
                }

                interstitialAdSettingsGUI.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        FunctionsClassDebug.Companion.PrintDebug("*** InterstitialAd Loaded | SettingGUI ***");

                        PublicVariable.interstitialAdLoaded = true;
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCodeAdRequest) {
                        FunctionsClassDebug.Companion.PrintDebug("*** AdFailedToLoad " + errorCodeAdRequest + " | SettingGUI ***");

                        if (PublicVariable.eligibleLoadShowAds) {
                            interstitialAdSettingsGUI.loadAd(adRequestinterstitialAd);
                        }
                    }

                    @Override
                    public void onAdOpened() {
                        FunctionsClassDebug.Companion.PrintDebug("*** OnAdOpened | SettingGUI ***");
                    }

                    @Override
                    public void onAdLeftApplication() {

                    }

                    @Override
                    public void onAdClosed() {
                        PublicVariable.interstitialAdLoaded = false;
                    }
                });
            }
        }

        if (ClassName.contains(WidgetConfigurations.class.getSimpleName())) {
            FunctionsClassDebug.Companion.PrintDebug("*** ClassName == " + ClassName + " ***");
            MobileAds.initialize(context, context.getString(R.string.AdAppId));

            final AdRequest adRequestinterstitialAd = new AdRequest.Builder()
                    .addTestDevice("CDCAA1F20B5C9C948119E886B31681DE")
                    .addTestDevice("D101234A6C1CF51023EE5815ABC285BD")
                    .addTestDevice("65B5827710CBE90F4A99CE63099E524C")
                    .addTestDevice("5901E5EE74F9B6652E05621140664A54")
                    .addTestDevice("DD428143B4772EC7AA87D1E2F9DA787C")
                    .addTestDevice("F54D998BCE077711A17272B899B44798")
                    .build();
            interstitialAdWidgetShortcuts = new InterstitialAd(context);
            interstitialAdWidgetShortcuts.setImmersiveMode(true);
            interstitialAdWidgetShortcuts.setAdUnitId(context.getString(R.string.AdUnitFloatingWidgetShortcuts));
            interstitialAdWidgetShortcuts.loadAd(adRequestinterstitialAd);
            interstitialAdWidgetShortcuts.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    FunctionsClassDebug.Companion.PrintDebug("*** InterstitialAd Loaded | WidgetConfigurations ***");
                }

                @Override
                public void onAdFailedToLoad(int errorCodeAdRequest) {
                    FunctionsClassDebug.Companion.PrintDebug("*** AdFailedToLoad " + errorCodeAdRequest + " | WidgetConfigurations ***");

                    if (PublicVariable.eligibleLoadShowAds) {
                        interstitialAdWidgetShortcuts.loadAd(adRequestinterstitialAd);
                    }
                }

                @Override
                public void onAdOpened() {

                }

                @Override
                public void onAdLeftApplication() {

                }

                @Override
                public void onAdClosed() {
                    if (PublicVariable.eligibleLoadShowAds) {
                        interstitialAdWidgetShortcuts.loadAd(adRequestinterstitialAd);
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                widgetToHomeScreen(FloatingWidgetHomeScreenShortcuts.class, packageNameWidget, shortcutNameWidget, widgetPreviewDrawable, widgetProviderClassName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 555);
                }
            });
        }
    }

    public static class InterstitialAdPlace {
        public static final int AppsList = 1;
        public static final int SettingGUI = 2;
    }

    public void ShowAds(int AdsPlace, String targetClassAfterAds) {
        PublicVariable.targetClassAfterAds = targetClassAfterAds;
        switch (AdsPlace) {
            case InterstitialAdPlace.AppsList: {
                if (interstitialAdApps != null) {
                    if (interstitialAdApps.isLoaded() && PublicVariable.eligibleLoadShowAds) {
                        interstitialAdApps.show();
                    }
                }

                break;
            }
            case InterstitialAdPlace.SettingGUI: {
                if (interstitialAdSettingsGUI != null) {
                    if (interstitialAdSettingsGUI.isLoaded() && PublicVariable.eligibleLoadShowAds) {
                        interstitialAdSettingsGUI.show();
                    }
                }

                break;
            }
        }
    }

    public void ShowAds(int AdsPlace) {
        PublicVariable.targetClassAfterAds = null;
        switch (AdsPlace) {
            case InterstitialAdPlace.AppsList: {
                if (interstitialAdApps != null) {
                    if (interstitialAdApps.isLoaded() && PublicVariable.eligibleLoadShowAds) {
                        interstitialAdApps.show();
                    }
                }

                break;
            }
            case InterstitialAdPlace.SettingGUI: {
                if (interstitialAdSettingsGUI != null) {
                    if (interstitialAdSettingsGUI.isLoaded() && PublicVariable.eligibleLoadShowAds) {
                        interstitialAdSettingsGUI.show();
                    }
                }

                break;
            }
        }
    }

    public boolean LoadAds() {
        boolean Ads = true;
        if (readPreference(".SubscribedItem", "remove.ads", false)) {
            Ads = false;
        } else {
            Ads = true;
        }
        return Ads;
    }

    public boolean adBlockerDetection(final Activity activity) {
        boolean adBlocked = false;
        try {
            if (networkConnection()) {
                final WebView webView = new WebView(context);
                webView.loadUrl("https://www.admob.com");
                webView.setDownloadListener(new DownloadListener() {
                    @Override
                    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                    }
                });
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                    }

                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        FunctionsClassDebug.Companion.PrintDebug("### " + request.getMethod() + " ### " + error.getDescription() + " # " + error.getErrorCode());

                        if (error.getDescription().equals("net::ERR_CONNECTION_REFUSED") || error.getErrorCode() == WebViewClient.ERROR_CONNECT) {
                            adBlockDialogue(activity);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adBlocked;
    }

    public void proVersionDialogue(final Activity activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(
                Html.fromHtml("<font color='" + context.getColor(R.color.default_color) + "'>" +
                        context.getString(R.string.pro_version) + "</font>"));

        if (firebaseRemoteConfig.getBoolean("boolean_new_data")) {
            alertDialog.setMessage(
                    Html.fromHtml("<font color='" + PublicVariable.colorLightDarkOpposite + "'>" +
                            firebaseRemoteConfig.getString("string_new_upgrade_info") + "</font>"));
        } else {
            alertDialog.setMessage(
                    Html.fromHtml("<font color='" + PublicVariable.colorLightDarkOpposite + "'>" +
                            context.getString(R.string.upgradeDesc) + "</font>"));
        }

        alertDialog.setIcon(context.getDrawable(R.drawable.ic_launcher));
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(context.getString(R.string.upgradeNow), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent p = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.play_store_link) + context.getPackageName() + context.getString(R.string.dot_pro)));
                p.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(p);

                Toast(context.getString(R.string.thanks), Gravity.TOP);
                dialog.dismiss();
            }
        });
        String IAB;
        if (!LoadAds()) {
            IAB = context.getString(R.string.donate);
        } else {
            IAB = context.getString(R.string.ads);
        }
        alertDialog.setNeutralButton(IAB, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivity(new Intent(context, InAppBilling.class));

                dialog.dismiss();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        alertDialog.show();
    }

    public void adBlockDialogue(final Activity activity) {
        context = activity.getApplicationContext();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        if (PublicVariable.themeLightDark == true) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        } else if (PublicVariable.themeLightDark == false) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
        }
        alertDialog.setTitle(Html.fromHtml(context.getString(R.string.ads_title)));
        alertDialog.setMessage(Html.fromHtml(context.getString(R.string.ads_msg)));
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton(context.getString(R.string.buy), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent r = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.play_store_link) + context.getPackageName() + context.getString(R.string.dot_pro)));
                r.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(r);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        uninstallApp(context.getPackageName());
                    }
                }, 2333);
            }
        });
        alertDialog.setNegativeButton(context.getString(R.string.ads), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(context, SettingGUILight.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
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
                if (PublicVariable.themeLightDark == true) {
                    builder = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
                } else if (PublicVariable.themeLightDark == false) {
                    builder = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
                }
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
                            email.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.feedback_tag) + " [" + appVersionName(context.getPackageName()) + "] ");
                            email.putExtra(Intent.EXTRA_TEXT, textMsg);
                            email.setType("text/*");
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
                    context.startActivity(
                            context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    );
                }
            }, 300);
        }
    }

    public String getRemoveAllAdsPrice() {
        String infoPrice = context.getString(R.string.minimumLocalPrice);
        try {
            infoPrice = context.getString(getStringIdentifier(context, "price_" + getCountryIso().toUpperCase()));
        } catch (Resources.NotFoundException ignore) {
            ignore.printStackTrace();
            infoPrice = context.getString(R.string.minimumLocalPrice);
        }

        FunctionsClassDebug.Companion.PrintDebug("*** " + getCountryIso() + " | " + infoPrice + " ***");
        return infoPrice;
    }

    public int getStringIdentifier(Context context, String variableName) {
        return context.getResources().getIdentifier(variableName, "string", context.getPackageName());
    }

    public void Preferences(boolean visibility) {
        final Button preferencesView = (Button) activity.findViewById(R.id.preferences);
        if (visibility == true && !preferencesView.isShown()) {
            LayerDrawable drawPrefAction = (LayerDrawable) context.getDrawable(R.drawable.draw_pref_action);
            Drawable backPrefAction = drawPrefAction.findDrawableByLayerId(R.id.backtemp);
            backPrefAction.setTint(PublicVariable.primaryColorOpposite);
            preferencesView.setBackground(drawPrefAction);

            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            preferencesView.startAnimation(animation);
            preferencesView.setVisibility(View.VISIBLE);
        } else if (visibility == false) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
            preferencesView.startAnimation(animation);
            preferencesView.setVisibility(View.INVISIBLE);
        }
        preferencesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublicVariable.actionCenter = false;
                PublicVariable.recoveryCenter = false;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(activity, preferencesView, "transition");
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass(activity, SettingGUIDark.class);
                        if (PublicVariable.themeLightDark) {
                            intent.setClass(activity, SettingGUILight.class);
                        } else if (!PublicVariable.themeLightDark) {
                            intent.setClass(activity, SettingGUIDark.class);
                        }
                        activity.startActivity(intent, activityOptionsCompat.toBundle());
                    }
                }, 113);
            }
        });
    }
    /*Free Functions*/

    public FunctionsClass(Context context) {
        this.context = context;
        API = Build.VERSION.SDK_INT;

        loadSavedColor();
        checkLightDarkTheme();
    }

    public static boolean ComponentEnabled(PackageManager packageManager, String packageName, String className) {
        ComponentName componentName = new ComponentName(packageName, className);
        int componentEnabledSetting = packageManager.getComponentEnabledSetting(componentName);

        switch (componentEnabledSetting) {
            case PackageManager.COMPONENT_ENABLED_STATE_DISABLED:
                return false;
            case PackageManager.COMPONENT_ENABLED_STATE_ENABLED:
                return true;
            case PackageManager.COMPONENT_ENABLED_STATE_DEFAULT:
            default:
                // We need to get the application info to get the component's default state
                try {
                    PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES
                            | PackageManager.GET_RECEIVERS
                            | PackageManager.GET_SERVICES
                            | PackageManager.GET_PROVIDERS
                            | PackageManager.GET_DISABLED_COMPONENTS);

                    List<ComponentInfo> components = new ArrayList<>();
                    if (packageInfo.activities != null)
                        Collections.addAll(components, packageInfo.activities);
                    if (packageInfo.services != null)
                        Collections.addAll(components, packageInfo.services);
                    if (packageInfo.providers != null)
                        Collections.addAll(components, packageInfo.providers);

                    for (ComponentInfo componentInfo : components) {
                        if (componentInfo.name.equals(className)) {
                            return componentInfo.isEnabled();
                        }
                    }

                    // the component is not declared in the AndroidManifest
                    return false;
                } catch (PackageManager.NameNotFoundException e) {
                    // the package isn't installed on the device
                    return false;
                }
        }
    }

    /*Database & Indexing*/
    public void IndexAppInfoShortcuts(final String contentAppIndex) throws Exception {
        Uri BASE_URL =
                Uri.parse("https://www.geeksempire.net/createshortcuts.html/");
        Indexable articleToIndex = new Indexable.Builder()
                .setName(contentAppIndex)
                .setUrl(String.valueOf(BASE_URL.buildUpon().appendPath(contentAppIndex).build()))
                .build();

        Task<Void> updateTask = FirebaseAppIndex.getInstance().update(articleToIndex);
        updateTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
        updateTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

        Task<Void> startTask = FirebaseUserActions.getInstance()
                .start(getAction(contentAppIndex,
                        String.valueOf(BASE_URL.buildUpon().appendPath(contentAppIndex).build())));
        startTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
        startTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void IndexAppInfoCategory(final String contentAppIndex) throws Exception {
        Uri BASE_URL =
                Uri.parse("https://www.geeksempire.net/createcategory.html/");
        Indexable articleToIndex = new Indexable.Builder()
                .setName(contentAppIndex)
                .setUrl(String.valueOf(BASE_URL.buildUpon().appendPath(contentAppIndex).build()))
                .build();

        Task<Void> updateTask = FirebaseAppIndex.getInstance().update(articleToIndex);
        updateTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
        updateTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

        Task<Void> startTask = FirebaseUserActions.getInstance()
                .start(getAction(contentAppIndex,
                        String.valueOf(BASE_URL.buildUpon().appendPath(contentAppIndex).build())));
        startTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
        startTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void endIndexAppInfo() {
        try {
            FirebaseUserActions.getInstance().end(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private com.google.firebase.appindexing.Action getAction(String titleForAction, String urlForAction) {
        return
                Actions.newView(titleForAction, urlForAction);
    }

    /*SuperShortcuts*/
    public void addAppShortcuts() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            try {
                final ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
                shortcutManager.removeAllDynamicShortcuts();
                List<ShortcutInfo> shortcutInfos = new ArrayList<ShortcutInfo>();
                shortcutInfos.clear();
                Intent intent = new Intent();
                if (context.getFileStreamPath("Frequently").exists()) {
                    List<String> appShortcuts = Arrays.asList(readFileLine("Frequently"));
                    for (int i = 0; i < 4; i++) {
                        try {
                            intent.setAction("Remote_Single_Floating_Shortcuts");
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("packageName", appShortcuts.get(i));

                            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(context, (appShortcuts.get(i)))
                                    .setShortLabel(appName(appShortcuts.get(i)))
                                    .setLongLabel(appName(appShortcuts.get(i)))
                                    //.setIcon(Icon.createWithBitmap(appIconBitmap(appShortcuts.get(i))))
                                    .setIcon(Icon.createWithBitmap(getAppIconBitmapCustomIcon(appShortcuts.get(i))))
                                    .setIntent(intent)
                                    .setRank(i)
                                    .build();

                            shortcutInfos.add(shortcutInfo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (context.getFileStreamPath(".uFile").exists()) {
                    if (countLineInnerFile(".uFile") > 0) {
                        List<String> appShortcuts = Arrays.asList(readFileLine(".uFile"));
                        int countAppShortcut = 4;
                        if (countLineInnerFile(".uFile") < 4) {
                            countAppShortcut = countLineInnerFile(".uFile");
                        }
                        for (int i = 0; i < countAppShortcut; i++) {
                            try {
                                intent.setAction("Remote_Single_Floating_Shortcuts");
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("packageName", appShortcuts.get(i));

                                ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(context, (appShortcuts.get(i)))
                                        .setShortLabel(appName(appShortcuts.get(i)))
                                        .setLongLabel(appName(appShortcuts.get(i)))
                                        //.setIcon(Icon.createWithBitmap(appIconBitmap(appShortcuts.get(i))))
                                        .setIcon(Icon.createWithBitmap(getAppIconBitmapCustomIcon(appShortcuts.get(i))))
                                        .setIntent(intent)
                                        .setRank(i)
                                        .build();

                                shortcutInfos.add(shortcutInfo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                try {
                    String dynamicLabel = null;
                    if (PreferenceManager.getDefaultSharedPreferences(context).getString("boot", "1").equals("0")) {
                        shortcutManager.addDynamicShortcuts(shortcutInfos);
                        return;
                    } else if (PreferenceManager.getDefaultSharedPreferences(context).getString("boot", "1").equals("1")) {
                        intent.setAction("Remote_Recover_Shortcuts");
                        dynamicLabel = context.getString(R.string.shortcuts);
                    } else if (PreferenceManager.getDefaultSharedPreferences(context).getString("boot", "1").equals("2")) {
                        intent.setAction("Remote_Recover_Categories");
                        dynamicLabel = context.getString(R.string.floatingCategory);
                    } else if (PreferenceManager.getDefaultSharedPreferences(context).getString("boot", "1").equals("3")) {
                        intent.setAction("Remote_Recover_All");
                        dynamicLabel = context.getString(R.string.recover_all);
                    }
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    LayerDrawable drawCategory = (LayerDrawable) context.getDrawable(R.drawable.draw_recovery_popup);
                    Drawable shapeTempDrawable = shapesDrawables();
                    if (shapeTempDrawable != null) {
                        Drawable frontDrawable = context.getDrawable(R.drawable.w_recovery_popup).mutate();
                        frontDrawable.setTint(Color.WHITE);
                        shapeTempDrawable.setTint(PublicVariable.primaryColor);
                        drawCategory.setDrawableByLayerId(R.id.backtemp, shapeTempDrawable);
                        drawCategory.setDrawableByLayerId(R.id.fronttemp, frontDrawable);
                    } else {
                        shapeTempDrawable = new ColorDrawable(Color.TRANSPARENT);
                        Drawable frontDrawable = context.getDrawable(R.drawable.w_recovery_popup).mutate();
                        frontDrawable.setTint(context.getColor(R.color.default_color));
                        shapeTempDrawable.setTint(PublicVariable.primaryColor);
                        drawCategory.setDrawableByLayerId(R.id.backtemp, shapeTempDrawable);
                        drawCategory.setDrawableByLayerId(R.id.fronttemp, frontDrawable);
                    }
                    Bitmap recoveryBitmap = Bitmap
                            .createBitmap(drawCategory.getIntrinsicWidth(), drawCategory.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    drawCategory.setBounds(0, 0, drawCategory.getIntrinsicWidth(), drawCategory.getIntrinsicHeight());
                    drawCategory.draw(new Canvas(recoveryBitmap));

                    ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(context, dynamicLabel)
                            .setShortLabel(dynamicLabel)
                            .setLongLabel(dynamicLabel)
                            .setIcon(Icon.createWithBitmap(recoveryBitmap))
                            .setIntent(intent)
                            .setRank(5)
                            .build();

                    shortcutInfos.add(shortcutInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                shortcutManager.addDynamicShortcuts(shortcutInfos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clearDynamicShortcuts() {
        ShortcutManager shortcutManager = null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutManager = context.getSystemService(ShortcutManager.class);
            shortcutManager.removeAllDynamicShortcuts();
        }
    }

    /*Unlimited Shortcuts Function*/
    public void runUnlimitedShortcutsService(String packageName) {

        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, CheckPoint.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        try {
            PublicVariable.floatingCounter++;
            PublicVariable.shortcutsCounter++;
            PublicVariable.FloatingShortcuts.add(PublicVariable.shortcutsCounter, packageName);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();

            PublicVariable.floatingCounter = PublicVariable.floatingCounter + 1;
            PublicVariable.shortcutsCounter = PublicVariable.shortcutsCounter + 1;
            PublicVariable.FloatingShortcuts.add(PublicVariable.shortcutsCounter, packageName);
        }

        Intent u = new Intent(context, App_Unlimited_Shortcuts.class);
        u.putExtra("pack", packageName);
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

    public void saveUnlimitedShortcutsService(String packageName) {
        boolean duplicated = false;
        String fileName = ".uFile";
        File uFile = context.getFileStreamPath(".uFile");
        if (!uFile.exists()) {
            saveFileAppendLine(fileName, packageName);
        } else if (uFile.exists()) {
            int countLine = countLineInnerFile(fileName);
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

    public void runUnlimitedWifi(String packageName) {

        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, CheckPoint.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        PublicVariable.floatingCounter++;

        Intent u = new Intent(context, App_Unlimited_Wifi.class);
        u.putExtra("pack", packageName);
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

    public void runUnlimitedBluetooth(String packageName) {

        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, CheckPoint.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        PublicVariable.floatingCounter++;

        Intent u = new Intent(context, App_Unlimited_Bluetooth.class);
        u.putExtra("pack", packageName);
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

    public void runUnlimitedGps(String packageName) {

        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, CheckPoint.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        PublicVariable.floatingCounter++;

        Intent u = new Intent(context, App_Unlimited_Gps.class);
        u.putExtra("pack", packageName);
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

    public void runUnlimitedNfc(String packageName) {

        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, CheckPoint.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        PublicVariable.floatingCounter++;

        Intent u = new Intent(context, App_Unlimited_Nfc.class);
        u.putExtra("pack", packageName);
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

    public void runUnlimitedTime(String packageName) {

        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, CheckPoint.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        PublicVariable.floatingCounter++;

        Intent u = new Intent(context, App_Unlimited_Time.class);
        u.putExtra("pack", packageName);
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

    public void runUnlimitedShortcutsServiceHIS(String packageName, String className) {

        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, CheckPoint.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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

    public void PopupOptionShortcuts(View anchorView, final String packageName, String classNameCommand, int startId, int X, int Y) {
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

    public void PopupOptionShortcuts(View anchorView, final String packageName, String className, String classNameCommand,
                                     int startId,
                                     int X, int Y) {
        try {
            Intent popupOptionsShortcuts = new Intent(context, PopupOptionsFloatingShortcuts.class);
            popupOptionsShortcuts.putExtra("packageName", packageName);
            popupOptionsShortcuts.putExtra("className", className);
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

    /*Category Function*/
    public void runUnlimitedCategoryService(String categoryName) {

        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, CheckPoint.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        try {
            PublicVariable.floatingCounter++;
            PublicVariable.floatingCategoryCounter_category++;
            PublicVariable.categoriesCounter++;
            PublicVariable.FloatingCategories.add(PublicVariable.categoriesCounter, categoryName);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();

            PublicVariable.floatingCounter = PublicVariable.floatingCounter + 1;
            PublicVariable.floatingCategoryCounter_category = PublicVariable.floatingCategoryCounter_category + 1;
            PublicVariable.categoriesCounter = PublicVariable.categoriesCounter + 1;
            PublicVariable.FloatingCategories.add(PublicVariable.categoriesCounter, categoryName);
        }

        Intent c = new Intent(context, Category_Unlimited_Floating.class);
        c.putExtra("categoryName", categoryName);
        c.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(c);
        if (PublicVariable.floatingCounter == 1) {
            if (API < 26) {
                context.startService(new Intent(context, BindServices.class));
            } else {
                context.startForegroundService(new Intent(context, BindServices.class));
            }
        }
    }

    public void runUnlimitedCategoryWifi(String categoryName, String[] categoryNamePackages) {

        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, CheckPoint.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        PublicVariable.floatingCounter++;
        PublicVariable.floatingCategoryCounter_wifi++;

        Intent c = new Intent(context, Category_Unlimited_Wifi.class);
        c.putExtra("categoryName", categoryName);
        c.putExtra("categoryNamePackages", categoryNamePackages);
        c.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(c);
        if (PublicVariable.floatingCounter == 1) {
            if (API < 26) {
                context.startService(new Intent(context, BindServices.class));
            } else {
                context.startForegroundService(new Intent(context, BindServices.class));
            }
        }
    }

    public void runUnlimitedCategoryBluetooth(String categoryName, String[] categoryNamePackages) {

        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, CheckPoint.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        PublicVariable.floatingCounter++;
        PublicVariable.floatingCategoryCounter_bluetooth++;

        Intent c = new Intent(context, Category_Unlimited_Bluetooth.class);
        c.putExtra("categoryName", categoryName);
        c.putExtra("categoryNamePackages", categoryNamePackages);
        c.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(c);
        if (PublicVariable.floatingCounter == 1) {
            if (API < 26) {
                context.startService(new Intent(context, BindServices.class));
            } else {
                context.startForegroundService(new Intent(context, BindServices.class));
            }
        }
    }

    public void runUnlimitedCategoryGps(String categoryName, String[] categoryNamePackages) {

        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, CheckPoint.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        PublicVariable.floatingCounter++;
        PublicVariable.floatingCategoryCounter_gps++;

        Intent c = new Intent(context, Category_Unlimited_Gps.class);
        c.putExtra("categoryName", categoryName);
        c.putExtra("categoryNamePackages", categoryNamePackages);
        c.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(c);
        if (PublicVariable.floatingCounter == 1) {
            if (API < 26) {
                context.startService(new Intent(context, BindServices.class));
            } else {
                context.startForegroundService(new Intent(context, BindServices.class));
            }
        }
    }

    public void runUnlimitedCategoryNfc(String categoryName, String[] categoryNamePackages) {

        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, CheckPoint.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        PublicVariable.floatingCounter++;
        PublicVariable.floatingCategoryCounter_nfc++;

        Intent c = new Intent(context, Category_Unlimited_Nfc.class);
        c.putExtra("categoryName", categoryName);
        c.putExtra("categoryNamePackages", categoryNamePackages);
        c.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(c);
        if (PublicVariable.floatingCounter == 1) {
            if (API < 26) {
                context.startService(new Intent(context, BindServices.class));
            } else {
                context.startForegroundService(new Intent(context, BindServices.class));
            }
        }
    }

    public void runUnlimitedCategoryTime(String categoryName, String[] categoryNamePackages) {

        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, CheckPoint.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        PublicVariable.floatingCounter++;
        PublicVariable.floatingCategoryCounter_time++;

        Intent c = new Intent(context, Category_Unlimited_Time.class);
        c.putExtra("categoryName", categoryName);
        c.putExtra("categoryNamePackages", categoryNamePackages);
        c.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(c);
        if (PublicVariable.floatingCounter == 1) {
            if (API < 26) {
                context.startService(new Intent(context, BindServices.class));
            } else {
                context.startForegroundService(new Intent(context, BindServices.class));
            }
        }
    }

    public void PopupAppListCategory(View anchorView,
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

    /*Floating Widgets Function*/
    public void runUnlimitedWidgetService(int WidgetId, String widgetLabel) {

        if (API > 22) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent(context, CheckPoint.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        try {
            PublicVariable.floatingCounter++;
            PublicVariable.floatingWidgetsCounter_Widgets++;
            PublicVariable.widgetsCounter++;
            PublicVariable.FloatingWidgets.add(PublicVariable.widgetsCounter, WidgetId);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();

            PublicVariable.floatingCounter = PublicVariable.floatingCounter + 1;
            PublicVariable.floatingWidgetsCounter_Widgets = PublicVariable.floatingWidgetsCounter_Widgets + 1;
            PublicVariable.widgetsCounter = PublicVariable.widgetsCounter + 1;
            PublicVariable.FloatingWidgets.add(PublicVariable.widgetsCounter, WidgetId);
        }

        Intent w = new Intent(context, Widget_Unlimited_Floating.class);
        w.putExtra("WidgetId", WidgetId);
        w.putExtra("WidgetLabel", widgetLabel);
        w.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(w);
        if (PublicVariable.floatingCounter == 1) {
            if (API < 26) {
                context.startService(new Intent(context, BindServices.class));
            } else {
                context.startForegroundService(new Intent(context, BindServices.class));
            }
        }
    }

    /*Floating Notification*/
    public void PopupNotificationShortcuts(View view, final String notificationPackage, String className, int startId, int iconColor, int X, int Y) {
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(50);

        ArrayList<NavDrawerItem> navDrawerItemsSaved = new ArrayList<NavDrawerItem>();
        try {
            int W = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    221,
                    context.getResources().getDisplayMetrics());

            navDrawerItemsSaved.clear();

            LoadCustomIcons loadCustomIcons = null;
            if (loadCustomIcons()) {
                loadCustomIcons = new LoadCustomIcons(context, customIconPackageName());
            }

            String[] packageContentTime = readFileLine(notificationPackage + "_" + "Notification" + "Package");
            for (String notificationTime : packageContentTime) {
                navDrawerItemsSaved.add(
                        new NavDrawerItem(
                                notificationTime,
                                notificationPackage,
                                appName(notificationPackage),
                                readFile(notificationTime + "_" + "Notification" + "Title"),
                                readFile(notificationTime + "_" + "Notification" + "Text"),
                                loadCustomIcons() ? loadCustomIcons.getDrawableIconForPackage(notificationPackage, shapedAppIcon(notificationPackage)) : shapedAppIcon(notificationPackage),
                                shapedNotificationUser(Drawable.createFromPath(context.getFileStreamPath(notificationTime + "_" + "Notification" + "Icon").getPath())),
                                readFile(notificationTime + "_" + "Notification" + "Key"),
                                PublicVariable.notificationIntent.get(notificationTime)
                        )
                );
            }
            PopupShortcutsNotification popupShortcutsNotification =
                    new PopupShortcutsNotification(context, navDrawerItemsSaved, className, notificationPackage, startId);

            final ListPopupWindow listPopupWindow = new ListPopupWindow(context);
            listPopupWindow.setAdapter(popupShortcutsNotification);
            listPopupWindow.setAnchorView(view);
            listPopupWindow.setWidth(W);
            listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            listPopupWindow.setModal(true);
            listPopupWindow.setBackgroundDrawable(null);
            try {
                listPopupWindow.show();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (returnAPI() < 23) {
                    if (listPopupWindow.isShowing()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                listPopupWindow.dismiss();
                            }
                        }, 2000);
                    }
                }
            }

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("Hide_PopupListView_Shortcuts_Notification");
            final ListPopupWindow finalListPopupWindow = listPopupWindow;
            final BroadcastReceiver counterReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals("Hide_PopupListView_Shortcuts_Notification")) {
                        if (finalListPopupWindow.isShowing()) {
                            finalListPopupWindow.dismiss();
                        }
                    }
                }
            };
            context.registerReceiver(counterReceiver, intentFilter);

            listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    context.unregisterReceiver(counterReceiver);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Checkpoint Dialogue Functions*/
    public void NotificationAccessService(Activity activity, final SwitchPreference switchPreference) {
        context = activity.getApplicationContext();
        if (returnAPI() < 23) {
            //"android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
            Intent notification = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            notification.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(notification);

            return;
        }

        AlertDialog.Builder alertDialog = null;
        if (PublicVariable.themeLightDark == true) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        } else if (PublicVariable.themeLightDark == false) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
        }
        alertDialog.setTitle(Html.fromHtml(context.getResources().getString(R.string.notificationTitle)));
        alertDialog.setMessage(Html.fromHtml(context.getResources().getString(R.string.notificationDesc)));
        alertDialog.setIcon(context.getDrawable(R.drawable.ic_launcher));
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(context.getString(R.string.grant), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent notification = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                notification.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(notification);

                dialog.dismiss();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                try {
                    switchPreference.setChecked(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialog.show();
    }

    public void NotificationAccessService(Activity activity) {
        context = activity.getApplicationContext();
        if (returnAPI() < 23) {
            //"android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
            Intent notification = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            notification.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(notification);

            return;
        }

        AlertDialog.Builder alertDialog = null;
        if (PublicVariable.themeLightDark == true) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        } else if (PublicVariable.themeLightDark == false) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
        }
        alertDialog.setTitle(Html.fromHtml(context.getResources().getString(R.string.notificationTitle)));
        alertDialog.setMessage(Html.fromHtml(context.getResources().getString(R.string.notificationDesc)));
        alertDialog.setIcon(context.getDrawable(R.drawable.ic_launcher));
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(context.getString(R.string.grant), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent notification = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                notification.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(notification);

                dialog.dismiss();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        alertDialog.show();
    }

    public void AccessibilityService(Activity activity, final SwitchPreference switchPreference) {
        context = activity.getApplicationContext();
        if (returnAPI() < 23) {
            Intent notification = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            notification.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(notification);

            return;
        }
        AlertDialog.Builder alertDialog = null;
        if (PublicVariable.themeLightDark == true) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        } else if (PublicVariable.themeLightDark == false) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
        }
        alertDialog.setTitle(Html.fromHtml(context.getResources().getString(R.string.observeTitle)));
        alertDialog.setMessage(Html.fromHtml(context.getResources().getString(R.string.observeDesc)));
        alertDialog.setIcon(context.getDrawable(R.drawable.ic_launcher));
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(context.getString(R.string.grant), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                dialog.dismiss();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                try {
                    switchPreference.setChecked(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialog.show();
    }

    public void AccessibilityService(final Activity activity) {
        context = activity.getApplicationContext();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        if (PublicVariable.themeLightDark == true) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        } else if (PublicVariable.themeLightDark == false) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
        }
        alertDialog.setTitle(Html.fromHtml(context.getResources().getString(R.string.observeTitle)));
        alertDialog.setMessage(Html.fromHtml(context.getResources().getString(R.string.observeDesc)));
        alertDialog.setIcon(context.getDrawable(R.drawable.ic_launcher));
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(context.getString(R.string.grant), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                dialog.dismiss();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                activity.finish();
            }
        });
        alertDialog.show();
    }

    public void UsageAccess(final Activity activity, final SwitchPreference switchPreference) {
        context = activity.getApplicationContext();
        if (returnAPI() < 23) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            return;
        }
        AlertDialog.Builder alertDialog = null;
        if (PublicVariable.themeLightDark == true) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        } else if (PublicVariable.themeLightDark == false) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
        }
        alertDialog.setTitle(context.getString(R.string.smartTitle));
        alertDialog.setMessage(Html.fromHtml(context.getString(R.string.smartPermission)));
        alertDialog.setIcon(context.getDrawable(R.drawable.ic_launcher));
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(context.getString(R.string.grant), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                activity.finish();
                dialog.dismiss();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                switchPreference.setChecked(false);
            }
        });
        alertDialog.show();
    }

    public void UsageAccess(Activity activity) {
        context = activity.getApplicationContext();
        if (returnAPI() < 23) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            return;
        }
        AlertDialog.Builder alertDialog = null;
        if (PublicVariable.themeLightDark == true) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        } else if (PublicVariable.themeLightDark == false) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
        }
        alertDialog.setTitle(context.getString(R.string.smartTitle));
        alertDialog.setMessage(Html.fromHtml(context.getString(R.string.smartPermission)));
        alertDialog.setIcon(context.getDrawable(R.drawable.ic_launcher));
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(context.getString(R.string.grant), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                dialog.dismiss();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
        alertDialog.show();
    }

    public void RemoteRecovery(Activity activity) {
        context = activity.getApplicationContext();
        AlertDialog.Builder alertDialog = null;
        if (PublicVariable.themeLightDark == true) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        } else if (PublicVariable.themeLightDark == false) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
        }
        alertDialog.setTitle(Html.fromHtml(context.getResources().getString(R.string.boot)));
        alertDialog.setMessage(Html.fromHtml(context.getResources().getString(R.string.bootPermission)));
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        if (!UsageStatsEnabled()) {
            alertDialog.setNeutralButton(context.getString(R.string.grant), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                    dialog.dismiss();
                }
            });
        }
        alertDialog.show();
    }

    public void FreeFormInformation(final Activity activity, final SwitchPreference switchPreference) {
        context = activity.getApplicationContext();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
        if (PublicVariable.themeLightDark == true) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        } else if (PublicVariable.themeLightDark == false) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
        }
        alertDialog.setTitle(Html.fromHtml(context.getResources().getString(R.string.freeFormTitle)));
        alertDialog.setMessage(Html.fromHtml(context.getResources().getString(R.string.freeFormInfo)));
        alertDialog.setIcon(context.getDrawable(R.drawable.ic_launcher));
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(context.getString(R.string.grantFreeForm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                switchPreference.setChecked(true);
            }
        });
        alertDialog.setNeutralButton(context.getString(R.string.support), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                switchPreference.setChecked(false);
                ContactSupport(activity);
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (freeFormSupport(context)) {
                    switchPreference.setChecked(true);
                } else {
                    switchPreference.setChecked(false);
                }
            }
        });
        alertDialog.show();
    }

    public void dialogueLicense(final Activity activity) {
        context = activity.getApplicationContext();
        if (returnAPI() < 23) {
            Intent r = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.play_store_link) + context.getPackageName()));
            r.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(r);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    uninstallApp(context.getPackageName());
                }
            }, 2333);
            return;
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        if (PublicVariable.themeLightDark == true) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        } else if (PublicVariable.themeLightDark == false) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
        }
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
                if (PublicVariable.themeLightDark == true) {
                    builder = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
                } else if (PublicVariable.themeLightDark == false) {
                    builder = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
                }
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
                            email.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.supportEmail)});
                            email.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.feedback_tag) + " [" + appVersionName(context.getPackageName()) + "] ");
                            email.putExtra(Intent.EXTRA_TEXT, textMsg);
                            email.setType("text/*");
                            email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(Intent.createChooser(email, context.getString(R.string.feedback_tag)));
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

        if (activity == null) {
            alertDialog.show();
        } else {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            );
        }
    }

    public void ChangeLog(Activity activity, boolean showChangeLog) {
        try {
            AlertDialog.Builder alertDialog = null;
            if (PublicVariable.themeLightDark == true) {
                alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
            } else if (PublicVariable.themeLightDark == false) {
                alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
            }
            alertDialog.setTitle(Html.fromHtml(context.getString(R.string.whatsnew)));
            alertDialog.setMessage(Html.fromHtml(context.getString(R.string.changelog)));

            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.setCancelable(true);
            alertDialog.setPositiveButton(context.getString(R.string.like), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.link_facebook_app)))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });

            alertDialog.setNeutralButton(context.getString(R.string.shareit), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.play_store_link) + context.getPackageName()))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    saveFile(".Updated", String.valueOf(appVersionCode(context.getPackageName())));

                    dialog.dismiss();
                }
            });

            if (showChangeLog == true) {
                alertDialog.show();
            } else if (!context.getFileStreamPath(".Updated").exists()) {
                alertDialog.show();
            } else {
                if (appVersionCode(context.getPackageName()) > Integer.parseInt(readFile(".Updated"))) {
                    alertDialog.show();

                    if (!BuildConfig.DEBUG && networkConnection()) {
                        FirebaseAuth.getInstance().addAuthStateListener(
                                new FirebaseAuth.AuthStateListener() {
                                    @Override
                                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        if (user == null) {
                                            savePreference(".UserInformation", "userEmail", null);

                                        } else {
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        File betaFile = new File("/data/data/" + context.getPackageName() + "/shared_prefs/.UserInformation.xml");
                                                        Uri uriBetaFile = Uri.fromFile(betaFile);
                                                        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

                                                        StorageReference storageReference = firebaseStorage.getReference("/Users/" + "API" + returnAPI() + "/" +
                                                                readPreference(".UserInformation", "userEmail", null));
                                                        UploadTask uploadTask = storageReference.putFile(uriBetaFile);

                                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                exception.printStackTrace();
                                                            }
                                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                FunctionsClassDebug.Companion.PrintDebug("Firebase Activities Done Successfully");
                                                            }
                                                        });

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, 333);
                                        }
                                    }
                                }
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ChangeLog(Activity activity, String betaChangeLog, String betaVersionCode, boolean showChangeLog) {
        try {
            AlertDialog.Builder alertDialog = null;
            if (PublicVariable.themeLightDark == true) {
                alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
            } else if (PublicVariable.themeLightDark == false) {
                alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
            }
            alertDialog.setTitle(Html.fromHtml(context.getString(R.string.whatsnew)));
            alertDialog.setMessage(Html.fromHtml(context.getString(R.string.changelog)));

            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.setCancelable(true);
            alertDialog.setPositiveButton(context.getString(R.string.like), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.link_facebook_app)))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });

            String neutralButtonText;
            if (betaChangeLog.contains(context.getPackageName())) {
                neutralButtonText = context.getString(R.string.shareit);
            } else {
                neutralButtonText = context.getString(R.string.betaUpdate);
            }
            alertDialog.setNeutralButton(neutralButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    if (neutralButtonText.equals(context.getString(R.string.shareit))) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.play_store_link) + context.getPackageName()))
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    } else if (neutralButtonText.equals(context.getString(R.string.betaUpdate))) {
                        upcomingChangeLog(activity, betaChangeLog, betaVersionCode);
                    }
                }
            });
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    saveFile(".Updated", String.valueOf(appVersionCode(context.getPackageName())));

                    dialog.dismiss();
                }
            });

            if (showChangeLog == true) {
                alertDialog.show();
            } else if (!context.getFileStreamPath(".Updated").exists()) {
                alertDialog.show();
            } else {
                if (appVersionCode(context.getPackageName()) > Integer.parseInt(readFile(".Updated"))) {
                    alertDialog.show();

                    if (!BuildConfig.DEBUG && networkConnection()) {
                        FirebaseAuth.getInstance().addAuthStateListener(
                                new FirebaseAuth.AuthStateListener() {
                                    @Override
                                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        if (user == null) {
                                            savePreference(".UserInformation", "userEmail", null);

                                        } else {
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        File betaFile = new File("/data/data/" + context.getPackageName() + "/shared_prefs/.UserInformation.xml");
                                                        Uri uriBetaFile = Uri.fromFile(betaFile);
                                                        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

                                                        StorageReference storageReference = firebaseStorage.getReference("/Users/" + "API" + returnAPI() + "/" +
                                                                readPreference(".UserInformation", "userEmail", null));
                                                        UploadTask uploadTask = storageReference.putFile(uriBetaFile);

                                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                exception.printStackTrace();
                                                            }
                                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                FunctionsClassDebug.Companion.PrintDebug("Firebase Activities Done Successfully");
                                                            }
                                                        });

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, 333);
                                        }
                                    }
                                }
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void upcomingChangeLog(Activity activity, String updateInfo, String versionCode) {
        AlertDialog.Builder alertDialog = null;
        if (PublicVariable.themeLightDark == true) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        } else if (PublicVariable.themeLightDark == false) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
        }
        alertDialog.setTitle(Html.fromHtml("<small>" + context.getString(R.string.whatsnew) + " | " + versionCode + "</small>"));
        alertDialog.setMessage(Html.fromHtml(updateInfo));

        LayerDrawable layerDrawableNewUpdate = (LayerDrawable) context.getDrawable(R.drawable.ic_update);
        BitmapDrawable gradientDrawableNewUpdate = (BitmapDrawable) layerDrawableNewUpdate.findDrawableByLayerId(R.id.ic_launcher_back_layer);
        gradientDrawableNewUpdate.setTint(PublicVariable.primaryColor);

        alertDialog.setIcon(layerDrawableNewUpdate);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(context.getString(R.string.like), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.link_facebook_app)))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        alertDialog.setNeutralButton(context.getString(R.string.newupdate), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.play_store_link) + context.getPackageName()))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Toast(context.getString(R.string.rate), Gravity.BOTTOM);
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void ContactSupport(final Activity activity) {
        String[] contactOption = new String[]{
                "Send an Email",
                "Send a Message",
                "Contact via Forum",
                "Join Beta Program",
                "Rate & Write Review"};
        AlertDialog.Builder builder = null;
        if (PublicVariable.themeLightDark == true) {
            builder = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        } else if (PublicVariable.themeLightDark == false) {
            builder = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
        }
        LayerDrawable drawSupport = (LayerDrawable) context.getDrawable(R.drawable.draw_support);
        Drawable backSupport = drawSupport.findDrawableByLayerId(R.id.backtemp);
        backSupport.setTint(PublicVariable.themeTextColor);
        builder.setIcon(drawSupport);
        builder.setTitle(context.getString(R.string.supportCategory));
        builder.setSingleChoiceItems(contactOption, 0, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                if (selectedPosition == 0) {
                    String textMsg = "\n\n\n\n\n"
                            + "[Essential Information]" + "\n"
                            + getDeviceName() + " | " + "API " + Build.VERSION.SDK_INT + " | " + getCountryIso().toUpperCase();
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.supportEmail)});
                    email.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.feedback_tag) + " [" + appVersionName(context.getPackageName()) + "] ");
                    email.putExtra(Intent.EXTRA_TEXT, textMsg);
                    email.setType("text/*");
                    email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(Intent.createChooser(email, context.getString(R.string.feedback_tag)));
                } else if (selectedPosition == 1) {
                    Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.link_facebook_app)));
                    activity.startActivity(a);
                } else if (selectedPosition == 2) {
                    Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.link_xda)));
                    activity.startActivity(a);
                } else if (selectedPosition == 3) {
                    Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.link_alpha) + context.getPackageName()));
                    activity.startActivity(a);

                    Toast(context.getResources().getString(R.string.alphaTitle), Gravity.BOTTOM);
                } else if (selectedPosition == 4) {
                    Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.play_store_link) + context.getPackageName()));
                    activity.startActivity(a);

                    Toast(context.getResources().getString(R.string.alphaTitle), Gravity.BOTTOM);
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        builder.show();
    }

    public void litePreferenceConfirm() {
        AlertDialog.Builder alertDialog = null;
        if (PublicVariable.themeLightDark == true) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        } else if (PublicVariable.themeLightDark == false) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
        }
        alertDialog.setTitle(Html.fromHtml("<small>" + context.getString(R.string.liteTitle) + "</small>"));
        alertDialog.setMessage(Html.fromHtml(context.getString(R.string.liteDesc)));

        LayerDrawable drawPrefLite = (LayerDrawable) context.getDrawable(R.drawable.draw_pref).mutate();
        Drawable backPrefLite = drawPrefLite.findDrawableByLayerId(R.id.backtemp).mutate();
        Drawable drawablePrefLite = drawPrefLite.findDrawableByLayerId(R.id.wPref);
        if (PublicVariable.themeLightDark == true) {
            backPrefLite.setTint(context.getColor(R.color.dark));
            drawablePrefLite.setTint(context.getColor(R.color.light));
        } else if (PublicVariable.themeLightDark == false) {
            backPrefLite.setTint(context.getColor(R.color.light));
            drawablePrefLite.setTint(context.getColor(R.color.dark));
        }
        alertDialog.setIcon(drawPrefLite);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        liteAppPreferences();
                    }
                }, 333);
                Runtime.getRuntime().gc();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /*Checkpoint Functions*/
    public boolean NotificationAccess() {
        boolean notificationAccess = false;
        try {
            notificationAccess = (Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners")).contains(context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
                for (String packageName : packageNames) {
                    if (packageName.contains(context.getPackageName())) {
                        return true;
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return notificationAccess;
    }

    public boolean NotificationListenerRunning() {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (NotificationListener.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean UsageStatsEnabled() {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOp("android:get_usage_stats", android.os.Process.myUid(), context.getPackageName());
        return (mode == AppOpsManager.MODE_ALLOWED);
    }

    public boolean AccessibilityServiceEnabled() {
        ComponentName expectedComponentName = new ComponentName(context, InteractionObserver.class);

        String enabledServicesSetting = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabledServicesSetting == null)
            return false;

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
        colonSplitter.setString(enabledServicesSetting);

        while (colonSplitter.hasNext()) {
            String componentNameString = colonSplitter.next();
            ComponentName enabledService = ComponentName.unflattenFromString(componentNameString);

            if (enabledService != null && enabledService.equals(expectedComponentName))
                return true;
        }

        return false;
    }

    public boolean networkConnection() throws Exception {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void navigateToClass(Class returnClass, final Activity activityToFinish) throws Exception {
        context.startActivity(new Intent(context, returnClass).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activityToFinish.finish();
            }
        }, 313);
    }

    public void navigateToClass(Class returnClass, ActivityOptions activityOptions) throws Exception {
        Intent intentOverride = new Intent(context, returnClass);
        if (returnClass.getSimpleName().equals(HybridViewOff.class.getSimpleName())) {
            intentOverride.putExtra("freq", PublicVariable.freqApps);
            intentOverride.putExtra("num", PublicVariable.freqLength);
        }
        activity.startActivity(intentOverride, activityOptions.toBundle());
    }

    public void overrideBackPress(final Activity activityToFinish) throws Exception {
        if (readPreference("OpenMode", "openClassName", HybridViewOff.class.getSimpleName()).equals(FoldersHandler.class.getSimpleName())) {
            Intent categoryIntent = new Intent(context, FoldersHandler.class);
            activity.startActivity(categoryIntent);
        } else {
            Intent hybridViewOff = new Intent(context, HybridViewOff.class);
            hybridViewOff.putExtra("freq", PublicVariable.freqApps);
            hybridViewOff.putExtra("num", PublicVariable.freqLength);
            hybridViewOff.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            activity.startActivity(hybridViewOff);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activityToFinish.finish();
                }
            }, 313);
        }
    }

    public void overrideBackPressToMain(final Activity activityToFinish) throws Exception {
        if (readPreference("OpenMode", "openClassName", HybridViewOff.class.getSimpleName()).equals(FoldersHandler.class.getSimpleName())) {
            Intent categoryInten = new Intent(context, FoldersHandler.class);
            activity.startActivity(categoryInten);
        } else {
            Intent hybridViewOff = new Intent(context, HybridViewOff.class);
            hybridViewOff.putExtra("freq", PublicVariable.freqApps);
            hybridViewOff.putExtra("num", PublicVariable.freqLength);
            hybridViewOff.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            activity.startActivity(hybridViewOff);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activityToFinish.finish();
            }
        }, 313);
    }

    public void overrideBackPressToShortcuts(final Activity activityToFinish) throws Exception {
        Intent hybridViewOff = new Intent(context, HybridViewOff.class);
        hybridViewOff.putExtra("freq", PublicVariable.freqApps);
        hybridViewOff.putExtra("num", PublicVariable.freqLength);
        hybridViewOff.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        activity.startActivity(hybridViewOff);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activityToFinish.finish();
            }
        }, 313);
    }

    public String appName(String packageName) {
        String Name = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo app = context.getPackageManager().getApplicationInfo(packageName, 0);
            Name = packageManager.getApplicationLabel(app).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Name;
    }

    public String activityLabel(ActivityInfo activityInfo) {
        String Name = context.getString(R.string.app_name);
        try {
            Name = activityInfo.loadLabel(context.getPackageManager()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            Name = appName(activityInfo.packageName);
        }
        return Name;
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

    public int serviceMode() {
        int ReturnValue = Service.START_NOT_STICKY;
        if (!SystemCache()) {
            ReturnValue = Service.START_NOT_STICKY;
        } else if (SystemCache()) {
            ReturnValue = Service.START_STICKY;
        }
        return ReturnValue;
    }

    public boolean SettingServiceRunning(Class aClass) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (aClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendInteractionObserverEvent(View view, String packageName, int accessibilityEvent, int accessibilityAction) {
        if (!AccessibilityServiceEnabled()) {
            AccessibilityService(activity);
        } else {
            AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService(ACCESSIBILITY_SERVICE);
            AccessibilityEvent event = AccessibilityEvent.obtain();
            event.setSource(view);
            event.setEventType(accessibilityEvent);
            event.setAction(accessibilityAction);
            event.setPackageName(packageName);
            event.getText().add(context.getPackageName());
            accessibilityManager.sendAccessibilityEvent(event);
        }
    }

    public boolean FreeForm() {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("freeForm", false);
    }

    public boolean SystemCache() {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("cache", false);
    }

    public boolean ControlPanel() {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("stable", false);
    }

    public int bindServicePriority() {
        int notificationPriority = Integer.MIN_VALUE;
        if (SystemCache() || automationFeatureEnable()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationPriority = NotificationManager.IMPORTANCE_MIN;
            } else {
                notificationPriority = Notification.PRIORITY_MIN;
            }
        } else if (ControlPanel()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationPriority = NotificationManager.IMPORTANCE_HIGH;
            } else {
                notificationPriority = Notification.PRIORITY_HIGH;
            }
        }
        return notificationPriority;
    }

    public void DownloadTask(final String fileURL, final String targetFile) {
        class DownloadTask extends AsyncTask<String, Integer, String> {
            @Override
            public void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... sUrl) {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(fileURL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return "Server HTTP Response ::"
                                + connection.getResponseCode() + " :: " + connection.getResponseMessage();
                    }

                    int fileLength = connection.getContentLength();

                    input = connection.getInputStream();
                    output = context.openFileOutput(targetFile, Context.MODE_PRIVATE);

                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        output.write(data, 0, count);
                    }
                } catch (Exception e) {
                    return e.toString();
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (IOException ignored) {
                        ignored.printStackTrace();
                    }

                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return "New FAQ Downloaded";
            }

            @Override
            public void onPostExecute(String result) {
                super.onPostExecute(result);
            }
        }
        new DownloadTask().execute();
    }

    public boolean getNetworkState() {
        boolean netState = false;
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (wifiManager.isWifiEnabled() && activeNetworkInfo.isConnected()) {
                netState = true;
            } else if (wifiManager.isWifiEnabled() && !activeNetworkInfo.isConnected()) {
                netState = false;
            } else {
                netState = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netState;
    }

    public void appInfoSetting(String packageName) {
        Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + packageName));
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    /*System Checkpoint Functions*/
    public void CheckSystemRAM(Activity activityToHandle) {
        try {
            ActivityManager activityManager = (ActivityManager) activityToHandle.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            if (activityManager != null) {
                activityManager.getMemoryInfo(memoryInfo);
                if (memoryInfo.lowMemory) {
                    FunctionsClassDebug.Companion.PrintDebug("*** SYSTEM RAM LOW | Finishing... " + activityToHandle + " ***");

                    activityToHandle.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean lowRAM() {
        boolean lowMemory = false;
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            if (activityManager != null) {
                activityManager.getMemoryInfo(memoryInfo);
                if (memoryInfo.lowMemory) {
                    lowMemory = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lowMemory;
    }

    public int returnAPI() {
        return API;
    }

    public boolean appIsInstalled(String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(packageName, 0);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        } catch (Exception e) {
            e.printStackTrace();
            app_installed = false;
        }
        return app_installed;
    }

    public void uninstallApp(String packageName) {
        Uri packageUri = Uri.parse("package:" + packageName);
        Intent uninstallIntent =
                new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
        uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(uninstallIntent);
    }

    public boolean ifSystem(String packageName) {
        boolean ifSystem = false;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo targetPkgInfo = packageManager.getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
            PackageInfo sys = packageManager.getPackageInfo(
                    "android", PackageManager.GET_SIGNATURES);
            ifSystem = (targetPkgInfo != null && targetPkgInfo.signatures != null && sys.signatures[0]
                    .equals(targetPkgInfo.signatures[0]));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            ifSystem = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ifSystem;
    }

    public boolean isDefaultLauncher(String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo defaultLauncher = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String defaultLauncherStr = defaultLauncher.activityInfo.packageName;
        return defaultLauncherStr.equals(packageName);
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

    /*FreeForm*/
    public boolean addFloatItItem() {
        boolean forceFloatIt = false;
        if (!FreeForm()) {
            if (freeFormSupport(context)) {
                forceFloatIt = true;
            }
        }
        return forceFloatIt;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public boolean freeFormSupport(Context context) {
        return (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FREEFORM_WINDOW_MANAGEMENT)) ||
                (Settings.Global.getInt(context.getContentResolver(), "enable_freeform_support", 0) != 0) &&
                        (Settings.Global.getInt(context.getContentResolver(), "force_resizable_activities", 0) != 0);
    }

    public String getWindowingModeMethodName() {
        if (returnAPI() >= 28)
            return "setLaunchWindowingMode";
        else
            return "setLaunchStackId";
    }

    private int getFreeformWindowModeId() {
        if (returnAPI() >= 28) {
            return WindowMode.WINDOWING_MODE_FREEFORM;
        } else {
            return WindowMode.FREEFORM_WORKSPACE_STACK_ID;
        }
    }

    /*Open Functions*/
    public boolean canLaunch(String packageName) {
        return (context.getPackageManager().getLaunchIntentForPackage(packageName) != null);
    }

    public void openApplicationFromActivity(String packageName) {
        if (appIsInstalled(packageName) == true) {
            try {
                Toast(appName(packageName), Gravity.BOTTOM);

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

    public void openApplicationFromActivity(String packageName, String className) {
        if (appIsInstalled(packageName) == true) {
            try {
                Toast(String.valueOf(context.getPackageManager().getActivityInfo(new ComponentName(packageName, className), 0).loadLabel(context.getPackageManager())), Gravity.BOTTOM);

                Intent openAlias = new Intent();
                openAlias.setClassName(packageName, className);
                activity.startActivity(openAlias);
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

    public void openApplicationFreeForm(final String PackageName, final int leftPositionX/*X*/, final int rightPositionX/*displayX/2*/, final int topPositionY/*Y*/, final int bottomPositionY/*displayY/2*/) {
        //Enable Developer Option & Turn ON 'Force Activities to be Resizable'
        //adb shell settings put global enable_freeform_support 1
        //adb shell settings put global force_resizable_activities 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (returnAPI() < 28) {
                Intent homeScreen = new Intent(Intent.ACTION_MAIN);
                homeScreen.addCategory(Intent.CATEGORY_HOME);
                homeScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(homeScreen);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityOptions activityOptions = ActivityOptions.makeBasic();
                    try {
                        Method method = ActivityOptions.class.getMethod(getWindowingModeMethodName(), int.class);
                        method.invoke(activityOptions, getFreeformWindowModeId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    switch (displaySection(leftPositionX, topPositionY)) {
                        case DisplaySection.TopLeft: {
                            FunctionsClassDebug.Companion.PrintDebug("***** DisplaySection.TopLeft");
                            activityOptions.setLaunchBounds(
                                    new Rect(
                                            leftPositionX,
                                            topPositionY,
                                            rightPositionX,
                                            bottomPositionY)
                            );
                            break;
                        }
                        case DisplaySection.TopRight: {
                            FunctionsClassDebug.Companion.PrintDebug("***** DisplaySection.TopRight");
                            activityOptions.setLaunchBounds(
                                    new Rect(
                                            leftPositionX - (displayX() / 2),
                                            topPositionY,
                                            leftPositionX,
                                            bottomPositionY)
                            );
                            break;
                        }
                        case DisplaySection.BottomRight: {
                            FunctionsClassDebug.Companion.PrintDebug("***** DisplaySection.BottomRight");
                            activityOptions.setLaunchBounds(
                                    new Rect(
                                            leftPositionX - (displayX() / 2),
                                            topPositionY - (displayY() / 2),
                                            leftPositionX,
                                            topPositionY)
                            );
                            break;
                        }
                        case DisplaySection.BottomLeft: {
                            FunctionsClassDebug.Companion.PrintDebug("***** DisplaySection.BottomLeft");
                            activityOptions.setLaunchBounds(
                                    new Rect(
                                            leftPositionX,
                                            topPositionY - (displayY() / 2),
                                            leftPositionX + (displayX() / 2),
                                            topPositionY)
                            );
                            break;
                        }
                        default: {
                            FunctionsClassDebug.Companion.PrintDebug("***** DisplaySection.Not.Supported");
                            activityOptions.setLaunchBounds(
                                    new Rect(
                                            displayX() / 4,
                                            (displayX() / 2),
                                            displayY() / 4,
                                            (displayY() / 2))
                            );
                            break;
                        }
                    }

                    Intent openAlias = context.getPackageManager().getLaunchIntentForPackage(PackageName);
                    openAlias.setFlags(
                            Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(openAlias, activityOptions.toBundle());
                }
            }, 3000);
        }
    }

    public void openApplicationFreeForm(String PackageName, String ClassName, int leftPositionX/*X*/, int rightPositionX, int topPositionY/*Y*/, int bottomPositionY) {
        //Enable Developer Option & Turn ON 'Force Activities to be Resizable'
        //adb shell settings put global enable_freeform_support 1
        //adb shell settings put global force_resizable_activities 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ActivityOptions activityOptions = ActivityOptions.makeBasic();
            try {
                Method method = ActivityOptions.class.getMethod(getWindowingModeMethodName(), int.class);
                method.invoke(activityOptions, getFreeformWindowModeId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (displaySection(leftPositionX, topPositionY)) {
                case DisplaySection.TopLeft: {
                    FunctionsClassDebug.Companion.PrintDebug("***** DisplaySection.TopLeft");
                    activityOptions.setLaunchBounds(
                            new Rect(
                                    leftPositionX,
                                    topPositionY,
                                    rightPositionX,
                                    bottomPositionY)
                    );
                    break;
                }
                case DisplaySection.TopRight: {
                    FunctionsClassDebug.Companion.PrintDebug("***** DisplaySection.TopRight");
                    activityOptions.setLaunchBounds(
                            new Rect(
                                    leftPositionX - (displayX() / 2),
                                    topPositionY,
                                    leftPositionX,
                                    bottomPositionY)
                    );
                    break;
                }
                case DisplaySection.BottomRight: {
                    FunctionsClassDebug.Companion.PrintDebug("***** DisplaySection.BottomRight");
                    activityOptions.setLaunchBounds(
                            new Rect(
                                    leftPositionX - (displayX() / 2),
                                    topPositionY - (displayY() / 2),
                                    leftPositionX,
                                    topPositionY)
                    );
                    break;
                }
                case DisplaySection.BottomLeft: {
                    FunctionsClassDebug.Companion.PrintDebug("***** DisplaySection.BottomLeft");
                    activityOptions.setLaunchBounds(
                            new Rect(
                                    leftPositionX,
                                    topPositionY - (displayY() / 2),
                                    leftPositionX + (displayX() / 2),
                                    topPositionY)
                    );
                    break;
                }
                default: {
                    FunctionsClassDebug.Companion.PrintDebug("***** DisplaySection.Not.Supported");
                    activityOptions.setLaunchBounds(
                            new Rect(
                                    displayX() / 4,
                                    (displayX() / 2),
                                    displayY() / 4,
                                    (displayY() / 2))
                    );
                    break;
                }
            }

            Intent openAlias = new Intent();
            openAlias.setClassName(PackageName, ClassName);
            openAlias.addCategory(Intent.CATEGORY_LAUNCHER);
            openAlias.setFlags(
                    Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(openAlias, activityOptions.toBundle());
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

    /*File Functions*/
    public void saveBitmapIcon(String fileName, Bitmap bitmapToSave) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmapToSave.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void saveBitmapIcon(String fileName, Drawable drawableToSave) {
        FileOutputStream fileOutputStream = null;
        try {
            Bitmap bitmapToSave = drawableToBitmap(drawableToSave);
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmapToSave.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void saveFileEmpty(String fileName) {
        try {
            FileOutputStream fOut = context.openFileOutput(fileName, Context.MODE_PRIVATE);

            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
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

                int count = countLineInnerFile(fileName);
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
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8), 1024);

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

    public int countLineInnerFile(String fileName) {
        int nLines = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(context.getFileStreamPath(fileName)));
            while (bufferedReader.readLine() != null) {
                nLines++;
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            nLines = 0;
        }
        return nLines;
    }

    public void removeLine(String fileName, String lineToRemove) {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName + ".tmp", Context.MODE_APPEND));

            String tmp = "";
            while ((tmp = bufferedReader.readLine()) != null) {
                if (!tmp.trim().equals(lineToRemove)) {
                    outputStreamWriter.write(tmp);
                    outputStreamWriter.write("\n");
                }
            }

            outputStreamWriter.close();
            bufferedReader.close();
            fileInputStream.close();

            File tmpD = context.getFileStreamPath(fileName + ".tmp");
            File New = context.getFileStreamPath(fileName);

            if (tmpD.isFile()) {
            }
            context.deleteFile(fileName);
            tmpD.renameTo(New);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void savePreference(String PreferenceName, String KEY, long VALUE) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
        editorSharedPreferences.putLong(KEY, VALUE);
        editorSharedPreferences.apply();
    }

    public void savePreference(String PreferenceName, String KEY, float VALUE) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
        editorSharedPreferences.putFloat(KEY, VALUE);
        editorSharedPreferences.apply();
    }

    public void savePreference(String PreferenceName, String KEY, boolean VALUE) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
        editorSharedPreferences.putBoolean(KEY, VALUE);
        editorSharedPreferences.apply();
    }

    public void saveDefaultPreference(String KEY, String VALUE) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
        editorSharedPreferences.putString(KEY, VALUE);
        editorSharedPreferences.apply();
    }

    public void saveDefaultPreference(String KEY, int VALUE) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
        editorSharedPreferences.putInt(KEY, VALUE);
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

    public long readPreference(String PreferenceName, String KEY, long defaultVALUE) {
        return context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE).getLong(KEY, defaultVALUE);
    }

    public float readPreference(String PreferenceName, String KEY, float defaultVALUE) {
        return context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE).getFloat(KEY, defaultVALUE);
    }

    public boolean readPreference(String PreferenceName, String KEY, boolean defaultVALUE) {
        return context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE).getBoolean(KEY, defaultVALUE);
    }

    public int readDefaultPreference(String KEY, int defaultVALUE) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY, defaultVALUE);
    }

    public String readDefaultPreference(String KEY, String defaultVALUE) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY, defaultVALUE);
    }

    public boolean readDefaultPreference(String KEY, boolean defaultVALUE) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY, defaultVALUE);
    }

    public boolean automationFeatureEnable() {
        boolean automationEnabled = false;
        if (returnAPI() >= 26) {
            List<String> autoFileName = new ArrayList<String>();
            File[] files = context.getFileStreamPath("").listFiles();
            for (File afile : files) {
                FunctionsClassDebug.Companion.PrintDebug("*** Automation Enabled == " + afile.getAbsolutePath());
                if (afile.getName().contains(".auto")) {
                    FunctionsClassDebug.Companion.PrintDebug("*** Automation File Found == " + afile.getAbsolutePath());

                    autoFileName.add(afile.getName());
                }
            }

            int totalCount = 0;
            for (String fileName : autoFileName) {
                int countLine = countLineInnerFile(fileName);
                totalCount += countLine;
            }

            if (totalCount > 0) {
                automationEnabled = true;

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("stable", true);
                editor.apply();
            }
        }
        return automationEnabled;
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

    public int shapesImageId() {
        int shapesImageId = 0;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
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

            case 0:
                shapeDrawable = null;
                break;
        }
        return shapeDrawable;
    }

    public Drawable shapesDrawablesCategory(TextView categoryChar) {
        Drawable shapeDrawable = null;
        switch (shapesImageId()) {
            case 1:
                shapeDrawable = context.getDrawable(R.drawable.category_droplet_icon).mutate();
                break;
            case 2:
                shapeDrawable = context.getDrawable(R.drawable.category_circle_icon).mutate();
                break;
            case 3:
                shapeDrawable = context.getDrawable(R.drawable.category_square_icon).mutate();
                break;
            case 4:
                shapeDrawable = context.getDrawable(R.drawable.category_squircle_icon).mutate();
                break;
            case 0:
                shapeDrawable = context.getDrawable(R.drawable.brilliant).mutate();
                shapeDrawable.setAlpha(0);
                categoryChar.setTextColor(PublicVariable.colorLightDarkOpposite);
                break;
        }
        return shapeDrawable;
    }

    public ShapesImage initShapesImage(ViewGroup viewGroup, int viewId) {
        ShapesImage shapesImage = viewGroup.findViewById(viewId);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        switch (sharedPreferences.getInt("iconShape", 0)) {
            case 1:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.droplet_icon));
                break;
            case 2:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.circle_icon));
                break;
            case 3:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.square_icon));
                break;
            case 4:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.squircle_icon));
                break;
            case 0:
                shapesImage.setShapeDrawable(null);
                break;
        }
        return shapesImage;
    }

    public ShapesImage initShapesImage(View view, int viewId) {
        ShapesImage shapesImage = view.findViewById(viewId);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        switch (sharedPreferences.getInt("iconShape", 0)) {
            case 1:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.droplet_icon));
                break;
            case 2:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.circle_icon));
                break;
            case 3:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.square_icon));
                break;
            case 4:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.squircle_icon));
                break;
            case 0:
                shapesImage.setShapeDrawable(null);
                break;
        }
        return shapesImage;
    }

    public ShapesImage initShapesImage(Activity activity, int viewId) {
        ShapesImage shapesImage = activity.findViewById(viewId);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        switch (sharedPreferences.getInt("iconShape", 0)) {
            case 1:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.droplet_icon));
                break;
            case 2:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.circle_icon));
                break;
            case 3:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.square_icon));
                break;
            case 4:
                shapesImage.setShapeDrawable(context.getDrawable(R.drawable.squircle_icon));
                break;
            case 0:
                shapesImage.setShapeDrawable(null);
                break;
        }
        return shapesImage;
    }

    public Drawable shapedNotificationUser(Drawable drawable) {
        Drawable drawableBack = new ColorDrawable(extractDominantColor(drawable));
        Drawable drawableFront = drawable;
        LayerDrawable shapedDrawable = new LayerDrawable(new Drawable[]{drawableBack, drawableFront});
        shapedDrawable.setPaddingMode(LayerDrawable.PADDING_MODE_NEST);
        shapedDrawable.setLayerInset(1, 7, 7, 7, 7);
        return shapedDrawable;
    }

    public Drawable shapedAppIcon(String packageName) {
        Drawable appIconDrawable = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
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

    public Drawable shapedAppIcon(ActivityInfo activityInfo) {
        Drawable appIconDrawable = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
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
            LayerDrawable layerDrawable = (LayerDrawable) drawable;

            bitmap = Bitmap.createBitmap(layerDrawable.getIntrinsicWidth(), layerDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            layerDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            layerDrawable.draw(canvas);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return bitmap;
    }

    public Bitmap genericDrawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public Bitmap layerDrawableToBitmap(LayerDrawable layerDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(layerDrawable.getIntrinsicWidth() / 2, layerDrawable.getIntrinsicHeight() / 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        layerDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        layerDrawable.draw(canvas);

        return bitmap;
    }

    public Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public Bitmap appIconBitmap(String packageName) {
        Bitmap bitmap = null;
        try {
            LayerDrawable drawAppShortcuts;
            Drawable drawableIcon = appIcon(packageName);
            Drawable shapeTempDrawable = shapesDrawables();
            if (shapeTempDrawable != null) {
                shapeTempDrawable.setTint(extractDominantColor(drawableIcon));
                drawAppShortcuts = new LayerDrawable(new Drawable[]{
                        shapeTempDrawable,
                        drawableIcon
                });
                drawAppShortcuts.setLayerInset(1, 43, 43, 43, 43);
            } else {
                shapeTempDrawable = new ColorDrawable(Color.TRANSPARENT);
                drawAppShortcuts = new LayerDrawable(new Drawable[]{
                        shapeTempDrawable,
                        drawableIcon
                });
                drawAppShortcuts.setLayerInset(1, 1, 1, 1, 1);
            }
            bitmap = Bitmap.createBitmap(drawAppShortcuts.getIntrinsicWidth(), drawAppShortcuts.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            drawAppShortcuts.setBounds(0, 0, drawAppShortcuts.getIntrinsicWidth(), drawAppShortcuts.getIntrinsicHeight());
            drawAppShortcuts.draw(new Canvas(bitmap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap activityIconBitmap(ActivityInfo activityInfo) {
        Drawable icon = null;
        try {
            icon = activityInfo.loadIcon(context.getPackageManager());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (icon == null) {
                try {
                    icon = context.getPackageManager().getDefaultActivityIcon();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return drawableToBitmap(icon);
    }

    private Drawable resizeDrawable(Drawable drawable, int dstWidth, int dstHeight) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, false);
        return new BitmapDrawable(context.getResources(), bitmapResized).mutate();
    }

    /*App GUI Functions*/
    public void setThemeColorFloating(View view, boolean transparent) {
        if (transparent == true) {
            if (wallpaperStaticLive()) {
                setBackgroundTheme();
            }
            view.setBackgroundColor(setColorAlpha(mixColors(PublicVariable.primaryColor, PublicVariable.colorLightDark, 0.03f), 180));

            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (PublicVariable.themeLightDark) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                if (API > 25) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                }
            }
            window.setStatusBarColor(setColorAlpha(mixColors(PublicVariable.primaryColor, PublicVariable.colorLightDark, 0.03f), 180));
            window.setNavigationBarColor(setColorAlpha(mixColors(PublicVariable.primaryColor, PublicVariable.colorLightDark, 0.03f), 180));
        } else if (transparent == false) {
            view.setBackgroundColor(PublicVariable.colorLightDark);

            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (PublicVariable.themeLightDark) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                if (API > 25) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                }
            }
            window.setStatusBarColor(PublicVariable.colorLightDark);
            window.setNavigationBarColor(PublicVariable.colorLightDark);
        }
    }

    public void setThemeColorAutomationFeature(View view, boolean transparent) {
        if (transparent == true) {
            if (wallpaperStaticLive()) {
                setBackgroundTheme();
            }
            view.setBackgroundColor(setColorAlpha(mixColors(PublicVariable.primaryColor, PublicVariable.colorLightDark, 0.03f), wallpaperStaticLive() ? 180 : 80));

            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (PublicVariable.themeLightDark) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                if (API > 25) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                }
            }
            window.setStatusBarColor(
                    setColorAlpha(
                            mixColors(
                                    PublicVariable.primaryColor, PublicVariable.colorLightDark,
                                    0.75f), wallpaperStaticLive() ? 245 : 113)
            );

            window.setNavigationBarColor(setColorAlpha(mixColors(PublicVariable.primaryColor, PublicVariable.colorLightDark, 0.03f), wallpaperStaticLive() ? 180 : 80));
        } else if (transparent == false) {
            view.setBackgroundColor(PublicVariable.colorLightDark);

            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (PublicVariable.themeLightDark) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                if (API > 25) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                }
            }
            window.setStatusBarColor(PublicVariable.primaryColor);
            window.setNavigationBarColor(PublicVariable.colorLightDark);
        }
    }

    public void setThemeColorPreferences(View view, boolean transparent, String title, String subTitle) {
        if (transparent == true) {
            try {
                if (wallpaperStaticLive()) {
                    setBackgroundTheme();
                }
                view.setBackgroundColor(setColorAlpha(PublicVariable.colorLightDark, wallpaperStaticLive() ? 180 : 80));

                ActionBar actionBar = activity.getActionBar();
                actionBar.setBackgroundDrawable(new ColorDrawable(setColorAlpha(mixColors(PublicVariable.primaryColor, PublicVariable.colorLightDark, 0.65f), wallpaperStaticLive() ? 130 : 30)));
                if (PublicVariable.themeLightDark) {
                    actionBar.setTitle(Html.fromHtml("<font color='" + context.getColor(R.color.dark) + "'>" + title + "</font>"));
                    actionBar.setSubtitle(Html.fromHtml("<small><font color='" + context.getColor(R.color.dark) + "'>" + subTitle + "</font></small>"));
                } else {
                    actionBar.setTitle(Html.fromHtml("<font color='" + context.getColor(R.color.light) + "'>" + title + "</font>"));
                    actionBar.setSubtitle(Html.fromHtml("<small><font color='" + context.getColor(R.color.light) + "'>" + subTitle + "</font></small>"));
                }

                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (PublicVariable.themeLightDark) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    if (API > 25) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                    }
                }
                window.setStatusBarColor(setColorAlpha(mixColors(PublicVariable.primaryColor, PublicVariable.colorLightDark, 0.65f), wallpaperStaticLive() ? 130 : 30));
                window.setNavigationBarColor(setColorAlpha(PublicVariable.colorLightDark, wallpaperStaticLive() ? 180 : 80));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (transparent == false) {
            view.setBackgroundColor(PublicVariable.colorLightDark);

            ActionBar actionBar = activity.getActionBar();
            actionBar.setBackgroundDrawable(new ColorDrawable(PublicVariable.primaryColor));
            actionBar.setTitle(Html.fromHtml("<font color='" + context.getColor(R.color.light) + "'>" + title + "</font>"));
            actionBar.setSubtitle(Html.fromHtml("<small><font color='" + context.getColor(R.color.light) + "'>" + subTitle + "</font></small>"));

            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (PublicVariable.themeLightDark) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                if (API > 25) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                }
            }
            window.setStatusBarColor(PublicVariable.primaryColor);
            window.setNavigationBarColor(PublicVariable.colorLightDark);
        }
    }

    public void setAppThemeBlur() {
        if (appThemeBlurry()) {
            try {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                BitmapDrawable wallpaper = (BitmapDrawable) wallpaperManager.getDrawable();

                Bitmap bitmapWallpaper = wallpaper.getBitmap();
                Bitmap inputBitmap = null;
                if (bitmapWallpaper.getWidth() < displayX() || bitmapWallpaper.getHeight() < displayY()) {
                    inputBitmap = Bitmap.createScaledBitmap(bitmapWallpaper, displayX(), displayY(), false);
                } else {
                    inputBitmap = Bitmap.createBitmap(
                            bitmapWallpaper,
                            (bitmapWallpaper.getWidth() / 2) - (displayX() / 2),
                            (bitmapWallpaper.getHeight() / 2) - (displayY() / 2),
                            displayX(),
                            displayY()
                    );
                }
                Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

                RenderScript renderScript = RenderScript.create(context);
                ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
                Allocation allocationIn = Allocation.createFromBitmap(renderScript, inputBitmap);
                Allocation allocationOut = Allocation.createFromBitmap(renderScript, outputBitmap);

                intrinsicBlur.setRadius(25);
                intrinsicBlur.setInput(allocationIn);
                intrinsicBlur.forEach(allocationOut);
                allocationOut.copyTo(outputBitmap);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), outputBitmap);
                activity.getWindow().getDecorView().setBackground(bitmapDrawable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setWallpaperToBackground() {
        try {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            BitmapDrawable wallpaperManagerDrawable = (BitmapDrawable) wallpaperManager.getDrawable();
            activity.getWindow().getDecorView().setBackground(wallpaperManagerDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBackgroundTheme() {
        if (appThemeBlurry()) {
            setAppThemeBlur();
        } else {
            setWallpaperToBackground();
        }
    }

    public boolean appThemeTransparent() {

        return PreferenceManager.
                getDefaultSharedPreferences(context).getBoolean("transparent", true);
    }

    public boolean appThemeBlurry() {

        return PreferenceManager.
                getDefaultSharedPreferences(context).getBoolean("blur", true);
    }

    public int optionMenuColor() {
        int color = 1;
        if (PublicVariable.themeLightDark) {
            color = appThemeTransparent() == true ?
                    PublicVariable.primaryColor : manipulateColor(PublicVariable.primaryColor, 1.30f);
        } else if (!PublicVariable.themeLightDark) {
            color = appThemeTransparent() == true ?
                    PublicVariable.primaryColor : manipulateColor(PublicVariable.primaryColor, 0.50f);
        }
        return color;
    }

    public void checkLightDarkTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String LightDark = sharedPreferences.getString("themeColor", "2");
        if (LightDark.equals("1")) {//light
            PublicVariable.themeLightDark = true;
        } else if (LightDark.equals("2")) {//dark
            PublicVariable.themeLightDark = false;
        } else if (LightDark.equals("3")) {
            if (colorLightDarkWallpaper()) {//light
                PublicVariable.themeLightDark = true;
            } else if (!colorLightDarkWallpaper()) {//dark
                PublicVariable.themeLightDark = false;
            }
        }
    }

    public boolean wallpaperStaticLive() {
        boolean wallpaperMode = false;
        if (WallpaperManager.getInstance(context).getWallpaperInfo() == null) {//static
            wallpaperMode = true;
        } else if (WallpaperManager.getInstance(context).getWallpaperInfo() != null) {//live
            wallpaperMode = false;
            PreferenceManager.
                    getDefaultSharedPreferences(context).edit().putBoolean("blur", false).apply();
        }
        return wallpaperMode;
    }

    private void takeScreenshot(View view) {
        try {
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            saveBitmapIcon("SnapShot", bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*GUI Functions*/
    public WindowManager.LayoutParams normalLayoutParams(int HW, int X, int Y) {
        int marginClear = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
        WindowManager.LayoutParams layoutParams = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
        int navBarHeight = 0;
        int resourceIdNav = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceIdNav > 0) {
            navBarHeight = context.getResources().getDimensionPixelSize(resourceIdNav);
        }
        int statusBarHeight = 0;
        int resourceIdStatus = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceIdStatus > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceIdStatus);
        }
        WindowManager.LayoutParams layoutParams = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams = new WindowManager.LayoutParams(
                    displayX(),
                    displayY() + statusBarHeight + navBarHeight,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);
        } else {
            layoutParams = new WindowManager.LayoutParams(
                    displayX(),
                    displayY() + statusBarHeight + navBarHeight,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);
        }

        layoutParams.gravity = Gravity.TOP | Gravity.START;
        layoutParams.x = X;
        layoutParams.y = Y - statusBarHeight;
        return layoutParams;
    }

    public WindowManager.LayoutParams backFromEdge(int HW, int X, int Y) {
        WindowManager.LayoutParams layoutParams = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

    public WindowManager.LayoutParams moveToEdge(String packageName, int HW) {
        WindowManager.LayoutParams layoutParams = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams = new WindowManager.LayoutParams(
                    HW,
                    HW,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);
        } else {
            layoutParams = new WindowManager.LayoutParams(
                    HW,
                    HW,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);
        }
        SharedPreferences sharedPrefPosition = null;
        try {
            sharedPrefPosition = context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        layoutParams.gravity = Gravity.TOP | Gravity.START;
        if (PreferenceManager.getDefaultSharedPreferences(context).getString("stick", "1").equals("1")) {//Left
            layoutParams.x = -(HW / 2);
        } else if (PreferenceManager.getDefaultSharedPreferences(context).getString("stick", "1").equals("2")) {//Right
            layoutParams.x = displayX() - (HW / 2);
        }
        layoutParams.y = sharedPrefPosition.getInt("Y", 0);
        layoutParams.windowAnimations = android.R.style.Animation_Dialog;
        return layoutParams;
    }

    public WindowManager.LayoutParams handleOrientationLandscape(String packageName, int HW) {
        WindowManager.LayoutParams layoutParams = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

        FunctionsClassDebug.Companion.PrintDebug(packageName);


        SharedPreferences sharedPrefPosition = context.getSharedPreferences(packageName, Context.MODE_PRIVATE);

        layoutParams.gravity = Gravity.TOP | Gravity.START;
        layoutParams.x = sharedPrefPosition.getInt("Y", 0);
        layoutParams.y = displayY() - sharedPrefPosition.getInt("X", 0);

        SharedPreferences.Editor editor = sharedPrefPosition.edit();
        editor.putInt("X", layoutParams.x);
        editor.putInt("Y", layoutParams.y);
        editor.apply();

        layoutParams.windowAnimations = android.R.style.Animation_Dialog;
        return layoutParams;
    }

    public WindowManager.LayoutParams handleOrientationPortrait(String packageName, int HW) {
        WindowManager.LayoutParams layoutParams = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

        FunctionsClassDebug.Companion.PrintDebug(packageName);

        SharedPreferences sharedPrefPosition = context.getSharedPreferences(packageName, Context.MODE_PRIVATE);

        layoutParams.gravity = Gravity.TOP | Gravity.START;
        layoutParams.x = displayX() - sharedPrefPosition.getInt("Y", 0);
        layoutParams.y = sharedPrefPosition.getInt("X", 0);

        SharedPreferences.Editor editor = sharedPrefPosition.edit();
        editor.putInt("X", layoutParams.x);
        editor.putInt("Y", layoutParams.y);
        editor.apply();

        layoutParams.windowAnimations = android.R.style.Animation_Dialog;
        return layoutParams;
    }

    public WindowManager.LayoutParams normalWidgetLayoutParams(String packageName, int widgetId, int widgetWidth, int widgetHeight) {
        WindowManager.LayoutParams layoutParams = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);
        } else {
            layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(widgetId + packageName, Context.MODE_PRIVATE);

        layoutParams.gravity = Gravity.TOP | Gravity.START;
        layoutParams.width = sharedPreferences.getInt("WidgetWidth", widgetWidth);
        layoutParams.height = sharedPreferences.getInt("WidgetHeight", widgetHeight);
        layoutParams.x = sharedPreferences.getInt("X", 19);
        layoutParams.y = sharedPreferences.getInt("Y", 19);
        layoutParams.windowAnimations = android.R.style.Animation_Dialog;

        return layoutParams;
    }

    public WindowManager.LayoutParams moveWidgetsToEdge(String packageName, int widgetId, int widgetMinimizeWH) {
        WindowManager.LayoutParams layoutParams = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);
        } else {
            layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(widgetId + packageName, Context.MODE_PRIVATE);

        layoutParams.gravity = Gravity.TOP | Gravity.START;
        layoutParams.width = widgetMinimizeWH;
        layoutParams.height = widgetMinimizeWH;
        if (PreferenceManager.getDefaultSharedPreferences(context).getString("stick", "1").equals("1")) {//Left
            layoutParams.x = -(widgetMinimizeWH / 2);
        } else if (PreferenceManager.getDefaultSharedPreferences(context).getString("stick", "1").equals("2")) {//Right
            layoutParams.x = displayX() - (widgetMinimizeWH / 2);
        }
        layoutParams.y = sharedPreferences.getInt("Y", 19);
        layoutParams.windowAnimations = android.R.style.Animation_Dialog;

        return layoutParams;
    }

    public void popupOptionShortcuts(final Context context, View anchorView, final String PackageName) {
        PopupMenu popupMenu = new PopupMenu(context, anchorView, Gravity.CENTER);
        if (PublicVariable.themeLightDark == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                popupMenu = new PopupMenu(context, anchorView, Gravity.CENTER, 0, R.style.GeeksEmpire_Dialogue_Light);
            }
        } else if (PublicVariable.themeLightDark == false) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                popupMenu = new PopupMenu(context, anchorView, Gravity.CENTER, 0, R.style.GeeksEmpire_Dialogue_Dark);
            }
        }

        String[] menuItems = context.getResources().getStringArray(R.array.ContextMenu);
        Drawable backgroundDrawable = shapesDrawables();
        Drawable foregroundDrawable = returnAPI() >= 28 ? resizeDrawable(context.getDrawable(R.drawable.w_pref_popup).mutate(), 100, 100) : context.getDrawable(R.drawable.w_pref_popup).mutate();
        if (shapesDrawables() == null) {
            backgroundDrawable = returnAPI() >= 28 ? resizeDrawable(context.getDrawable(R.drawable.ic_launcher).mutate(), 100, 100) : context.getDrawable(R.drawable.ic_launcher).mutate();
            backgroundDrawable.setAlpha(0);
            if (!loadCustomIcons()) {
                foregroundDrawable.setTint(extractVibrantColor(appIcon(PackageName)));
            }
        } else {
            backgroundDrawable = returnAPI() >= 28 ? resizeDrawable(shapesDrawables().mutate(), 100, 100) : shapesDrawables().mutate();
            backgroundDrawable.setTint(extractVibrantColor(appIcon(PackageName)));
        }
        if (loadCustomIcons()) {
            backgroundDrawable = returnAPI() >= 28 ? resizeDrawable(appIcon(customIconPackageName()).mutate(), 100, 100) : appIcon(customIconPackageName()).mutate();
            backgroundDrawable.setTint(extractVibrantColor(appIcon(PackageName)));
        }
        LayerDrawable popupItemIcon = new LayerDrawable(
                new Drawable[]{
                        backgroundDrawable,
                        foregroundDrawable
                });
        for (int itemId = 0; itemId < menuItems.length; itemId++) {
            popupMenu.getMenu()
                    .add(Menu.NONE, itemId, itemId, Html.fromHtml("<font color='" + PublicVariable.colorLightDarkOpposite + "'>" + menuItems[itemId] + "</font>"))
                    .setIcon(popupItemIcon);
        }

        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 0) {
                    PublicVariable.size = 26;
                    PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, context.getResources().getDisplayMetrics());

                    runUnlimitedShortcutsService(PackageName);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setSizeBack();
                        }
                    }, 50);
                } else if (item.getItemId() == 1) {
                    PublicVariable.size = 39;
                    PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, context.getResources().getDisplayMetrics());

                    runUnlimitedShortcutsService(PackageName);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setSizeBack();
                        }
                    }, 50);
                } else if (item.getItemId() == 2) {
                    PublicVariable.size = 52;
                    PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, context.getResources().getDisplayMetrics());

                    runUnlimitedShortcutsService(PackageName);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setSizeBack();
                        }
                    }, 50);
                }
                updateRecoverShortcuts();
                return true;
            }
        });
        popupMenu.show();
    }

    public void popupOptionCategory(final Context context, View anchorView, final String categoryName, final int indicatorPosition) {
        PopupMenu popupMenu = new PopupMenu(context, anchorView, Gravity.CENTER);
        if (PublicVariable.themeLightDark == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                popupMenu = new PopupMenu(context, anchorView, Gravity.CENTER, 0, R.style.GeeksEmpire_Dialogue_Category_Light);
            }
        } else if (PublicVariable.themeLightDark == false) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                popupMenu = new PopupMenu(context, anchorView, Gravity.CENTER, 0, R.style.GeeksEmpire_Dialogue_Category_Dark);
            }
        }
        String[] menuItems;
        if (loadRecoveryIndicatorCategory(categoryName)) {
            menuItems = context.getResources().getStringArray(R.array.ContextMenuCategoryRemove);
        } else {
            menuItems = context.getResources().getStringArray(R.array.ContextMenuCategory);
        }

        Drawable popupItemIcon = returnAPI() >= 28 ? resizeDrawable(context.getDrawable(R.drawable.w_pref_popup), 100, 100) : context.getDrawable(R.drawable.w_pref_popup);
        popupItemIcon.setTint(PublicVariable.primaryColorOpposite);

        for (int itemId = 0; itemId < menuItems.length; itemId++) {
            popupMenu.getMenu()
                    .add(Menu.NONE, itemId, itemId, Html.fromHtml("<font color='" + PublicVariable.colorLightDarkOpposite + "'>" + menuItems[itemId] + "</font>"))
                    .setIcon(popupItemIcon);
        }

        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 0) {
                    PublicVariable.size = 26;
                    runUnlimitedCategoryService(categoryName);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setSizeBack();
                        }
                    }, 100);
                } else if (item.getItemId() == 1) {
                    PublicVariable.size = 52;
                    runUnlimitedCategoryService(categoryName);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setSizeBack();
                        }
                    }, 100);
                } else if (item.getItemId() == 2) {
                    PublicVariable.size = 78;
                    runUnlimitedCategoryService(categoryName);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setSizeBack();
                        }
                    }, 100);
                } else if (item.getItemId() == 3) {
                    if (!loadRecoveryIndicatorCategory(categoryName)) {
                        saveFileAppendLine(".uCategory", categoryName);
                    } else {
                        removeLine(".uCategory", categoryName);
                    }
                    context.sendBroadcast(new Intent("Category_Reload"));
                } else if (item.getItemId() == 4) {
                    try {
                        String[] categoryContent = readFileLine(categoryName);
                        for (String packageName : categoryContent) {
                            context.deleteFile(packageName + categoryName);
                        }
                        removeLine(".categoryInfo", categoryName);
                        removeLine(".uCategory", categoryName);
                        context.deleteFile(categoryName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        context.sendBroadcast(new Intent("Category_Reload"));
                    }
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public void popupOptionWidget(Activity activity, Context context, View anchorView, String packageName, final String providerClassName, String widgetLabel, Drawable widgetPreview, boolean addedWidgetRecovery) {
        PopupMenu popupMenu = new PopupMenu(context, anchorView, Gravity.CENTER);
        if (PublicVariable.themeLightDark == true) {
            popupMenu = new PopupMenu(context, anchorView, Gravity.CENTER, 0, R.style.GeeksEmpire_Dialogue_Category_Light);
        } else if (PublicVariable.themeLightDark == false) {
            popupMenu = new PopupMenu(context, anchorView, Gravity.CENTER, 0, R.style.GeeksEmpire_Dialogue_Category_Dark);
        }
        String[] menuItems;
        if (addedWidgetRecovery) {
            menuItems = context.getResources().getStringArray(R.array.ContextMenuWidgetRemove);
        } else {
            menuItems = context.getResources().getStringArray(R.array.ContextMenuWidget);
        }

        Drawable popupItemIcon = returnAPI() >= 28 ? resizeDrawable(context.getDrawable(R.drawable.w_pref_popup), 100, 100) : context.getDrawable(R.drawable.w_pref_popup);
        popupItemIcon.setTint(extractVibrantColor(appIcon(packageName)));

        if (interstitialAdWidgetShortcuts.isLoaded()) {
            for (int itemId = 0; itemId < menuItems.length; itemId++) {
                popupMenu.getMenu()
                        .add(Menu.NONE, itemId, itemId,
                                Html.fromHtml("<font color='" + PublicVariable.colorLightDarkOpposite + "'>" + menuItems[itemId] + (interstitialAdWidgetShortcuts.isLoaded() ? " 📺 " : "") + "</font>"));
            }
        } else {
            for (int itemId = 0; itemId < menuItems.length; itemId++) {
                popupMenu.getMenu()
                        .add(Menu.NONE, itemId, itemId,
                                Html.fromHtml("<font color='" + PublicVariable.colorLightDarkOpposite + "'>" + menuItems[itemId] + "</font>"))
                        .setIcon(popupItemIcon);
            }

            try {
                Field[] fields = popupMenu.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popupMenu);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper
                                .getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod(
                                "setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 0: {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                WidgetDataInterface widgetDataInterface = Room.databaseBuilder(context, WidgetDataInterface.class, PublicVariable.WIDGET_DATA_DATABASE_NAME)
                                        .fallbackToDestructiveMigration()
                                        .addCallback(new RoomDatabase.Callback() {
                                            @Override
                                            public void onCreate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                                                super.onCreate(supportSQLiteDatabase);
                                            }

                                            @Override
                                            public void onOpen(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                                                super.onOpen(supportSQLiteDatabase);

                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        context.sendBroadcast(new Intent("FORCE_RELOAD"));

                                                        try {
                                                            removeWidgetToHomeScreen(FloatingWidgetHomeScreenShortcuts.class, packageName, widgetLabel, widgetPreview, providerClassName);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }
                                        })
                                        .build();
                                widgetDataInterface.initDataAccessObject().deleteByWidgetClassNameProviderWidget(packageName, providerClassName);
                                widgetDataInterface.close();
                            }
                        }).start();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            context.deleteSharedPreferences(providerClassName + packageName);
                        }

                        break;
                    }
                    case 1: {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                WidgetDataInterface widgetDataInterface = Room.databaseBuilder(context, WidgetDataInterface.class, PublicVariable.WIDGET_DATA_DATABASE_NAME)
                                        .fallbackToDestructiveMigration()
                                        .addCallback(new RoomDatabase.Callback() {
                                            @Override
                                            public void onCreate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                                                super.onCreate(supportSQLiteDatabase);
                                            }

                                            @Override
                                            public void onOpen(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                                                super.onOpen(supportSQLiteDatabase);

                                            }
                                        })
                                        .build();
                                widgetDataInterface.initDataAccessObject().updateRecoveryByClassNameProviderWidget(packageName, providerClassName, !addedWidgetRecovery);
                                widgetDataInterface.close();
                            }
                        }).start();
                        context.sendBroadcast(new Intent("FORCE_RELOAD"));

                        break;
                    }
                    case 2: {
                        if (PublicVariable.eligibleLoadShowAds && interstitialAdWidgetShortcuts.isLoaded()) {
                            packageNameWidget = packageName;
                            shortcutNameWidget = widgetLabel;
                            widgetPreviewDrawable = widgetPreview;
                            widgetProviderClassName = providerClassName;

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    interstitialAdWidgetShortcuts.show();
                                }
                            }, 333);
                        } else {
                            try {
                                widgetToHomeScreen(FloatingWidgetHomeScreenShortcuts.class, packageName, widgetLabel, widgetPreview, providerClassName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    }
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public void setSizeBack() {
        PublicVariable.size = readDefaultPreference("floatingSize", 39);
        PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, context.getResources().getDisplayMetrics());
    }

    public void openContextMenu(Activity activity, View view) {
        activity.openContextMenu(view);
    }

    public void Toast(String toastContent, int toastGravity/*, int toastColor*/) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_view, null/*(ViewGroup) activity.findViewById(R.id.toastView)*/);

        LayerDrawable drawToast = null;
        if (toastGravity == Gravity.TOP) {
            drawToast = (LayerDrawable) context.getDrawable(R.drawable.toast_background_top);
        } else if (toastGravity == Gravity.BOTTOM) {
            drawToast = (LayerDrawable) context.getDrawable(R.drawable.toast_background_bottom);
        }
        Drawable backToast = drawToast.findDrawableByLayerId(R.id.backtemp);

        TextView textView = layout.findViewById(R.id.toastText);
        textView.setText(Html.fromHtml("<small>" + toastContent + "</small>"));
        if (appThemeTransparent() == true) {
            if (PublicVariable.themeLightDark) {
                backToast.setTint(context.getColor(R.color.light_transparent));
                textView.setBackground(drawToast);
                textView.setTextColor(context.getColor(R.color.dark));
                textView.setShadowLayer(0.02f, 2, 2, context.getColor(R.color.dark_transparent_high));
            } else if (!PublicVariable.themeLightDark) {
                backToast.setTint(context.getColor(R.color.dark_transparent));
                textView.setBackground(drawToast);
                textView.setTextColor(context.getColor(R.color.light));
                textView.setShadowLayer(0.02f, 2, 2, context.getColor(R.color.light_transparent_high));
            }
        } else {
            backToast.setTint(context.getColor(R.color.light_transparent));
            textView.setBackground(drawToast);
            textView.setTextColor(context.getColor(R.color.dark));
            textView.setShadowLayer(0.02f, 2, 2, context.getColor(R.color.dark_transparent_high));
        }
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | toastGravity, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void Toast(String toastContent, int toastGravity, int toastColor) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_view, null/*(ViewGroup) activity.findViewById(R.id.toastView)*/);

        LayerDrawable drawToast = null;
        if (toastGravity == Gravity.TOP) {
            drawToast = (LayerDrawable) context.getDrawable(R.drawable.toast_background_top);
        } else if (toastGravity == Gravity.BOTTOM) {
            drawToast = (LayerDrawable) context.getDrawable(R.drawable.toast_background_bottom);
        }
        Drawable backToast = drawToast.findDrawableByLayerId(R.id.backtemp);
        backToast.setTint(toastColor);

        TextView textView = layout.findViewById(R.id.toastText);
        textView.setText(Html.fromHtml("<small>" + toastContent + "</small>"));
        if (appThemeTransparent() == true) {
            if (PublicVariable.themeLightDark) {
                textView.setBackground(drawToast);
                textView.setTextColor(context.getColor(R.color.dark));
                textView.setShadowLayer(0.02f, 2, 2, context.getColor(R.color.dark_transparent_high));
            } else if (!PublicVariable.themeLightDark) {
                textView.setBackground(drawToast);
                textView.setTextColor(context.getColor(R.color.light));
                textView.setShadowLayer(0.02f, 2, 2, context.getColor(R.color.light_transparent_high));
            }
        } else {
            backToast.setTint(context.getColor(R.color.light_transparent));
            textView.setBackground(drawToast);
            textView.setTextColor(context.getColor(R.color.dark));
            textView.setShadowLayer(0.02f, 2, 2, context.getColor(R.color.dark_transparent_high));
        }
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | toastGravity, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void updateRecoverShortcuts() {
        try {
            if (context.getFileStreamPath(".uFile").exists()) {
                PublicVariable.RecoveryShortcuts = new ArrayList<String>();

                FileInputStream fileInputStream = new FileInputStream(context.getFileStreamPath(".uFile"));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

                String line = "";
                int u = 0;
                while ((line = bufferedReader.readLine()) != null) {
                    PublicVariable.RecoveryShortcuts.add(u, line);
                    u++;
                }

                fileInputStream.close();
                bufferedReader.close();
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

    public boolean loadRecoveryIndicatorCategory(String categoryName) {
        boolean inRecovery = false;
        try {
            for (String anAppNameArrayRecovery : readFileLine(".uCategory")) {
                if (categoryName.equals(anAppNameArrayRecovery)) {
                    inRecovery = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inRecovery;
    }

    public float DpToPixel(float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public float PixelToDp(float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
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
        int section = 1;
        if (X < displayX() / 2 && Y < displayY() / 2) {//top-left
            section = 1;
        } else if (X > displayX() / 2 && Y < displayY() / 2) {//top-right
            section = 2;
        } else if (X < displayX() / 2 && Y > displayY() / 2) {//bottom-left
            section = 3;
        } else if (X > displayX() / 2 && Y > displayY() / 2) {//bottom-right
            section = 4;
        }
        return section;
    }

    public int columnCount(int itemWidth) {
        return (int) (displayX() / DpToPixel(itemWidth));
    }

    public void shortcutToHomeScreen(Class className, String shortcutName, Drawable drawableIcon, int shortcutId) {
        Intent differentIntent = new Intent(context, className);
        differentIntent.setAction(Intent.ACTION_MAIN);
        differentIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        differentIntent.putExtra("ShortcutsId", shortcutId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(context, String.valueOf(shortcutId))
                    .setShortLabel(shortcutName)
                    .setLongLabel(shortcutName)
                    .setIcon(Icon.createWithBitmap(genericDrawableToBitmap(drawableIcon)))
                    .setIntent(differentIntent)
                    .build();
            context.getSystemService(ShortcutManager.class).requestPinShortcut(shortcutInfo, null);
        } else {
            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, differentIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, genericDrawableToBitmap(drawableIcon));
            addIntent.putExtra("duplicate", true);
            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            context.sendBroadcast(addIntent);
        }
    }

    public void removeWidgetToHomeScreen(Class className, String packageName, String shortcutName, Drawable widgetPreviewDrawable, String providerClassName) throws Exception {
        Intent differentIntent = new Intent(context, className);
        differentIntent.setAction(Intent.ACTION_MAIN);
        differentIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        differentIntent.putExtra("ShortcutsId", providerClassName);
        differentIntent.putExtra("ShortcutLabel", shortcutName);

        Drawable forNull = context.getDrawable(R.drawable.ic_launcher);
        forNull.setAlpha(0);
        LayerDrawable widgetShortcutIcon = (LayerDrawable) context.getDrawable(R.drawable.widget_home_screen_shortcuts);
        try {
            widgetShortcutIcon.setDrawableByLayerId(R.id.one, widgetPreviewDrawable);
        } catch (Exception e) {
            widgetShortcutIcon.setDrawableByLayerId(R.id.one, forNull);
        }
        try {
            if (widgetPreviewDrawable.getIntrinsicHeight() < DpToInteger(52)) {

            } else {
                widgetShortcutIcon.setDrawableByLayerId(R.id.two, getAppIconDrawableCustomIcon(packageName));
            }
        } catch (Exception e) {
            widgetShortcutIcon.setDrawableByLayerId(R.id.two, forNull);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            List<String> shortcutToDelete = new ArrayList<String>();
            shortcutToDelete.add((packageName + providerClassName));

            context.getSystemService(ShortcutManager.class).disableShortcuts(shortcutToDelete);
        } else {
            Intent removeIntent = new Intent();
            removeIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, differentIntent);
            removeIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
            removeIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, layerDrawableToBitmap(widgetShortcutIcon));
            removeIntent.putExtra("duplicate", true);
            removeIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
            context.sendBroadcast(removeIntent);
        }
    }

    public void widgetToHomeScreen(Class className, String packageName, String shortcutName, Drawable widgetPreviewDrawable, String providerClassName) throws Exception {
        Intent differentIntent = new Intent(context, className);
        differentIntent.setAction("CREATE_FLOATING_WIDGET_HOME_SCREEN_SHORTCUTS");
        differentIntent.addCategory(Intent.CATEGORY_DEFAULT);
        differentIntent.putExtra("PackageName", packageName);
        differentIntent.putExtra("ProviderClassName", providerClassName);
        differentIntent.putExtra("ShortcutLabel", shortcutName);

        Drawable forNull = context.getDrawable(R.drawable.ic_launcher);
        forNull.setAlpha(0);
        LayerDrawable widgetShortcutIcon = (LayerDrawable) context.getDrawable(R.drawable.widget_home_screen_shortcuts);
        try {
            widgetShortcutIcon.setDrawableByLayerId(R.id.one, widgetPreviewDrawable);
        } catch (Exception e) {
            widgetShortcutIcon.setDrawableByLayerId(R.id.one, forNull);
        }
        try {
            if (widgetPreviewDrawable.getIntrinsicHeight() < DpToInteger(77)) {

            } else {
                widgetShortcutIcon.setDrawableByLayerId(R.id.two, getAppIconDrawableCustomIcon(packageName));
            }
        } catch (Exception e) {
            widgetShortcutIcon.setDrawableByLayerId(R.id.two, forNull);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(context, (packageName + providerClassName))
                    .setShortLabel(shortcutName)
                    .setLongLabel(shortcutName)
                    .setIcon(Icon.createWithBitmap(layerDrawableToBitmap(widgetShortcutIcon)))
                    .setIntent(differentIntent)
                    .build();
            context.getSystemService(ShortcutManager.class).requestPinShortcut(shortcutInfo, null);
        } else {
            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, differentIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, layerDrawableToBitmap(widgetShortcutIcon));
            addIntent.putExtra("duplicate", true);
            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            context.sendBroadcast(addIntent);
        }
    }

    public void removeWidgetHomeScreen(Class className, String packageName, String shortcutName, Drawable widgetPreviewDrawable, int shortcutId) throws Exception {
        Intent differentIntent = new Intent(context, className);
        differentIntent.setAction(Intent.ACTION_MAIN);
        differentIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        differentIntent.putExtra("ShortcutsId", shortcutId);
        differentIntent.putExtra("ShortcutLabel", shortcutName);

        Drawable forNull = context.getDrawable(R.drawable.ic_launcher);
        forNull.setAlpha(0);
        LayerDrawable widgetShortcutIcon = (LayerDrawable) context.getDrawable(R.drawable.widget_home_screen_shortcuts);
        try {
            widgetShortcutIcon.setDrawableByLayerId(R.id.one, widgetPreviewDrawable);
        } catch (Exception e) {
            widgetShortcutIcon.setDrawableByLayerId(R.id.one, forNull);
        }
        try {
            if (widgetPreviewDrawable.getIntrinsicHeight() < DpToInteger(52)) {

            } else {
                widgetShortcutIcon.setDrawableByLayerId(R.id.two, getAppIconDrawableCustomIcon(packageName));
            }
        } catch (Exception e) {
            widgetShortcutIcon.setDrawableByLayerId(R.id.two, forNull);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            List<String> shortcutToDelete = new ArrayList<String>();
            shortcutToDelete.add(String.valueOf(shortcutId));

            context.getSystemService(ShortcutManager.class).disableShortcuts(shortcutToDelete);
        } else {
            Intent removeIntent = new Intent();
            removeIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, differentIntent);
            removeIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
            removeIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, layerDrawableToBitmap(widgetShortcutIcon));
            removeIntent.putExtra("duplicate", true);
            removeIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
            context.sendBroadcast(removeIntent);
        }
    }

    public void ShortcutsDialogue(final Class className, final String activityAlias, final String shortcutName, final int drawableId) {
        AlertDialog.Builder alertDialog = null;
        if (PublicVariable.themeLightDark == true) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Light);
        } else if (PublicVariable.themeLightDark == false) {
            alertDialog = new AlertDialog.Builder(activity, R.style.GeeksEmpire_Dialogue_Dark);
        }
        alertDialog.setCancelable(true);

        final String aliasButton;
        if (ComponentEnabled(context.getPackageManager(), context.getPackageName(), context.getPackageName() + activityAlias)) {
            aliasButton = context.getString(R.string.deleteAlias);
        } else {
            aliasButton = context.getString(R.string.createAlias);
        }
        String[] shortcutOption = new String[]{context.getString(R.string.createShortcut), aliasButton};
        alertDialog.setSingleChoiceItems(shortcutOption, 0, null);
        alertDialog.setTitle(Html.fromHtml("<small>" + shortcutName + "</small>"));
        alertDialog.setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                switch (selectedPosition) {
                    case 0:
//                        appToDesktop(className, shortcutName, drawableId);
                        break;
                    case 1:
//                        if (aliasButton.equals(context.getString(R.string.deleteAlias))) {
//                            context.getPackageManager().setComponentEnabledSetting(
//                                    new ComponentName(context.getPackageName(), context.getPackageName() + activityAlias),
//                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//                        } else if (aliasButton.equals(context.getString(R.string.createAlias))) {
//                            context.getPackageManager().setComponentEnabledSetting(
//                                    new ComponentName(context.getPackageName(), context.getPackageName() + activityAlias),
//                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//                        }
                        break;
                }
                dialog.dismiss();
            }
        });

        alertDialog.setNegativeButton(context.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.dismiss();
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
            e.printStackTrace();
        }
    }

    public boolean checkStickyEdge() {
        boolean stickyEdge = true;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String stick = sharedPrefs.getString("stick", "1");
        if (stick.equals("1")) {//Left
            stickyEdge = true;
        } else if (stick.equals("2")) {//Right
            stickyEdge = false;
        }
        return stickyEdge;
    }

    public static class WindowMode {
        static final int WINDOWING_MODE_FREEFORM = 5;
        private static final int FREEFORM_WORKSPACE_STACK_ID = 2;
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
        boolean getFirstApp = false;
        Animator.AnimatorListener scaleDownListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewToBeat.animate().scaleXBy(0.33f).scaleYBy(0.33f).setDuration(233).setListener(scaleUpListener);
                if (PublicVariable.hearBeatCheckPoint) {
                    if (getFirstApp) {
                        try {
                            UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                            List<UsageStats> queryUsageStats = mUsageStatsManager
                                    .queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                                            System.currentTimeMillis() - 1000 * 60,            //begin
                                            System.currentTimeMillis());                            //end
                            Collections.sort(queryUsageStats, new Comparator<UsageStats>() {
                                @Override
                                public int compare(UsageStats left, UsageStats right) {
                                    return Long.compare(right.getLastTimeUsed(), left.getLastTimeUsed());
                                }
                            });
                            String inFrontPackageName = queryUsageStats.get(0).getPackageName();
                            String secondPackageName = queryUsageStats.get(1).getPackageName();
                            if (inFrontPackageName.contains(packageName) || secondPackageName.contains(packageName)) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        context.stopService(new Intent(context, FloatingSplash.class));
                                    }
                                }, 133);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
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
                viewToBeat.animate().scaleXBy(-0.33f).scaleYBy(-0.33f).setDuration(333).setListener(scaleDownListener);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        };

        public HeartBeat(String packageName, View viewToBeat) {
            this.packageName = packageName;
            this.viewToBeat = viewToBeat;
            this.viewToBeat.animate().scaleXBy(0.33f).scaleYBy(0.33f).setDuration(233).setListener(scaleUpListener);

            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOp("android:get_usage_stats", android.os.Process.myUid(), context.getPackageName());
            if (mode == AppOpsManager.MODE_ALLOWED) {
                getFirstApp = true;
            }
        }
    }

    public class HeartBeatClose {
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
                viewToBeat.animate().scaleXBy(0.33f).scaleYBy(0.33f).setDuration(233).setListener(scaleUpListener);

                try {
                    UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                    List<UsageStats> queryUsageStats = mUsageStatsManager
                            .queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                                    System.currentTimeMillis() - 1000 * 60,            //begin
                                    System.currentTimeMillis());                    //end
                    Collections.sort(queryUsageStats, new Comparator<UsageStats>() {
                        @Override
                        public int compare(UsageStats left, UsageStats right) {
                            return Long.compare(right.getLastTimeUsed(), left.getLastTimeUsed());
                        }
                    });
                    String inFrontPackageName = queryUsageStats.get(0).getPackageName();

                    PackageManager localPackageManager = context.getPackageManager();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    String defaultLauncher = localPackageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY).activityInfo.packageName;
                    if (inFrontPackageName.contains(defaultLauncher)) {
                        context.stopService(new Intent(context, FloatingSplash.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    context.stopService(new Intent(context, FloatingSplash.class));
                }
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
                viewToBeat.animate().scaleXBy(-0.33f).scaleYBy(-0.33f).setDuration(333).setListener(scaleDownListener);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        };

        public HeartBeatClose(String packageName, View viewToBeat) {
            this.packageName = packageName;
            this.viewToBeat = viewToBeat;
            this.viewToBeat.animate().scaleXBy(0.33f).scaleYBy(0.33f).setDuration(233).setListener(scaleUpListener);
        }
    }

    /*Floating Splash*/
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
                                           boolean circularExpandCollapse) throws Exception {
        if (circularExpandCollapse) {
            view.setBackgroundColor(iconColor);
            new HeartBeat(packageName, childView);

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
            if (UsageStatsEnabled()) {
                animator.setDuration(333);
            } else {
                animator.setDuration(555);
            }
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (className != null) {
                                if (FreeForm()) {
                                    openApplicationFreeForm(packageName,
                                            className,
                                            xPosition,
                                            (displayX() / 2),
                                            yPosition,
                                            (displayY() / 2)
                                    );
                                } else {
                                    appsLaunchPad(packageName, className);
                                }
                            } else {
                                if (FreeForm()) {
                                    openApplicationFreeForm(packageName,
                                            xPosition,
                                            (displayX() / 2),
                                            yPosition,
                                            (displayY() / 2)
                                    );
                                } else {
                                    appsLaunchPad(packageName);
                                }
                            }
                        }
                    }, 51);
                    if (UsageStatsEnabled()) {
                        try {
                            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                            List<UsageStats> queryUsageStats = usageStatsManager
                                    .queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                                            System.currentTimeMillis() - 1000 * 60,            //begin
                                            System.currentTimeMillis());                    //end
                            Collections.sort(queryUsageStats, new Comparator<UsageStats>() {
                                @Override
                                public int compare(UsageStats left, UsageStats right) {
                                    return Long.compare(right.getLastTimeUsed(), left.getLastTimeUsed());
                                }
                            });
                            String inFrontPackage = queryUsageStats.get(0).getPackageName();
                            if (inFrontPackage.contains(packageName)) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        context.stopService(new Intent(context, FloatingSplash.class));
                                    }
                                }, 133);
                            } else {
                                PublicVariable.hearBeatCheckPoint = true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        context.stopService(new Intent(context, FloatingSplash.class));
                    }
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

    public void circularRevealSplashScreenClose(final String packageName, final View view, final View childView,
                                                final int xPosition, final int yPosition) {
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
        view.setBackground(bitmapToDrawable(inputBitmap));

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
        animator.setDuration(333);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (UsageStatsEnabled()) {
                    try {
                        Intent homeScreen = new Intent(Intent.ACTION_MAIN);
                        homeScreen.addCategory(Intent.CATEGORY_HOME);
                        homeScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(homeScreen,
                                ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out).toBundle());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        new HeartBeatClose(packageName, childView);
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    public boolean splashReveal() {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("floatingSplash", false);
    }

    /*Color GUI Functions*/
    public void loadSavedColor() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(".themeColor", Context.MODE_PRIVATE);

        PublicVariable.themeColor = sharedPreferences.getInt("themeColor", context.getColor(R.color.default_color));//getVibrantColor
        PublicVariable.themeTextColor = sharedPreferences.getInt("themeTextColor", context.getColor(R.color.default_color));//getDarkMutedColor
        PublicVariable.themeColorString = sharedPreferences.getString("themeColorString", String.valueOf((context.getColor(R.color.default_color))));

        if (PublicVariable.themeLightDark) {
            PublicVariable.primaryColor = PublicVariable.themeColor;
            PublicVariable.primaryColorOpposite = PublicVariable.themeTextColor;
            PublicVariable.colorLightDark = context.getColor(R.color.light);
            PublicVariable.colorLightDarkOpposite = context.getColor(R.color.dark);
        } else if (!PublicVariable.themeLightDark) {
            PublicVariable.primaryColor = PublicVariable.themeTextColor;
            PublicVariable.primaryColorOpposite = PublicVariable.themeColor;
            PublicVariable.colorLightDark = context.getColor(R.color.dark);
            PublicVariable.colorLightDarkOpposite = context.getColor(R.color.light);
        }
        PublicVariable.dominantColor = sharedPreferences.getInt("dominantColor", context.getColor(R.color.default_color));
    }

    public void extractWallpaperColor() {
        int themeColor, themeTextColor, dominantColor;
        String themeColorString;
        Palette currentColor;
        try {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);

            final Drawable currentWallpaper = wallpaperManager.getDrawable();
            final Bitmap bitmap = ((BitmapDrawable) currentWallpaper).getBitmap();

            if (bitmap != null && !bitmap.isRecycled()) {
                currentColor = Palette.from(bitmap).generate();
            } else {
                Bitmap bitmapTemp = BitmapFactory.decodeResource(context.getResources(), R.drawable.brilliant);
                currentColor = Palette.from(bitmapTemp).generate();
            }

            int defaultColor = context.getColor(R.color.default_color);

            themeColor = currentColor.getVibrantColor(defaultColor);
            themeTextColor = currentColor.getDarkMutedColor(defaultColor);
            themeColorString = "#" + Integer.toHexString(currentColor.getDarkMutedColor(defaultColor)).substring(2);

            dominantColor = currentColor.getDominantColor(defaultColor);
        } catch (Exception e) {
            e.printStackTrace();

            themeColor = context.getColor(R.color.default_color);
            themeTextColor = context.getColor(R.color.default_color);
            themeColorString = "" + context.getColor(R.color.default_color);

            dominantColor = context.getColor(R.color.default_color);
        }


        SharedPreferences sharedPreferences = context.getSharedPreferences(".themeColor", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("themeColor", themeColor);
        editor.putInt("themeTextColor", themeTextColor);
        editor.putString("themeColorString", themeColorString);
        editor.putInt("dominantColor", dominantColor);
        editor.apply();
    }

    public int extractVibrantColor(Drawable drawable) {
        int VibrantColor = context.getColor(R.color.default_color);
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

                int defaultColor = context.getColor(R.color.default_color);
                VibrantColor = currentColor.getVibrantColor(defaultColor);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (bitmap != null && !bitmap.isRecycled()) {
                    currentColor = Palette.from(bitmap).generate();

                    int defaultColor = context.getColor(R.color.default_color);
                    VibrantColor = currentColor.getMutedColor(defaultColor);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return VibrantColor;
    }

    public int extractDominantColor(Drawable drawable) {
        int DominanctColor = context.getColor(R.color.default_color);
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

                int defaultColor = context.getColor(R.color.default_color);
                DominanctColor = currentColor.getDominantColor(defaultColor);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (bitmap != null && !bitmap.isRecycled()) {
                    currentColor = Palette.from(bitmap).generate();

                    int defaultColor = context.getColor(R.color.default_color);
                    DominanctColor = currentColor.getMutedColor(defaultColor);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return DominanctColor;
    }

    public Bitmap brightenBitmap(Bitmap bitmap) {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Color.RED);
        ColorFilter filter = new LightingColorFilter(0xFFFFFFFF, 0x00222222); // lighten
        //ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
        paint.setColorFilter(filter);
        canvas.drawBitmap(bitmap, new Matrix(), paint);
        return bitmap;
    }

    public Bitmap darkenBitmap(Bitmap bitmap) {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Color.RED);
        //ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
        ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
        paint.setColorFilter(filter);
        canvas.drawBitmap(bitmap, new Matrix(), paint);
        return bitmap;
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

    public int mixColors(int color1, int color2, float ratio /*0 -- 1*/) {
        /*ratio = 1 >> color1*/
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    public int setColorAlpha(int color, float alphaValue /*1 -- 255*/) {
        int alpha = Math.round(Color.alpha(color) * alphaValue);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public boolean colorLightDarkWallpaper() {
        boolean LightDark = false;
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(".themeColor", Context.MODE_PRIVATE);
            int vibrantColor = sharedPreferences.getInt("themeColor", context.getColor(R.color.default_color));
            int darkMutedColor = sharedPreferences.getInt("themeTextColor", context.getColor(R.color.default_color));
            int dominantColor = sharedPreferences.getInt("dominantColor", context.getColor(R.color.default_color));

            int initMix = mixColors(vibrantColor, darkMutedColor, 0.50f);
            int finalMix = mixColors(dominantColor, initMix, 0.50f);

            FunctionsClassDebug.Companion.PrintDebug("*** Vibrant ::: " + vibrantColor + " >>> " + ColorUtils.calculateLuminance(vibrantColor));
            FunctionsClassDebug.Companion.PrintDebug("*** Dark ::: " + darkMutedColor + " >>> " + ColorUtils.calculateLuminance(darkMutedColor));
            FunctionsClassDebug.Companion.PrintDebug("*** Dominant ::: " + dominantColor + " >>> " + ColorUtils.calculateLuminance(dominantColor));

            FunctionsClassDebug.Companion.PrintDebug("*** initMix ::: " + initMix + " >>> " + ColorUtils.calculateLuminance(initMix));
            FunctionsClassDebug.Companion.PrintDebug("*** finalMix ::: " + finalMix + " >>> " + ColorUtils.calculateLuminance(finalMix));

            double calculateLuminance = ColorUtils.calculateLuminance(dominantColor);
            if (calculateLuminance > 0.50) {//light
                LightDark = true;
            } else if (calculateLuminance <= 0.50) {//dark
                LightDark = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LightDark;
    }

    public boolean isDrawableLightDark(Drawable drawable) {
        boolean isLightDark = false;

        double calculateLuminance = ColorUtils.calculateLuminance(extractDominantColor(drawable));
        if (calculateLuminance > 0.66) {//light
            isLightDark = true;
        } else if (calculateLuminance <= 0.50) {//dark
            isLightDark = false;
        }

        return isLightDark;
    }

    public void doVibrate(long millisecondVibrate) {
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(millisecondVibrate);
    }

    /*Custom Icons*/
    public String customIconPackageName() {
        //com.Fraom.Smugy
        return readDefaultPreference("customIcon", context.getPackageName());
    }

    public boolean loadCustomIcons() {
        boolean doLoadCustomIcon = false;
        if (customIconPackageName().equals(context.getPackageName())) {
            doLoadCustomIcon = false;
        } else if (!customIconPackageName().equals(context.getPackageName())) {
            doLoadCustomIcon = true;
        }
        return doLoadCustomIcon;
    }

    public Bitmap getAppIconBitmapCustomIcon(String packageName) {
        LoadCustomIcons loadCustomIcons = null;
        if (loadCustomIcons()) {
            loadCustomIcons = new LoadCustomIcons(context, customIconPackageName());
            loadCustomIcons.load();
        }
        return loadCustomIcons() ? loadCustomIcons.getIconForPackage(packageName, (appIconBitmap(packageName))) : (appIconBitmap(packageName));
    }

    public Drawable getAppIconDrawableCustomIcon(String packageName) {
        LoadCustomIcons loadCustomIcons = null;
        if (loadCustomIcons()) {
            loadCustomIcons = new LoadCustomIcons(context, customIconPackageName());
            loadCustomIcons.load();
        }
        return loadCustomIcons() ? loadCustomIcons.getDrawableIconForPackage(packageName, (appIcon(packageName))) : (appIcon(packageName));
    }

    /*Action Center*/
    public void openActionMenuOption(final View fullActionElements, View actionButton, boolean startAnimation) {
        if (startAnimation == false) {
            int xPosition = (int) (actionButton.getX() + (actionButton.getWidth() / 2));
            int yPosition = (int) (actionButton.getY() + (actionButton.getHeight() / 2));

            int startRadius = 0;
            int endRadius = (int) Math.hypot(displayX(), displayY());

            Animator animator = ViewAnimationUtils.createCircularReveal(fullActionElements, xPosition, yPosition, startRadius, endRadius);
            animator.setDuration(555);
            animator.start();
        }

        fullActionElements.setVisibility(View.VISIBLE);
        fullActionElements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.findViewById(R.id.recoveryAction).setVisibility(View.VISIBLE);

                int finalRadius = (int) Math.hypot(displayX(), displayY());
                Animator circularReveal = ViewAnimationUtils.createCircularReveal(activity.findViewById(R.id.recoveryAction), (int) actionButton.getX(), (int) actionButton.getY(), DpToInteger(13), finalRadius);
                circularReveal.setDuration(1300);
                circularReveal.setInterpolator(new AccelerateInterpolator());
                circularReveal.start();
                circularReveal.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        activity.findViewById(R.id.recoveryAction).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                closeActionMenuOption(fullActionElements, actionButton);
            }
        });

        LayerDrawable drawFloating = (LayerDrawable) context.getDrawable(R.drawable.draw_floating);
        Drawable backFloating = drawFloating.findDrawableByLayerId(R.id.backtemp);
        backFloating.setTint(PublicVariable.primaryColor);

        CharSequence[] charSequence = new CharSequence[]{
                activity.getClass().getSimpleName().equals(AppAutoFeatures.class.getSimpleName()) || activity.getClass().getSimpleName().equals(CategoryAutoFeatures.class.getSimpleName())
                        ? context.getString(R.string.floatingCategory) : context.getString(R.string.automation),
        };
        Drawable[] drawables = new Drawable[]{
                drawFloating,
        };

        navDrawerItems = new ArrayList<NavDrawerItem>();
        for (int navItem = 0; navItem < charSequence.length; navItem++) {
            CharSequence itemText = charSequence[navItem];
            Drawable itemIcon = drawables[navItem];

            navDrawerItems.add(new NavDrawerItem(itemText, itemIcon));
        }

        if (appThemeTransparent() == true) {
            if (PublicVariable.themeLightDark) {
                fullActionElements.setBackground(new ColorDrawable(context.getColor(R.color.transparent_light)));

                final Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (PublicVariable.themeLightDark) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    if (API > 25) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                    }
                }
                ValueAnimator colorAnimation = ValueAnimator
                        .ofArgb(activity.getWindow().getNavigationBarColor(), context.getColor(R.color.fifty_light_twice));
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        window.setStatusBarColor((Integer) animator.getAnimatedValue());
                        window.setNavigationBarColor((Integer) animator.getAnimatedValue());
                    }
                });
                colorAnimation.start();
            } else if (!PublicVariable.themeLightDark) {
                fullActionElements.setBackground(new ColorDrawable(context.getColor(R.color.dark_transparent)));

                final Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                ValueAnimator colorAnimation = ValueAnimator
                        .ofArgb(activity.getWindow().getNavigationBarColor(), context.getColor(R.color.transparent_dark_high_twice));
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        window.setStatusBarColor((Integer) animator.getAnimatedValue());
                        window.setNavigationBarColor((Integer) animator.getAnimatedValue());
                    }
                });
                colorAnimation.start();
            }
        } else {
            if (PublicVariable.themeLightDark) {
                fullActionElements.setBackground(new ColorDrawable(context.getColor(R.color.transparent_light)));
                if (PublicVariable.themeLightDark) {
                    if (API > 25) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                    }
                }

                ValueAnimator colorAnimation = ValueAnimator
                        .ofArgb(activity.getWindow().getNavigationBarColor(), mixColors(context.getColor(R.color.light), activity.getWindow().getNavigationBarColor(), 0.70f));
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        activity.getWindow().setNavigationBarColor((Integer) animator.getAnimatedValue());
                        activity.getWindow().setStatusBarColor((Integer) animator.getAnimatedValue());
                    }
                });
                colorAnimation.start();
            } else if (!PublicVariable.themeLightDark) {
                fullActionElements.setBackground(new ColorDrawable(context.getColor(R.color.dark_transparent)));

                ValueAnimator colorAnimation = ValueAnimator
                        .ofArgb(activity.getWindow().getNavigationBarColor(), mixColors(context.getColor(R.color.dark), activity.getWindow().getNavigationBarColor(), 0.70f));
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        activity.getWindow().setNavigationBarColor((Integer) animator.getAnimatedValue());
                        activity.getWindow().setStatusBarColor((Integer) animator.getAnimatedValue());
                    }
                });
                colorAnimation.start();
            }
        }

        Animation elementsAnim = AnimationUtils.loadAnimation(context, R.anim.up_down);
        LayoutAnimationController itemController = new LayoutAnimationController(elementsAnim, 0.777f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Preferences(true);
            }
        }, 222);
        PublicVariable.actionCenter = true;
    }

    public void closeActionMenuOption(final View fullActionElements, View actionButton) {
        if (appThemeTransparent() == true) {
            final Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (PublicVariable.themeLightDark) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                if (API > 25) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                }
            }

            ValueAnimator valueAnimator = ValueAnimator
                    .ofArgb(activity.getWindow().getNavigationBarColor(), setColorAlpha(mixColors(PublicVariable.primaryColor, PublicVariable.colorLightDark, 0.03f), 180));
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    window.setStatusBarColor((Integer) animator.getAnimatedValue());
                    window.setNavigationBarColor((Integer) animator.getAnimatedValue());
                }
            });
            valueAnimator.start();
        } else {
            if (PublicVariable.themeLightDark) {
                final Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (PublicVariable.themeLightDark) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    if (API > 25) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                    }
                }
            }

            ValueAnimator colorAnimation = ValueAnimator
                    .ofArgb(activity.getWindow().getNavigationBarColor(), PublicVariable.colorLightDark);
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    activity.getWindow().setNavigationBarColor((Integer) animator.getAnimatedValue());
                    activity.getWindow().setStatusBarColor((Integer) animator.getAnimatedValue());
                }
            });
            colorAnimation.start();
        }

        int xPosition = (int) (actionButton.getX() + (actionButton.getWidth() / 2));
        int yPosition = (int) (actionButton.getY() + (actionButton.getHeight() / 2));

        int startRadius = (int) Math.hypot(displayX(), displayY());
        int endRadius = 0;

        Animator animator = ViewAnimationUtils.createCircularReveal(fullActionElements, xPosition, yPosition, startRadius, endRadius);
        animator.setDuration(555);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fullActionElements.setVisibility(View.INVISIBLE);
                Preferences(false);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        PublicVariable.actionCenter = false;
    }

    public void notificationCreator(String titleText, String contentText, int notificationId) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder notificationBuilder = new Notification.Builder(context);
            notificationBuilder.setContentTitle(Html.fromHtml("<b><font color='" + PublicVariable.primaryColorOpposite + "'>" + titleText + "</font></b>"));
            notificationBuilder.setContentText(Html.fromHtml("<font color='" + PublicVariable.primaryColor + "'>" + contentText + "</font>"));
            notificationBuilder.setTicker(context.getResources().getString(R.string.app_name));
            notificationBuilder.setSmallIcon(R.drawable.ic_notification);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setColor(context.getColor(R.color.default_color));
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);

            Intent newUpdate = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.play_store_link) + context.getPackageName()));
            PendingIntent newUpdatePendingIntent = PendingIntent.getActivity(context, 5, newUpdate, PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(context.getPackageName(), context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(notificationChannel);
                notificationBuilder.setChannelId(context.getPackageName());
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Notification.Action.Builder builderActionNotification = new Notification.Action.Builder(
                        Icon.createWithResource(context, R.drawable.draw_share_menu),
                        context.getString(R.string.rate),
                        newUpdatePendingIntent
                );
                notificationBuilder.addAction(builderActionNotification.build());
            }
            notificationBuilder.setContentIntent(newUpdatePendingIntent);
            notificationManager.notify(notificationId, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Notification bindServiceNotification() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notificationBuilder = new Notification.Builder(context);
        int notification_controller;
        if (returnAPI() < 24) {
            notification_controller = R.layout.notification_controller_low;
        } else {
            notification_controller = R.layout.notification_controller;
        }
        RemoteViews remoteNotification = new RemoteViews(context.getPackageName(), notification_controller);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (PublicVariable.themeLightDark) {
            remoteNotification.setInt(R.id.bindShortcutView, "setBackgroundColor", PublicVariable.primaryColor);
        } else if (!PublicVariable.themeLightDark) {
            remoteNotification.setInt(R.id.bindShortcutView, "setBackgroundColor", PublicVariable.primaryColor);
        }
        String sticky = sharedPreferences.getString("stick", "1");
        if (sticky.equals("1")) {
            remoteNotification.setTextViewText(R.id.moveEdge, context.getString(R.string.moveEdgeLeft));
            remoteNotification.setTextViewText(R.id.backEdge, context.getString(R.string.backToScreenLeft));
        } else if (sticky.equals("2")) {
            remoteNotification.setTextViewText(R.id.moveEdge, context.getString(R.string.moveEdgeRight));
            remoteNotification.setTextViewText(R.id.backEdge, context.getString(R.string.backToScreenRight));
        }

        Intent CancelStable = new Intent(context, RemoteController.class);
        CancelStable.putExtra("RemoteController", "CancelRemote");
        PendingIntent cancelPending = PendingIntent.getService(context, 0, CancelStable, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent RecoverAll = new Intent(context, RemoteController.class);
        RecoverAll.putExtra("RemoteController", "RecoverAll");
        PendingIntent pendingRecoverAll = PendingIntent.getService(context, 1, RecoverAll, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent RemoveAll = new Intent(context, RemoteController.class);
        RemoveAll.putExtra("RemoteController", "RemoveAll");
        PendingIntent pendingRemoveAll = PendingIntent.getService(context, 2, RemoveAll, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent Sticky_Edge = new Intent(context, RemoteController.class);
        Sticky_Edge.putExtra("RemoteController", "Sticky_Edge");
        PendingIntent pendingSticky_Edge = PendingIntent.getService(context, 3, Sticky_Edge, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent Sticky_Edge_No = new Intent(context, RemoteController.class);
        Sticky_Edge_No.putExtra("RemoteController", "Sticky_Edge_No");
        PendingIntent pendingSticky_Edge_No = PendingIntent.getService(context, 4, Sticky_Edge_No, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteNotification.setOnClickPendingIntent(R.id.recoverAll, pendingRecoverAll);
        remoteNotification.setOnClickPendingIntent(R.id.removeAll, pendingRemoveAll);
        remoteNotification.setOnClickPendingIntent(R.id.moveEdge, pendingSticky_Edge);
        remoteNotification.setOnClickPendingIntent(R.id.backEdge, pendingSticky_Edge_No);

        notificationBuilder.setContentTitle(Html.fromHtml("<b><font color='" + PublicVariable.primaryColor + "'>" + context.getResources().getString(R.string.app_name) + "</font></b>"));
        notificationBuilder.setContentText(Html.fromHtml("<font color='" + PublicVariable.primaryColor + "'>" + context.getResources().getString(R.string.bindDesc) + "</font>"));
        notificationBuilder.setTicker(context.getResources().getString(R.string.app_name));
        notificationBuilder.setSmallIcon(R.drawable.ic_notification);
        notificationBuilder.setAutoCancel(false);
        notificationBuilder.setColor(PublicVariable.primaryColor);
        notificationBuilder.setPriority(bindServicePriority());

        Intent ListGrid = new Intent(context, Configurations.class);
        PendingIntent ListGridPendingIntent = PendingIntent.getActivity(context, 5, ListGrid, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(ListGridPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                NotificationChannel notificationChannel = new NotificationChannel(context.getPackageName(), context.getString(R.string.app_name), bindServicePriority());
                notificationManager.createNotificationChannel(notificationChannel);
                notificationBuilder.setChannelId(context.getPackageName());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();

                NotificationChannel notificationChannel = new NotificationChannel(context.getPackageName(), context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(notificationChannel);
                notificationBuilder.setChannelId(context.getPackageName());
            }
        }

        if (returnAPI() < 24) {
            notificationBuilder.setContent(remoteNotification);
        }
        Notification notification = notificationBuilder.build();
        if (returnAPI() >= 24) {
            notification.bigContentView = remoteNotification;
        }

        return notification;
    }

    /*Lite App Preferences*/
    public void liteAppPreferences() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor defaultSharedPreferencesEditor = defaultSharedPreferences.edit();

        /*OFF Always Ready*/
        defaultSharedPreferencesEditor.putBoolean("cache", false);

        /*OFF Control Panel*/
        defaultSharedPreferencesEditor.putBoolean("stable", false);
        context.stopService(new Intent(context, BindServices.class));

        /*Dark App Theme*/
        defaultSharedPreferencesEditor.putString("themeColor", "2");

        /*OFF Blurry Theme*/
        defaultSharedPreferencesEditor.putBoolean("blur", false);

        /*OFF Transparent Theme*/
        defaultSharedPreferencesEditor.putBoolean("transparent", false);

        /*OFF Floating Splash*/
        defaultSharedPreferencesEditor.putBoolean("floatingSplash", false);

        /*OFF Floating Transparency*/
        defaultSharedPreferencesEditor.putInt("autoTrans", 255);
        defaultSharedPreferencesEditor.putInt("autoTransProgress", 0);

        /*No Icon Theme*/
        saveDefaultPreference("customIcon", context.getPackageName());

        /*No Icon Shape*/
        defaultSharedPreferencesEditor.putInt("iconShape", 0);

        /*Apply All Lite Preferences*/
        defaultSharedPreferencesEditor.apply();

        saveDefaultPreference("LitePreferences", true);

        activity.finish();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.startActivity(new Intent(context, Configurations.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }, 333);
    }

    public boolean litePreferencesEnabled() {
        return readDefaultPreference("LitePreferences", false);
    }

    /*Time Functions*/
    public void initialAlarm(Calendar newAlarmTime, String setTime, int position) {
        Intent alarmIntent = new Intent(context, AlarmAlertBroadcastReceiver.class);
        alarmIntent.putExtra("time", setTime);
        alarmIntent.putExtra("position", position);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        newAlarmTime.add(Calendar.DAY_OF_MONTH, 1);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                newAlarmTime.getTimeInMillis(),
                /*AlarmManager.INTERVAL_DAY*/86400000,
                pendingIntent);

        FunctionsClassDebug.Companion.PrintDebug("*** " + newAlarmTime.getTime());

        context.stopService(new Intent(context, SetupAlarms.class));
    }

    /*Let Me Know*/
    public List<String> letMeKnow(Context context, int maxValue, long startTime /*‪86400000‬ = 1 days*/, long endTime  /*System.currentTimeMillis()*/) {
        /*‪86400000 = 24h --- 82800000 = 23h‬*/
        List<String> freqApps = new ArrayList<String>();
        try {
            if (UsageStatsEnabled()) {
                UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                List<UsageStats> queryUsageStats = mUsageStatsManager
                        .queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                                System.currentTimeMillis() - startTime,
                                endTime);
                Collections.sort(
                        queryUsageStats,
                        new Comparator<UsageStats>() {
                            @Override
                            public int compare(UsageStats left, UsageStats right) {
                                return Long.compare(
                                        right.getTotalTimeInForeground(), left.getTotalTimeInForeground()
                                );
                            }
                        }
                );
                for (int i = 0; i < maxValue; i++) {
                    String aPackageName = queryUsageStats.get(i).getPackageName();
                    try {
                        if (!aPackageName.equals(context.getPackageName())) {
                            if (appIsInstalled(aPackageName)) {
                                if (!ifSystem(aPackageName)) {
                                    if (!isDefaultLauncher(aPackageName)) {
                                        if (canLaunch(aPackageName)) {
                                            freqApps.add(aPackageName);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Set<String> stringHashSet = new LinkedHashSet<>(freqApps);
                freqApps.clear();
                freqApps.addAll(stringHashSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return freqApps;
    }

    /*Firebase Remote Config*/
    public boolean joinedBetaProgram() {
        return readDefaultPreference("JoinedBetaProgrammer", false);
    }

    public String versionCodeRemoteConfigKey() {
        String versionCodeKey = null;
        if (joinedBetaProgram()) {
            versionCodeKey = context.getString(R.string.BETAintegerVersionCodeNewUpdatePhone);
        } else {
            versionCodeKey = context.getString(R.string.integerVersionCodeNewUpdatePhone);
        }
        return versionCodeKey;
    }

    public String versionNameRemoteConfigKey() {
        String versionCodeKey = null;
        if (joinedBetaProgram()) {
            versionCodeKey = context.getString(R.string.BETAstringVersionNameNewUpdatePhone);
        } else {
            versionCodeKey = context.getString(R.string.stringVersionNameNewUpdatePhone);
        }
        return versionCodeKey;
    }

    public String upcomingChangeLogRemoteConfigKey() {
        String versionCodeKey = null;
        if (joinedBetaProgram()) {
            versionCodeKey = context.getString(R.string.BETAstringUpcomingChangeLogPhone);
        } else {
            versionCodeKey = context.getString(R.string.stringUpcomingChangeLogPhone);
        }
        return versionCodeKey;
    }

    public String upcomingChangeLogSummaryConfigKey() {
        String versionCodeKey = null;
        if (joinedBetaProgram()) {
            versionCodeKey = context.getString(R.string.BETAstringUpcomingChangeLogSummaryPhone);
        } else {
            versionCodeKey = context.getString(R.string.stringUpcomingChangeLogSummaryPhone);
        }
        return versionCodeKey;
    }

    /*In-App Purchase*/
    public boolean removeAdsPurchased() {

        return readPreference(".SubscribedItem", "remove.ads", false);
    }

    public boolean alreadyDonated() {

        return readPreference(".PurchasedItem", "donation", false);
    }
}