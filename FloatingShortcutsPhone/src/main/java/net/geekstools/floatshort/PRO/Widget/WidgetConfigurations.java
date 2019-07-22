package net.geekstools.floatshort.PRO.Widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import net.geekstools.floatshort.PRO.Automation.Apps.AppAutoFeatures;
import net.geekstools.floatshort.PRO.Category.CategoryHandler;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Shortcuts.HybridViewOff;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.NavAdapter.RecycleViewSmoothLayoutGrid;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryCategory;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryShortcuts;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryWidgets;
import net.geekstools.floatshort.PRO.Util.SettingGUI.SettingGUIDark;
import net.geekstools.floatshort.PRO.Util.SettingGUI.SettingGUILight;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;
import net.geekstools.floatshort.PRO.Util.UI.SimpleGestureFilterSwitch;
import net.geekstools.floatshort.PRO.Widget.NavAdapter.ConfiguredWidgetsAdapter;
import net.geekstools.floatshort.PRO.Widget.NavAdapter.InstalledWidgetsAdapter;
import net.geekstools.floatshort.PRO.Widget.NavAdapter.WidgetSectionedGridRecyclerViewAdapter;
import net.geekstools.floatshort.PRO.Widget.RoomDatabase.WidgetDataInterface;
import net.geekstools.floatshort.PRO.Widget.RoomDatabase.WidgetDataModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class WidgetConfigurations extends Activity implements SimpleGestureFilterSwitch.SimpleGestureListener {

    FunctionsClass functionsClass;

    RelativeLayout wholeWidget, fullActionViews;
    ImageView addWidget,
            actionButton, recoverFloatingCategories, recoverFloatingApps;
    MaterialButton switchApps, switchCategories, recoveryAction, automationAction;

    RecyclerView installedWidgetsLoadView, configuredWidgetsLoadView;
    ScrollView installedWidgetsNestedScrollView, configuredWidgetsNestedScrollView,
            nestedIndexScrollView, installedNestedIndexScrollView;

    LinearLayout indexView, indexViewInstalled;
    TextView popupIndex;

    Map<String, Integer> mapIndexFirstItem, mapIndexLastItem,
            mapIndexFirstItemInstalled, mapIndexLastItemInstalled;
    Map<Integer, String> mapRangeIndex,
            mapRangeIndexInstalled;
    NavigableMap<String, Integer> indexItems, indexItemsInstalled;
    List<String> indexListConfigured, indexListInstalled;


    List<WidgetSectionedGridRecyclerViewAdapter.Section> installedWidgetsSections, configuredWidgetsSections;
    RecyclerView.Adapter installedWidgetsRecyclerViewAdapter, configuredWidgetsRecyclerViewAdapter;
    WidgetSectionedGridRecyclerViewAdapter configuredWidgetsSectionedGridRecyclerViewAdapter;
    GridLayoutManager installedWidgetsRecyclerViewLayoutManager, configuredWidgetsRecyclerViewLayoutManager;

    RelativeLayout loadingSplash;
    ProgressBar loadingBarLTR;
    TextView gx;

    List<AppWidgetProviderInfo> widgetProviderInfoList;
    ArrayList<NavDrawerItem> installedWidgetsNavDrawerItems, configuredWidgetsNavDrawerItems;

    AppWidgetManager appWidgetManager;
    AppWidgetHost appWidgetHost;

    LoadCustomIcons loadCustomIcons;

    SimpleGestureFilterSwitch simpleGestureFilterSwitch;

    FirebaseRemoteConfig firebaseRemoteConfig;

    boolean installedWidgetsLoaded = false, configuredWidgetAvailable = false;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.widget_configurations_views);

        functionsClass = new FunctionsClass(getApplicationContext(), WidgetConfigurations.this);

        wholeWidget = (RelativeLayout) findViewById(R.id.wholeWidget);
        fullActionViews = (RelativeLayout) findViewById(R.id.fullActionViews);

        installedWidgetsNestedScrollView = (ScrollView) findViewById(R.id.installedNestedScrollView);
        installedWidgetsLoadView = (RecyclerView) findViewById(R.id.installedWidgetList);

        configuredWidgetsNestedScrollView = (ScrollView) findViewById(R.id.configuredWidgetNestedScrollView);
        configuredWidgetsLoadView = (RecyclerView) findViewById(R.id.configuredWidgetList);

        popupIndex = (TextView) findViewById(R.id.popupIndex);

        addWidget = (ImageView) findViewById(R.id.addWidget);
        actionButton = (ImageView) findViewById(R.id.actionButton);
        switchApps = (MaterialButton) findViewById(R.id.switchApps);
        switchCategories = (MaterialButton) findViewById(R.id.switchCategories);
        recoveryAction = (MaterialButton) findViewById(R.id.recoveryAction);
        automationAction = (MaterialButton) findViewById(R.id.automationAction);
        recoverFloatingCategories = (ImageView) findViewById(R.id.recoverFloatingCategories);
        recoverFloatingApps = (ImageView) findViewById(R.id.recoverFloatingApps);

        nestedIndexScrollView = (ScrollView) findViewById(R.id.nestedIndexScrollView);
        installedNestedIndexScrollView = (ScrollView) findViewById(R.id.installedNestedIndexScrollView);

        indexView = (LinearLayout) findViewById(R.id.side_index);
        indexViewInstalled = (LinearLayout) findViewById(R.id.installed_side_index);

        simpleGestureFilterSwitch = new SimpleGestureFilterSwitch(getApplicationContext(), this);

        TextView widgetPickerTitle = (TextView) findViewById(R.id.widgetPickerTitle);
        widgetPickerTitle.setText(Html.fromHtml(getString(R.string.widgetPickerTitle)));
        widgetPickerTitle.setTextColor(PublicVariable.themeLightDark ? getColor(R.color.dark) : getColor(R.color.light));

        installedWidgetsRecyclerViewLayoutManager = new RecycleViewSmoothLayoutGrid(getApplicationContext(), functionsClass.columnCount(190), OrientationHelper.VERTICAL, false);
        installedWidgetsLoadView.setLayoutManager(installedWidgetsRecyclerViewLayoutManager);

        installedWidgetsSections = new ArrayList<WidgetSectionedGridRecyclerViewAdapter.Section>();

        configuredWidgetsRecyclerViewLayoutManager = new RecycleViewSmoothLayoutGrid(getApplicationContext(), functionsClass.columnCount(190), OrientationHelper.VERTICAL, false);
        configuredWidgetsLoadView.setLayoutManager(configuredWidgetsRecyclerViewLayoutManager);

        configuredWidgetsSections = new ArrayList<WidgetSectionedGridRecyclerViewAdapter.Section>();

        if (functionsClass.appThemeTransparent() == true) {
            functionsClass.setThemeColorFloating(wholeWidget, true);
        } else {
            functionsClass.setThemeColorFloating(wholeWidget, false);
        }

        appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetHost = new AppWidgetHost(getApplicationContext(), (int) System.currentTimeMillis());

        installedWidgetsNavDrawerItems = new ArrayList<NavDrawerItem>();
        configuredWidgetsNavDrawerItems = new ArrayList<NavDrawerItem>();
        indexListConfigured = new ArrayList<String>();
        indexListInstalled = new ArrayList<String>();
        mapIndexFirstItem = new LinkedHashMap<String, Integer>();
        mapIndexFirstItemInstalled = new LinkedHashMap<String, Integer>();
        mapIndexLastItem = new LinkedHashMap<String, Integer>();
        mapIndexLastItemInstalled = new LinkedHashMap<String, Integer>();
        mapRangeIndex = new LinkedHashMap<Integer, String>();
        mapRangeIndexInstalled = new LinkedHashMap<Integer, String>();
        indexItems = new TreeMap<String, Integer>();
        indexItemsInstalled = new TreeMap<String, Integer>();

        if (functionsClass.loadCustomIcons()) {
            loadCustomIcons = new LoadCustomIcons(getApplicationContext(), functionsClass.customIconPackageName());
        }

        if (getDatabasePath(PublicVariable.WIDGET_DATA_DATABASE_NAME).exists()) {
            LoadConfiguredWidgets loadConfiguredWidgets = new LoadConfiguredWidgets();
            loadConfiguredWidgets.execute();
        } else {
            actionButton.bringToFront();
            addWidget.bringToFront();

            addWidget.animate().scaleXBy(0.23f).scaleYBy(0.23f).setDuration(223).setListener(scaleUpListener);

            loadingSplash = (RelativeLayout) findViewById(R.id.loadingSplash);
            if (functionsClass.appThemeTransparent() == true) {
                loadingSplash.setBackgroundColor(Color.TRANSPARENT);
            } else {
                loadingSplash.setBackgroundColor(getWindow().getNavigationBarColor());
            }

            loadingBarLTR = (ProgressBar) findViewById(R.id.loadingProgressltr);
            gx = (TextView) findViewById(R.id.gx);
            Typeface face = Typeface.createFromAsset(getAssets(), "upcil.ttf");
            gx.setTypeface(face);

            if (PublicVariable.themeLightDark) {
                loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.themeTextColor, android.graphics.PorterDuff.Mode.MULTIPLY);
                gx.setTextColor(getColor(R.color.dark));
            } else if (!PublicVariable.themeLightDark) {
                loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
                gx.setTextColor(getColor(R.color.light));
            }
        }

        LayerDrawable drawAddWidget = (LayerDrawable) getDrawable(R.drawable.draw_pref_add_widget);
        GradientDrawable backAddWidget = (GradientDrawable) drawAddWidget.findDrawableByLayerId(R.id.backtemp);
        Drawable frontAddWidget = drawAddWidget.findDrawableByLayerId(R.id.frontTemp).mutate();
        backAddWidget.setColor(PublicVariable.primaryColor);
        frontAddWidget.setTint(getColor(R.color.light));
        addWidget.setImageDrawable(drawAddWidget);

        LayerDrawable drawPreferenceAction = (LayerDrawable) getDrawable(R.drawable.draw_pref_action);
        GradientDrawable backPreferenceAction = (GradientDrawable) drawPreferenceAction.findDrawableByLayerId(R.id.backtemp);
        backPreferenceAction.setColor(PublicVariable.primaryColorOpposite);
        actionButton.setImageDrawable(drawPreferenceAction);

        switchApps.setTextColor(getColor(R.color.light));
        switchCategories.setTextColor(getColor(R.color.light));
        if (PublicVariable.themeLightDark /*light*/ && functionsClass.appThemeTransparent() /*transparent*/) {
            switchApps.setTextColor(getColor(R.color.dark));
            switchCategories.setTextColor(getColor(R.color.dark));
        }

        switchCategories.setBackgroundColor(functionsClass.appThemeTransparent() ? functionsClass.setColorAlpha(PublicVariable.primaryColor, 51) : PublicVariable.primaryColor);
        switchCategories.setRippleColor(ColorStateList.valueOf(functionsClass.appThemeTransparent() ? functionsClass.setColorAlpha(PublicVariable.primaryColorOpposite, 51) : PublicVariable.primaryColorOpposite));

        switchApps.setBackgroundColor(functionsClass.appThemeTransparent() ? functionsClass.setColorAlpha(PublicVariable.primaryColor, 51) : PublicVariable.primaryColor);
        switchApps.setRippleColor(ColorStateList.valueOf(functionsClass.appThemeTransparent() ? functionsClass.setColorAlpha(PublicVariable.primaryColorOpposite, 51) : PublicVariable.primaryColorOpposite));

        recoveryAction.setBackgroundColor(PublicVariable.primaryColorOpposite);
        recoveryAction.setRippleColor(ColorStateList.valueOf(PublicVariable.primaryColor));

        automationAction.setBackgroundColor(PublicVariable.primaryColorOpposite);
        automationAction.setRippleColor(ColorStateList.valueOf(PublicVariable.primaryColor));

        try {
            LayerDrawable drawRecoverFloatingCategories = (LayerDrawable) getDrawable(R.drawable.draw_recovery).mutate();
            GradientDrawable backRecoverFloatingCategories = (GradientDrawable) drawRecoverFloatingCategories.findDrawableByLayerId(R.id.backtemp).mutate();
            backRecoverFloatingCategories.setColor(functionsClass.appThemeTransparent() ? functionsClass.setColorAlpha(PublicVariable.primaryColor, 51) : PublicVariable.primaryColor);

            recoverFloatingCategories.setImageDrawable(drawRecoverFloatingCategories);
            recoverFloatingApps.setImageDrawable(drawRecoverFloatingCategories);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionsClass.doVibrate(33);

                if (PublicVariable.actionCenter == false) {
                    if (installedWidgetsNestedScrollView.isShown()) {
                        installedNestedIndexScrollView.setVisibility(View.INVISIBLE);

                        if (!configuredWidgetAvailable) {
                            addWidget.animate().scaleXBy(0.23f).scaleYBy(0.23f).setDuration(223).setListener(scaleUpListener);
                        }

                        ViewCompat.animate(addWidget)
                                .rotation(0.0F)
                                .withLayer()
                                .setDuration(300L)
                                .setInterpolator(new OvershootInterpolator(3.0f))
                                .start();

                        int xPosition = (int) (addWidget.getX() + (addWidget.getWidth() / 2));
                        int yPosition = (int) (addWidget.getY() + (addWidget.getHeight() / 2));

                        int startRadius = 0;
                        int endRadius = (int) Math.hypot(functionsClass.displayX(), functionsClass.displayY());

                        Animator circularReveal = ViewAnimationUtils.createCircularReveal(installedWidgetsNestedScrollView, xPosition, yPosition, endRadius, startRadius);
                        circularReveal.setDuration(864);
                        circularReveal.start();
                        circularReveal.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                installedWidgetsNestedScrollView.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });

                        if (functionsClass.appThemeTransparent() == true) {
                            final Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            if (PublicVariable.themeLightDark) {
                                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                                if (functionsClass.returnAPI() > 25) {
                                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                                }
                            }

                            ValueAnimator valueAnimator = ValueAnimator
                                    .ofArgb(getWindow().getNavigationBarColor(), functionsClass.setColorAlpha(functionsClass.mixColors(PublicVariable.primaryColor, PublicVariable.colorLightDark, 0.03f), 180));
                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animator) {
                                    window.setStatusBarColor((Integer) animator.getAnimatedValue());
                                    window.setNavigationBarColor((Integer) animator.getAnimatedValue());
                                }
                            });
                            valueAnimator.start();
                        } else {
                            final Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            if (PublicVariable.themeLightDark) {
                                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                                if (functionsClass.returnAPI() > 25) {
                                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                                }
                            }

                            ValueAnimator colorAnimation = ValueAnimator
                                    .ofArgb(getWindow().getNavigationBarColor(), PublicVariable.colorLightDark);
                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animator) {
                                    getWindow().setNavigationBarColor((Integer) animator.getAnimatedValue());
                                    getWindow().setStatusBarColor((Integer) animator.getAnimatedValue());
                                }
                            });
                            colorAnimation.start();
                        }
                    }

                    int finalRadius = (int) Math.hypot(functionsClass.displayX(), functionsClass.displayY());
                    Animator circularReveal = ViewAnimationUtils.createCircularReveal(recoveryAction, (int) actionButton.getX(), (int) actionButton.getY(), finalRadius, functionsClass.DpToInteger(13));
                    circularReveal.setDuration(777);
                    circularReveal.setInterpolator(new AccelerateInterpolator());
                    circularReveal.start();
                    circularReveal.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            recoveryAction.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    functionsClass.openActionMenuOption(fullActionViews, actionButton, fullActionViews.isShown());
                } else {
                    recoveryAction.setVisibility(View.VISIBLE);

                    int finalRadius = (int) Math.hypot(functionsClass.displayX(), functionsClass.displayY());
                    Animator circularReveal = ViewAnimationUtils.createCircularReveal(recoveryAction, (int) actionButton.getX(), (int) actionButton.getY(), functionsClass.DpToInteger(13), finalRadius);
                    circularReveal.setDuration(1300);
                    circularReveal.setInterpolator(new AccelerateInterpolator());
                    circularReveal.start();
                    circularReveal.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            recoveryAction.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    functionsClass.closeActionMenuOption(fullActionViews, actionButton);
                }
            }
        });
        switchCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    functionsClass.navigateToClass(CategoryHandler.class,
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_left, R.anim.slide_to_right));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        switchApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HybridViewOff.class),
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_right, R.anim.slide_to_left).toBundle());
            }
        });
        automationAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionsClass.doVibrate(50);

                Intent intent = new Intent(getApplicationContext(), AppAutoFeatures.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent, ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.up_down, android.R.anim.fade_out).toBundle());
            }
        });
        recoveryAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecoveryWidgets.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);
            }
        });
        recoverFloatingCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecoveryCategory.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.recovery_actions_hide);
                recoverFloatingCategories.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        recoverFloatingCategories.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
        recoverFloatingApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecoveryShortcuts.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.recovery_actions_hide);
                recoverFloatingApps.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        recoverFloatingApps.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        actionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(WidgetConfigurations.this, actionButton, "transition");
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (PublicVariable.themeLightDark) {
                            intent.setClass(WidgetConfigurations.this, SettingGUILight.class);
                        } else if (!PublicVariable.themeLightDark) {
                            intent.setClass(WidgetConfigurations.this, SettingGUIDark.class);
                        }
                        startActivity(intent, activityOptionsCompat.toBundle());
                    }
                }, 113);

                return true;
            }
        });
        switchCategories.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!recoverFloatingCategories.isShown()) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.recovery_actions_show);
                    recoverFloatingCategories.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            recoverFloatingCategories.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                } else {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.recovery_actions_hide);
                    recoverFloatingCategories.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            recoverFloatingCategories.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }

                return true;
            }
        });
        switchApps.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!recoverFloatingApps.isShown()) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.recovery_actions_show);
                    recoverFloatingApps.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            recoverFloatingApps.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                } else {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.recovery_actions_hide);
                    recoverFloatingApps.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            recoverFloatingApps.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }

                return true;
            }
        });

        ImageView floatingLogo = (ImageView) findViewById(R.id.loadLogo);
        LayerDrawable drawFloatingLogo = (LayerDrawable) getDrawable(R.drawable.draw_floating_widgets);
        GradientDrawable backFloatingLogo = (GradientDrawable) drawFloatingLogo.findDrawableByLayerId(R.id.backtemp);
        backFloatingLogo.setColor(PublicVariable.primaryColorOpposite);
        floatingLogo.setImageDrawable(drawFloatingLogo);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("FORCE_RELOAD");
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("FORCE_RELOAD")) {
                    try {
                        configuredWidgetsNavDrawerItems.clear();
                        configuredWidgetsSections.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    LoadConfiguredWidgets loadConfiguredWidgets = new LoadConfiguredWidgets();
                    loadConfiguredWidgets.execute();
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onStart() {
        super.onStart();

        addWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionsClass.doVibrate(77);

                if (installedWidgetsNestedScrollView.isShown()) {
                    installedNestedIndexScrollView.setVisibility(View.INVISIBLE);

                    if (!configuredWidgetAvailable) {
                        addWidget.animate().scaleXBy(0.23f).scaleYBy(0.23f).setDuration(223).setListener(scaleUpListener);
                    }

                    ViewCompat.animate(addWidget)
                            .rotation(0.0F)
                            .withLayer()
                            .setDuration(300L)
                            .setInterpolator(new OvershootInterpolator(3.0f))
                            .start();

                    int xPosition = (int) (addWidget.getX() + (addWidget.getWidth() / 2));
                    int yPosition = (int) (addWidget.getY() + (addWidget.getHeight() / 2));

                    int startRadius = 0;
                    int endRadius = (int) Math.hypot(functionsClass.displayX(), functionsClass.displayY());

                    Animator circularReveal = ViewAnimationUtils.createCircularReveal(installedWidgetsNestedScrollView, xPosition, yPosition, endRadius, startRadius);
                    circularReveal.setDuration(864);
                    circularReveal.start();
                    circularReveal.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            installedWidgetsNestedScrollView.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

                    if (functionsClass.appThemeTransparent() == true) {
                        final Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        if (PublicVariable.themeLightDark) {
                            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                            if (functionsClass.returnAPI() > 25) {
                                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                            }
                        }

                        ValueAnimator valueAnimator = ValueAnimator
                                .ofArgb(getWindow().getNavigationBarColor(), functionsClass.setColorAlpha(functionsClass.mixColors(PublicVariable.primaryColor, PublicVariable.colorLightDark, 0.03f), 180));
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                window.setStatusBarColor((Integer) animator.getAnimatedValue());
                                window.setNavigationBarColor((Integer) animator.getAnimatedValue());
                            }
                        });
                        valueAnimator.start();
                    } else {
                        final Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        if (PublicVariable.themeLightDark) {
                            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                            if (functionsClass.returnAPI() > 25) {
                                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                            }
                        }

                        ValueAnimator colorAnimation = ValueAnimator
                                .ofArgb(getWindow().getNavigationBarColor(), PublicVariable.colorLightDark);
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                getWindow().setNavigationBarColor((Integer) animator.getAnimatedValue());
                                getWindow().setStatusBarColor((Integer) animator.getAnimatedValue());
                            }
                        });
                        colorAnimation.start();
                    }
                } else {
                    if (PublicVariable.actionCenter) {
                        recoveryAction.setVisibility(View.VISIBLE);

                        int finalRadius = (int) Math.hypot(functionsClass.displayX(), functionsClass.displayY());
                        Animator circularReveal = ViewAnimationUtils.createCircularReveal(recoveryAction, (int) actionButton.getX(), (int) actionButton.getY(), functionsClass.DpToInteger(13), finalRadius);
                        circularReveal.setDuration(1300);
                        circularReveal.setInterpolator(new AccelerateInterpolator());
                        circularReveal.start();
                        circularReveal.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                recoveryAction.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });

                        functionsClass.closeActionMenuOption(fullActionViews, actionButton);
                    }

                    LoadInstalledWidgets loadInstalledWidgets = new LoadInstalledWidgets();
                    loadInstalledWidgets.execute();
                }
            }
        });

        addWidget.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                int appWidgetId = appWidgetHost.allocateAppWidgetId();
                Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
                pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                ArrayList<AppWidgetProviderInfo> appWidgetProviderInfos = new ArrayList<AppWidgetProviderInfo>();
                pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, appWidgetProviderInfos);
                ArrayList<Bundle> bundleArrayList = new ArrayList<Bundle>();
                pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, bundleArrayList);
                startActivityForResult(pickIntent, InstalledWidgetsAdapter.SYSTEM_WIDGET_PICKER);

                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_default);
        firebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(WidgetConfigurations.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.activate().addOnSuccessListener(new OnSuccessListener<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    if (firebaseRemoteConfig.getLong(functionsClass.versionCodeRemoteConfigKey()) > functionsClass.appVersionCode(getPackageName())) {
                                        functionsClass.notificationCreator(
                                                getString(R.string.updateAvailable),
                                                firebaseRemoteConfig.getString(functionsClass.upcomingChangeLogSummaryConfigKey()),
                                                (int) firebaseRemoteConfig.getLong(functionsClass.versionCodeRemoteConfigKey())
                                        );
                                    } else {

                                    }
                                }
                            });
                        } else {

                        }
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (PublicVariable.actionCenter == true) {
            functionsClass.closeActionMenuOption(fullActionViews, actionButton);
        }
    }

    @Override
    public void onBackPressed() {
        if (installedWidgetsNestedScrollView.isShown()) {
            functionsClass.doVibrate(77);
            installedNestedIndexScrollView.setVisibility(View.INVISIBLE);

            if (!configuredWidgetAvailable) {
                addWidget.animate().scaleXBy(0.23f).scaleYBy(0.23f).setDuration(223).setListener(scaleUpListener);
            }

            ViewCompat.animate(addWidget)
                    .rotation(0.0F)
                    .withLayer()
                    .setDuration(300L)
                    .setInterpolator(new OvershootInterpolator(3.0f))
                    .start();

            int xPosition = (int) (addWidget.getX() + (addWidget.getWidth() / 2));
            int yPosition = (int) (addWidget.getY() + (addWidget.getHeight() / 2));

            int startRadius = 0;
            int endRadius = (int) Math.hypot(functionsClass.displayX(), functionsClass.displayY());

            Animator circularReveal = ViewAnimationUtils.createCircularReveal(installedWidgetsNestedScrollView, xPosition, yPosition, endRadius, startRadius);
            circularReveal.setDuration(864);
            circularReveal.start();
            circularReveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    installedWidgetsNestedScrollView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            if (functionsClass.appThemeTransparent() == true) {
                final Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (PublicVariable.themeLightDark) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    if (functionsClass.returnAPI() > 25) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                    }
                }

                ValueAnimator valueAnimator = ValueAnimator
                        .ofArgb(getWindow().getNavigationBarColor(), functionsClass.setColorAlpha(functionsClass.mixColors(PublicVariable.primaryColor, PublicVariable.colorLightDark, 0.03f), 180));
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        window.setStatusBarColor((Integer) animator.getAnimatedValue());
                        window.setNavigationBarColor((Integer) animator.getAnimatedValue());
                    }
                });
                valueAnimator.start();
            } else {
                final Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (PublicVariable.themeLightDark) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    if (functionsClass.returnAPI() > 25) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                    }
                }

                ValueAnimator colorAnimation = ValueAnimator
                        .ofArgb(getWindow().getNavigationBarColor(), PublicVariable.colorLightDark);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        getWindow().setNavigationBarColor((Integer) animator.getAnimatedValue());
                        getWindow().setStatusBarColor((Integer) animator.getAnimatedValue());
                    }
                });
                colorAnimation.start();
            }
        } else {
            try {
                functionsClass.overrideBackPressToMain(WidgetConfigurations.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSwipe(int direction) {
        switch (direction) {
            case SimpleGestureFilterSwitch.SWIPE_RIGHT: {
                try {
                    functionsClass.navigateToClass(CategoryHandler.class,
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_left, R.anim.slide_to_right));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case SimpleGestureFilterSwitch.SWIPE_LEFT: {
                try {
                    functionsClass.navigateToClass(HybridViewOff.class,
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_right, R.anim.slide_to_left));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        this.simpleGestureFilterSwitch.onTouchEvent(motionEvent);

        return super.dispatchTouchEvent(motionEvent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case InstalledWidgetsAdapter.WIDGET_CONFIGURATION_REQUEST: {

                    Bundle dataExtras = data.getExtras();
                    int appWidgetId = dataExtras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            WidgetDataModel widgetDataModel = new WidgetDataModel(
                                    appWidgetId,
                                    InstalledWidgetsAdapter.pickedWidgetPackageName,
                                    functionsClass.appName(InstalledWidgetsAdapter.pickedWidgetPackageName),
                                    InstalledWidgetsAdapter.pickedWidgetLabel,
                                    false
                            );

                            WidgetDataInterface widgetDataInterface = Room.databaseBuilder(getApplicationContext(), WidgetDataInterface.class, PublicVariable.WIDGET_DATA_DATABASE_NAME)
                                    .fallbackToDestructiveMigration()
                                    .addCallback(new RoomDatabase.Callback() {
                                        @Override
                                        public void onCreate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                                            super.onCreate(supportSQLiteDatabase);
                                        }

                                        @Override
                                        public void onOpen(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                                            super.onOpen(supportSQLiteDatabase);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    LoadConfiguredWidgets loadConfiguredWidgets = new LoadConfiguredWidgets();
                                                    loadConfiguredWidgets.execute();
                                                }
                                            });
                                        }
                                    })
                                    .build();
                            widgetDataInterface.initDataAccessObject().insertNewWidgetData(widgetDataModel);
                            widgetDataInterface.close();
                        }
                    }).start();

                    break;
                }
                case InstalledWidgetsAdapter.SYSTEM_WIDGET_PICKER: {

                    Bundle extras = data.getExtras();
                    int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
                    AppWidgetProviderInfo appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId);
                    if (appWidgetInfo.configure != null) {

                        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
                        intent.setComponent(appWidgetInfo.configure);
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                        startActivityForResult(intent, InstalledWidgetsAdapter.SYSTEM_WIDGET_PICKER_CONFIGURATION);

                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                WidgetDataModel widgetDataModel = new WidgetDataModel(
                                        appWidgetId,
                                        InstalledWidgetsAdapter.pickedWidgetPackageName,
                                        functionsClass.appName(InstalledWidgetsAdapter.pickedWidgetPackageName),
                                        InstalledWidgetsAdapter.pickedWidgetLabel,
                                        false
                                );

                                WidgetDataInterface widgetDataInterface = Room.databaseBuilder(getApplicationContext(), WidgetDataInterface.class, PublicVariable.WIDGET_DATA_DATABASE_NAME)
                                        .fallbackToDestructiveMigration()
                                        .addCallback(new RoomDatabase.Callback() {
                                            @Override
                                            public void onCreate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                                                super.onCreate(supportSQLiteDatabase);
                                            }

                                            @Override
                                            public void onOpen(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                                                super.onOpen(supportSQLiteDatabase);

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        LoadConfiguredWidgets loadConfiguredWidgets = new LoadConfiguredWidgets();
                                                        loadConfiguredWidgets.execute();
                                                    }
                                                });
                                            }
                                        })
                                        .build();
                                widgetDataInterface.initDataAccessObject().insertNewWidgetData(widgetDataModel);
                                widgetDataInterface.close();
                            }
                        }).start();
                    }

                    break;
                }
                case InstalledWidgetsAdapter.SYSTEM_WIDGET_PICKER_CONFIGURATION: {
                    Bundle extras = data.getExtras();
                    int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            WidgetDataModel widgetDataModel = new WidgetDataModel(
                                    appWidgetId,
                                    InstalledWidgetsAdapter.pickedWidgetPackageName,
                                    functionsClass.appName(InstalledWidgetsAdapter.pickedWidgetPackageName),
                                    InstalledWidgetsAdapter.pickedWidgetLabel,
                                    false
                            );

                            WidgetDataInterface widgetDataInterface = Room.databaseBuilder(getApplicationContext(), WidgetDataInterface.class, PublicVariable.WIDGET_DATA_DATABASE_NAME)
                                    .fallbackToDestructiveMigration()
                                    .addCallback(new RoomDatabase.Callback() {
                                        @Override
                                        public void onCreate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                                            super.onCreate(supportSQLiteDatabase);
                                        }

                                        @Override
                                        public void onOpen(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                                            super.onOpen(supportSQLiteDatabase);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    LoadConfiguredWidgets loadConfiguredWidgets = new LoadConfiguredWidgets();
                                                    loadConfiguredWidgets.execute();
                                                }
                                            });
                                        }
                                    })
                                    .build();
                            widgetDataInterface.initDataAccessObject().insertNewWidgetData(widgetDataModel);
                            widgetDataInterface.close();
                        }
                    }).start();

                    break;
                }
            }
        }

    }

    public class LoadConfiguredWidgets extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                configuredWidgetsNavDrawerItems.clear();
                configuredWidgetsSections.clear();
                configuredWidgetsLoadView.removeAllViews();
            } catch (Exception e) {
                e.printStackTrace();
            }

            loadingSplash = (RelativeLayout) findViewById(R.id.loadingSplash);
            if (functionsClass.appThemeTransparent() == true) {
                loadingSplash.setBackgroundColor(Color.TRANSPARENT);
            } else {
                loadingSplash.setBackgroundColor(getWindow().getNavigationBarColor());
            }

            loadingBarLTR = (ProgressBar) findViewById(R.id.loadingProgressltr);
            gx = (TextView) findViewById(R.id.gx);
            Typeface face = Typeface.createFromAsset(getAssets(), "upcil.ttf");
            gx.setTypeface(face);

            if (PublicVariable.themeLightDark) {
                loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.themeTextColor, android.graphics.PorterDuff.Mode.MULTIPLY);
                gx.setTextColor(getColor(R.color.dark));
            } else if (!PublicVariable.themeLightDark) {
                loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
                gx.setTextColor(getColor(R.color.light));
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            configuredWidgetsNavDrawerItems.clear();
            configuredWidgetsSections.clear();

            configuredWidgetAvailable = false;
            try {
                if (functionsClass.loadCustomIcons()) {
                    loadCustomIcons.load();
                    FunctionsClass.println("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIcons());
                }

                WidgetDataInterface widgetDataInterface = Room.databaseBuilder(getApplicationContext(), WidgetDataInterface.class, PublicVariable.WIDGET_DATA_DATABASE_NAME)
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

                List<WidgetDataModel> widgetDataModels = widgetDataInterface.initDataAccessObject().getAllWidgetData();
                if (widgetDataModels.size() > 0) {
                    configuredWidgetAvailable = true;
                } else {
                    return false;
                }

                String oldAppName = "";
                int widgetIndex = 0;
                for (WidgetDataModel widgetDataModel : widgetDataModels) {
                    try {
                        int appWidgetId = widgetDataModel.getWidgetId();

                        AppWidgetProviderInfo appWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId);
                        String packageName = widgetDataModel.getPackageName();
                        String newAppName = functionsClass.appName(packageName);
                        Drawable appIcon = functionsClass.loadCustomIcons() ? loadCustomIcons.getDrawableIconForPackage(packageName, functionsClass.shapedAppIcon(packageName)) : functionsClass.shapedAppIcon(packageName);

                        if (widgetIndex == 0) {
                            configuredWidgetsSections.add(new WidgetSectionedGridRecyclerViewAdapter.Section(widgetIndex, newAppName, appIcon));
                            indexListConfigured.add(newAppName.substring(0, 1).toUpperCase());
                        } else {
                            if (!oldAppName.equals(newAppName)) {
                                configuredWidgetsSections.add(new WidgetSectionedGridRecyclerViewAdapter.Section(widgetIndex, newAppName, appIcon));
                                indexListConfigured.add(newAppName.substring(0, 1).toUpperCase());
                            }
                        }

                        oldAppName = functionsClass.appName(packageName);

                        indexListConfigured.add(newAppName.substring(0, 1).toUpperCase());
                        configuredWidgetsNavDrawerItems.add(new NavDrawerItem(
                                newAppName,
                                packageName,
                                widgetDataModel.getWidgetLabel(),
                                appIcon,
                                appWidgetProviderInfo,
                                appWidgetId,
                                widgetDataModel.getRecovery()
                        ));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    widgetIndex++;
                }

                configuredWidgetsRecyclerViewAdapter = new ConfiguredWidgetsAdapter(WidgetConfigurations.this, getApplicationContext(), configuredWidgetsNavDrawerItems, appWidgetManager, appWidgetHost);

                widgetDataInterface.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return configuredWidgetAvailable;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                configuredWidgetsRecyclerViewAdapter.notifyDataSetChanged();
                WidgetSectionedGridRecyclerViewAdapter.Section[] sectionsData = new WidgetSectionedGridRecyclerViewAdapter.Section[configuredWidgetsSections.size()];
                configuredWidgetsSectionedGridRecyclerViewAdapter = new WidgetSectionedGridRecyclerViewAdapter(
                        getApplicationContext(),
                        R.layout.widgets_sections,
                        configuredWidgetsLoadView,
                        configuredWidgetsRecyclerViewAdapter
                );
                configuredWidgetsSectionedGridRecyclerViewAdapter.setSections(configuredWidgetsSections.toArray(sectionsData));
                configuredWidgetsSectionedGridRecyclerViewAdapter.notifyDataSetChanged();
                configuredWidgetsLoadView.setAdapter(configuredWidgetsSectionedGridRecyclerViewAdapter);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        configuredWidgetsNestedScrollView.scrollTo(0, 0);

                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                        loadingSplash.setVisibility(View.INVISIBLE);
                        loadingSplash.startAnimation(animation);
                    }
                }, 333);
            } else {
                installedWidgetsLoaded = false;
                addWidget.animate().scaleXBy(0.23f).scaleYBy(0.23f).setDuration(223).setListener(scaleUpListener);
                loadingSplash.setVisibility(View.VISIBLE);
                try {
                    configuredWidgetsRecyclerViewAdapter.notifyDataSetChanged();
                    configuredWidgetsSectionedGridRecyclerViewAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            LoadApplicationsIndexConfigured loadApplicationsIndexConfigured = new LoadApplicationsIndexConfigured();
            loadApplicationsIndexConfigured.execute();
        }
    }

    public class LoadInstalledWidgets extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            installedWidgetsNestedScrollView.setBackgroundColor(PublicVariable.themeLightDark ? getColor(R.color.transparent_light_high_twice) : getColor(R.color.transparent_dark_high_twice));
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            installedWidgetsNavDrawerItems.clear();
            installedWidgetsSections.clear();

            try {
                widgetProviderInfoList = appWidgetManager.getInstalledProviders();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    widgetProviderInfoList.sort(new Comparator<AppWidgetProviderInfo>() {
                        @Override
                        public int compare(AppWidgetProviderInfo appWidgetProviderInfoLeft, AppWidgetProviderInfo appWidgetProviderInfoRight) {
                            return functionsClass.appName(appWidgetProviderInfoLeft.provider.getPackageName())
                                    .compareTo(functionsClass.appName(appWidgetProviderInfoRight.provider.getPackageName()));
                        }
                    });
                }


                if (functionsClass.loadCustomIcons()) {
                    loadCustomIcons.load();
                    FunctionsClass.println("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIcons());
                }

                String oldAppName = "";
                int widgetIndex = 0;
                for (AppWidgetProviderInfo appWidgetProviderInfo : widgetProviderInfoList) {
                    try {
                        String packageName = appWidgetProviderInfo.provider.getPackageName();
                        String newAppName = functionsClass.appName(packageName);
                        Drawable newAppIcon = functionsClass.loadCustomIcons() ? loadCustomIcons.getDrawableIconForPackage(packageName, functionsClass.shapedAppIcon(packageName)) : functionsClass.shapedAppIcon(packageName);

                        if (widgetIndex == 0) {
                            installedWidgetsSections.add(new WidgetSectionedGridRecyclerViewAdapter.Section(widgetIndex, newAppName, newAppIcon));
                            indexListInstalled.add(newAppName.substring(0, 1).toUpperCase());
                        } else {
                            if (!oldAppName.equals(newAppName)) {
                                installedWidgetsSections.add(new WidgetSectionedGridRecyclerViewAdapter.Section(widgetIndex, newAppName, newAppIcon));
                                indexListInstalled.add(newAppName.substring(0, 1).toUpperCase());
                            }
                        }

                        oldAppName = functionsClass.appName(appWidgetProviderInfo.provider.getPackageName());

                        Drawable widgetPreviewDrawable = appWidgetProviderInfo.loadPreviewImage(getApplicationContext(), DisplayMetrics.DENSITY_HIGH);
                        String widgetLabel = appWidgetProviderInfo.loadLabel(getPackageManager());

                        indexListInstalled.add(newAppName.substring(0, 1).toUpperCase());
                        installedWidgetsNavDrawerItems.add(new NavDrawerItem(functionsClass.appName(appWidgetProviderInfo.provider.getPackageName()),
                                appWidgetProviderInfo.provider.getPackageName(),
                                (widgetLabel != null) ? widgetLabel : newAppName,
                                newAppIcon,
                                (widgetPreviewDrawable != null) ? widgetPreviewDrawable : appWidgetProviderInfo.loadIcon(getApplicationContext(), DisplayMetrics.DENSITY_HIGH),
                                appWidgetProviderInfo
                        ));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    widgetIndex++;
                }

                installedWidgetsRecyclerViewAdapter = new InstalledWidgetsAdapter(WidgetConfigurations.this, getApplicationContext(), installedWidgetsNavDrawerItems, appWidgetHost);
            } catch (Exception e) {
                e.printStackTrace();
                this.cancel(true);
            }

            return configuredWidgetAvailable;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            installedWidgetsLoaded = true;

            ViewPropertyAnimator viewPropertyAnimator = addWidget.animate()
                    .rotation(135.0F)
                    .withLayer()
                    .setDuration(500L)
                    .setInterpolator(new OvershootInterpolator(13.0f));
            viewPropertyAnimator.start();

            int xPosition = (int) (addWidget.getX() + (addWidget.getWidth() / 2));
            int yPosition = (int) (addWidget.getY() + (addWidget.getHeight() / 2));

            int startRadius = 0;
            int endRadius = (int) Math.hypot(functionsClass.displayX(), functionsClass.displayY());

            installedWidgetsNestedScrollView.setBackgroundColor(PublicVariable.themeLightDark ? getColor(R.color.transparent_light) : getColor(R.color.dark_transparent));
            installedWidgetsNestedScrollView.setVisibility(View.VISIBLE);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(installedWidgetsNestedScrollView, xPosition, yPosition, startRadius, endRadius);
            circularReveal.setDuration(864);
            circularReveal.start();
            circularReveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    installedWidgetsNestedScrollView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            final Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            if (functionsClass.appThemeTransparent()) {
                ValueAnimator colorAnimation = ValueAnimator
                        .ofArgb(getWindow().getNavigationBarColor(), PublicVariable.themeLightDark ? getColor(R.color.fifty_light_twice) : getColor(R.color.transparent_dark_high_twice));
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        window.setStatusBarColor((Integer) animator.getAnimatedValue());
                        window.setNavigationBarColor((Integer) animator.getAnimatedValue());
                    }
                });
                colorAnimation.start();
            } else {
                if (PublicVariable.themeLightDark) {
                    installedWidgetsNestedScrollView.setBackground(new ColorDrawable(getColor(R.color.transparent_light)));
                    if (PublicVariable.themeLightDark) {
                        if (functionsClass.returnAPI() > 25) {
                            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                        }
                    }

                    ValueAnimator colorAnimation = ValueAnimator
                            .ofArgb(getWindow().getNavigationBarColor(), functionsClass.mixColors(getColor(R.color.light), getWindow().getNavigationBarColor(), 0.70f));
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            getWindow().setNavigationBarColor((Integer) animator.getAnimatedValue());
                            getWindow().setStatusBarColor((Integer) animator.getAnimatedValue());
                        }
                    });
                    colorAnimation.start();
                } else if (!PublicVariable.themeLightDark) {
                    installedWidgetsNestedScrollView.setBackground(new ColorDrawable(getColor(R.color.dark_transparent)));

                    ValueAnimator colorAnimation = ValueAnimator
                            .ofArgb(getWindow().getNavigationBarColor(), functionsClass.mixColors(getColor(R.color.dark), getWindow().getNavigationBarColor(), 0.70f));
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            getWindow().setNavigationBarColor((Integer) animator.getAnimatedValue());
                            getWindow().setStatusBarColor((Integer) animator.getAnimatedValue());
                        }
                    });
                    colorAnimation.start();
                }
            }

            installedWidgetsRecyclerViewAdapter.notifyDataSetChanged();
            WidgetSectionedGridRecyclerViewAdapter.Section[] sectionsData = new WidgetSectionedGridRecyclerViewAdapter.Section[installedWidgetsSections.size()];
            WidgetSectionedGridRecyclerViewAdapter widgetSectionedGridRecyclerViewAdapter = new WidgetSectionedGridRecyclerViewAdapter(
                    getApplicationContext(),
                    R.layout.widgets_sections,
                    installedWidgetsLoadView,
                    installedWidgetsRecyclerViewAdapter
            );
            widgetSectionedGridRecyclerViewAdapter.setSections(installedWidgetsSections.toArray(sectionsData));
            widgetSectionedGridRecyclerViewAdapter.notifyDataSetChanged();
            installedWidgetsLoadView.setAdapter(widgetSectionedGridRecyclerViewAdapter);

            if (!getDatabasePath(PublicVariable.WIDGET_DATA_DATABASE_NAME).exists() || !result) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                        loadingSplash.setVisibility(View.INVISIBLE);
                        loadingSplash.startAnimation(animation);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                ((LinearLayout) findViewById(R.id.switchFloating)).setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }, 200);
            }

            LoadApplicationsIndexInstalled loadApplicationsIndexInstalled = new LoadApplicationsIndexInstalled();
            loadApplicationsIndexInstalled.execute();
        }
    }

    private class LoadApplicationsIndexConfigured extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            indexView.removeAllViews();
        }

        @Override
        protected Void doInBackground(Void... params) {
            int indexCount = indexListConfigured.size();
            for (int navItem = 0; navItem < indexCount; navItem++) {
                try {
                    String indexText = indexListConfigured.get(navItem);
                    if (mapIndexFirstItem.get(indexText) == null/*avoid duplication*/) {
                        mapIndexFirstItem.put(indexText, navItem);
                    }

                    mapIndexLastItem.put(indexText, navItem);

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
            List<String> indexListFinal = new ArrayList<String>(mapIndexFirstItem.keySet());
            for (String index : indexListFinal) {
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

                    setupFastScrollingIndexingConfigured();
                }
            }, 700);
        }
    }

    private class LoadApplicationsIndexInstalled extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            indexViewInstalled.removeAllViews();
        }

        @Override
        protected Void doInBackground(Void... params) {
            int indexCount = indexListInstalled.size();
            for (int navItem = 0; navItem < indexCount; navItem++) {
                try {
                    String indexText = indexListInstalled.get(navItem);
                    if (mapIndexFirstItemInstalled.get(indexText) == null/*avoid duplication*/) {
                        mapIndexFirstItemInstalled.put(indexText, navItem);
                    }

                    mapIndexLastItemInstalled.put(indexText, navItem);

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
            List<String> indexListFinal = new ArrayList<String>(mapIndexFirstItemInstalled.keySet());
            for (String index : indexListFinal) {
                textView = (TextView) getLayoutInflater()
                        .inflate(R.layout.side_index_item, null);
                textView.setText(index.toUpperCase());
                textView.setTextColor(PublicVariable.colorLightDarkOpposite);
                indexViewInstalled.addView(textView);
            }

            TextView finalTextView = textView;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int upperRange = (int) (indexViewInstalled.getY() - finalTextView.getHeight());
                    for (int i = 0; i < indexViewInstalled.getChildCount(); i++) {
                        String indexText = ((TextView) indexViewInstalled.getChildAt(i)).getText().toString();
                        int indexRange = (int) (indexViewInstalled.getChildAt(i).getY() + indexViewInstalled.getY() + finalTextView.getHeight());
                        for (int jRange = upperRange; jRange <= (indexRange); jRange++) {
                            mapRangeIndexInstalled.put(jRange, indexText);
                        }

                        upperRange = indexRange;
                    }

                    setupFastScrollingIndexingInstalled();
                }
            }, 700);
        }
    }

    public static void createWidget(Context context, ViewGroup widgetView, AppWidgetManager appWidgetManager, AppWidgetHost appWidgetHost, AppWidgetProviderInfo appWidgetProviderInfo, int widgetId) {
        try {
            widgetView.removeAllViews();

            FunctionsClass functionsClass = new FunctionsClass(context);

            appWidgetHost.startListening();

            AppWidgetHostView hostView = appWidgetHost.createView(context, widgetId, appWidgetProviderInfo);
            hostView.setAppWidget(widgetId, appWidgetProviderInfo);

            int widgetWidth = 213, widgetHeight = 213;

            hostView.setMinimumWidth(widgetWidth);
            hostView.setMinimumHeight(widgetHeight);

            Bundle bundle = new Bundle();
            bundle.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, widgetWidth);
            bundle.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, widgetHeight);
            bundle.putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, functionsClass.displayX() / 2);
            bundle.putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, functionsClass.displayY() / 2);
            appWidgetManager.bindAppWidgetIdIfAllowed(widgetId, appWidgetProviderInfo.provider, bundle);

            widgetView.addView(hostView);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Animator.AnimatorListener scaleDownListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!installedWidgetsLoaded) {
                addWidget.animate().scaleXBy(0.23f).scaleYBy(0.23f).setDuration(223).setListener(scaleUpListener);
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
            addWidget.animate().scaleXBy(-0.23f).scaleYBy(-0.23f).setDuration(323).setListener(scaleDownListener);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }
    };

    @SuppressLint("ClickableViewAccessibility")
    public void setupFastScrollingIndexingConfigured() {
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
                            String indexText = mapRangeIndex.get(((int) motionEvent.getY()));

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
                                    configuredWidgetsNestedScrollView.smoothScrollTo(
                                            0,
                                            ((int) configuredWidgetsLoadView.getChildAt(mapIndexFirstItem.get(mapRangeIndex.get(((int) motionEvent.getY())))).getY())
                                    );
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
                                configuredWidgetsNestedScrollView.smoothScrollTo(
                                        0,
                                        ((int) configuredWidgetsLoadView.getChildAt(mapIndexFirstItem.get(mapRangeIndex.get(((int) motionEvent.getY())))).getY())
                                );
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (popupIndex.isShown()) {
                                try {
                                    configuredWidgetsNestedScrollView.smoothScrollTo(
                                            0,
                                            ((int) configuredWidgetsLoadView.getChildAt(mapIndexFirstItem.get(mapRangeIndex.get(((int) motionEvent.getY())))).getY())
                                    );
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

    @SuppressLint("ClickableViewAccessibility")
    public void setupFastScrollingIndexingInstalled() {
        Drawable popupIndexBackground = getDrawable(R.drawable.ic_launcher_balloon).mutate();
        popupIndexBackground.setTint(PublicVariable.primaryColorOpposite);
        popupIndex.setBackground(popupIndexBackground);

        installedNestedIndexScrollView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
        installedNestedIndexScrollView.setVisibility(View.VISIBLE);

        float popupIndexOffsetY = PublicVariable.statusBarHeight + PublicVariable.actionBarHeight + (functionsClass.UsageStatsEnabled() ? functionsClass.DpToInteger(7) : functionsClass.DpToInteger(7));
        installedNestedIndexScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if (functionsClass.litePreferencesEnabled()) {

                        } else {
                            String indexText = mapRangeIndexInstalled.get(((int) motionEvent.getY()));

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
                            String indexText = mapRangeIndexInstalled.get(((int) motionEvent.getY()));

                            if (indexText != null) {
                                if (!popupIndex.isShown()) {
                                    popupIndex.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                                    popupIndex.setVisibility(View.VISIBLE);
                                }
                                popupIndex.setY(motionEvent.getRawY() - popupIndexOffsetY);
                                popupIndex.setText(indexText);

                                try {
                                    installedWidgetsNestedScrollView.smoothScrollTo(
                                            0,
                                            ((int) installedWidgetsLoadView.getChildAt(mapIndexFirstItemInstalled.get(mapRangeIndexInstalled.get(((int) motionEvent.getY())))).getY())
                                    );
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
                                installedWidgetsNestedScrollView.smoothScrollTo(
                                        0,
                                        ((int) installedWidgetsLoadView.getChildAt(mapIndexFirstItemInstalled.get(mapRangeIndexInstalled.get(((int) motionEvent.getY())))).getY())
                                );
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (popupIndex.isShown()) {
                                try {
                                    installedWidgetsNestedScrollView.smoothScrollTo(
                                            0,
                                            ((int) installedWidgetsLoadView.getChildAt(mapIndexFirstItemInstalled.get(mapRangeIndexInstalled.get(((int) motionEvent.getY())))).getY())
                                    );
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
