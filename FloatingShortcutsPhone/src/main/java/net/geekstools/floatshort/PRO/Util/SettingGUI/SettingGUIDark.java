package net.geekstools.floatshort.PRO.Util.SettingGUI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.text.Html;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClassDebug;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.IAP.InAppBilling;
import net.geekstools.floatshort.PRO.Util.InteractionObserver.InteractionObserver;
import net.geekstools.floatshort.PRO.Util.NavAdapter.CustomIconsThemeAdapter;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.NavAdapter.RecycleViewSmoothLayoutList;
import net.geekstools.floatshort.PRO.Widget.WidgetConfigurations;

import java.util.ArrayList;

public class SettingGUIDark extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    FunctionsClass functionsClass;
    ListPreference themeColor, stick;
    SharedPreferences sharedPreferences;
    SwitchPreference stable, cache, themeTrans, smart, blur, observe, notification, floatingSplash, freeForm;
    Preference shapes, autotrans, sizes, delayPressHold, flingSensitivity, boot, lite, support, whatsnew, adApp;

    Runnable runnablePressHold = null;
    Handler handlerPressHold = new Handler();
    boolean touchingDelay = false, FromWidgetsConfigurations = false;

    View rootLayout;

    FirebaseRemoteConfig firebaseRemoteConfig;

    String betaChangeLog = "net.geekstools.floatshort.PRO", betaVersionCode = "0";

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        addPreferencesFromResource(R.xml.setting_gui);
        PublicVariable.eligibleLoadShowAds = true;

        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PublicVariable.activityStatic = SettingGUIDark.this;
        functionsClass = new FunctionsClass(getApplicationContext(), SettingGUIDark.this);

        this.getListView().setCacheColorHint(Color.TRANSPARENT);
        this.getListView().setVerticalFadingEdgeEnabled(true);
        this.getListView().setFadingEdgeLength(functionsClass.DpToInteger(13));
        this.getListView().setDivider(new ColorDrawable(Color.TRANSPARENT));
        this.getListView().setDividerHeight((int) functionsClass.DpToPixel(3));
        this.getListView().setScrollBarSize(0);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        FromWidgetsConfigurations = getIntent().hasExtra("FromWidgetsConfigurations") ? getIntent().getBooleanExtra("FromWidgetsConfigurations", false) : false;

        if (functionsClass.appThemeTransparent() == true) {
            functionsClass.setThemeColorPreferences(this.getListView(), true, getString(R.string.settingTitle), functionsClass.appVersionName(getPackageName()));
        } else {
            functionsClass.setThemeColorPreferences(this.getListView(), false, getString(R.string.settingTitle), functionsClass.appVersionName(getPackageName()));
        }

        rootLayout = this.getWindow().getDecorView();
        rootLayout.setVisibility(View.INVISIBLE);
        ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int finalRadius = (int) Math.hypot(functionsClass.displayX(), functionsClass.displayY());
                    Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, (functionsClass.displayX() / 2), (functionsClass.displayY() / 2), functionsClass.DpToInteger(55), finalRadius);
                    circularReveal.setDuration(1300);
                    circularReveal.setInterpolator(new AccelerateInterpolator());

                    rootLayout.setVisibility(View.VISIBLE);
                    circularReveal.start();
                    rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    circularReveal.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            rootLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                }
            });
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }

        stable = (SwitchPreference) findPreference("stable");
        cache = (SwitchPreference) findPreference("cache");
        themeTrans = (SwitchPreference) findPreference("transparent");
        blur = (SwitchPreference) findPreference("blur");
        smart = (SwitchPreference) findPreference("smart");
        observe = (SwitchPreference) findPreference("observe");
        notification = (SwitchPreference) findPreference("notification");
        floatingSplash = (SwitchPreference) findPreference("floatingSplash");
        freeForm = (SwitchPreference) findPreference("freeForm");

        shapes = (Preference) findPreference("shapes");
        autotrans = (Preference) findPreference("hide");
        sizes = (Preference) findPreference("sizes");
        flingSensitivity = (Preference) findPreference("flingSensitivity");
        delayPressHold = (Preference) findPreference("delayPressHold");
        lite = (Preference) findPreference("lite");
        adApp = (Preference) findPreference("app");
        whatsnew = (Preference) findPreference("whatsnew");

        String sticky = sharedPreferences.getString("stick", "1");
        stick = (ListPreference) findPreference("stick");
        if (sticky.equals("1")) {
            stick.setSummary(getString(R.string.leftEdge));
        } else if (sticky.equals("2")) {
            stick.setSummary(getString(R.string.rightEdge));
        }

        String b = sharedPreferences.getString("boot", "1");
        boot = (Preference) findPreference("boot");
        if (b.equals("0")) {
            boot.setSummary(getString(R.string.boot_none));
        } else if (b.equals("1")) {
            boot.setSummary(getString(R.string.shortcuts));
        } else if (b.equals("2")) {
            boot.setSummary(getString(R.string.floatingCategory));
        } else if (b.equals("3")) {
            boot.setSummary(getString(R.string.boot_warning));
        }

        functionsClass.checkLightDarkTheme();
        String appTheme = sharedPreferences.getString("themeColor", "2");
        themeColor = (ListPreference) findPreference("themeColor");
        if (appTheme.equals("1")) {
            themeColor.setSummary(getString(R.string.light));
            PublicVariable.themeLightDark = true;
        } else if (appTheme.equals("2")) {
            themeColor.setSummary(getString(R.string.dark));
            PublicVariable.themeLightDark = false;
        } else if (appTheme.equals("3")) {
            functionsClass.checkLightDarkTheme();
            themeColor.setSummary(getString(R.string.dynamic));
        }

        delayPressHold.setSummary(functionsClass.readDefaultPreference("delayPressHold", 333) + " " + getString(R.string.millis));

        cache.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (PublicVariable.Stable) {
                    stopService(new Intent(getApplicationContext(), BindServices.class));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startService(new Intent(getApplicationContext(), BindServices.class));
                        }
                    }, 333);
                } else {
                    if (sharedPreferences.getBoolean("cache", true) == true) {
                        startService(new Intent(getApplicationContext(), BindServices.class));
                    } else if (sharedPreferences.getBoolean("cache", true) == false) {
                        if (PublicVariable.floatingCounter == 0) {

                            stopService(new Intent(getApplicationContext(), BindServices.class));
                        }

                        functionsClass.saveDefaultPreference("LitePreferences", false);
                    }
                }
                return false;
            }
        });
        stable.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (sharedPreferences.getBoolean("stable", false) == true) {
                    PublicVariable.Stable = true;
                    startService(new Intent(getApplicationContext(), BindServices.class));
                } else if (sharedPreferences.getBoolean("stable", false) == false) {
                    PublicVariable.Stable = false;
                    if (PublicVariable.floatingCounter == 0) {

                        stopService(new Intent(getApplicationContext(), BindServices.class));
                    }

                    functionsClass.saveDefaultPreference("LitePreferences", false);
                }
                return false;
            }
        });
        themeTrans.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (functionsClass.appThemeTransparent() == true) {
                    functionsClass.setThemeColorPreferences(SettingGUIDark.this.getListView(), true, getString(R.string.settingTitle), functionsClass.appVersionName(getPackageName()));
                } else {
                    functionsClass.setThemeColorPreferences(SettingGUIDark.this.getListView(), false, getString(R.string.settingTitle), functionsClass.appVersionName(getPackageName()));
                }

                functionsClass.saveDefaultPreference("LitePreferences", false);
                return false;
            }
        });
        blur.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (functionsClass.appThemeTransparent() == true) {
                    functionsClass.setThemeColorPreferences(SettingGUIDark.this.getListView(), true, getString(R.string.settingTitle), functionsClass.appVersionName(getPackageName()));
                } else {
                    functionsClass.setThemeColorPreferences(SettingGUIDark.this.getListView(), false, getString(R.string.settingTitle), functionsClass.appVersionName(getPackageName()));
                }

                functionsClass.saveDefaultPreference("LitePreferences", false);
                return false;
            }
        });

        support = (Preference) findPreference("support");
        support.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                functionsClass.ContactSupport(SettingGUIDark.this);
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        LayerDrawable layerDrawableLoadLogo = (LayerDrawable) getDrawable(R.drawable.ic_launcher_layer);
        BitmapDrawable gradientDrawableLoadLogo = (BitmapDrawable) layerDrawableLoadLogo.findDrawableByLayerId(R.id.ic_launcher_back_layer);
        gradientDrawableLoadLogo.setTint(PublicVariable.primaryColorOpposite);
        whatsnew.setIcon(layerDrawableLoadLogo);
        whatsnew.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                functionsClass.ChangeLog(SettingGUIDark.this, betaChangeLog, betaVersionCode, true);

                return true;
            }
        });

        LayerDrawable layerDrawableAdApp = (LayerDrawable) getDrawable(R.drawable.ic_ad_app_layer);
        BitmapDrawable gradientDrawableAdApp = (BitmapDrawable) layerDrawableAdApp.findDrawableByLayerId(R.id.ic_launcher_back_layer);
        gradientDrawableAdApp.setTint(PublicVariable.primaryColorOpposite);
        adApp.setIcon(layerDrawableAdApp);
        adApp.setTitle(Html.fromHtml(getString(R.string.adApp)));
        adApp.setSummary(Html.fromHtml(getString(R.string.adAppSummary)));
        adApp.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_ad_app))));
                return true;
            }
        });

        smart.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (!Settings.ACTION_USAGE_ACCESS_SETTINGS.isEmpty()) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    if (sharedPreferences.getBoolean("smart", true) == true) {
                        smart.setChecked(true);

                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivity(intent);
                        finish();
                    } else if (sharedPreferences.getBoolean("smart", true) == false) {
                        smart.setChecked(false);

                        functionsClass.UsageAccess(SettingGUIDark.this, smart);
                    }
                }
                return true;
            }
        });

        observe.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (!functionsClass.AccessibilityServiceEnabled() && !functionsClass.SettingServiceRunning(InteractionObserver.class)) {
                    functionsClass.AccessibilityService(SettingGUIDark.this, observe);
                } else {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                return true;
            }
        });

        boot.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String[] remoteOptions = getResources().getStringArray(R.array.Boot);
                AlertDialog.Builder builder = null;
                if (PublicVariable.themeLightDark == true) {
                    builder = new AlertDialog.Builder(SettingGUIDark.this, R.style.GeeksEmpire_Dialogue_Light);
                } else if (PublicVariable.themeLightDark == false) {
                    builder = new AlertDialog.Builder(SettingGUIDark.this, R.style.GeeksEmpire_Dialogue_Dark);
                }
                builder.setTitle(getString(R.string.boot));
                builder.setSingleChoiceItems(remoteOptions, Integer.parseInt(sharedPreferences.getString("boot", "1")), null);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        editor.putString("boot", String.valueOf(selectedPosition));
                        editor.apply();

                        String b = sharedPreferences.getString("boot", "1");
                        boot = (Preference) findPreference("boot");
                        if (b.equals("0")) {
                            boot.setSummary(getString(R.string.boot_none));
                        } else if (b.equals("1")) {
                            boot.setSummary(getString(R.string.shortcuts));
                        } else if (b.equals("2")) {
                            boot.setSummary(getString(R.string.floatingCategory));
                        } else if (b.equals("3")) {
                            boot.setSummary(getString(R.string.boot_warning));
                        }
                        functionsClass.addAppShortcuts();
                    }
                });
                if (functionsClass.returnAPI() > 22) {
                    builder.setNeutralButton(getString(R.string.read), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            functionsClass.RemoteRecovery(SettingGUIDark.this);
                        }
                    });
                }

                builder.show();
                return true;
            }
        });

        notification.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (functionsClass.NotificationAccess() && functionsClass.NotificationListenerRunning()) {
                    Intent notification = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    notification.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(notification);
                } else {
                    functionsClass.NotificationAccessService(SettingGUIDark.this, notification);
                }
                return true;
            }
        });

        shapes.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setupShapes();

                return true;
            }
        });

        freeForm.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (functionsClass.FreeForm()) {
                    functionsClass.FreeFormInformation(SettingGUIDark.this, freeForm);
                } else {
                    freeForm.setChecked(false);
                }
                return true;
            }
        });

        autotrans.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                int dialogueWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                int dialogueHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 270, getResources().getDisplayMetrics());

                layoutParams.width = dialogueWidth;
                layoutParams.height = dialogueHeight;
                layoutParams.windowAnimations = android.R.style.Animation_Dialog;
                layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                layoutParams.dimAmount = 0.57f;

                final Dialog dialog = new Dialog(SettingGUIDark.this);
                dialog.setContentView(R.layout.seekbar_preferences);
                dialog.setTitle(Html.fromHtml("<font color='" + PublicVariable.colorLightDarkOpposite + "'>" + getString(R.string.autotrans) + "</font>"));
                dialog.getWindow().setAttributes(layoutParams);
                dialog.getWindow().getDecorView().setBackgroundColor(PublicVariable.colorLightDark);
                dialog.setCancelable(true);

                final ImageView transparentIcon = (ImageView) dialog.findViewById(R.id.transparentIcon);
                final SeekBar seekBarPreferences = (SeekBar) dialog.findViewById(R.id.seekBarPreferences);
                TextView revertDefault = (TextView) dialog.findViewById(R.id.revertDefault);

                seekBarPreferences.setThumbTintList(ColorStateList.valueOf(PublicVariable.primaryColor));
                seekBarPreferences.setThumbTintMode(PorterDuff.Mode.SRC_IN);
                seekBarPreferences.setProgressTintList(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));
                seekBarPreferences.setProgressTintMode(PorterDuff.Mode.SRC_IN);

                seekBarPreferences.setMax(213);
                seekBarPreferences.setProgress(functionsClass.readDefaultPreference("autoTransProgress", 0));

                Drawable layerDrawableLoadLogo;
                try {
                    Drawable backgroundDot = functionsClass.shapesDrawables().mutate();
                    backgroundDot.setTint(PublicVariable.primaryColorOpposite);
                    layerDrawableLoadLogo = new LayerDrawable(new Drawable[]{
                            backgroundDot,
                            getDrawable(R.drawable.ic_launcher_dots)
                    });
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    layerDrawableLoadLogo = getDrawable(R.drawable.ic_launcher);
                }

                transparentIcon.setImageAlpha(functionsClass.readDefaultPreference("autoTrans", 255));
                transparentIcon.setImageDrawable(layerDrawableLoadLogo);

                revertDefault.setTextColor(PublicVariable.colorLightDarkOpposite);

                seekBarPreferences.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        int alpha = 255 - progress;
                        transparentIcon.setImageAlpha(alpha);

                        functionsClass.saveDefaultPreference("autoTrans", alpha);
                        functionsClass.saveDefaultPreference("autoTransProgress", progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                revertDefault.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        functionsClass.saveDefaultPreference("autoTrans", 113);
                        functionsClass.saveDefaultPreference("autoTransProgress", 95);

                        transparentIcon.setImageAlpha(113);
                        seekBarPreferences.setProgress(95);
                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        LayerDrawable drawPrefAutoTrans = (LayerDrawable) getDrawable(R.drawable.draw_pref).mutate();
                        GradientDrawable backPrefAutoTrans = (GradientDrawable) drawPrefAutoTrans.findDrawableByLayerId(R.id.backtemp).mutate();
                        backPrefAutoTrans.setColor(PublicVariable.primaryColor);
                        backPrefAutoTrans.setAlpha(functionsClass.readDefaultPreference("autoTrans", 255));
                        autotrans.setIcon(drawPrefAutoTrans);

                        PublicVariable.forceReload = false;

                        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    }
                });
                dialog.show();

                return true;
            }
        });

        sizes.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                int dialogueWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                int dialogueHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 270, getResources().getDisplayMetrics());

                layoutParams.width = dialogueWidth;
                layoutParams.height = dialogueHeight;
                layoutParams.windowAnimations = android.R.style.Animation_Dialog;
                layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                layoutParams.dimAmount = 0.57f;

                final Dialog dialog = new Dialog(SettingGUIDark.this);
                dialog.setContentView(R.layout.seekbar_preferences);
                dialog.setTitle(Html.fromHtml("<font color='" + PublicVariable.colorLightDarkOpposite + "'>" + getString(R.string.shortsizepref) + "</font>"));
                dialog.getWindow().setAttributes(layoutParams);
                dialog.getWindow().getDecorView().setBackgroundColor(PublicVariable.colorLightDark);
                dialog.setCancelable(true);

                final ImageView transparentIcon = (ImageView) dialog.findViewById(R.id.transparentIcon);
                final SeekBar seekBarPreferences = (SeekBar) dialog.findViewById(R.id.seekBarPreferences);
                TextView revertDefault = (TextView) dialog.findViewById(R.id.revertDefault);

                seekBarPreferences.setThumbTintList(ColorStateList.valueOf(PublicVariable.primaryColor));
                seekBarPreferences.setThumbTintMode(PorterDuff.Mode.SRC_IN);
                seekBarPreferences.setProgressTintList(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));
                seekBarPreferences.setProgressTintMode(PorterDuff.Mode.SRC_IN);

                seekBarPreferences.setMax(5);
                seekBarPreferences.setProgress(functionsClass.readDefaultPreference("floatingSizeProgress", 2));

                Drawable layerDrawableLoadLogo;
                try {
                    Drawable backgroundDot = functionsClass.shapesDrawables().mutate();
                    backgroundDot.setTint(PublicVariable.primaryColorOpposite);
                    layerDrawableLoadLogo = new LayerDrawable(new Drawable[]{
                            backgroundDot,
                            getDrawable(R.drawable.ic_launcher_dots)
                    });
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    layerDrawableLoadLogo = getDrawable(R.drawable.ic_launcher);
                }

                int iconHW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, functionsClass.readDefaultPreference("floatingSize", 39), getResources().getDisplayMetrics());
                RelativeLayout.LayoutParams layoutParamsIcon = new RelativeLayout.LayoutParams(
                        iconHW,
                        iconHW
                );
                layoutParamsIcon.addRule(RelativeLayout.CENTER_HORIZONTAL, R.id.seekBarView);
                transparentIcon.setLayoutParams(layoutParamsIcon);
                transparentIcon.setImageDrawable(layerDrawableLoadLogo);

                revertDefault.setTextColor(PublicVariable.colorLightDarkOpposite);

                final int[] progressTemp = new int[]{1, 2, 3, 4, 5, 6};
                seekBarPreferences.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        int size = 13 * progressTemp[progress];
                        functionsClass.saveDefaultPreference("floatingSize", size);
                        functionsClass.saveDefaultPreference("floatingSizeProgress", progress);

                        int iconHW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
                        RelativeLayout.LayoutParams layoutParamsIcon = new RelativeLayout.LayoutParams(
                                iconHW,
                                iconHW
                        );
                        layoutParamsIcon.addRule(RelativeLayout.CENTER_HORIZONTAL, R.id.seekBarView);
                        transparentIcon.setLayoutParams(layoutParamsIcon);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                revertDefault.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        functionsClass.saveDefaultPreference("floatingSize", 39);
                        functionsClass.saveDefaultPreference("floatingSizeProgress", 2);

                        int iconHW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 39, getResources().getDisplayMetrics());
                        RelativeLayout.LayoutParams layoutParamsIcon = new RelativeLayout.LayoutParams(
                                iconHW,
                                iconHW
                        );
                        layoutParamsIcon.addRule(RelativeLayout.CENTER_HORIZONTAL, R.id.seekBarView);
                        transparentIcon.setLayoutParams(layoutParamsIcon);
                        seekBarPreferences.setProgress(2);
                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        PublicVariable.forceReload = false;

                        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    }
                });
                dialog.show();

                return true;
            }
        });

        delayPressHold.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                int dialogueWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                int dialogueHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 270, getResources().getDisplayMetrics());

                layoutParams.width = dialogueWidth;
                layoutParams.height = dialogueHeight;
                layoutParams.windowAnimations = android.R.style.Animation_Dialog;
                layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                layoutParams.dimAmount = 0.57f;

                final Dialog dialog = new Dialog(SettingGUIDark.this);
                dialog.setContentView(R.layout.seekbar_preferences);
                dialog.setTitle(Html.fromHtml("<font color='" + PublicVariable.colorLightDarkOpposite + "'>" + getString(R.string.delayPressHold) + "</font>"));
                dialog.getWindow().setAttributes(layoutParams);
                dialog.getWindow().getDecorView().setBackgroundColor(PublicVariable.colorLightDark);
                dialog.setCancelable(true);

                final TextView extraInfo = (TextView) dialog.findViewById(R.id.extraInfo);
                final ImageView delayIcon = (ImageView) dialog.findViewById(R.id.transparentIcon);
                final SeekBar seekBarPreferences = (SeekBar) dialog.findViewById(R.id.seekBarPreferences);
                TextView revertDefault = (TextView) dialog.findViewById(R.id.revertDefault);

                extraInfo.setVisibility(View.VISIBLE);
                extraInfo.setTextColor(PublicVariable.colorLightDarkOpposite);
                extraInfo.setText(getString(R.string.delayPressHoldExtraInfo));

                seekBarPreferences.setThumbTintList(ColorStateList.valueOf(PublicVariable.primaryColor));
                seekBarPreferences.setThumbTintMode(PorterDuff.Mode.SRC_IN);
                seekBarPreferences.setProgressTintList(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));
                seekBarPreferences.setProgressTintMode(PorterDuff.Mode.SRC_IN);

                seekBarPreferences.setMax(1000);
                seekBarPreferences.setProgress(functionsClass.readDefaultPreference("delayPressHoldProgress", 0));

                Drawable layerDrawableLoadLogo;
                try {
                    Drawable backgroundDot = functionsClass.shapesDrawables().mutate();
                    backgroundDot.setTint(PublicVariable.primaryColorOpposite);
                    layerDrawableLoadLogo = new LayerDrawable(new Drawable[]{
                            backgroundDot,
                            getDrawable(R.drawable.ic_launcher_dots)
                    });
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    layerDrawableLoadLogo = getDrawable(R.drawable.ic_launcher);
                }

                delayIcon.setImageDrawable(layerDrawableLoadLogo);

                revertDefault.setTextColor(PublicVariable.colorLightDarkOpposite);

                delayIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
                delayIcon.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                    }
                });
                delayIcon.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                touchingDelay = true;
                                runnablePressHold = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (touchingDelay) {
                                            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                            vibrator.vibrate(333);

                                            /*
                                             *
                                             */
                                            FunctionsClassDebug.Companion.PrintDebug("*** millis delay ::: " + functionsClass.readDefaultPreference("delayPressHold", 333));
                                        }
                                    }
                                };
                                handlerPressHold.postDelayed(runnablePressHold, functionsClass.readDefaultPreference("delayPressHold", 333));
                                break;
                            case MotionEvent.ACTION_UP:
                                touchingDelay = false;
                                handlerPressHold.removeCallbacks(runnablePressHold);

                                break;
                        }
                        return false;
                    }
                });

                seekBarPreferences.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        int delay = 333 + progress;

                        functionsClass.saveDefaultPreference("delayPressHold", delay);
                        functionsClass.saveDefaultPreference("delayPressHoldProgress", progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                revertDefault.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        functionsClass.saveDefaultPreference("delayPressHold", 333);
                        functionsClass.saveDefaultPreference("delayPressHoldProgress", 0);

                        seekBarPreferences.setProgress(0);
                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        delayPressHold.setSummary(functionsClass.readDefaultPreference("delayPressHold", 333) + " " + getString(R.string.millis));

                        PublicVariable.forceReload = false;

                        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    }
                });
                dialog.show();

                return true;
            }
        });

        flingSensitivity.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setupFlingSensitivity();

                return true;
            }
        });

        if (!getFileStreamPath(".LitePreferenceCheckpoint").exists()) {
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            if (activityManager != null) {
                activityManager.getMemoryInfo(memoryInfo);
                if (memoryInfo.totalMem <= 2000000000 || memoryInfo.lowMemory) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getListView().smoothScrollToPosition(getListView().getBottom());

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    functionsClass.litePreferenceConfirm();

                                    functionsClass.saveFileEmpty(".LitePreferenceCheckpoint");
                                }
                            }, 333);
                        }
                    }, 333);
                }
            }
        }
        lite.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                functionsClass.litePreferenceConfirm();
                return true;
            }
        });

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_default);
        firebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(SettingGUIDark.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.activate().addOnSuccessListener(new OnSuccessListener<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    if (firebaseRemoteConfig.getLong(functionsClass.versionCodeRemoteConfigKey()) > functionsClass.appVersionCode(getPackageName())) {
                                        functionsClass.upcomingChangeLog(
                                                SettingGUIDark.this,
                                                firebaseRemoteConfig.getString(functionsClass.upcomingChangeLogRemoteConfigKey()),
                                                String.valueOf(firebaseRemoteConfig.getLong(functionsClass.versionCodeRemoteConfigKey()))
                                        );
                                    }
                                    if (firebaseRemoteConfig.getLong(getString(R.string.BETAintegerVersionCodeNewUpdatePhone)) > functionsClass.appVersionCode(getPackageName())) {
                                        whatsnew.setSummary(getString(R.string.betaUpdateAvailable));

                                        betaChangeLog = firebaseRemoteConfig.getString(getString(R.string.BETAstringUpcomingChangeLogPhone));
                                        betaVersionCode = firebaseRemoteConfig.getString(getString(R.string.BETAintegerVersionCodeNewUpdatePhone));
                                    }
                                    if (firebaseRemoteConfig.getBoolean(getString(R.string.adAppForceLoad))) {
                                        Glide.with(getApplicationContext())
                                                .load(firebaseRemoteConfig.getString(getString(R.string.adAppIconLink)))
                                                .addListener(new RequestListener<Drawable>() {
                                                    @Override
                                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                SettingGUIDark.this.adApp.setIcon(resource);
                                                            }
                                                        });

                                                        return false;
                                                    }
                                                }).submit();

                                        SettingGUIDark.this.adApp.setTitle(Html.fromHtml(firebaseRemoteConfig.getString(getString(R.string.adAppTitle))));
                                        SettingGUIDark.this.adApp.setSummary(Html.fromHtml(firebaseRemoteConfig.getString(getString(R.string.adAppSummaries))));
                                        SettingGUIDark.this.adApp.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                                            @Override
                                            public boolean onPreferenceClick(Preference preference) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(firebaseRemoteConfig.getString(getString(R.string.adAppLink)))));
                                                return true;
                                            }
                                        });

                                        FunctionsClassDebug.Companion.PrintDebug("*** " + firebaseRemoteConfig.getLong(getString(R.string.adAppForceTime)) + " ***");
                                        if (firebaseRemoteConfig.getLong(getString(R.string.adAppForceTime)) > functionsClass.readPreference(".AdApp", "FetchTime", Long.valueOf(0))) {
                                            SettingGUIDark.this.getListView().smoothScrollToPosition(SettingGUIDark.this.getListView().getBottom());
                                            functionsClass.savePreference(".AdApp", "FetchTime", firebaseRemoteConfig.getLong(getString(R.string.adAppForceTime)));
                                        }
                                    }
                                }
                            });
                        } else {

                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        PublicVariable.eligibleLoadShowAds = true;

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        LayerDrawable drawSmart = (LayerDrawable) getDrawable(R.drawable.draw_smart);
        GradientDrawable backSmart = (GradientDrawable) drawSmart.findDrawableByLayerId(R.id.backtemp);

        LayerDrawable drawPref = (LayerDrawable) getDrawable(R.drawable.draw_pref);
        GradientDrawable backPref = (GradientDrawable) drawPref.findDrawableByLayerId(R.id.backtemp);

        LayerDrawable drawPrefAutoTrans = (LayerDrawable) getDrawable(R.drawable.draw_pref).mutate();
        GradientDrawable backPrefAutoTrans = (GradientDrawable) drawPrefAutoTrans.findDrawableByLayerId(R.id.backtemp).mutate();

        LayerDrawable drawPrefLite = (LayerDrawable) getDrawable(R.drawable.draw_pref).mutate();
        GradientDrawable backPrefLite = (GradientDrawable) drawPrefLite.findDrawableByLayerId(R.id.backtemp).mutate();
        Drawable drawablePrefLite = drawPrefLite.findDrawableByLayerId(R.id.wPref);
        backPrefLite.setColor(getColor(R.color.light));
        drawablePrefLite.setTint(getColor(R.color.dark));
        lite.setIcon(drawPrefLite);

        LayerDrawable drawFloatIt = (LayerDrawable) getDrawable(R.drawable.draw_floatit);
        GradientDrawable backFloatIt = (GradientDrawable) drawFloatIt.findDrawableByLayerId(R.id.backtemp);

        LayerDrawable drawSupport = (LayerDrawable) getDrawable(R.drawable.draw_support);
        GradientDrawable backSupport = (GradientDrawable) drawSupport.findDrawableByLayerId(R.id.backtemp);

        backSmart.setColor(PublicVariable.primaryColor);
        backPref.setColor(PublicVariable.primaryColor);
        backPrefAutoTrans.setColor(PublicVariable.primaryColor);
        backFloatIt.setColor(PublicVariable.primaryColor);
        backPrefAutoTrans.setAlpha(functionsClass.readDefaultPreference("autoTrans", 255));
        backSupport.setColor(PublicVariable.primaryColorOpposite);

        stable.setIcon(drawPref);
        cache.setIcon(drawPref);
        autotrans.setIcon(drawPrefAutoTrans);
        floatingSplash.setIcon(drawPref);
        themeColor.setIcon(drawPref);
        sizes.setIcon(drawPref);
        delayPressHold.setIcon(drawPref);
        flingSensitivity.setIcon(drawPref);
        themeTrans.setIcon(drawPref);
        blur.setIcon(drawPref);
        stick.setIcon(drawPref);
        notification.setIcon(drawPref);

        smart.setIcon(drawSmart);
        observe.setIcon(drawSmart);
        boot.setIcon(drawSmart);
        freeForm.setIcon(drawFloatIt);

        support.setIcon(drawSupport);

        switch (sharedPreferences.getInt("iconShape", 0)) {
            case 1:
                final Drawable drawableTeardrop = getDrawable(R.drawable.droplet_icon);
                drawableTeardrop.setTint(PublicVariable.primaryColor);
                LayerDrawable layerDrawable1 = new LayerDrawable(new Drawable[]{drawableTeardrop, getDrawable(R.drawable.w_pref_gui)});
                shapes.setIcon(layerDrawable1);
                shapes.setSummary(getString(R.string.droplet));
                break;
            case 2:
                final Drawable drawableCircle = getDrawable(R.drawable.circle_icon);
                drawableCircle.setTint(PublicVariable.primaryColor);
                LayerDrawable layerDrawable2 = new LayerDrawable(new Drawable[]{drawableCircle, getDrawable(R.drawable.w_pref_gui)});
                shapes.setIcon(layerDrawable2);
                shapes.setSummary(getString(R.string.circle));
                break;
            case 3:
                final Drawable drawableSquare = getDrawable(R.drawable.square_icon);
                drawableSquare.setTint(PublicVariable.primaryColor);
                LayerDrawable layerDrawable3 = new LayerDrawable(new Drawable[]{drawableSquare, getDrawable(R.drawable.w_pref_gui)});
                shapes.setIcon(layerDrawable3);
                shapes.setSummary(getString(R.string.square));
                break;
            case 4:
                Drawable drawableSquircle = getDrawable(R.drawable.squircle_icon);
                drawableSquircle.setTint(PublicVariable.primaryColor);
                LayerDrawable layerDrawable4 = new LayerDrawable(new Drawable[]{drawableSquircle, getDrawable(R.drawable.w_pref_gui)});
                shapes.setIcon(layerDrawable4);
                shapes.setSummary(getString(R.string.squircle));
                break;
            case 0:
                Drawable drawableNoShape = getDrawable(R.drawable.w_pref_noshape);
                drawableNoShape.setTint(PublicVariable.primaryColor);
                shapes.setIcon(drawableNoShape);
                break;
        }
        if (functionsClass.loadCustomIcons()) {
            shapes.setIcon(functionsClass.appIcon(functionsClass.customIconPackageName()));
            shapes.setSummary(functionsClass.appName(functionsClass.customIconPackageName()));
        }

        if (functionsClass.UsageStatsEnabled()) {
            smart.setChecked(true);
        } else {
            smart.setChecked(false);
        }

        if (functionsClass.returnAPI() < 24) {
            observe.setSummary(getString(R.string.observeSum));
            observe.setEnabled(false);

            freeForm.setSummary(getString(R.string.observeSum));
            freeForm.setEnabled(false);
        }
        if (functionsClass.AccessibilityServiceEnabled() && functionsClass.SettingServiceRunning(InteractionObserver.class)) {
            observe.setChecked(true);
        } else {
            observe.setChecked(false);
        }

        if (functionsClass.NotificationAccess() && functionsClass.NotificationListenerRunning()) {
            notification.setChecked(true);
        } else {
            notification.setChecked(false);
        }

        if (!functionsClass.wallpaperStaticLive()) {
            blur.setEnabled(false);
        }

        if (functionsClass.freeFormSupport(getApplicationContext()) && functionsClass.FreeForm()) {
            freeForm.setChecked(true);
        } else {
            freeForm.setChecked(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        PublicVariable.eligibleLoadShowAds = false;

        functionsClass.loadSavedColor();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        functionsClass.CheckSystemRAM(SettingGUIDark.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (functionsClass.SystemCache() || functionsClass.automationFeatureEnable()) {
            startService(new Intent(getApplicationContext(), BindServices.class));
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (FromWidgetsConfigurations) {
                Intent intent = new Intent(getApplicationContext(), WidgetConfigurations.class);
                startActivity(intent);
            } else {
                if (PublicVariable.forceReload) {
                    PublicVariable.forceReload = false;
                    functionsClass.overrideBackPressToMain(SettingGUIDark.this);
                }
            }

            float finalRadius = (int) Math.hypot(functionsClass.displayX(), functionsClass.displayY());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    rootLayout, (functionsClass.displayX() / 2), (functionsClass.displayY() / 2), finalRadius, 0);

            circularReveal.setDuration(213);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                }
            });
            circularReveal.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SettingGUIDark.this.finish();
            }
        }, 700);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem facebook = menu.findItem(R.id.facebook);
        MenuItem gift = menu.findItem(R.id.donate);

        LayerDrawable drawOSP = (LayerDrawable) getDrawable(R.drawable.draw_facebook);
        GradientDrawable backOSP = (GradientDrawable) drawOSP.findDrawableByLayerId(R.id.backtemp);
        backOSP.setColor(PublicVariable.primaryColorOpposite);
        facebook.setIcon(drawOSP);

        if (!functionsClass.alreadyDonated()) {
            LayerDrawable drawableDonate = (LayerDrawable) getDrawable(R.drawable.draw_gift);
            GradientDrawable backgroundShare = (GradientDrawable) drawableDonate.findDrawableByLayerId(R.id.backtemp);
            backgroundShare.setColor(PublicVariable.primaryColorOpposite);
            gift.setIcon(drawableDonate);
        } else {
            gift.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case R.id.facebook: {
                Intent b = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_facebook_app)));
                b.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(b);

                break;
            }
            case R.id.donate: {
                startActivity(new Intent(getApplicationContext(), InAppBilling.class));

                break;
            }
            case android.R.id.home: {
                try {
                    if (PublicVariable.forceReload) {
                        PublicVariable.forceReload = false;
                        functionsClass.overrideBackPressToMain(SettingGUIDark.this);
                    }

                    float finalRadius = (int) Math.hypot(functionsClass.displayX(), functionsClass.displayY());
                    Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                            rootLayout, (functionsClass.displayX() / 2), (functionsClass.displayY() / 2), finalRadius, 0);

                    circularReveal.setDuration(213);
                    circularReveal.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            rootLayout.setVisibility(View.INVISIBLE);
                        }
                    });
                    circularReveal.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SettingGUIDark.this.finish();
                    }
                }, 700);
                break;
            }
            default: {
                result = super.onOptionsItemSelected(item);
                break;
            }
        }
        return result;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        String sticky = sharedPreferences.getString("stick", "1");
        stick = (ListPreference) findPreference("stick");
        if (sticky.equals("1")) {
            PublicVariable.forceReload = false;
            stick.setSummary(getString(R.string.leftEdge));
        } else if (sticky.equals("2")) {
            PublicVariable.forceReload = false;
            stick.setSummary(getString(R.string.rightEdge));
        }

        functionsClass.checkLightDarkTheme();
        String appTheme = sharedPreferences.getString("themeColor", "2");
        themeColor = (ListPreference) findPreference("themeColor");
        if (appTheme.equals("1")) {
            PublicVariable.forceReload = true;

            themeColor.setSummary(getString(R.string.light));
            PublicVariable.themeLightDark = true;
            startActivity(new Intent(getApplicationContext(), SettingGUILight.class));

            functionsClass.saveDefaultPreference("LitePreferences", false);
        } else if (appTheme.equals("2")) {
            PublicVariable.forceReload = true;

            themeColor.setSummary(getString(R.string.dark));
            PublicVariable.themeLightDark = false;
        } else if (appTheme.equals("3")) {
            PublicVariable.forceReload = true;

            functionsClass.checkLightDarkTheme();
            themeColor.setSummary(getString(R.string.dynamic));
            if (PublicVariable.themeLightDark) {
                startActivity(new Intent(getApplicationContext(), SettingGUILight.class));
            } else if (!PublicVariable.themeLightDark) {
            }
        }

        if (sharedPreferences.getBoolean("transparent", true) == true) {
            blur.setEnabled(true);
            if (!functionsClass.wallpaperStaticLive()) {
                blur.setChecked(false);
                blur.setEnabled(false);
            }

            functionsClass.saveDefaultPreference("LitePreferences", false);
        } else if (sharedPreferences.getBoolean("transparent", true) == false) {
            blur.setChecked(false);
            blur.setEnabled(false);
        }
    }

    /*Custom Package of Shapes/Icons*/
    public void setupShapes() {
        int currentShape = sharedPreferences.getInt("iconShape", 0);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        int dialogueWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 377, getResources().getDisplayMetrics());
        int dialogueHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 387, getResources().getDisplayMetrics());

				/*layoutParams.gravity = Gravity.TOP | Gravity.START;
				layoutParams.x = 0;
				layoutParams.y = 0;*/
        layoutParams.width = dialogueWidth;
        layoutParams.height = dialogueHeight;
        layoutParams.windowAnimations = android.R.style.Animation_Dialog;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.57f;

        final Dialog dialog = new Dialog(SettingGUIDark.this);
        dialog.setContentView(R.layout.icons_shapes_preferences);
        dialog.setTitle(Html.fromHtml("<font color='" + PublicVariable.colorLightDarkOpposite + "'>" + getString(R.string.shapedDesc) + "</font>"));
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().getDecorView().setBackgroundColor(PublicVariable.colorLightDark);
        dialog.setCancelable(true);

        final Drawable drawableTeardrop = getDrawable(R.drawable.droplet_icon);
        drawableTeardrop.setTint(PublicVariable.primaryColor);
        final Drawable drawableCircle = getDrawable(R.drawable.circle_icon);
        drawableCircle.setTint(PublicVariable.primaryColor);
        final Drawable drawableSquare = getDrawable(R.drawable.square_icon);
        drawableSquare.setTint(PublicVariable.primaryColor);
        final Drawable drawableSquircle = getDrawable(R.drawable.squircle_icon);
        drawableSquircle.setTint(PublicVariable.primaryColor);

        RelativeLayout teardropShape = (RelativeLayout) dialog.findViewById(R.id.teardropShape);
        RelativeLayout circleShape = (RelativeLayout) dialog.findViewById(R.id.circleShape);
        RelativeLayout squareShape = (RelativeLayout) dialog.findViewById(R.id.squareShape);
        RelativeLayout squircleShape = (RelativeLayout) dialog.findViewById(R.id.squircleShape);

        Button customIconPack = (Button) dialog.findViewById(R.id.customIconPack);
        Button noShape = (Button) dialog.findViewById(R.id.noShape);

        ImageView teardropImage = (ImageView) dialog.findViewById(R.id.teardropImage);
        ImageView circleImage = (ImageView) dialog.findViewById(R.id.circleImage);
        ImageView squareImage = (ImageView) dialog.findViewById(R.id.squareImage);
        ImageView squircleImage = (ImageView) dialog.findViewById(R.id.squircleImage);

        TextView teardropText = (TextView) dialog.findViewById(R.id.teardropText);
        TextView circleText = (TextView) dialog.findViewById(R.id.circleText);
        TextView squareText = (TextView) dialog.findViewById(R.id.squareText);
        TextView squircleText = (TextView) dialog.findViewById(R.id.squircleText);

        teardropImage.setImageDrawable(drawableTeardrop);
        circleImage.setImageDrawable(drawableCircle);
        squareImage.setImageDrawable(drawableSquare);
        squircleImage.setImageDrawable(drawableSquircle);

        teardropText.setTextColor(PublicVariable.colorLightDarkOpposite);
        circleText.setTextColor(PublicVariable.colorLightDarkOpposite);
        squareText.setTextColor(PublicVariable.colorLightDarkOpposite);
        squircleText.setTextColor(PublicVariable.colorLightDarkOpposite);

        customIconPack.setTextColor(PublicVariable.colorLightDarkOpposite);
        noShape.setTextColor(PublicVariable.colorLightDarkOpposite);

        teardropShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionsClass.saveDefaultPreference("customIcon", getPackageName());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("iconShape", 1);
                editor.apply();

                LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{drawableTeardrop, getDrawable(R.drawable.w_pref_gui)});
                shapes.setIcon(layerDrawable);
                shapes.setSummary(getString(R.string.droplet));

                functionsClass.saveDefaultPreference("LitePreferences", false);
                dialog.dismiss();
            }
        });
        circleShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionsClass.saveDefaultPreference("customIcon", getPackageName());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("iconShape", 2);
                editor.apply();

                LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{drawableCircle, getDrawable(R.drawable.w_pref_gui)});
                shapes.setIcon(layerDrawable);
                shapes.setSummary(getString(R.string.circle));

                functionsClass.saveDefaultPreference("LitePreferences", false);
                dialog.dismiss();
            }
        });
        squareShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionsClass.saveDefaultPreference("customIcon", getPackageName());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("iconShape", 3);
                editor.apply();

                LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{drawableSquare, getDrawable(R.drawable.w_pref_gui)});
                shapes.setIcon(layerDrawable);
                shapes.setSummary(getString(R.string.square));

                functionsClass.saveDefaultPreference("LitePreferences", false);
                dialog.dismiss();
            }
        });
        squircleShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionsClass.saveDefaultPreference("customIcon", getPackageName());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("iconShape", 4);
                editor.apply();

                LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{drawableSquircle, getDrawable(R.drawable.w_pref_gui)});
                shapes.setIcon(layerDrawable);
                shapes.setSummary(getString(R.string.squircle));

                functionsClass.saveDefaultPreference("LitePreferences", false);
                dialog.dismiss();
            }
        });

        customIconPack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                listCustomIconPack();
            }
        });

        noShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionsClass.saveDefaultPreference("customIcon", getPackageName());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("iconShape", 0);
                editor.apply();

                Drawable drawableNoShape = getDrawable(R.drawable.w_pref_noshape);
                drawableNoShape.setTint(PublicVariable.primaryColor);
                shapes.setIcon(drawableNoShape);
                shapes.setSummary("");

                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                functionsClass.addAppShortcuts();

                if (currentShape != sharedPreferences.getInt("iconShape", 0)) {
                    PublicVariable.forceReload = true;
                }

                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        dialog.show();
    }

    public void listCustomIconPack() {
        String currentCustomIconPack = sharedPreferences.getString("customIcon", getPackageName());

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        int dialogueWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 377, getResources().getDisplayMetrics());

        layoutParams.width = dialogueWidth;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.windowAnimations = android.R.style.Animation_Dialog;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.57f;

        final Dialog dialog = new Dialog(SettingGUIDark.this);
        dialog.setContentView(R.layout.custom_icons);
        dialog.setTitle(Html.fromHtml("<font color='" + PublicVariable.colorLightDarkOpposite + "'>" + getString(R.string.customIconTitle) + "</font>"));
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().getDecorView().setBackgroundColor(PublicVariable.colorLightDark);
        dialog.setCancelable(true);

        TextView defaultTheme = (TextView) dialog.findViewById(R.id.setDefault);
        RecyclerView customIconList = (RecyclerView) dialog.findViewById(R.id.customIconList);

        defaultTheme.setTextColor(PublicVariable.colorLightDarkOpposite);

        RecycleViewSmoothLayoutList recyclerViewLayoutManager = new RecycleViewSmoothLayoutList(getApplicationContext(), OrientationHelper.VERTICAL, false);
        customIconList.setLayoutManager(recyclerViewLayoutManager);
        customIconList.removeAllViews();
        final ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
        navDrawerItems.clear();
        for (String packageName : PublicVariable.customIconsPackages) {
            navDrawerItems.add(new NavDrawerItem(
                    functionsClass.appName(packageName),
                    packageName,
                    functionsClass.appIcon(packageName)
            ));
        }
        CustomIconsThemeAdapter customIconsThemeAdapter = new CustomIconsThemeAdapter(SettingGUIDark.this, getApplicationContext(), navDrawerItems);
        customIconList.setAdapter(customIconsThemeAdapter);

        defaultTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionsClass.saveDefaultPreference("customIcon", getPackageName());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("iconShape", 0);
                editor.apply();

                Drawable drawableNoShape = getDrawable(R.drawable.w_pref_noshape);
                drawableNoShape.setTint(PublicVariable.primaryColor);
                shapes.setIcon(drawableNoShape);
                shapes.setSummary("");

                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                navDrawerItems.clear();

                if (!currentCustomIconPack.equals(sharedPreferences.getString("customIcon", getPackageName()))) {
                    PublicVariable.forceReload = true;
                }

                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        dialog.show();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("CUSTOM_DIALOGUE_DISMISS");
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("CUSTOM_DIALOGUE_DISMISS")) {
                    shapes.setIcon(functionsClass.appIcon(functionsClass.customIconPackageName()));
                    shapes.setSummary(functionsClass.appName(functionsClass.customIconPackageName()));

                    functionsClass.addAppShortcuts();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("iconShape", 0);
                    editor.apply();

                    dialog.dismiss();
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    /*Fling Sensitivity*/
    public void setupFlingSensitivity() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        int dialogueWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        int dialogueHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());

        layoutParams.width = dialogueWidth;
        layoutParams.height = dialogueHeight;
        layoutParams.windowAnimations = android.R.style.Animation_Dialog;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.57f;

        final Dialog dialog = new Dialog(SettingGUIDark.this);
        dialog.setContentView(R.layout.seekbar_preferences_fling);
        dialog.setTitle(Html.fromHtml("<font color='" + PublicVariable.colorLightDarkOpposite + "'>" + getString(R.string.flingSensitivityTitle) + "</font>"));
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().getDecorView().setBackgroundColor(PublicVariable.colorLightDark);
        dialog.setCancelable(true);

        final ImageView flingingIcon = (ImageView) dialog.findViewById(R.id.transparentIcon);
        final SeekBar seekBarPreferences = (SeekBar) dialog.findViewById(R.id.seekBarPreferences);
        TextView revertDefault = (TextView) dialog.findViewById(R.id.revertDefault);

        seekBarPreferences.setThumbTintList(ColorStateList.valueOf(PublicVariable.primaryColor));
        seekBarPreferences.setThumbTintMode(PorterDuff.Mode.SRC_IN);
        seekBarPreferences.setProgressTintList(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));
        seekBarPreferences.setProgressTintMode(PorterDuff.Mode.SRC_IN);

        seekBarPreferences.setMax(50);
        seekBarPreferences.setProgress(functionsClass.readPreference("FlingSensitivity", "FlingSensitivityValueProgress", 30));

        Drawable layerDrawableLoadLogo;
        try {
            Drawable backgroundDot = functionsClass.shapesDrawables().mutate();
            backgroundDot.setTint(PublicVariable.primaryColorOpposite);
            layerDrawableLoadLogo = new LayerDrawable(new Drawable[]{
                    backgroundDot,
                    getDrawable(R.drawable.ic_launcher_dots)
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            layerDrawableLoadLogo = getDrawable(R.drawable.ic_launcher);
        }

        int iconHW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, functionsClass.readDefaultPreference("floatingSize", 39), getResources().getDisplayMetrics());
        RelativeLayout.LayoutParams layoutParamsIcon = new RelativeLayout.LayoutParams(
                iconHW,
                iconHW
        );
        layoutParamsIcon.addRule(RelativeLayout.CENTER_HORIZONTAL, R.id.seekBarView);
        flingingIcon.setLayoutParams(layoutParamsIcon);
        flingingIcon.setImageDrawable(layerDrawableLoadLogo);

        revertDefault.setTextColor(PublicVariable.colorLightDarkOpposite);

        FlingAnimation flingAnimationX = new FlingAnimation(flingingIcon, DynamicAnimation.TRANSLATION_X)
                .setFriction(functionsClass.readPreference("FlingSensitivity", "FlingSensitivityValue", 3.0f));
        FlingAnimation flingAnimationY = new FlingAnimation(flingingIcon, DynamicAnimation.TRANSLATION_Y)
                .setFriction(functionsClass.readPreference("FlingSensitivity", "FlingSensitivityValue", 3.0f));

        flingAnimationX.setMinValue(-functionsClass.DpToPixel(97));
        flingAnimationX.setMaxValue(functionsClass.DpToPixel(97));

        flingAnimationY.setMinValue(0);
        flingAnimationY.setMaxValue(functionsClass.DpToPixel(197));

        GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent motionEvent) {

                return true;
            }

            @Override
            public boolean onFling(MotionEvent motionEventFirst, MotionEvent motionEventLast, float velocityX, float velocityY) {

                flingAnimationX.setStartVelocity(velocityX);
                flingAnimationY.setStartVelocity(velocityY);

                flingAnimationX.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
                    @Override
                    public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {

                    }
                });
                flingAnimationY.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
                    @Override
                    public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {

                    }
                });

                flingAnimationX.start();
                flingAnimationY.start();

                return false;
            }
        };

        flingAnimationX.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {

            }
        });

        flingAnimationY.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {

            }
        });

        GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), simpleOnGestureListener);

        flingingIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                flingAnimationX.cancel();
                flingAnimationY.cancel();

                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        seekBarPreferences.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                functionsClass.savePreference("FlingSensitivity", "FlingSensitivityValueProgress", progress);
                if (progress <= 10) {
                    seekBarPreferences.setProgressTintList(ColorStateList.valueOf(getColor(R.color.red)));

                    functionsClass.savePreference("FlingSensitivity", "FlingSensitivityValue", 0.5f);
                    try {
                        flingAnimationX.setFriction(0.5f);
                        flingAnimationY.setFriction(0.5f);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    seekBarPreferences.setProgressTintList(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));

                    functionsClass.savePreference("FlingSensitivity", "FlingSensitivityValue", (float) (progress / 10));
                    try {
                        flingAnimationX.setFriction(functionsClass.readPreference("FlingSensitivity", "FlingSensitivityValue", 3.0f));
                        flingAnimationY.setFriction(functionsClass.readPreference("FlingSensitivity", "FlingSensitivityValue", 3.0f));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        revertDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionsClass.savePreference("FlingSensitivity", "FlingSensitivityValueProgress", 30);
                functionsClass.savePreference("FlingSensitivity", "FlingSensitivityValue", 3.0f);

                seekBarPreferences.setProgress(30);
                seekBarPreferences.setProgressTintList(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });

        dialog.show();
    }
}
