package net.geekstools.floatshort.PRO.Category;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import net.geekstools.floatshort.PRO.Category.NavAdapter.AppSavedListAdapter;
import net.geekstools.floatshort.PRO.Category.NavAdapter.AppSelectionListAdapter;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;
import net.geekstools.imageview.customshapes.ShapesImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppSelectionList extends Activity implements View.OnClickListener {

    FunctionsClass functionsClass;

    ListPopupWindow listPopupWindow;
    RelativeLayout popupAnchorView;
    ScrollView nestedScrollView, nestedIndexScrollView;
    RecyclerView loadView;
    RelativeLayout wholeAuto, confirmLayout;
    LinearLayout indexView, splitView;
    RelativeLayout loadingSplash;
    TextView desc, popupIndex, counterView, splitHint;
    ImageView loadIcon;
    ShapesImage tempIcon, one, two;
    MaterialButton categoryName;

    List<ApplicationInfo> applicationInfoList;
    Map<String, Integer> mapIndexFirstItem, mapIndexLastItem;
    Map<Integer, String> mapRangeIndex;
    ArrayList<NavDrawerItem> navDrawerItems, navDrawerItemsSaved;
    RecyclerView.Adapter appSelectionListAdapter;
    LinearLayoutManager recyclerViewLayoutManager;
    AppSavedListAdapter advanceSavedListAdapter;

    String PackageName, AppName = "Application";
    Drawable AppIcon;

    boolean resetAdapter = false;

    LoadCustomIcons loadCustomIcons;

    @Override
    protected void onCreate(Bundle Saved) {
        super.onCreate(Saved);
        setContentView(R.layout.advance_app_selection_list);

        functionsClass = new FunctionsClass(getApplicationContext(), this);

        listPopupWindow = new ListPopupWindow(AppSelectionList.this);
        desc = (TextView) findViewById(R.id.desc);
        counterView = (TextView) findViewById(R.id.counter);
        loadIcon = (ImageView) findViewById(R.id.loadLogo);
        nestedScrollView = (ScrollView) findViewById(R.id.nestedScrollView);
        nestedIndexScrollView = (ScrollView) findViewById(R.id.nestedIndexScrollView);
        loadView = (RecyclerView) findViewById(R.id.listFav);
        popupAnchorView = (RelativeLayout) findViewById(R.id.popupAnchorView);
        indexView = (LinearLayout) findViewById(R.id.side_index);
        splitView = (LinearLayout) findViewById(R.id.splitView);
        wholeAuto = (RelativeLayout) findViewById(R.id.wholeAuto);
        loadingSplash = (RelativeLayout) findViewById(R.id.loadingSplash);
        tempIcon = (ShapesImage) findViewById(R.id.tempIcon);
        tempIcon.bringToFront();
        one = functionsClass.initShapesImage(AppSelectionList.this, R.id.one);
        two = functionsClass.initShapesImage(AppSelectionList.this, R.id.two);
        splitHint = (TextView) findViewById(R.id.splitHint);
        confirmLayout = (RelativeLayout) findViewById(R.id.confirmLayout);
        confirmLayout.bringToFront();
        categoryName = (MaterialButton) findViewById(R.id.categoryName);
        popupIndex = (TextView) findViewById(R.id.popupIndex);

        if (functionsClass.appThemeTransparent() == true) {
            functionsClass.setThemeColorFloating(wholeAuto, true);
        } else {
            functionsClass.setThemeColorFloating(wholeAuto, false);
        }

        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext(), OrientationHelper.VERTICAL, false);
        loadView.setLayoutManager(recyclerViewLayoutManager);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        navDrawerItemsSaved = new ArrayList<NavDrawerItem>();
        mapIndexFirstItem = new LinkedHashMap<String, Integer>();
        mapIndexLastItem = new LinkedHashMap<String, Integer>();
        mapRangeIndex = new LinkedHashMap<Integer, String>();

        Typeface face = Typeface.createFromAsset(getAssets(), "upcil.ttf");
        desc.setTypeface(face);
        desc.setTextColor(PublicVariable.colorLightDarkOpposite);
        desc.setText(PublicVariable.categoryName);
        counterView.setTypeface(face);
        counterView.bringToFront();

        if (functionsClass.loadRecoveryIndicatorCategory(PublicVariable.categoryName)) {
            categoryName.setText(PublicVariable.categoryName + " " + "\uD83D\uDD04");
        } else {
            categoryName.setText(PublicVariable.categoryName);
        }

        categoryName.setBackgroundColor(functionsClass.appThemeTransparent() ? functionsClass.setColorAlpha(PublicVariable.primaryColor, 51) : PublicVariable.primaryColor);
        categoryName.setRippleColor(ColorStateList.valueOf(functionsClass.appThemeTransparent() ? functionsClass.setColorAlpha(PublicVariable.primaryColorOpposite, 51) : PublicVariable.primaryColorOpposite));

        LayerDrawable layerDrawableLoadLogo = (LayerDrawable) getDrawable(R.drawable.ic_launcher_layer);
        BitmapDrawable gradientDrawableLoadLogo = (BitmapDrawable) layerDrawableLoadLogo.findDrawableByLayerId(R.id.ic_launcher_back_layer);
        gradientDrawableLoadLogo.setTint(PublicVariable.primaryColor);
        loadIcon.setImageDrawable(layerDrawableLoadLogo);

        ProgressBar loadingBarLTR = (ProgressBar) findViewById(R.id.loadingProgressltr);
        loadingBarLTR.getIndeterminateDrawable().setColorFilter(getColor(R.color.dark), PorterDuff.Mode.MULTIPLY);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getString(R.string.counterActionAdvance));
        intentFilter.addAction(getString(R.string.savedActionAdvance));
        intentFilter.addAction(getString(R.string.savedActionHideAdvance));
        intentFilter.addAction(getString(R.string.checkboxActionAdvance));
        intentFilter.addAction(getString(R.string.splitActionAdvance));
        BroadcastReceiver counterReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(context.getString(R.string.counterActionAdvance))) {
                    counterView.setText(String.valueOf(functionsClass.countLineInnerFile(PublicVariable.categoryName)));
                } else if (intent.getAction().equals(context.getString(R.string.savedActionAdvance))) {
                    if (getFileStreamPath(PublicVariable.categoryName).exists() && functionsClass.countLineInnerFile(PublicVariable.categoryName) > 0) {
                        navDrawerItemsSaved.clear();
                        String[] savedLine = functionsClass.readFileLine(PublicVariable.categoryName);
                        for (String aSavedLine : savedLine) {
                            navDrawerItemsSaved.add(new NavDrawerItem(
                                    functionsClass.appName(aSavedLine),
                                    aSavedLine,
                                    functionsClass.loadCustomIcons() ?
                                            loadCustomIcons.getDrawableIconForPackage(aSavedLine, functionsClass.shapedAppIcon(aSavedLine))
                                            :
                                            functionsClass.shapedAppIcon(aSavedLine)));
                        }
                        advanceSavedListAdapter = new AppSavedListAdapter(AppSelectionList.this, getApplicationContext(), navDrawerItemsSaved, 1);
                        listPopupWindow = new ListPopupWindow(AppSelectionList.this);
                        listPopupWindow.setAdapter(advanceSavedListAdapter);
                        listPopupWindow.setAnchorView(popupAnchorView);
                        listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                        listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                        listPopupWindow.setModal(true);
                        listPopupWindow.setBackgroundDrawable(null);
                        try {
                            listPopupWindow.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (functionsClass.returnAPI() < 23) {
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
                        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                sendBroadcast(new Intent(getString(R.string.visibilityActionAdvance)));
                            }
                        });
                    }
                } else if (intent.getAction().equals(getString(R.string.savedActionHideAdvance))) {
                    try {
                        listPopupWindow.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if ((intent.getAction().equals(getString(R.string.checkboxActionAdvance)))) {
                    resetAdapter = true;
                    loadDataOff();
                    listPopupWindow.dismiss();
                    sendBroadcast(new Intent(getString(R.string.visibilityActionAdvance)));
                } else if (intent.getAction().equals(getString(R.string.splitActionAdvance))) {
                    listPopupWindow.dismiss();


                    if (getFileStreamPath(PublicVariable.categoryName + ".SplitOne").exists()) {
                        one.setImageDrawable(functionsClass.loadCustomIcons() ?
                                loadCustomIcons.getDrawableIconForPackage(functionsClass.readFile(PublicVariable.categoryName + ".SplitOne"), functionsClass.shapedAppIcon(functionsClass.readFile(PublicVariable.categoryName + ".SplitOne")))
                                :
                                functionsClass.shapedAppIcon(functionsClass.readFile(PublicVariable.categoryName + ".SplitOne")));
                    }
                    if (getFileStreamPath(PublicVariable.categoryName + ".SplitTwo").exists()) {
                        two.setImageDrawable(functionsClass.loadCustomIcons() ?
                                loadCustomIcons.getDrawableIconForPackage(functionsClass.readFile(PublicVariable.categoryName + ".SplitTwo"), functionsClass.shapedAppIcon(functionsClass.readFile(PublicVariable.categoryName + ".SplitOne")))
                                :
                                functionsClass.shapedAppIcon(functionsClass.readFile(PublicVariable.categoryName + ".SplitTwo")));
                    }
                }
            }
        };
        registerReceiver(counterReceiver, intentFilter);

        loadDataOff();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (functionsClass.returnAPI() < 24) {
            one.setVisibility(View.INVISIBLE);
            two.setVisibility(View.INVISIBLE);
            splitHint.setVisibility(View.INVISIBLE);

            int padTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
            loadView.setPaddingRelative(
                    loadView.getPaddingStart(),
                    padTop,
                    loadView.getPaddingEnd(),
                    loadView.getPaddingBottom());

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            nestedScrollView.setLayoutParams(params);
        } else {
            splitHint.setTextColor(PublicVariable.primaryColorOpposite);
            if (getFileStreamPath(PublicVariable.categoryName + ".SplitOne").exists()) {
                one.setImageDrawable(functionsClass.loadCustomIcons() ?
                        loadCustomIcons.getDrawableIconForPackage(functionsClass.readFile(PublicVariable.categoryName + ".SplitOne"), functionsClass.shapedAppIcon(functionsClass.readFile(PublicVariable.categoryName + ".SplitOne")))
                        :
                        functionsClass.shapedAppIcon(functionsClass.readFile(PublicVariable.categoryName + ".SplitOne")));
            } else {
                Drawable addOne = getDrawable(R.drawable.add_quick_app);
                addOne.setTint(functionsClass.setColorAlpha(PublicVariable.primaryColorOpposite, 175));
                one.setImageDrawable(addOne);
            }
            if (getFileStreamPath(PublicVariable.categoryName + ".SplitTwo").exists()) {
                two.setImageDrawable(functionsClass.loadCustomIcons() ?
                        loadCustomIcons.getDrawableIconForPackage(functionsClass.readFile(PublicVariable.categoryName + ".SplitTwo"), functionsClass.shapedAppIcon(functionsClass.readFile(PublicVariable.categoryName + ".SplitOne")))
                        :
                        functionsClass.shapedAppIcon(functionsClass.readFile(PublicVariable.categoryName + ".SplitTwo")));

            } else {
                Drawable addTwo = getDrawable(R.drawable.add_quick_app);
                addTwo.setTint(functionsClass.setColorAlpha(PublicVariable.primaryColorOpposite, 175));
                two.setImageDrawable(addTwo);
            }
            one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getFileStreamPath(PublicVariable.categoryName).exists() && functionsClass.countLineInnerFile(PublicVariable.categoryName) > 0) {
                        navDrawerItemsSaved.clear();
                        String[] savedLine = functionsClass.readFileLine(PublicVariable.categoryName);
                        for (String aSavedLine : savedLine) {
                            navDrawerItemsSaved.add(new NavDrawerItem(
                                    functionsClass.appName(aSavedLine),
                                    aSavedLine,
                                    functionsClass.loadCustomIcons() ?
                                            loadCustomIcons.getDrawableIconForPackage(aSavedLine, functionsClass.shapedAppIcon(aSavedLine))
                                            :
                                            functionsClass.shapedAppIcon(aSavedLine)));
                        }
                        advanceSavedListAdapter = new AppSavedListAdapter(AppSelectionList.this, getApplicationContext(), navDrawerItemsSaved, 1);
                        listPopupWindow = new ListPopupWindow(AppSelectionList.this);
                        listPopupWindow.setAdapter(advanceSavedListAdapter);
                        listPopupWindow.setAnchorView(popupAnchorView);
                        listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                        listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                        listPopupWindow.setModal(true);
                        listPopupWindow.setBackgroundDrawable(null);
                        try {
                            listPopupWindow.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            sendBroadcast(new Intent(getString(R.string.setDismissAdvance)));
                        }
                        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                sendBroadcast(new Intent(getString(R.string.visibilityActionAdvance)));
                            }
                        });
                    }
                }
            });
            two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getFileStreamPath(PublicVariable.categoryName).exists() && functionsClass.countLineInnerFile(PublicVariable.categoryName) > 0) {
                        navDrawerItemsSaved.clear();
                        String[] savedLine = functionsClass.readFileLine(PublicVariable.categoryName);
                        for (String aSavedLine : savedLine) {
                            navDrawerItemsSaved.add(new NavDrawerItem(
                                    functionsClass.appName(aSavedLine),
                                    aSavedLine,
                                    functionsClass.loadCustomIcons() ?
                                            loadCustomIcons.getDrawableIconForPackage(aSavedLine, functionsClass.shapedAppIcon(aSavedLine))
                                            :
                                            functionsClass.shapedAppIcon(aSavedLine)));
                        }
                        advanceSavedListAdapter = new AppSavedListAdapter(AppSelectionList.this, getApplicationContext(), navDrawerItemsSaved, 2);
                        listPopupWindow = new ListPopupWindow(AppSelectionList.this);
                        listPopupWindow.setAdapter(advanceSavedListAdapter);
                        listPopupWindow.setAnchorView(popupAnchorView);
                        listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                        listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                        listPopupWindow.setModal(true);
                        listPopupWindow.setBackgroundDrawable(null);
                        try {
                            listPopupWindow.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            sendBroadcast(new Intent(getString(R.string.setDismissAdvance)));
                        }
                        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                sendBroadcast(new Intent(getString(R.string.visibilityActionAdvance)));
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            functionsClass.navigateToClass(CategoryHandler.class, AppSelectionList.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

    }

    public void loadDataOff() {
        if (functionsClass.loadCustomIcons()) {
            loadCustomIcons = new LoadCustomIcons(getApplicationContext(), functionsClass.customIconPackageName());
        }

        LoadApplicationsOffLimited loadApplicationsOffLimited = new LoadApplicationsOffLimited();
        loadApplicationsOffLimited.execute();
    }

    private class LoadApplicationsOffLimited extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            indexView.removeAllViews();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                applicationInfoList = getApplicationContext().getPackageManager().getInstalledApplications(0);
                Collections.sort(applicationInfoList, new ApplicationInfo.DisplayNameComparator(getPackageManager()));

                for (int appInfo = 0; appInfo < applicationInfoList.size(); appInfo++) {
                    if (getApplicationContext().getPackageManager().getLaunchIntentForPackage(applicationInfoList.get(appInfo).packageName) != null) {
                        try {
                            PackageName = applicationInfoList.get(appInfo).packageName;
                            AppName = functionsClass.appName(PackageName);
//                            AppIcon = functionsClass.shapedAppIcon(PackageName);
                            AppIcon = functionsClass.loadCustomIcons() ? loadCustomIcons.getDrawableIconForPackage(PackageName, functionsClass.shapedAppIcon(PackageName)) : functionsClass.shapedAppIcon(PackageName);

                            navDrawerItems.add(new NavDrawerItem(AppName, PackageName, AppIcon));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                appSelectionListAdapter = new AppSelectionListAdapter(AppSelectionList.this, getApplicationContext(), navDrawerItems);
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
            loadView.setAdapter(appSelectionListAdapter);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    categoryName.setVisibility(View.VISIBLE);

                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                    loadingSplash.setVisibility(View.INVISIBLE);
                    if (resetAdapter == false) {
                        loadingSplash.startAnimation(anim);
                    }
                    sendBroadcast(new Intent(getString(R.string.visibilityActionAdvance)));

                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                    counterView.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            counterView.setText(String.valueOf(functionsClass.countLineInnerFile(PublicVariable.categoryName)));
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            counterView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    resetAdapter = false;
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

                    mapIndexLastItem.put(index, itemCount);
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
