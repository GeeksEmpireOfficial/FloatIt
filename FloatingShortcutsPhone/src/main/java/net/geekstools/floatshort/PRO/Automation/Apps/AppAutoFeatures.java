package net.geekstools.floatshort.PRO.Automation.Apps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import net.geekstools.floatshort.PRO.Automation.Categories.CategoryAutoFeatures;
import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;
import net.geekstools.floatshort.PRO.Util.UI.SimpleGestureFilterFull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppAutoFeatures extends AppCompatActivity implements View.OnClickListener, SimpleGestureFilterFull.SimpleGestureListener {

    Activity activity;
    FunctionsClass functionsClass;
    ListView listView, acttionElementsList;
    RelativeLayout fullActionButton, MainView;
    LinearLayout indexView, autoIdentifier;
    RelativeLayout loadingSplash;
    ProgressBar loadingBarLTR;
    TextView desc, popupIndex;
    Button wifi, bluetooth, gps, nfc, time, autoApps, autoCategories;

    ScrollView nestedIndexScrollView;

    List<ApplicationInfo> applicationInfoList;
    Map<String, Integer> mapIndexFirstItem, mapIndexLastItem;
    Map<Integer, String> mapRangeIndex;

    ArrayList<NavDrawerItem> navDrawerItems;
    AppAutoListAdapter adapter;

    String PackageName;
    String AppName = "Application";
    String AppTime = "00:00";
    Drawable AppIcon;

    int color, pressColor;

    SimpleGestureFilterFull simpleGestureFilterFull;

    LoadCustomIcons loadCustomIcons;

    @Override
    protected void onCreate(Bundle Saved) {
        super.onCreate(Saved);
        setContentView(R.layout.auto_apps);

        listView = (ListView) findViewById(R.id.listFav);
        indexView = (LinearLayout) findViewById(R.id.side_index);
        autoIdentifier = (LinearLayout) findViewById(R.id.autoid);
        autoIdentifier.bringToFront();
        MainView = (RelativeLayout) findViewById(R.id.MainView);
        fullActionButton = (RelativeLayout) findViewById(R.id.fullActionViews);
        acttionElementsList = (ListView) findViewById(R.id.acttionElementsList);
        autoApps = (Button) findViewById(R.id.autoApps);
        autoApps.bringToFront();
        autoCategories = (Button) findViewById(R.id.autoCategories);
        autoCategories.bringToFront();
        wifi = (Button) findViewById(R.id.wifi);
        bluetooth = (Button) findViewById(R.id.bluetooth);
        gps = (Button) findViewById(R.id.gps);
        nfc = (Button) findViewById(R.id.nfc);
        time = (Button) findViewById(R.id.time);

        popupIndex = (TextView) findViewById(R.id.popupIndex);
        nestedIndexScrollView = (ScrollView) findViewById(R.id.nestedIndexScrollView);

        simpleGestureFilterFull = new SimpleGestureFilterFull(getApplicationContext(), this);
        functionsClass = new FunctionsClass(getApplicationContext(), this);
        activity = this;

        if (functionsClass.returnAPI() >= 26) {
            if (!functionsClass.ControlPanel()) {
                Snackbar snackbar = Snackbar.make(MainView, Html.fromHtml("<big>" + getString(R.string.enableControlPanel) + "</big>"), Snackbar.LENGTH_INDEFINITE)
                        .setAction(Html.fromHtml("<b>" + getString(R.string.enable).toUpperCase() + "</b>"), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("stable", true);
                                editor.apply();
                                startService(new Intent(getApplicationContext(), BindServices.class));
                            }
                        });
                snackbar.setActionTextColor(PublicVariable.colorLightDarkOpposite);
                snackbar.setActionTextColor(getColor(R.color.red));

                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.BOTTOM_TOP,
                        new int[]{
                                Color.TRANSPARENT,
                                functionsClass.setColorAlpha(PublicVariable.primaryColor, 207),
                                Color.TRANSPARENT
                        });

                View view = snackbar.getView();
                view.setBackground(gradientDrawable/*functionsClass.setColorAlpha(functionsClass.mixColors(PublicVariable.primaryColor, PublicVariable.colorLightDark, 0.03f), 180)*/);

                snackbar.show();
            }
        }

        if (functionsClass.appThemeTransparent() == true) {
            functionsClass.setThemeColorAutomationFeature(MainView, true);
        } else {
            functionsClass.setThemeColorAutomationFeature(MainView, false);
        }

        if (functionsClass.loadCustomIcons()) {
            loadCustomIcons = new LoadCustomIcons(getApplicationContext(), functionsClass.customIconPackageName());
        }

        navDrawerItems = new ArrayList<NavDrawerItem>();
        mapIndexFirstItem = new LinkedHashMap<String, Integer>();
        mapIndexLastItem = new LinkedHashMap<String, Integer>();
        mapRangeIndex = new LinkedHashMap<Integer, String>();

        autoApps.setTextColor(getColor(R.color.light));
        autoCategories.setTextColor(getColor(R.color.light));
        if (PublicVariable.themeLightDark /*light*/ && functionsClass.appThemeTransparent() /*transparent*/) {
            autoApps.setTextColor(getColor(R.color.dark));
            autoCategories.setTextColor(getColor(R.color.dark));
        }

        RippleDrawable rippleDrawableShortcuts = (RippleDrawable) getDrawable(R.drawable.draw_shortcuts);
        GradientDrawable gradientDrawableShortcutsForeground = (GradientDrawable) rippleDrawableShortcuts.findDrawableByLayerId(R.id.foregroundItem);
        GradientDrawable gradientDrawableShortcutsBackground = (GradientDrawable) rippleDrawableShortcuts.findDrawableByLayerId(R.id.backgroundItem);
        GradientDrawable gradientDrawableMaskShortcuts = (GradientDrawable) rippleDrawableShortcuts.findDrawableByLayerId(android.R.id.mask);

        if (functionsClass.appThemeTransparent()) {
            rippleDrawableShortcuts.setColor(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));
            gradientDrawableShortcutsForeground.setColor(
                    functionsClass.setColorAlpha(
                            functionsClass.mixColors(
                                    PublicVariable.primaryColor, PublicVariable.colorLightDark,
                                    0.75f), functionsClass.wallpaperStaticLive() ? 245 : 113)
            );
            gradientDrawableShortcutsBackground.setTint(functionsClass.setColorAlpha(PublicVariable.primaryColor, functionsClass.wallpaperStaticLive() ? 150 : 155));
            gradientDrawableMaskShortcuts.setColor(PublicVariable.primaryColorOpposite);
        } else {
            rippleDrawableShortcuts.setColor(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));
            gradientDrawableShortcutsForeground.setColor(PublicVariable.primaryColor);
            gradientDrawableShortcutsBackground.setTint(PublicVariable.primaryColor);
            gradientDrawableMaskShortcuts.setColor(PublicVariable.primaryColorOpposite);
        }
        autoApps.setBackground(rippleDrawableShortcuts);

        RippleDrawable rippleDrawableCategories = (RippleDrawable) getDrawable(R.drawable.draw_categories);
        GradientDrawable gradientDrawableCategoriesForeground = (GradientDrawable) rippleDrawableCategories.findDrawableByLayerId(R.id.foregroundItem);
        GradientDrawable gradientDrawableCategoriesBackground = (GradientDrawable) rippleDrawableCategories.findDrawableByLayerId(R.id.backgroundItem);
        GradientDrawable gradientDrawableMaskCategories = (GradientDrawable) rippleDrawableCategories.findDrawableByLayerId(android.R.id.mask);

        if (functionsClass.appThemeTransparent()) {
            rippleDrawableCategories.setColor(ColorStateList.valueOf(PublicVariable.primaryColor));
            gradientDrawableCategoriesForeground.setColor(functionsClass.setColorAlpha(PublicVariable.primaryColorOpposite, 255));
            if (functionsClass.returnAPI() > 21) {
                gradientDrawableCategoriesBackground.setTint(functionsClass.setColorAlpha(PublicVariable.primaryColorOpposite, 175));
            } else {
                gradientDrawableShortcutsBackground.setColor(functionsClass.setColorAlpha(PublicVariable.primaryColorOpposite, 175));
            }
            gradientDrawableMaskCategories.setColor(PublicVariable.primaryColor);
        } else {
            rippleDrawableCategories.setColor(ColorStateList.valueOf(PublicVariable.primaryColor));
            gradientDrawableCategoriesForeground.setColor(PublicVariable.primaryColorOpposite);
            gradientDrawableCategoriesBackground.setTint(PublicVariable.primaryColorOpposite);
            gradientDrawableMaskCategories.setColor(PublicVariable.primaryColor);
        }
        autoCategories.setBackground(rippleDrawableCategories);

        ImageView floatingLogo = (ImageView) findViewById(R.id.loadLogo);
        LayerDrawable drawFloatingLogo = (LayerDrawable) getDrawable(R.drawable.draw_floating_logo);
        GradientDrawable backFloatingLogo = (GradientDrawable) drawFloatingLogo.findDrawableByLayerId(R.id.backtemp);
        backFloatingLogo.setColor(PublicVariable.primaryColorOpposite);
        floatingLogo.setImageDrawable(drawFloatingLogo);

        loadingSplash = (RelativeLayout) findViewById(R.id.loadingSplash);
        if (functionsClass.appThemeTransparent() == true) {
            loadingSplash.setBackgroundColor(Color.TRANSPARENT);
        } else {
            loadingSplash.setBackgroundColor(getWindow().getNavigationBarColor());
        }

        loadingBarLTR = (ProgressBar) findViewById(R.id.loadingProgressltr);
        desc = (TextView) findViewById(R.id.desc);
        Typeface face = Typeface.createFromAsset(getAssets(), "upcil.ttf");
        desc.setTypeface(face);

        if (PublicVariable.themeLightDark) {
            loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.themeTextColor, android.graphics.PorterDuff.Mode.MULTIPLY);
            desc.setTextColor(getColor(R.color.dark));
        } else if (!PublicVariable.themeLightDark) {
            loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
            desc.setTextColor(getColor(R.color.light));
        }

        autoCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublicVariable.autoID = null;
                try {
                    functionsClass.navigateToClass(CategoryAutoFeatures.class,
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_right, R.anim.slide_to_left));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (PublicVariable.themeLightDark) {
            color = PublicVariable.themeColor;
            pressColor = PublicVariable.themeTextColor;
        } else if (!PublicVariable.themeLightDark) {
            color = PublicVariable.themeTextColor;
            pressColor = PublicVariable.themeColor;
        }

        if (PublicVariable.autoID != null) {
            final LayerDrawable drawWifi = (LayerDrawable) getDrawable(R.drawable.draw_wifi);
            final GradientDrawable backWifi = (GradientDrawable) drawWifi.findDrawableByLayerId(R.id.backtemp);

            final LayerDrawable drawBluetooth = (LayerDrawable) getDrawable(R.drawable.draw_bluetooth);
            final GradientDrawable backBluetooth = (GradientDrawable) drawBluetooth.findDrawableByLayerId(R.id.backtemp);

            final LayerDrawable drawGPS = (LayerDrawable) getDrawable(R.drawable.draw_gps);
            final GradientDrawable backGPS = (GradientDrawable) drawGPS.findDrawableByLayerId(R.id.backtemp);

            final LayerDrawable drawNfc = (LayerDrawable) getDrawable(R.drawable.draw_nfc);
            final GradientDrawable backNfc = (GradientDrawable) drawNfc.findDrawableByLayerId(R.id.backtemp);

            final LayerDrawable drawTime = (LayerDrawable) getDrawable(R.drawable.draw_time);
            final GradientDrawable backTime = (GradientDrawable) drawTime.findDrawableByLayerId(R.id.backtemp);

            wifi.setBackground(drawWifi);
            bluetooth.setBackground(drawBluetooth);
            gps.setBackground(drawGPS);
            nfc.setBackground(drawNfc);
            time.setBackground(drawTime);

            if (PublicVariable.autoID.equals(getString(R.string.wifi))) {
                backWifi.setColor(pressColor);
                wifi.setBackground(drawWifi);

                autoWiFi();

                backBluetooth.setColor(color);
                backGPS.setColor(color);
                backNfc.setColor(color);
                backTime.setColor(color);

                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            } else if (PublicVariable.autoID.equals(getString(R.string.bluetooth))) {
                backBluetooth.setColor(pressColor);
                bluetooth.setBackground(drawBluetooth);

                autoBluetooth();

                backWifi.setColor(color);
                backGPS.setColor(color);
                backNfc.setColor(color);
                backTime.setColor(color);

                wifi.setBackground(drawWifi);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            } else if (PublicVariable.autoID.equals(getString(R.string.gps))) {
                backGPS.setColor(pressColor);
                gps.setBackground(drawGPS);

                autoGPS();

                backWifi.setColor(color);
                backBluetooth.setColor(color);
                backNfc.setColor(color);
                backTime.setColor(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            } else if (PublicVariable.autoID.equals(getString(R.string.nfc))) {
                backNfc.setColor(pressColor);
                nfc.setBackground(drawNfc);

                autoNfc();

                backWifi.setColor(color);
                backBluetooth.setColor(color);
                backGPS.setColor(color);
                backTime.setColor(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                time.setBackground(drawTime);
            } else if (PublicVariable.autoID.equals(getString(R.string.time))) {
                backTime.setColor(pressColor);
                time.setBackground(drawTime);

                autoTime();

                backWifi.setColor(color);
                backBluetooth.setColor(color);
                backGPS.setColor(color);
                backNfc.setColor(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        final LayerDrawable drawWifi = (LayerDrawable) getDrawable(R.drawable.draw_wifi);
        final GradientDrawable backWifi = (GradientDrawable) drawWifi.findDrawableByLayerId(R.id.backtemp);

        final LayerDrawable drawBluetooth = (LayerDrawable) getDrawable(R.drawable.draw_bluetooth);
        final GradientDrawable backBluetooth = (GradientDrawable) drawBluetooth.findDrawableByLayerId(R.id.backtemp);

        final LayerDrawable drawGPS = (LayerDrawable) getDrawable(R.drawable.draw_gps);
        final GradientDrawable backGPS = (GradientDrawable) drawGPS.findDrawableByLayerId(R.id.backtemp);

        final LayerDrawable drawNfc = (LayerDrawable) getDrawable(R.drawable.draw_nfc);
        final GradientDrawable backNfc = (GradientDrawable) drawNfc.findDrawableByLayerId(R.id.backtemp);

        final LayerDrawable drawTime = (LayerDrawable) getDrawable(R.drawable.draw_time);
        final GradientDrawable backTime = (GradientDrawable) drawTime.findDrawableByLayerId(R.id.backtemp);

        if (PublicVariable.themeLightDark) {
            color = PublicVariable.themeColor;
            pressColor = PublicVariable.themeTextColor;
        } else if (!PublicVariable.themeLightDark) {
            color = PublicVariable.themeTextColor;
            pressColor = PublicVariable.themeColor;
        }

        backWifi.setColor(color);
        backBluetooth.setColor(color);
        backGPS.setColor(color);
        backNfc.setColor(color);
        backTime.setColor(color);

        wifi.setBackground(drawWifi);
        bluetooth.setBackground(drawBluetooth);
        gps.setBackground(drawGPS);
        nfc.setBackground(drawNfc);
        time.setBackground(drawTime);

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backWifi.setColor(pressColor);
                wifi.setBackground(drawWifi);

                autoWiFi();

                backBluetooth.setColor(color);
                backGPS.setColor(color);
                backNfc.setColor(color);
                backTime.setColor(color);

                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            }
        });
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backBluetooth.setColor(pressColor);
                bluetooth.setBackground(drawBluetooth);

                autoBluetooth();

                backWifi.setColor(color);
                backGPS.setColor(color);
                backNfc.setColor(color);
                backTime.setColor(color);

                wifi.setBackground(drawWifi);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            }
        });
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backGPS.setColor(pressColor);
                gps.setBackground(drawGPS);

                autoGPS();

                backWifi.setColor(color);
                backBluetooth.setColor(color);
                backNfc.setColor(color);
                backTime.setColor(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            }
        });
        nfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backNfc.setColor(pressColor);
                nfc.setBackground(drawNfc);

                autoNfc();

                backWifi.setColor(color);
                backBluetooth.setColor(color);
                backGPS.setColor(color);
                backTime.setColor(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                time.setBackground(drawTime);
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backTime.setColor(pressColor);
                time.setBackground(drawTime);

                autoTime();

                backWifi.setColor(color);
                backBluetooth.setColor(color);
                backGPS.setColor(color);
                backNfc.setColor(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        functionsClass.CheckSystemRAM(AppAutoFeatures.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PublicVariable.autoID = null;
    }

    @Override
    public void onBackPressed() {
        try {
            functionsClass.overrideBackPressToMain(AppAutoFeatures.this);
            overridePendingTransition(android.R.anim.fade_in, R.anim.go_up);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSwipe(int direction) {
        switch (direction) {
            case SimpleGestureFilterFull.SWIPE_RIGHT: {
                FunctionsClass.println("Swipe Right");

                break;
            }
            case SimpleGestureFilterFull.SWIPE_LEFT: {
                FunctionsClass.println("Swipe Left");
                try {
                    functionsClass.navigateToClass(CategoryAutoFeatures.class,
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_right, R.anim.slide_to_left));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }
            case SimpleGestureFilterFull.SWIPE_UP: {
                FunctionsClass.println("SWIPE UP");
                try {
                    if (listView.getLastVisiblePosition() == (listView.getAdapter().getCount() - 1)) {
                        try {
                            functionsClass.overrideBackPressToMain(AppAutoFeatures.this);
                            overridePendingTransition(android.R.anim.fade_in, R.anim.go_up);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    try {
                        functionsClass.overrideBackPressToMain(AppAutoFeatures.this);
                        overridePendingTransition(android.R.anim.fade_in, R.anim.go_up);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        this.simpleGestureFilterFull.onTouchEvent(me);

        return super.dispatchTouchEvent(me);
    }

    public void autoWiFi() {
        PublicVariable.autoID = getString(R.string.wifi);
        int API = android.os.Build.VERSION.SDK_INT;
        if (API > 22) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_DENIED) {
                String[] gpsPermissions = {android.Manifest.permission.ACCESS_WIFI_STATE};
                requestPermissions(gpsPermissions, 888);
                return;
            }
        }

        LoadAutoApplications loadAutoApplications = new LoadAutoApplications();
        loadAutoApplications.execute();
    }

    public void autoBluetooth() {
        PublicVariable.autoID = getString(R.string.bluetooth);
        int API = android.os.Build.VERSION.SDK_INT;
        if (API > 22) {
            if (checkSelfPermission(android.Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_DENIED) {
                String[] gpsPermissions = {android.Manifest.permission.BLUETOOTH};
                requestPermissions(gpsPermissions, 999);
                return;
            }
        }

        LoadAutoApplications loadAutoApplications = new LoadAutoApplications();
        loadAutoApplications.execute();
    }

    public void autoGPS() {
        PublicVariable.autoID = getString(R.string.gps);
        int API = android.os.Build.VERSION.SDK_INT;
        if (API > 22) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                String[] gpsPermissions = {android.Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(gpsPermissions, 777);
                return;
            }
        }

        LoadAutoApplications loadAutoApplications = new LoadAutoApplications();
        loadAutoApplications.execute();
    }

    public void autoNfc() {
        PublicVariable.autoID = getString(R.string.nfc);
        int API = android.os.Build.VERSION.SDK_INT;
        if (API > 22) {
            if (checkSelfPermission(Manifest.permission.NFC) == PackageManager.PERMISSION_DENIED) {
                String[] gpsPermissions = {Manifest.permission.NFC};
                requestPermissions(gpsPermissions, 888);
                return;
            }
        }

        LoadAutoApplications loadAutoApplications = new LoadAutoApplications();
        loadAutoApplications.execute();
    }

    public void autoTime() {
        PublicVariable.autoID = getString(R.string.time);

        LoadAutoApplications loadAutoApplications = new LoadAutoApplications();
        loadAutoApplications.execute();
    }

    /******************************************/
    private class LoadAutoApplications extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingSplash = (RelativeLayout) findViewById(R.id.loadingSplash);
            loadingSplash.setVisibility(View.VISIBLE);
            if (functionsClass.appThemeTransparent() == true) {
                loadingSplash.setBackgroundColor(Color.TRANSPARENT);
            } else {
                loadingSplash.setBackgroundColor(getWindow().getNavigationBarColor());
            }

            loadingBarLTR = (ProgressBar) findViewById(R.id.loadingProgressltr);
            desc = (TextView) findViewById(R.id.desc);
            Typeface face = Typeface.createFromAsset(getAssets(), "upcil.ttf");
            desc.setTypeface(face);

            if (PublicVariable.themeLightDark) {
                loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.themeTextColor, android.graphics.PorterDuff.Mode.MULTIPLY);
            } else if (!PublicVariable.themeLightDark) {
                loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
            }


            listView.clearChoices();
            indexView.removeAllViews();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                applicationInfoList = getApplicationContext().getPackageManager().getInstalledApplications(0);
                Collections.sort(applicationInfoList, new ApplicationInfo.DisplayNameComparator(getPackageManager()));

                navDrawerItems = new ArrayList<NavDrawerItem>();
                mapIndexFirstItem = new LinkedHashMap<String, Integer>();

                if (functionsClass.loadCustomIcons()) {
                    loadCustomIcons.load();
                    FunctionsClass.println("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIcons());
                }

                for (int appInfo = 0; appInfo < applicationInfoList.size(); appInfo++) {
                    if (getApplicationContext().getPackageManager().getLaunchIntentForPackage(applicationInfoList.get(appInfo).packageName) != null) {
                        try {
                            PackageName = applicationInfoList.get(appInfo).packageName;
                            AppName = functionsClass.appName(PackageName);
                            AppIcon = functionsClass.loadCustomIcons() ? loadCustomIcons.getDrawableIconForPackage(PackageName, functionsClass.shapedAppIcon(PackageName)) : functionsClass.shapedAppIcon(PackageName);

                            if (getFileStreamPath(PackageName + ".Time").exists()) {
                                AppTime = functionsClass.readFile(PackageName + ".Time");
                                String tempTimeHour = AppTime.split(":")[0];
                                String tempTimeMinute = AppTime.split(":")[1];
                                if (Integer.parseInt(tempTimeMinute) < 10) {
                                    AppTime = tempTimeHour + ":" + "0" + tempTimeMinute;
                                }
                            }

                            navDrawerItems.add(new NavDrawerItem(AppName, PackageName, AppIcon, AppTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                adapter = new AppAutoListAdapter(activity, getApplicationContext(), navDrawerItems);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            listView.setAdapter(adapter);
            registerForContextMenu(listView);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                    loadingSplash.setVisibility(View.INVISIBLE);
                    loadingSplash.startAnimation(anim);
                    autoIdentifier.setVisibility(View.VISIBLE);
                }
            }, 100);

            LoadApplicationsIndex loadApplicationsIndex = new LoadApplicationsIndex();
            loadApplicationsIndex.execute();
        }
    }

    private class LoadApplicationsIndex extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            indexView.removeAllViews();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int itemCount = 0; itemCount < navDrawerItems.size(); itemCount++) {
                try {
                    String index = (navDrawerItems.get(itemCount).getAppName()).substring(0, 1).toUpperCase();
                    if (mapIndexFirstItem.get(index) == null) {
                        mapIndexFirstItem.put(index, itemCount);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            TextView textView = (TextView) getLayoutInflater()
                    .inflate(R.layout.side_index_item, null);
            List<String> indexList = new ArrayList<String>(mapIndexFirstItem.keySet());
            for (String index : indexList) {
                textView = (TextView) getLayoutInflater()
                        .inflate(R.layout.side_index_item, null);
                textView.setText(index.toUpperCase());
                textView.setTextColor(PublicVariable.colorLightDarkOpposite);
                indexView.addView(textView);
            }

            TextView finalTextView = textView;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int upperRange = (int) (indexView.getY() - finalTextView.getHeight());
                    for (int i = 0; i < indexView.getChildCount(); i++) {
                        String indexText = ((TextView) indexView.getChildAt(i)).getText().toString();
                        int indexRange = (int) (indexView.getChildAt(i).getY() + indexView.getY() + finalTextView.getHeight());
                        for (int jRange = upperRange; jRange <= (indexRange); jRange++) {
                            mapRangeIndex.put(jRange, indexText);
                        }

                        upperRange = indexRange;
                    }

                    setupFastScrollingIndexing();
                }
            }, 700);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setupFastScrollingIndexing() {
        Drawable popupIndexBackground = getDrawable(R.drawable.ic_launcher_balloon).mutate();
        popupIndexBackground.setTint(PublicVariable.primaryColorOpposite);
        popupIndex.setBackground(popupIndexBackground);

        nestedIndexScrollView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
        nestedIndexScrollView.setVisibility(View.VISIBLE);

        float popupIndexOffsetY = PublicVariable.statusBarHeight + PublicVariable.actionBarHeight + (functionsClass.UsageStatsEnabled() ? functionsClass.DpToInteger(7) : functionsClass.DpToInteger(7));
        nestedIndexScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if (functionsClass.litePreferencesEnabled()) {

                        } else {
                            String indexText = mapRangeIndex.get((((int) motionEvent.getY())));

                            if (indexText != null) {
                                popupIndex.setY(motionEvent.getRawY() - popupIndexOffsetY);
                                popupIndex.setText(indexText);
                                popupIndex.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                                popupIndex.setVisibility(View.VISIBLE);
                            }
                        }

                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        if (functionsClass.litePreferencesEnabled()) {

                        } else {
                            String indexText = mapRangeIndex.get(((int) motionEvent.getY()));

                            if (indexText != null) {
                                if (!popupIndex.isShown()) {
                                    popupIndex.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                                    popupIndex.setVisibility(View.VISIBLE);
                                }
                                popupIndex.setY(motionEvent.getRawY() - popupIndexOffsetY);
                                popupIndex.setText(indexText);

                                try {
                                    listView.smoothScrollToPositionFromTop(mapIndexFirstItem.get(mapRangeIndex.get(((int) motionEvent.getY()))), 0, 200);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (popupIndex.isShown()) {
                                    popupIndex.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                                    popupIndex.setVisibility(View.INVISIBLE);
                                }
                            }
                        }

                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        if (functionsClass.litePreferencesEnabled()) {
                            try {
                                listView.smoothScrollToPositionFromTop(mapIndexFirstItem.get(mapRangeIndex.get(((int) motionEvent.getY()))), 0, 200);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (popupIndex.isShown()) {
                                try {
                                    listView.smoothScrollToPositionFromTop(mapIndexFirstItem.get(mapRangeIndex.get(((int) motionEvent.getY()))), 0, 200);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                popupIndex.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out));
                                popupIndex.setVisibility(View.INVISIBLE);
                            }
                        }

                        break;
                    }
                }
                return true;
            }
        });
    }
}
