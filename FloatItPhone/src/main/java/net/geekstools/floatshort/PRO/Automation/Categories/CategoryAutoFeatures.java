package net.geekstools.floatshort.PRO.Automation.Categories;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import net.geekstools.floatshort.PRO.Automation.Apps.AppAutoFeatures;
import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.Category.FoldersHandler;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClassDebug;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;
import net.geekstools.floatshort.PRO.Util.UI.SimpleGestureFilterFull;

import java.io.File;
import java.util.ArrayList;

public class CategoryAutoFeatures extends AppCompatActivity implements View.OnClickListener, SimpleGestureFilterFull.SimpleGestureListener {

    Activity activity;
    FunctionsClass functionsClass;
    RecyclerView categorylist;
    ListView actionElementsList;
    RelativeLayout fullActionButton, MainView;
    LinearLayout autoIdentifier;
    RelativeLayout loadingSplash;
    ProgressBar loadingBarLTR;
    TextView desc;
    Button wifi, bluetooth, gps, nfc, time, autoApps, autoCategories;

    String AppTime;
    ArrayList<NavDrawerItem> navDrawerItems;
    RecyclerView.Adapter categoryAutoListAdapter;
    LinearLayoutManager recyclerViewLayoutManager;

    int color, pressColor;

    SimpleGestureFilterFull simpleGestureFilterFull;

    LoadCustomIcons loadCustomIcons;

    @Override
    protected void onCreate(Bundle Saved) {
        super.onCreate(Saved);
        setContentView(R.layout.auto_categories);
        PublicVariable.eligibleLoadShowAds = true;

        categorylist = (RecyclerView) findViewById(R.id.listFav);
        autoIdentifier = (LinearLayout) findViewById(R.id.autoid);
        autoIdentifier.bringToFront();
        MainView = (RelativeLayout) findViewById(R.id.MainView);
        fullActionButton = (RelativeLayout) findViewById(R.id.fullActionViews);
        actionElementsList = (ListView) findViewById(R.id.acttionElementsList);
        autoApps = (Button) findViewById(R.id.autoApps);
        autoApps.bringToFront();
        autoCategories = (Button) findViewById(R.id.autoCategories);
        autoCategories.bringToFront();
        wifi = (Button) findViewById(R.id.wifi);
        bluetooth = (Button) findViewById(R.id.bluetooth);
        gps = (Button) findViewById(R.id.gps);
        nfc = (Button) findViewById(R.id.nfc);
        time = (Button) findViewById(R.id.time);

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

        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), OrientationHelper.VERTICAL, false);
        categorylist.setLayoutManager(recyclerViewLayoutManager);

        autoApps.setTextColor(getColor(R.color.light));
        autoCategories.setTextColor(getColor(R.color.light));
        if (PublicVariable.themeLightDark && functionsClass.appThemeTransparent()) {
            autoApps.setTextColor(getColor(R.color.dark));
            autoCategories.setTextColor(getColor(R.color.dark));
        }

        RippleDrawable rippleDrawableShortcuts = (RippleDrawable) getDrawable(R.drawable.draw_shortcuts);
        Drawable gradientDrawableShortcutsForeground = rippleDrawableShortcuts.findDrawableByLayerId(R.id.foregroundItem);
        Drawable gradientDrawableShortcutsBackground = rippleDrawableShortcuts.findDrawableByLayerId(R.id.backgroundItem);
        Drawable gradientDrawableMaskShortcuts = rippleDrawableShortcuts.findDrawableByLayerId(android.R.id.mask);

        if (functionsClass.appThemeTransparent()) {
            rippleDrawableShortcuts.setColor(ColorStateList.valueOf(PublicVariable.primaryColor));
            gradientDrawableShortcutsForeground.setTint(functionsClass.setColorAlpha(PublicVariable.primaryColorOpposite, 255));
            if (functionsClass.returnAPI() > 21) {
                gradientDrawableShortcutsBackground.setTint(functionsClass.setColorAlpha(PublicVariable.primaryColorOpposite, 175));
            } else {
                gradientDrawableShortcutsBackground.setTint(functionsClass.setColorAlpha(PublicVariable.primaryColorOpposite, 175));
            }
            gradientDrawableMaskShortcuts.setTint(PublicVariable.primaryColor);
        } else {
            rippleDrawableShortcuts.setColor(ColorStateList.valueOf(PublicVariable.primaryColor));
            gradientDrawableShortcutsForeground.setTint(PublicVariable.primaryColorOpposite);
            gradientDrawableShortcutsBackground.setTint(PublicVariable.primaryColorOpposite);
            gradientDrawableMaskShortcuts.setTint(PublicVariable.primaryColor);
        }
        autoApps.setBackground(rippleDrawableShortcuts);

        RippleDrawable rippleDrawableCategories = (RippleDrawable) getDrawable(R.drawable.draw_categories);
        Drawable gradientDrawableCategoriesForeground = rippleDrawableCategories.findDrawableByLayerId(R.id.foregroundItem);
        Drawable gradientDrawableCategoriesBackground = rippleDrawableCategories.findDrawableByLayerId(R.id.backgroundItem);
        Drawable gradientDrawableMaskCategories = rippleDrawableCategories.findDrawableByLayerId(android.R.id.mask);

        if (functionsClass.appThemeTransparent()) {
            rippleDrawableCategories.setColor(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));
            gradientDrawableCategoriesForeground.setTint(
                    functionsClass.setColorAlpha(
                            functionsClass.mixColors(
                                    PublicVariable.primaryColor, PublicVariable.colorLightDark,
                                    0.75f), functionsClass.wallpaperStaticLive() ? 245 : 113)
            );
            gradientDrawableCategoriesBackground.setTint(functionsClass.setColorAlpha(PublicVariable.primaryColor, functionsClass.wallpaperStaticLive() ? 150 : 155));
            gradientDrawableMaskCategories.setTint(PublicVariable.primaryColorOpposite);
        } else {
            rippleDrawableCategories.setColor(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));
            gradientDrawableCategoriesForeground.setTint(PublicVariable.primaryColor);
            gradientDrawableCategoriesBackground.setTint(PublicVariable.primaryColor);
            gradientDrawableMaskCategories.setTint(PublicVariable.primaryColorOpposite);
        }
        autoCategories.setBackground(rippleDrawableCategories);

        ImageView floatingLogo = (ImageView) findViewById(R.id.loadLogo);
        LayerDrawable drawFloatingLogo = (LayerDrawable) getDrawable(R.drawable.draw_floating_logo);
        Drawable backFloatingLogo = drawFloatingLogo.findDrawableByLayerId(R.id.backtemp);
        backFloatingLogo.setTint(PublicVariable.primaryColorOpposite);
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

        autoApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublicVariable.autoID = null;
                try {
                    functionsClass.navigateToClass(AppAutoFeatures.class,
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_left, R.anim.slide_to_right));
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
        PublicVariable.eligibleLoadShowAds = true;

        if (PublicVariable.themeLightDark) {
            color = PublicVariable.themeColor;
            pressColor = PublicVariable.themeTextColor;
        } else if (!PublicVariable.themeLightDark) {
            color = PublicVariable.themeTextColor;
            pressColor = PublicVariable.themeColor;
        }

        if (PublicVariable.autoID != null) {
            final LayerDrawable drawWifi = (LayerDrawable) getDrawable(R.drawable.draw_wifi);
            final Drawable backWifi = drawWifi.findDrawableByLayerId(R.id.backtemp);

            final LayerDrawable drawBluetooth = (LayerDrawable) getDrawable(R.drawable.draw_bluetooth);
            final Drawable backBluetooth = drawBluetooth.findDrawableByLayerId(R.id.backtemp);

            final LayerDrawable drawGPS = (LayerDrawable) getDrawable(R.drawable.draw_gps);
            final Drawable backGPS = drawGPS.findDrawableByLayerId(R.id.backtemp);

            final LayerDrawable drawNfc = (LayerDrawable) getDrawable(R.drawable.draw_nfc);
            final Drawable backNfc = drawNfc.findDrawableByLayerId(R.id.backtemp);

            final LayerDrawable drawTime = (LayerDrawable) getDrawable(R.drawable.draw_time);
            final Drawable backTime = drawTime.findDrawableByLayerId(R.id.backtemp);

            wifi.setBackground(drawWifi);
            bluetooth.setBackground(drawBluetooth);
            gps.setBackground(drawGPS);
            nfc.setBackground(drawNfc);
            time.setBackground(drawTime);

            if (PublicVariable.autoID.equals(getString(R.string.wifi_category))) {
                backWifi.setTint(pressColor);
                wifi.setBackground(drawWifi);

                autoWiFi();

                backBluetooth.setTint(color);
                backGPS.setTint(color);
                backNfc.setTint(color);
                backTime.setTint(color);

                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            } else if (PublicVariable.autoID.equals(getString(R.string.bluetooth_category))) {
                backBluetooth.setTint(pressColor);
                bluetooth.setBackground(drawBluetooth);

                autoBluetooth();

                backWifi.setTint(color);
                backGPS.setTint(color);
                backNfc.setTint(color);
                backTime.setTint(color);

                wifi.setBackground(drawWifi);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            } else if (PublicVariable.autoID.equals(getString(R.string.gps_category))) {
                backGPS.setTint(pressColor);
                gps.setBackground(drawGPS);

                autoGPS();

                backWifi.setTint(color);
                backBluetooth.setTint(color);
                backNfc.setTint(color);
                backTime.setTint(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            } else if (PublicVariable.autoID.equals(getString(R.string.nfc_category))) {
                backNfc.setTint(pressColor);
                nfc.setBackground(drawNfc);

                autoNfc();

                backWifi.setTint(color);
                backBluetooth.setTint(color);
                backGPS.setTint(color);
                backTime.setTint(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                time.setBackground(drawTime);
            } else if (PublicVariable.autoID.equals(getString(R.string.time_category))) {
                backTime.setTint(pressColor);
                time.setBackground(drawTime);

                autoTime();

                backWifi.setTint(color);
                backBluetooth.setTint(color);
                backGPS.setTint(color);
                backNfc.setTint(color);

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
        final Drawable backWifi = drawWifi.findDrawableByLayerId(R.id.backtemp);

        final LayerDrawable drawBluetooth = (LayerDrawable) getDrawable(R.drawable.draw_bluetooth);
        final Drawable backBluetooth = drawBluetooth.findDrawableByLayerId(R.id.backtemp);

        final LayerDrawable drawGPS = (LayerDrawable) getDrawable(R.drawable.draw_gps);
        final Drawable backGPS = drawGPS.findDrawableByLayerId(R.id.backtemp);

        final LayerDrawable drawNfc = (LayerDrawable) getDrawable(R.drawable.draw_nfc);
        final Drawable backNfc = drawNfc.findDrawableByLayerId(R.id.backtemp);

        final LayerDrawable drawTime = (LayerDrawable) getDrawable(R.drawable.draw_time);
        final Drawable backTime = drawTime.findDrawableByLayerId(R.id.backtemp);

        if (PublicVariable.themeLightDark) {
            color = PublicVariable.themeColor;
            pressColor = PublicVariable.themeTextColor;
        } else if (!PublicVariable.themeLightDark) {
            color = PublicVariable.themeTextColor;
            pressColor = PublicVariable.themeColor;
        }

        backWifi.setTint(color);
        backBluetooth.setTint(color);
        backGPS.setTint(color);
        backNfc.setTint(color);
        backTime.setTint(color);

        wifi.setBackground(drawWifi);
        bluetooth.setBackground(drawBluetooth);
        gps.setBackground(drawGPS);
        nfc.setBackground(drawNfc);
        time.setBackground(drawTime);

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backWifi.setTint(pressColor);
                wifi.setBackground(drawWifi);

                autoWiFi();

                backBluetooth.setTint(color);
                backGPS.setTint(color);
                backNfc.setTint(color);
                backTime.setTint(color);

                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            }
        });
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backBluetooth.setTint(pressColor);
                bluetooth.setBackground(drawBluetooth);

                autoBluetooth();

                backWifi.setTint(color);
                backGPS.setTint(color);
                backNfc.setTint(color);
                backTime.setTint(color);

                wifi.setBackground(drawWifi);
                gps.setBackground(drawGPS);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            }
        });
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backGPS.setTint(pressColor);
                gps.setBackground(drawGPS);

                autoGPS();

                backWifi.setTint(color);
                backBluetooth.setTint(color);
                backNfc.setTint(color);
                backTime.setTint(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                nfc.setBackground(drawNfc);
                time.setBackground(drawTime);
            }
        });
        nfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backNfc.setTint(pressColor);
                nfc.setBackground(drawNfc);

                autoNfc();

                backWifi.setTint(color);
                backBluetooth.setTint(color);
                backGPS.setTint(color);
                backTime.setTint(color);

                wifi.setBackground(drawWifi);
                bluetooth.setBackground(drawBluetooth);
                gps.setBackground(drawGPS);
                time.setBackground(drawTime);
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backTime.setTint(pressColor);
                time.setBackground(drawTime);

                autoTime();

                backWifi.setTint(color);
                backBluetooth.setTint(color);
                backGPS.setTint(color);
                backNfc.setTint(color);

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
        PublicVariable.eligibleLoadShowAds = false;

        functionsClass.CheckSystemRAM(CategoryAutoFeatures.this);

        if (functionsClass.automationFeatureEnable()) {
            startService(new Intent(getApplicationContext(), BindServices.class));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PublicVariable.autoID = null;
    }

    @Override
    public void onBackPressed() {
        try {
            functionsClass.overrideBackPressToMain(CategoryAutoFeatures.this);
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
                FunctionsClassDebug.Companion.PrintDebug("Swipe Right");

                break;
            }
            case SimpleGestureFilterFull.SWIPE_LEFT: {
                FunctionsClassDebug.Companion.PrintDebug("Swipe Left");
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
                FunctionsClassDebug.Companion.PrintDebug("Swipe UP");
                try {
                    if (recyclerViewLayoutManager.findLastCompletelyVisibleItemPosition() == (categorylist.getAdapter().getItemCount() - 1)) {
                        try {
                            functionsClass.overrideBackPressToMain(CategoryAutoFeatures.this);
                            overridePendingTransition(android.R.anim.fade_in, R.anim.go_up);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    try {
                        functionsClass.overrideBackPressToMain(CategoryAutoFeatures.this);
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
        PublicVariable.autoID = getString(R.string.wifi_category);
        int API = android.os.Build.VERSION.SDK_INT;
        if (API > 22) {
            if (checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_DENIED) {
                String[] gpsPermissions = {Manifest.permission.ACCESS_WIFI_STATE};
                requestPermissions(gpsPermissions, 888);
                return;
            }
        }

        LoadAutoCategories loadAutoCategories = new LoadAutoCategories();
        loadAutoCategories.execute();
    }

    public void autoBluetooth() {
        PublicVariable.autoID = getString(R.string.bluetooth_category);
        int API = android.os.Build.VERSION.SDK_INT;
        if (API > 22) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_DENIED) {
                String[] gpsPermissions = {Manifest.permission.BLUETOOTH};
                requestPermissions(gpsPermissions, 999);
                return;
            }
        }

        LoadAutoCategories loadAutoCategories = new LoadAutoCategories();
        loadAutoCategories.execute();
    }

    public void autoGPS() {
        PublicVariable.autoID = getString(R.string.gps_category);
        int API = android.os.Build.VERSION.SDK_INT;
        if (API > 22) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                String[] gpsPermissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(gpsPermissions, 777);
                return;
            }
        }

        LoadAutoCategories loadAutoCategories = new LoadAutoCategories();
        loadAutoCategories.execute();
    }

    public void autoNfc() {
        PublicVariable.autoID = getString(R.string.nfc_category);
        int API = android.os.Build.VERSION.SDK_INT;
        if (API > 22) {
            if (checkSelfPermission(Manifest.permission.NFC) == PackageManager.PERMISSION_DENIED) {
                String[] gpsPermissions = {Manifest.permission.NFC};
                requestPermissions(gpsPermissions, 888);
                return;
            }
        }

        LoadAutoCategories loadAutoCategories = new LoadAutoCategories();
        loadAutoCategories.execute();
    }

    public void autoTime() {
        PublicVariable.autoID = getString(R.string.time_category);

        LoadAutoCategories loadAutoCategories = new LoadAutoCategories();
        loadAutoCategories.execute();
    }

    /******************************************/
    private class LoadAutoCategories extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            File f = getApplicationContext().getFileStreamPath(".categoryInfo");
            if (!f.exists()) {
                finish();
                return;
            }

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
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String[] appData = functionsClass.readFileLine(".categoryInfo");

                if (functionsClass.loadCustomIcons()) {
                    loadCustomIcons.load();
                    FunctionsClassDebug.Companion.PrintDebug("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIcons());
                }

                navDrawerItems = new ArrayList<NavDrawerItem>();
                for (int navItem = 0; navItem < appData.length; navItem++) {
                    try {
                        if (getFileStreamPath(appData[navItem] + ".Time").exists()) {
                            AppTime = functionsClass.readFile(appData[navItem] + ".Time");
                            String tempTimeHour = AppTime.split(":")[0];
                            String tempTimeMinute = AppTime.split(":")[1];
                            if (Integer.parseInt(tempTimeMinute) < 10) {
                                AppTime = tempTimeHour + ":" + "0" + tempTimeMinute;
                            }
                        }

                        navDrawerItems.add(new NavDrawerItem(
                                appData[navItem],
                                functionsClass.readFileLine(appData[navItem]),
                                AppTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                categoryAutoListAdapter = new CategoryAutoListAdapter(activity, getApplicationContext(), navDrawerItems);
            } catch (Exception e) {
                e.printStackTrace();
                startActivity(new Intent(getApplicationContext(), FoldersHandler.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            categorylist.setAdapter(categoryAutoListAdapter);
            registerForContextMenu(categorylist);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                    loadingSplash.setVisibility(View.INVISIBLE);
                    loadingSplash.startAnimation(anim);
                    autoIdentifier.setVisibility(View.VISIBLE);
                }
            }, 100);
        }
    }
}
