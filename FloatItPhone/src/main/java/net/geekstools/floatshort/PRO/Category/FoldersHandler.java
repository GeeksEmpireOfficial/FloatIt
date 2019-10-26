package net.geekstools.floatshort.PRO.Category;

import android.animation.Animator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import net.geekstools.floatshort.PRO.Automation.Categories.CategoryAutoFeatures;
import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.BuildConfig;
import net.geekstools.floatshort.PRO.Category.NavAdapter.CategoryListAdapter;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Shortcuts.HybridViewOff;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClassDebug;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.IAP.InAppBilling;
import net.geekstools.floatshort.PRO.Util.LicenseValidator;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryFolders;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryShortcuts;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryWidgets;
import net.geekstools.floatshort.PRO.Util.SettingGUI.SettingGUIDark;
import net.geekstools.floatshort.PRO.Util.SettingGUI.SettingGUILight;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;
import net.geekstools.floatshort.PRO.Util.UI.SimpleGestureFilterSwitch;
import net.geekstools.floatshort.PRO.Widget.WidgetConfigurations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FoldersHandler extends Activity implements View.OnClickListener, View.OnLongClickListener, SimpleGestureFilterSwitch.SimpleGestureListener {

    FunctionsClass functionsClass;

    RelativeLayout fullActionViews, wholeCategory;
    RecyclerView categorylist;
    ImageView actionButton, recoverFloatingApps, recoverFloatingWidgets;
    MaterialButton switchWidgets, switchApps, recoveryAction, automationAction;

    RecyclerView.Adapter categoryListAdapter;
    ArrayList<NavDrawerItem> navDrawerItems;

    String[] appData;

    RelativeLayout loadingSplash;
    ProgressBar loadingBarLTR;
    TextView gx;

    BroadcastReceiver broadcastReceiver;

    SimpleGestureFilterSwitch simpleGestureFilterSwitch;

    LoadCustomIcons loadCustomIcons;

    FirebaseRemoteConfig firebaseRemoteConfig;

    ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    /*Rewarded Ads for Floating Widgets*/
    RewardedVideoAd rewardedVideoAdInstance;
    /*Rewarded Ads for Floating Widgets*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_handler);
        PublicVariable.eligibleLoadShowAds = true;

        wholeCategory = (RelativeLayout) findViewById(R.id.wholeCategory);
        categorylist = (RecyclerView) findViewById(R.id.categorylist);
        fullActionViews = (RelativeLayout) findViewById(R.id.fullActionViews);

        actionButton = (ImageView) findViewById(R.id.actionButton);
        switchWidgets = (MaterialButton) findViewById(R.id.switchWidgets);
        switchApps = (MaterialButton) findViewById(R.id.switchApps);
        recoveryAction = (MaterialButton) findViewById(R.id.recoveryAction);
        automationAction = (MaterialButton) findViewById(R.id.automationAction);
        recoverFloatingApps = (ImageView) findViewById(R.id.recoverFloatingApps);
        recoverFloatingWidgets = (ImageView) findViewById(R.id.recoverFloatingWidgets);

        simpleGestureFilterSwitch = new SimpleGestureFilterSwitch(getApplicationContext(), this);
        functionsClass = new FunctionsClass(getApplicationContext(), this);
        functionsClass.ChangeLog(FoldersHandler.this, false);

        if (functionsClass.appThemeTransparent() == true) {
            functionsClass.setThemeColorFloating(wholeCategory, true);
        } else {
            functionsClass.setThemeColorFloating(wholeCategory, false);
        }
        navDrawerItems = new ArrayList<NavDrawerItem>();

        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), OrientationHelper.VERTICAL, false);
        categorylist.setLayoutManager(recyclerViewLayoutManager);

        ImageView floatingLogo = (ImageView) findViewById(R.id.loadLogo);
        LayerDrawable drawFloatingLogo = (LayerDrawable) getDrawable(R.drawable.draw_floating_logo);
        Drawable backFloatingLogo = drawFloatingLogo.findDrawableByLayerId(R.id.backtemp);
        backFloatingLogo.setTint(PublicVariable.primaryColorOpposite);
        floatingLogo.setImageDrawable(drawFloatingLogo);

        if (functionsClass.loadCustomIcons()) {
            loadCustomIcons = new LoadCustomIcons(getApplicationContext(), functionsClass.customIconPackageName());
        }

        LoadCategory loadCategory = new LoadCategory();
        loadCategory.execute();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Category_Reload");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("Category_Reload")) {
                    LoadCategory loadCategory = new LoadCategory();
                    loadCategory.execute();
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);

        LayerDrawable drawPreferenceAction = (LayerDrawable) getDrawable(R.drawable.draw_pref_action);
        Drawable backPreferenceAction = drawPreferenceAction.findDrawableByLayerId(R.id.backtemp);
        backPreferenceAction.setTint(PublicVariable.primaryColorOpposite);
        actionButton.setImageDrawable(drawPreferenceAction);

        switchWidgets.setTextColor(getColor(R.color.light));
        switchApps.setTextColor(getColor(R.color.light));
        if (PublicVariable.themeLightDark /*light*/ && functionsClass.appThemeTransparent() /*transparent*/) {
            switchWidgets.setTextColor(getColor(R.color.dark));
            switchApps.setTextColor(getColor(R.color.dark));
        }

        switchApps.setBackgroundColor(functionsClass.appThemeTransparent() ? functionsClass.setColorAlpha(PublicVariable.primaryColor, 51) : PublicVariable.primaryColor);
        switchApps.setRippleColor(ColorStateList.valueOf(functionsClass.appThemeTransparent() ? functionsClass.setColorAlpha(PublicVariable.primaryColorOpposite, 51) : PublicVariable.primaryColorOpposite));

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

            recoverFloatingApps.setImageDrawable(drawRecoverFloatingCategories);
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
        switchApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    functionsClass.navigateToClass(HybridViewOff.class,
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_left, R.anim.slide_to_right));
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

                Intent intent = new Intent(getApplicationContext(), CategoryAutoFeatures.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent, ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.up_down, android.R.anim.fade_out).toBundle());
            }
        });
        recoveryAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecoveryFolders.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);
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
                                makeSceneTransitionAnimation(FoldersHandler.this, actionButton, "transition");
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (PublicVariable.themeLightDark) {
                            intent.setClass(FoldersHandler.this, SettingGUILight.class);
                        } else if (!PublicVariable.themeLightDark) {
                            intent.setClass(FoldersHandler.this, SettingGUIDark.class);
                        }
                        startActivity(intent, activityOptionsCompat.toBundle());
                    }
                }, 113);

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
                BillingClient billingClient = BillingClient.newBuilder(FoldersHandler.this).setListener(new PurchasesUpdatedListener() {
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
                BillingClient billingClient = BillingClient.newBuilder(FoldersHandler.this).setListener(new PurchasesUpdatedListener() {
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
                    functionsClass.dialogueLicense(FoldersHandler.this);
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

                    progressDialog = new ProgressDialog(FoldersHandler.this,
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
                .addOnCompleteListener(FoldersHandler.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.activate().addOnSuccessListener(new OnSuccessListener<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    if (((int) firebaseRemoteConfig.getLong(functionsClass.versionCodeRemoteConfigKey())) > functionsClass.appVersionCode(getPackageName())) {
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
                                                        FoldersHandler.this,
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
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        PublicVariable.eligibleLoadShowAds = false;

        if (PublicVariable.actionCenter == true) {
            functionsClass.closeActionMenuOption(fullActionViews, actionButton);
        }

        functionsClass.savePreference("OpenMode", "openClassName", this.getClass().getSimpleName());
        functionsClass.CheckSystemRAM(FoldersHandler.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PublicVariable.inMemory = false;

        functionsClass.endIndexAppInfo();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent homeScreen = new Intent(Intent.ACTION_MAIN);
        homeScreen.addCategory(Intent.CATEGORY_HOME);
        homeScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeScreen, ActivityOptions.makeCustomAnimation(getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle());
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onLongClick(View view) {
        return true;
    }

    @Override
    public void onSwipe(int direction) {
        switch (direction) {
            case SimpleGestureFilterSwitch.SWIPE_RIGHT: {
                try {
                    functionsClass.navigateToClass(HybridViewOff.class,
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_left, R.anim.slide_to_right));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case SimpleGestureFilterSwitch.SWIPE_LEFT: {
                try {


                    if (rewardedVideoAdInstance.isLoaded()) {
                        rewardedVideoAdInstance.show();
                    } else {
                        try {
                            functionsClass.navigateToClass(WidgetConfigurations.class,
                                    ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_from_right, R.anim.slide_to_left));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
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

    public class LoadCategory extends AsyncTask<Void, Void, Void> {
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
            gx = (TextView) findViewById(R.id.gx);
            Typeface face = Typeface.createFromAsset(getAssets(), "upcil.ttf");
            gx.setTypeface(face);

            if (PublicVariable.themeLightDark) {
                loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.themeTextColor, android.graphics.PorterDuff.Mode.MULTIPLY);
                gx.setTextColor(getColor(R.color.dark));
            } else {
                loadingBarLTR.getIndeterminateDrawable().setColorFilter(PublicVariable.themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
                gx.setTextColor(getColor(R.color.light));
            }
            if (!getFileStreamPath(".categoryInfo").exists()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                        loadingSplash.setVisibility(View.INVISIBLE);
                        loadingSplash.startAnimation(anim);
                    }
                }, 200);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                navDrawerItems = new ArrayList<NavDrawerItem>();

                if (functionsClass.loadCustomIcons()) {
                    loadCustomIcons.load();
                    FunctionsClassDebug.Companion.PrintDebug("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIcons());
                }

                if (getFileStreamPath(".categoryInfo").exists() && functionsClass.countLineInnerFile(".categoryInfo") > 0) {
                    try {
                        appData = functionsClass.readFileLine(".categoryInfo");
                        navDrawerItems = new ArrayList<NavDrawerItem>();
                        for (int navItem = 0; navItem < appData.length; navItem++) {
                            try {
                                navDrawerItems.add(new NavDrawerItem(
                                        appData[navItem],
                                        functionsClass.readFileLine(appData[navItem])));
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                functionsClass.IndexAppInfoCategory(appData[navItem] + " | " + getString(R.string.floatingCategory));
                            }
                        }
                        navDrawerItems.add(new NavDrawerItem(getPackageName(), new String[]{getPackageName()}));
                        categoryListAdapter = new CategoryListAdapter(FoldersHandler.this, getApplicationContext(), navDrawerItems);
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.cancel(true);
                    }
                } else {
                    navDrawerItems = new ArrayList<NavDrawerItem>();
                    navDrawerItems.add(new NavDrawerItem(getPackageName(), new String[]{getPackageName()}));
                    categoryListAdapter = new CategoryListAdapter(FoldersHandler.this, getApplicationContext(), navDrawerItems);
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.cancel(true);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            categorylist.setAdapter(categoryListAdapter);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                    loadingSplash = (RelativeLayout) findViewById(R.id.loadingSplash);
                    loadingSplash.setVisibility(View.INVISIBLE);
                    loadingSplash.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            categorylist.scrollTo(0, 0);
                            ((LinearLayout) findViewById(R.id.switchFloating)).setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }, 333);

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
}
