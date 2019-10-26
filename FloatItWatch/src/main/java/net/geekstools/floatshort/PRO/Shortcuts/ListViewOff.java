package net.geekstools.floatshort.PRO.Shortcuts;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import net.geekstools.floatshort.PRO.Automation.RecoveryShortcuts;
import net.geekstools.floatshort.PRO.BuildConfig;
import net.geekstools.floatshort.PRO.Category_Unlimited_Category;
import net.geekstools.floatshort.PRO.Configurations;
import net.geekstools.floatshort.PRO.LicenseValidator;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Shortcuts.NavAdapter.CardListAdapter;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.SettingGUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListViewOff extends WearableActivity implements View.OnClickListener {

    Activity activity;
    FunctionsClass functionsClass;
    RecyclerView loadView;
    RelativeLayout listWhole, loadingSplash;
    ProgressBar loadingBarLTR;
    ImageView newUpdate;

    List<ApplicationInfo> applicationInfoList;
    ArrayList<NavDrawerItem> navDrawerItems;
    RecyclerView.Adapter cardListAdapter;
    LinearLayoutManager recyclerViewLayoutManager;

    String PackageName, AppName = "Application";
    int loadViewPosition = 0, limitedCountLine;
    Drawable AppIcon;

    FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle Saved) {
        super.onCreate(Saved);
        setContentView(R.layout.list_off);
        setAmbientEnabled();

        loadView = (RecyclerView) findViewById(R.id.list);
        listWhole = (RelativeLayout) findViewById(R.id.listWhole);
        newUpdate = (ImageView) findViewById(R.id.newUpdate);

        functionsClass = new FunctionsClass(getApplicationContext());
        activity = this;

        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        loadView.setLayoutManager(recyclerViewLayoutManager);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        LoadApplicationsOffInit loadApplicationsOffInit = new LoadApplicationsOffInit();
        loadApplicationsOffInit.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (functionsClass.checkThemeLightDark() == true) {
            listWhole.setBackgroundColor(getColor(R.color.light_trans));
        } else if (functionsClass.checkThemeLightDark() == false) {
            listWhole.setBackgroundColor(getColor(R.color.trans_black));
        }

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_default);
        firebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(ListViewOff.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.activateFetched();
                            if (firebaseRemoteConfig.getLong(functionsClass.versionCodeRemoteConfigKey()) > functionsClass.appVersionCode(getPackageName())) {
                                Toast.makeText(getApplicationContext(), getString(R.string.updateAvailable), Toast.LENGTH_LONG).show();

                                LayerDrawable layerDrawableNewUpdate = (LayerDrawable) getDrawable(R.drawable.ic_update);
                                BitmapDrawable gradientDrawableNewUpdate = (BitmapDrawable) layerDrawableNewUpdate.findDrawableByLayerId(R.id.ic_launcher_back_layer);
                                gradientDrawableNewUpdate.setTint(PublicVariable.primaryColor);
                                newUpdate.setImageDrawable(layerDrawableNewUpdate);
                                newUpdate.setVisibility(View.VISIBLE);
                            } else {
                            }
                        } else {
                        }
                    }
                });
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
                    functionsClass.dialogueLicense(ListViewOff.this);
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
        if (!getFileStreamPath(".License").exists() && functionsClass.networkConnection() == true) {
            if (!BuildConfig.DEBUG || !functionsClass.appVersionName(getPackageName()).contains("[BETA]")) {
                startService(new Intent(getApplicationContext(), LicenseValidator.class));
            }
        }

        ImageView settingGUI = (ImageView) findViewById(R.id.setting);
        ImageView recovery = (ImageView) findViewById(R.id.recovery);
        settingGUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingGUI.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(getApplicationContext(), RecoveryShortcuts.class));
            }
        });
        recovery.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    if (Category_Unlimited_Category.running == false) {
                        String[] categoryContent = functionsClass.readFileLine(".uFile");
                        if (categoryContent.length > 0) {
                            functionsClass.runUnlimitedCategoryService(getString(R.string.group), categoryContent);
                        }
                    }

                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }
        });

        loadView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                loadViewPosition = recyclerViewLayoutManager.findFirstVisibleItemPosition();
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
        functionsClass.savePreference("LoadView", "LoadViewPosition", recyclerViewLayoutManager.findFirstVisibleItemPosition());
        this.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
    }

    private class LoadApplicationsOffInit extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingSplash = (RelativeLayout) findViewById(R.id.loadingSplash);
            loadingSplash.setBackgroundColor(getColor(R.color.light));

            loadingBarLTR = (ProgressBar) findViewById(R.id.loadingProgressltr);
            loadingBarLTR.getIndeterminateDrawable().setColorFilter(getColor(R.color.dark), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                applicationInfoList = getApplicationContext().getPackageManager().getInstalledApplications(0);
                Collections.sort(applicationInfoList, new ApplicationInfo.DisplayNameComparator(getPackageManager()));
                limitedCountLine = ((int) applicationInfoList.size() / 3);

                for (int appInfo = 0; appInfo < limitedCountLine; appInfo++) {
                    if (getApplicationContext().getPackageManager().getLaunchIntentForPackage(applicationInfoList.get(appInfo).packageName) != null) {
                        try {
                            PackageName = applicationInfoList.get(appInfo).packageName;
                            AppName = functionsClass.appName(PackageName);
                            AppIcon = functionsClass.shapedAppIcon(PackageName);

                            navDrawerItems.add(new NavDrawerItem(AppName, PackageName, AppIcon));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                cardListAdapter = new CardListAdapter(activity, getApplicationContext(), navDrawerItems);
                cardListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                this.cancel(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), Configurations.class));
                    }
                }, 113);
                ListViewOff.this.finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            loadView.setAdapter(cardListAdapter);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
            loadingSplash = (RelativeLayout) findViewById(R.id.loadingSplash);
            loadingSplash.setVisibility(View.INVISIBLE);
            loadingSplash.startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            LoadApplicationsOff loadApplicationsOff = new LoadApplicationsOff();
            loadApplicationsOff.execute();
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
                for (int appInfo = limitedCountLine; appInfo < applicationInfoList.size(); appInfo++) {
                    if (getApplicationContext().getPackageManager().getLaunchIntentForPackage(applicationInfoList.get(appInfo).packageName) != null) {
                        try {
                            PackageName = applicationInfoList.get(appInfo).packageName;
                            AppName = functionsClass.appName(PackageName);
                            AppIcon = functionsClass.shapedAppIcon(PackageName);

                            navDrawerItems.add(new NavDrawerItem(AppName, PackageName, AppIcon));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                cardListAdapter = new CardListAdapter(activity, getApplicationContext(), navDrawerItems);
                cardListAdapter.notifyDataSetChanged();

                functionsClass.savePreference("InstalledApps", "countApps", navDrawerItems.size());
            } catch (Exception e) {
                e.printStackTrace();
                this.cancel(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), Configurations.class));
                    }
                }, 113);
                ListViewOff.this.finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            loadView.setAdapter(cardListAdapter);
            if (loadViewPosition == 0) {
                recyclerViewLayoutManager.scrollToPosition(getSharedPreferences("LoadView", Context.MODE_PRIVATE).getInt("LoadViewPosition", 0));
            } else {
                recyclerViewLayoutManager.scrollToPosition(loadViewPosition);
            }
        }
    }
}
