package net.geekstools.floatshort.PRO.Shortcuts;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.geekstools.floatshort.PRO.Automation.Apps.AppAutoFeatures;
import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.BuildConfig;
import net.geekstools.floatshort.PRO.Category.FoldersHandler;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Shortcuts.NavAdapter.CardHybridAdapter;
import net.geekstools.floatshort.PRO.Shortcuts.NavAdapter.HybridSectionedGridRecyclerViewAdapter;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClassDebug;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.IAP.InAppBilling;
import net.geekstools.floatshort.PRO.Util.LicenseValidator;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.NavAdapter.RecycleViewSmoothLayoutGrid;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryFolders;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryShortcuts;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryWidgets;
import net.geekstools.floatshort.PRO.Util.SettingGUI.SettingGUIDark;
import net.geekstools.floatshort.PRO.Util.SettingGUI.SettingGUILight;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;
import net.geekstools.floatshort.PRO.Util.UI.SimpleGestureFilterSwitch;
import net.geekstools.floatshort.PRO.Widget.WidgetConfigurations;
import net.geekstools.imageview.customshapes.ShapesImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class HybridViewOff extends Activity implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener, SimpleGestureFilterSwitch.SimpleGestureListener {

    FunctionsClass functionsClass;

    RecyclerView loadView;
    ScrollView nestedScrollView, nestedIndexScrollView;
    RelativeLayout scrollRelativeLayout, fullActionViews, MainView, loadingSplash;
    LinearLayout indexView, freqView;
    ProgressBar loadingBarLTR;
    ImageView loadLogo, actionButton, recoverFloatingCategories, recoverFloatingWidgets;
    TextView popupIndex;
    MaterialButton switchWidgets, switchCategories, recoveryAction, automationAction;

    List<ApplicationInfo> applicationInfoList;
    Map<String, Integer> mapIndexFirstItem, mapIndexLastItem;
    Map<Integer, String> mapRangeIndex;
    NavigableMap<String, Integer> indexItems;
    ArrayList<NavDrawerItem> navDrawerItems;

    List<String> indexList;
    List<HybridSectionedGridRecyclerViewAdapter.Section> sections;
    RecyclerView.Adapter recyclerViewAdapter;
    GridLayoutManager recyclerViewLayoutManager;

    String PackageName;
    String AppName = "Application";
    Drawable AppIcon;

    HorizontalScrollView freqlist;
    String[] freqApps;
    int limitedCountLine, hybridItem = 0, lastIntentItem = 0;
    int[] counter;
    boolean loadFreq = false;

    SimpleGestureFilterSwitch simpleGestureFilterSwitch;

    FirebaseRemoteConfig firebaseRemoteConfig;

    ProgressDialog progressDialog;

    LoadCustomIcons loadCustomIcons;

    private FirebaseAuth firebaseAuth;

    /*Rewarded Ads for Floating Widgets*/
    RewardedVideoAd rewardedVideoAdInstance;
    /*Rewarded Ads for Floating Widgets*/

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.off_hybrid_view);
        PublicVariable.eligibleLoadShowAds = true;

        nestedScrollView = (ScrollView) findViewById(R.id.nestedScrollView);
        nestedIndexScrollView = (ScrollView) findViewById(R.id.nestedIndexScrollView);
        scrollRelativeLayout = (RelativeLayout) findViewById(R.id.scrollRelativeLayout);
        loadView = (RecyclerView) findViewById(R.id.list);
        indexView = (LinearLayout) findViewById(R.id.side_index);
        freqView = (LinearLayout) findViewById(R.id.freqItem);
        MainView = (RelativeLayout) findViewById(R.id.MainView);
        freqlist = (HorizontalScrollView) findViewById(R.id.freqList);
        fullActionViews = (RelativeLayout) findViewById(R.id.fullActionViews);
        loadingSplash = (RelativeLayout) findViewById(R.id.loadingSplash);

        actionButton = (ImageView) findViewById(R.id.actionButton);
        switchWidgets = (MaterialButton) findViewById(R.id.switchWidgets);
        switchCategories = (MaterialButton) findViewById(R.id.switchCategories);
        recoveryAction = (MaterialButton) findViewById(R.id.recoveryAction);
        automationAction = (MaterialButton) findViewById(R.id.automationAction);
        recoverFloatingCategories = (ImageView) findViewById(R.id.recoverFloatingCategories);
        recoverFloatingWidgets = (ImageView) findViewById(R.id.recoverFloatingWidgets);
        popupIndex = (TextView) findViewById(R.id.popupIndex);

        simpleGestureFilterSwitch = new SimpleGestureFilterSwitch(getApplicationContext(), this);
        functionsClass = new FunctionsClass(getApplicationContext(), this);
        functionsClass.ChangeLog(HybridViewOff.this, false);

        recyclerViewLayoutManager = new RecycleViewSmoothLayoutGrid(getApplicationContext(), functionsClass.columnCount(105), OrientationHelper.VERTICAL, false);
        loadView.setLayoutManager(recyclerViewLayoutManager);

        indexList = new ArrayList<String>();
        sections = new ArrayList<HybridSectionedGridRecyclerViewAdapter.Section>();
        indexItems = new TreeMap<String, Integer>();

        if (functionsClass.appThemeTransparent() == true) {
            functionsClass.setThemeColorFloating(MainView, true);
        } else {
            functionsClass.setThemeColorFloating(MainView, false);
        }

        applicationInfoList = new ArrayList<ApplicationInfo>();
        navDrawerItems = new ArrayList<NavDrawerItem>();
        mapIndexFirstItem = new LinkedHashMap<String, Integer>();
        mapIndexLastItem = new LinkedHashMap<String, Integer>();
        mapRangeIndex = new LinkedHashMap<Integer, String>();

        if (functionsClass.loadCustomIcons()) {
            loadCustomIcons = new LoadCustomIcons(getApplicationContext(), functionsClass.customIconPackageName());
        }

        LoadApplicationsOffLimited loadApplicationsOffLimited = new LoadApplicationsOffLimited();
        loadApplicationsOffLimited.execute();

        LayerDrawable drawPreferenceAction = (LayerDrawable) getDrawable(R.drawable.draw_pref_action);
        Drawable backPreferenceAction = drawPreferenceAction.findDrawableByLayerId(R.id.backtemp);
        backPreferenceAction.setTint(PublicVariable.primaryColorOpposite);
        actionButton.setImageDrawable(drawPreferenceAction);

        switchWidgets.setTextColor(getColor(R.color.light));
        switchCategories.setTextColor(getColor(R.color.light));
        if (PublicVariable.themeLightDark /*light*/ && functionsClass.appThemeTransparent() /*transparent*/) {
            switchWidgets.setTextColor(getColor(R.color.dark));
            switchCategories.setTextColor(getColor(R.color.dark));
        }

        switchCategories.setBackgroundColor(functionsClass.appThemeTransparent() ? functionsClass.setColorAlpha(PublicVariable.primaryColor, 51) : PublicVariable.primaryColor);
        switchCategories.setRippleColor(ColorStateList.valueOf(functionsClass.appThemeTransparent() ? functionsClass.setColorAlpha(PublicVariable.primaryColorOpposite, 51) : PublicVariable.primaryColorOpposite));

        switchWidgets.setBackgroundColor(functionsClass.appThemeTransparent() ? functionsClass.setColorAlpha(PublicVariable.primaryColor, 51) : PublicVariable.primaryColor);
        switchWidgets.setRippleColor(ColorStateList.valueOf(functionsClass.appThemeTransparent() ? functionsClass.setColorAlpha(PublicVariable.primaryColorOpposite, 51) : PublicVariable.primaryColorOpposite));

        recoveryAction.setBackgroundColor(PublicVariable.primaryColorOpposite);
        recoveryAction.setRippleColor(ColorStateList.valueOf(PublicVariable.primaryColor));

        automationAction.setBackgroundColor(PublicVariable.primaryColorOpposite);
        automationAction.setRippleColor(ColorStateList.valueOf(PublicVariable.primaryColor));

        try {
            LayerDrawable drawRecoverFloatingCategories = (LayerDrawable) getDrawable(R.drawable.draw_recovery).mutate();
            Drawable backRecoverFloatingCategories = drawRecoverFloatingCategories.findDrawableByLayerId(R.id.backtemp).mutate();
            backRecoverFloatingCategories.setTint(functionsClass.appThemeTransparent() ? functionsClass.setColorAlpha(PublicVariable.primaryColor, 51) : PublicVariable.primaryColor);

            LayerDrawable drawRecoverFloatingWidgets = (LayerDrawable) getDrawable(R.drawable.draw_recovery_widgets).mutate();
            Drawable backRecoverFloatingWidgets = drawRecoverFloatingWidgets.findDrawableByLayerId(R.id.backtemp).mutate();
            backRecoverFloatingWidgets.setTint(functionsClass.appThemeTransparent() ? functionsClass.setColorAlpha(PublicVariable.primaryColor, 51) : PublicVariable.primaryColor);

            recoverFloatingCategories.setImageDrawable(drawRecoverFloatingCategories);
            recoverFloatingWidgets.setImageDrawable(drawRecoverFloatingWidgets);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        /*Rewarded Ads for Floating Widgets*/
        MobileAds.initialize(getApplicationContext(), getString(R.string.AdAppId));

        rewardedVideoAdInstance = MobileAds.getRewardedVideoAdInstance(getApplicationContext());
        rewardedVideoAdInstance.setImmersiveMode(true);
        if (PublicVariable.eligibleLoadShowAds) {
            rewardedVideoAdInstance.loadAd(getString(R.string.AdUnitReward), new AdRequest.Builder()
                    .addTestDevice("CDCAA1F20B5C9C948119E886B31681DE")
                    .addTestDevice("D101234A6C1CF51023EE5815ABC285BD")
                    .addTestDevice("65B5827710CBE90F4A99CE63099E524C")
                    .addTestDevice("5901E5EE74F9B6652E05621140664A54")
                    .addTestDevice("DD428143B4772EC7AA87D1E2F9DA787C")
                    .addTestDevice("F54D998BCE077711A17272B899B44798")
                    .build());
        }
        rewardedVideoAdInstance.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                FunctionsClassDebug.Companion.PrintDebug("*** Rewarded Ads Loaded ***");

                switchWidgets.setIconGravity(MaterialButton.ICON_GRAVITY_TEXT_END);
                switchWidgets.setIconSize(functionsClass.DpToInteger(19));
                switchWidgets.setIcon(getDrawable(R.drawable.ads_icon));
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                if (PublicVariable.AdsRewarded) {
                    try {
                        PublicVariable.AdsRewarded = false;

                        functionsClass.navigateToClass(WidgetConfigurations.class,
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_left, R.anim.slide_to_right));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (PublicVariable.eligibleLoadShowAds) {
                        rewardedVideoAdInstance.loadAd(getString(R.string.AdUnitReward), new AdRequest.Builder()
                                .addTestDevice("CDCAA1F20B5C9C948119E886B31681DE")
                                .addTestDevice("D101234A6C1CF51023EE5815ABC285BD")
                                .addTestDevice("65B5827710CBE90F4A99CE63099E524C")
                                .addTestDevice("F54D998BCE077711A17272B899B44798")
                                .build());
                    }
                }
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                PublicVariable.AdsRewarded = true;
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int failedCode) {
                if (PublicVariable.eligibleLoadShowAds) {
                    rewardedVideoAdInstance.loadAd(getString(R.string.AdUnitReward), new AdRequest.Builder()
                            .addTestDevice("CDCAA1F20B5C9C948119E886B31681DE")
                            .addTestDevice("D101234A6C1CF51023EE5815ABC285BD")
                            .addTestDevice("65B5827710CBE90F4A99CE63099E524C")
                            .addTestDevice("F54D998BCE077711A17272B899B44798")
                            .build());
                }
            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
        /*Rewarded Ads for Floating Widgets*/

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionsClass.doVibrate(33);

                if (PublicVariable.actionCenter == false) {

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
                    functionsClass.navigateToClass(FoldersHandler.class,
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_right, R.anim.slide_to_left));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        switchWidgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (functionsClass.networkConnection() && firebaseAuth.getCurrentUser() != null) {
                        if (rewardedVideoAdInstance.isLoaded()) {
                            rewardedVideoAdInstance.show();
                        } else {
                            try {
                                functionsClass.navigateToClass(WidgetConfigurations.class,
                                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_left, R.anim.slide_to_right));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.internetError), Toast.LENGTH_LONG).show();

                        if (firebaseAuth.getCurrentUser() == null) {
                            Toast.makeText(getApplicationContext(), getString(R.string.authError), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                Intent intent = new Intent(getApplicationContext(), RecoveryShortcuts.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);
            }
        });
        recoverFloatingCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecoveryFolders.class);
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
        recoverFloatingWidgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecoveryWidgets.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.recovery_actions_hide);
                recoverFloatingWidgets.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        recoverFloatingWidgets.setVisibility(View.INVISIBLE);
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
                                makeSceneTransitionAnimation(HybridViewOff.this, actionButton, "transition");
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (PublicVariable.themeLightDark) {
                            intent.setClass(HybridViewOff.this, SettingGUILight.class);
                        } else if (!PublicVariable.themeLightDark) {
                            intent.setClass(HybridViewOff.this, SettingGUIDark.class);
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
        switchWidgets.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!recoverFloatingWidgets.isShown()) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.recovery_actions_show);
                    recoverFloatingWidgets.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            recoverFloatingWidgets.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                } else {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.recovery_actions_hide);
                    recoverFloatingWidgets.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            recoverFloatingWidgets.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }

                return true;
            }
        });

        //Consume Donation
        try {
            if (functionsClass.alreadyDonated() && functionsClass.networkConnection()) {
                BillingClient billingClient = BillingClient.newBuilder(HybridViewOff.this).setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
                        if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {

                        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {

                        } else {

                        }

                    }
                }).build();
                billingClient.startConnection(new BillingClientStateListener() {
                    @Override
                    public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                        if (billingResponseCode == BillingClient.BillingResponse.OK) {
                            List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList();
                            for (Purchase purchase : purchases) {
                                FunctionsClassDebug.Companion.PrintDebug("*** Purchased Item: " + purchase + " ***");


                                if (purchase.getSku().equals("donation")) {
                                    ConsumeResponseListener consumeResponseListener = new ConsumeResponseListener() {
                                        @Override
                                        public void onConsumeResponse(@BillingClient.BillingResponse int responseCode, String outToken) {
                                            if (responseCode == BillingClient.BillingResponse.OK) {
                                                FunctionsClassDebug.Companion.PrintDebug("*** Consumed Item: " + outToken + " ***");

                                                functionsClass.savePreference(".PurchasedItem", purchase.getSku(), false);
                                            }
                                        }
                                    };
                                    billingClient.consumeAsync(purchase.getPurchaseToken(), consumeResponseListener);
                                }
                            }
                        }
                    }

                    @Override
                    public void onBillingServiceDisconnected() {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Restore Subscribed Item
        try {
            if (functionsClass.networkConnection()) {
                BillingClient billingClient = BillingClient.newBuilder(HybridViewOff.this).setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
                        if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {

                        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {

                        } else {

                        }

                    }
                }).build();
                billingClient.startConnection(new BillingClientStateListener() {
                    @Override
                    public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                        if (billingResponseCode == BillingClient.BillingResponse.OK) {
                            functionsClass.savePreference(".SubscribedItem", "remove.ads", false);

                            List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.SUBS).getPurchasesList();
                            for (Purchase purchase : purchases) {
                                FunctionsClassDebug.Companion.PrintDebug("*** Subscribed Item: " + purchase + " ***");
                                functionsClass.savePreference(".SubscribedItem", purchase.getSku(), true);
                            }
                        }
                    }

                    @Override
                    public void onBillingServiceDisconnected() {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getString(R.string.license));
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(getString(R.string.license))) {
                    functionsClass.dialogueLicense(HybridViewOff.this);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stopService(new Intent(getApplicationContext(), LicenseValidator.class));
                        }
                    }, 1000);
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
        try {
            if (!getFileStreamPath(".License").exists() && functionsClass.networkConnection() == true) {
                if (!BuildConfig.DEBUG) {
                    startService(new Intent(getApplicationContext(), LicenseValidator.class));
                }
            } else {
                try {
                    unregisterReceiver(broadcastReceiver);
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (functionsClass.SystemCache()) {
            startService(new Intent(getApplicationContext(), BindServices.class));
        }

        try {
            if (functionsClass.networkConnection()) {
                try {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    firebaseUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                                @Override
                                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user == null) {
                                        functionsClass.savePreference(".UserInformation", "userEmail", null);
                                    } else {

                                    }


                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (functionsClass.readPreference(".UserInformation", "userEmail", null) == null) {
                    GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.webClientId))
                            .requestEmail()
                            .build();

                    GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
                    try {
                        googleSignInClient.signOut();
                        googleSignInClient.revokeAccess();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent signInIntent = googleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, 666);

                    progressDialog = new ProgressDialog(HybridViewOff.this,
                            PublicVariable.themeLightDark ? R.style.GeeksEmpire_Dialogue_Light : R.style.GeeksEmpire_Dialogue_Dark);
                    progressDialog.setMessage(Html.fromHtml(
                            "<big><font color='" + PublicVariable.colorLightDarkOpposite + "'>" + getString(R.string.signinTitle) + "</font></big>"
                                    + "<br/>" +
                                    "<small><font color='" + PublicVariable.colorLightDarkOpposite + "'>" + getString(R.string.signinMessage) + "</font></small>"));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    PublicVariable.eligibleLoadShowAds = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView shareIt = (ImageView) findViewById(R.id.share);
        if (functionsClass.LoadAds()) {
            LayerDrawable drawableShare = (LayerDrawable) getDrawable(R.drawable.draw_no_ads);
            Drawable backgroundShare = drawableShare.findDrawableByLayerId(R.id.backtemp);
            backgroundShare.setTint(PublicVariable.primaryColor);
            shareIt.setImageDrawable(drawableShare);
            shareIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), InAppBilling.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle());
                }
            });
        } else {
            LayerDrawable drawableShare = (LayerDrawable) getDrawable(R.drawable.draw_share);
            Drawable backgroundShare = drawableShare.findDrawableByLayerId(R.id.backtemp);
            backgroundShare.setTint(PublicVariable.primaryColor);
            shareIt.setImageDrawable(drawableShare);
            shareIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String shareText =
                            getString(R.string.shareTitle) +
                                    "\n" + getString(R.string.shareSummary) +
                                    "\n" + getString(R.string.play_store_link) + getPackageName();

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    sharingIntent.setType("text/plain");
                    sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(sharingIntent);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PublicVariable.inMemory = true;
        PublicVariable.eligibleLoadShowAds = true;

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_default);
        firebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(HybridViewOff.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.activate().addOnSuccessListener(new OnSuccessListener<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    if (firebaseRemoteConfig.getLong(functionsClass.versionCodeRemoteConfigKey()) > functionsClass.appVersionCode(getPackageName())) {
                                        LayerDrawable layerDrawableNewUpdate = (LayerDrawable) getDrawable(R.drawable.ic_update);
                                        BitmapDrawable gradientDrawableNewUpdate = (BitmapDrawable) layerDrawableNewUpdate.findDrawableByLayerId(R.id.ic_launcher_back_layer);
                                        gradientDrawableNewUpdate.setTint(PublicVariable.primaryColor);

                                        ImageView newUpdate = (ImageView) findViewById(R.id.newUpdate);
                                        newUpdate.setImageDrawable(layerDrawableNewUpdate);
                                        newUpdate.setVisibility(View.VISIBLE);
                                        newUpdate.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                functionsClass.upcomingChangeLog(
                                                        HybridViewOff.this,
                                                        firebaseRemoteConfig.getString(functionsClass.upcomingChangeLogRemoteConfigKey()),
                                                        String.valueOf(firebaseRemoteConfig.getLong(functionsClass.versionCodeRemoteConfigKey()))
                                                );

                                            }
                                        });

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

        functionsClass.addAppShortcuts();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        PublicVariable.eligibleLoadShowAds = false;

        functionsClass.addAppShortcuts();
        functionsClass.savePreference("LoadView", "LoadViewPosition", recyclerViewLayoutManager.findFirstVisibleItemPosition());
        if (PublicVariable.actionCenter == true) {
            functionsClass.closeActionMenuOption(fullActionViews, actionButton);
        }
        functionsClass.savePreference("OpenMode", "openClassName", this.getClass().getSimpleName());
        functionsClass.CheckSystemRAM(HybridViewOff.this);

        if (functionsClass.SystemCache()) {
            startService(new Intent(getApplicationContext(), BindServices.class));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PublicVariable.inMemory = false;
    }

    @Override
    public void onBackPressed() {
        Intent homeScreen = new Intent(Intent.ACTION_MAIN);
        homeScreen.addCategory(Intent.CATEGORY_HOME);
        homeScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeScreen, ActivityOptions.makeCustomAnimation(getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view instanceof ImageView) {
            ImageView freqApp = (ImageView) view;
            int position = freqApp.getId();
            functionsClass.runUnlimitedShortcutsService(freqApps[position]);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (view instanceof ImageView) {
            ImageView freqApp = (ImageView) view;
            int position = freqApp.getId();
            functionsClass.appsLaunchPad(freqApps[position]);
        }
        return true;
    }

    @Override
    public void onSwipe(int direction) {
        switch (direction) {
            case SimpleGestureFilterSwitch.SWIPE_RIGHT: {
                if (rewardedVideoAdInstance.isLoaded()) {
                    rewardedVideoAdInstance.show();
                } else {
                    try {
                        functionsClass.navigateToClass(WidgetConfigurations.class,
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_left, R.anim.slide_to_right));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            }
            case SimpleGestureFilterSwitch.SWIPE_LEFT: {
                try {
                    functionsClass.navigateToClass(FoldersHandler.class,
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 666) {
            try {
                Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount googleSignInAccount = googleSignInAccountTask.getResult(ApiException.class);

                AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                firebaseAuth.signInWithCredential(authCredential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    if (firebaseUser != null) {
                                        functionsClass.savePreference(".UserInformation", "userEmail", firebaseUser.getEmail());

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    File betaFile = new File("/data/data/" + getPackageName() + "/shared_prefs/.UserInformation.xml");
                                                    Uri uriBetaFile = Uri.fromFile(betaFile);
                                                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

                                                    StorageReference storageReference = firebaseStorage.getReference("/Users/" + "API" + functionsClass.returnAPI() + "/" +
                                                            functionsClass.readPreference(".UserInformation", "userEmail", null));
                                                    UploadTask uploadTask = storageReference.putFile(uriBetaFile);

                                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception exception) {
                                                            exception.printStackTrace();
                                                            try {
                                                                progressDialog.setMessage(Html.fromHtml(
                                                                        "<big><font color='" + PublicVariable.colorLightDarkOpposite + "'>" + getString(R.string.error) + "</font></big>"
                                                                                + "<br/>" +
                                                                                "<small><font color='" + PublicVariable.colorLightDarkOpposite + "'>" + exception.getMessage() + "</font></small>"));
                                                                progressDialog.setCancelable(true);
                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        progressDialog.dismiss();
                                                                        PublicVariable.eligibleLoadShowAds = true;
                                                                    }
                                                                }, 777);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            FunctionsClassDebug.Companion.PrintDebug("Firebase Activities Done Successfully");
                                                            functionsClass.Toast(getString(R.string.signinFinished), Gravity.TOP);
                                                            try {
                                                                progressDialog.dismiss();
                                                                PublicVariable.eligibleLoadShowAds = true;
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, 333);
                                    }
                                } else {

                                }
                            }
                        });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void LoadFrequentlyApps() {
        try {
            freqView.removeAllViews();

            counter = new int[25];
            freqApps = getIntent().getStringArrayExtra("freq");
            int freqLength = getIntent().getIntExtra("num", -1);
            if (getFileStreamPath("Frequently").exists()) {
                functionsClass.removeLine(".categoryInfo", "Frequently");
                deleteFile("Frequently");
            }
            functionsClass.saveFileAppendLine(".categoryInfo", "Frequently");

            freqlist.setVisibility(View.VISIBLE);

            ShapesImage shapesImage;
            RelativeLayout freqLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.freq_item, null);
            for (int i = 0; i < freqLength; i++) {
                freqLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.freq_item, null);
                shapesImage = functionsClass.initShapesImage(freqLayout, R.id.freqItems);
                shapesImage.setId(i);
                shapesImage.setOnClickListener(HybridViewOff.this);
                shapesImage.setOnLongClickListener(HybridViewOff.this);
                shapesImage.setImageDrawable(functionsClass.loadCustomIcons() ?
                        loadCustomIcons.getDrawableIconForPackage(freqApps[i], functionsClass.shapedAppIcon(freqApps[i]))
                        :
                        functionsClass.shapedAppIcon(freqApps[i]));
                freqView.addView(freqLayout);

                functionsClass.saveFileAppendLine("Frequently", freqApps[i]);
                functionsClass.saveFile(freqApps[i] + "Frequently", freqApps[i]);
            }
            functionsClass.addAppShortcuts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LoadApplicationsOffLimited extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingSplash = (RelativeLayout) findViewById(R.id.loadingSplash);
            if (functionsClass.appThemeTransparent() == true) {
                loadingSplash.setBackgroundColor(Color.TRANSPARENT);
            } else {
                loadingSplash.setBackgroundColor(getWindow().getNavigationBarColor());
            }

            loadingBarLTR = (ProgressBar) findViewById(R.id.loadingProgressltr);
            if (PublicVariable.themeLightDark) {
                loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.themeTextColor, android.graphics.PorterDuff.Mode.MULTIPLY);
            } else if (!PublicVariable.themeLightDark) {
                loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
            }

            loadLogo = (ImageView) findViewById(R.id.loadLogo);
            LayerDrawable layerDrawableLoadLogo = (LayerDrawable) getDrawable(R.drawable.ic_launcher_layer);
            BitmapDrawable gradientDrawableLoadLogo = (BitmapDrawable) layerDrawableLoadLogo.findDrawableByLayerId(R.id.ic_launcher_back_layer);
            gradientDrawableLoadLogo.setTint(PublicVariable.primaryColor);
            loadLogo.setImageDrawable(layerDrawableLoadLogo);

            indexView.removeAllViews();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                applicationInfoList = getApplicationContext().getPackageManager().getInstalledApplications(0);
                Collections.sort(applicationInfoList, new ApplicationInfo.DisplayNameComparator(getPackageManager()));
                limitedCountLine = ((int) applicationInfoList.size() / 3);

                if (functionsClass.loadCustomIcons()) {
                    loadCustomIcons.load();
                    FunctionsClassDebug.Companion.PrintDebug("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIcons());
                }

                int itemOfIndex = 1;
                for (int appInfo = 0; appInfo < limitedCountLine; appInfo++) {
                    if (getPackageManager().getLaunchIntentForPackage(applicationInfoList.get(appInfo).packageName) != null) {
                        try {
                            String newChar = functionsClass.appName(applicationInfoList.get(appInfo).packageName).substring(0, 1).toUpperCase();
                            if (appInfo == 0) {
                                sections.add(new HybridSectionedGridRecyclerViewAdapter.Section(hybridItem, newChar));
                            } else {
                                String oldChar = functionsClass.appName(applicationInfoList.get(lastIntentItem).packageName).substring(0, 1).toUpperCase();
                                if (!oldChar.equals(newChar)) {
                                    sections.add(new HybridSectionedGridRecyclerViewAdapter.Section(hybridItem, newChar));
                                    indexList.add(newChar);
                                    itemOfIndex = 1;
                                }
                            }

                            PackageName = applicationInfoList.get(appInfo).packageName;
                            AppName = functionsClass.appName(PackageName);
                            AppIcon = functionsClass.loadCustomIcons() ? loadCustomIcons.getDrawableIconForPackage(PackageName, functionsClass.shapedAppIcon(PackageName)) : functionsClass.shapedAppIcon(PackageName);

                            navDrawerItems.add(new NavDrawerItem(AppName, PackageName, AppIcon));
                            indexList.add(newChar);
                            indexItems.put(newChar, itemOfIndex++);


                            hybridItem = hybridItem + 1;
                            lastIntentItem = appInfo;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                recyclerViewAdapter = new CardHybridAdapter(HybridViewOff.this, getApplicationContext(), navDrawerItems);
            } catch (Exception e) {
                e.printStackTrace();
                this.cancel(true);
                finish();
            } finally {
                if (getIntent().getStringArrayExtra("freq") != null) {
                    freqApps = getIntent().getStringArrayExtra("freq");
                    int freqLength = getIntent().getIntExtra("num", -1);
                    PublicVariable.freqApps = freqApps;
                    PublicVariable.freqLength = freqLength;
                    if (freqLength > 1) {
                        loadFreq = true;
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!loadFreq) {
                MainView.removeView(freqlist);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                );
                nestedScrollView.setLayoutParams(layoutParams);
            } else {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                );
                layoutParams.addRule(RelativeLayout.ABOVE, R.id.freqList);
                scrollRelativeLayout.setPadding(0, scrollRelativeLayout.getPaddingTop(), 0, 0);
                nestedScrollView.setLayoutParams(layoutParams);

                nestedIndexScrollView.setPadding(0, 0, 0, functionsClass.DpToInteger(66));
            }
            recyclerViewAdapter.notifyDataSetChanged();
            HybridSectionedGridRecyclerViewAdapter.Section[] sectionsData = new HybridSectionedGridRecyclerViewAdapter.Section[sections.size()];
            HybridSectionedGridRecyclerViewAdapter hybridSectionedGridRecyclerViewAdapter = new HybridSectionedGridRecyclerViewAdapter(
                    getApplicationContext(),
                    R.layout.hybrid_sections,
                    R.id.section_text,
                    loadView,
                    recyclerViewAdapter
            );
            hybridSectionedGridRecyclerViewAdapter.setSections(sections.toArray(sectionsData));
            loadView.setAdapter(hybridSectionedGridRecyclerViewAdapter);

            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
            loadingSplash.startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (loadFreq == true) {
                        LoadFrequentlyApps();
                    }
                    ((LinearLayout) findViewById(R.id.switchFloating)).setVisibility(View.VISIBLE);
                    LoadApplicationsOff loadApplicationsOff = new LoadApplicationsOff();
                    loadApplicationsOff.execute();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    loadingSplash = (RelativeLayout) findViewById(R.id.loadingSplash);
                    loadingSplash.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private class LoadApplicationsOff extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                int itemOfIndex = 1;
                for (int appInfo = limitedCountLine; appInfo < applicationInfoList.size(); appInfo++) {
                    if (getPackageManager().getLaunchIntentForPackage(applicationInfoList.get(appInfo).packageName) != null) {
                        try {
                            String newChar = functionsClass.appName(applicationInfoList.get(appInfo).packageName).substring(0, 1).toUpperCase();
                            if (appInfo == 0) {
                            } else {
                                String oldChar = functionsClass.appName(applicationInfoList.get(lastIntentItem).packageName).substring(0, 1).toUpperCase();
                                if (!oldChar.equals(newChar)) {
                                    sections.add(new HybridSectionedGridRecyclerViewAdapter.Section(hybridItem, newChar));
                                    indexList.add(newChar);
                                    itemOfIndex = 1;
                                }
                            }

                            PackageName = applicationInfoList.get(appInfo).packageName;
                            AppName = functionsClass.appName(PackageName);
                            AppIcon = functionsClass.loadCustomIcons() ? loadCustomIcons.getDrawableIconForPackage(PackageName, functionsClass.shapedAppIcon(PackageName)) : functionsClass.shapedAppIcon(PackageName);

                            navDrawerItems.add(new NavDrawerItem(AppName, PackageName, AppIcon));
                            indexList.add(newChar);
                            indexItems.put(newChar, itemOfIndex++);


                            hybridItem = hybridItem + 1;
                            lastIntentItem = appInfo;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (loadFreq == false) {
                    for (int i = hybridItem; i < (hybridItem + 2); i++) {
                        //    sections.add(new HybridSectionedGridRecyclerViewAdapter.Section(i, ""));
                    }
                }

                recyclerViewAdapter = new CardHybridAdapter(HybridViewOff.this, getApplicationContext(), navDrawerItems);
            } catch (Exception e) {
                e.printStackTrace();
                this.cancel(true);
                finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            recyclerViewAdapter.notifyDataSetChanged();
            HybridSectionedGridRecyclerViewAdapter.Section[] sectionsData = new HybridSectionedGridRecyclerViewAdapter.Section[HybridViewOff.this.sections.size()];
            HybridSectionedGridRecyclerViewAdapter hybridSectionedGridRecyclerViewAdapter = new HybridSectionedGridRecyclerViewAdapter(
                    getApplicationContext(),
                    R.layout.hybrid_sections,
                    R.id.section_text,
                    loadView,
                    recyclerViewAdapter
            );
            hybridSectionedGridRecyclerViewAdapter.setSections(HybridViewOff.this.sections.toArray(sectionsData));
            loadView.setAdapter(hybridSectionedGridRecyclerViewAdapter);

            recyclerViewLayoutManager.scrollToPosition(0);
            nestedScrollView.scrollTo(0, 0);

            LoadApplicationsIndex loadApplicationsIndex = new LoadApplicationsIndex();
            loadApplicationsIndex.execute();

            try {
                Intent goHome = getIntent();
                if (goHome.hasExtra("goHome")) {
                    if (goHome.getBooleanExtra("goHome", false)) {
                        Intent homeScreen = new Intent(Intent.ACTION_MAIN);
                        homeScreen.addCategory(Intent.CATEGORY_HOME);
                        homeScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(homeScreen);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
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
            int indexCount = indexList.size();
            for (int navItem = 0; navItem < indexCount; navItem++) {
                try {
                    String indexText = indexList.get(navItem);
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

                    setupFastScrollingIndexing();
                }
            }, 700);

            LoadInstalledCustomIcons loadInstalledCustomIcons = new LoadInstalledCustomIcons();
            loadInstalledCustomIcons.execute();
        }
    }

    private class LoadInstalledCustomIcons extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                PackageManager packageManager = getApplicationContext().getPackageManager();

                //ACTION: com.novalauncher.THEME
                //CATEGORY: com.novalauncher.category.CUSTOM_ICON_PICKER
                Intent intentCustomIcons = new Intent();
                intentCustomIcons.setAction("com.novalauncher.THEME");
                intentCustomIcons.addCategory("com.novalauncher.category.CUSTOM_ICON_PICKER");
                List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intentCustomIcons, 0);
                try {
                    PublicVariable.customIconsPackages.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (ResolveInfo resolveInfo : resolveInfos) {
                    FunctionsClassDebug.Companion.PrintDebug("CustomIconPackages ::: " + resolveInfo.activityInfo.packageName);

                    PublicVariable.customIconsPackages.add(resolveInfo.activityInfo.packageName);
                }

            } catch (Exception e) {
                e.printStackTrace();
                this.cancel(true);
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
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
                                    nestedScrollView.smoothScrollTo(
                                            0,
                                            ((int) loadView.getChildAt(mapIndexFirstItem.get(mapRangeIndex.get(((int) motionEvent.getY())))).getY())
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
                                nestedScrollView.smoothScrollTo(
                                        0,
                                        ((int) loadView.getChildAt(mapIndexFirstItem.get(mapRangeIndex.get(((int) motionEvent.getY())))).getY())
                                );
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (popupIndex.isShown()) {
                                try {
                                    nestedScrollView.smoothScrollTo(
                                            0,
                                            ((int) loadView.getChildAt(mapIndexFirstItem.get(mapRangeIndex.get(((int) motionEvent.getY())))).getY())
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
