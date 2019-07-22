package net.geekstools.floatshort.PRO;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.RelativeLayout;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.InteractionObserver.InteractionObserver;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;
import net.geekstools.imageview.customshapes.ShapesImage;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class Category_Unlimited_Gps extends Service {

    FunctionsClass functionsClass;
    WindowManager windowManager;
    WindowManager.LayoutParams[] layoutParams;
    WindowManager.LayoutParams[] StickyEdgeParams;

    ViewGroup[] floatingView;
    RelativeLayout wholeCategoryFloating;
    ShapesImage one, two, three, four;
    ShapesImage[] pin, notificationDot;

    int xPos, yPos, xInit = 19, yInit = 19, xMove, yMove;

    int array, categorySize;
    String[] categoryName;
    boolean[] allowMove, touchingDelay, trans, StickyEdge, openIt;

    String notificationPackage;
    boolean showNotificationDot = false;

    BroadcastReceiver broadcastReceiver;
    SharedPreferences sharedPrefPosition;

    Map<String, String> mapContentCategoryName;
    Map<String, Integer> mapCategoryNameStartId;

    Runnable runnablePressHold = null;
    Handler handlerPressHold = new Handler();

    LoadCustomIcons loadCustomIcons;

    GestureDetector.SimpleOnGestureListener[] simpleOnGestureListener;
    GestureDetector[] gestureDetector;

    FlingAnimation[] flingAnimationX, flingAnimationY;

    float flingPositionX = 0, flingPositionY = 0;

    int startIdCounter = 1;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                for (int J = 1; J <= startIdCounter; J++) {
                    try {
                        if (floatingView != null) {
                            if (floatingView[J].isShown()) {
                                layoutParams[J] = functionsClass.handleOrientationPortrait(categoryName[J], layoutParams[J].height);
                                windowManager.updateViewLayout(floatingView[J], layoutParams[J]);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {
                for (int J = 1; J <= startIdCounter; J++) {
                    try {
                        if (floatingView != null) {
                            if (floatingView[J].isShown()) {
                                layoutParams[J] = functionsClass.handleOrientationLandscape(categoryName[J], layoutParams[J].height);
                                windowManager.updateViewLayout(floatingView[J], layoutParams[J]);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            }
            default: {

                break;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        FunctionsClass.println(this.getClass().getSimpleName() + " ::: StartId ::: " + startId);
        startIdCounter = startId;

        if (functionsClass.loadCustomIcons()) {
            if (loadCustomIcons == null) {
                loadCustomIcons = new LoadCustomIcons(getApplicationContext(), functionsClass.customIconPackageName());
            }
        }

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        try {
            categoryName[startId] = intent.getStringExtra("categoryName");

            touchingDelay[startId] = false;
            StickyEdge[startId] = false;
            allowMove[startId] = true;
            openIt[startId] = true;
        } catch (Exception e) {
            e.printStackTrace();
            return Service.START_NOT_STICKY;
        }

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        floatingView[startId] = (ViewGroup) layoutInflater.inflate(R.layout.floating_category_medium, null, false);
        if (PublicVariable.size == 13 || PublicVariable.size == 26) {
            floatingView[startId] = (ViewGroup) layoutInflater.inflate(R.layout.floating_category_small, null, false);
            categorySize = 24;
        } else if (PublicVariable.size == 39 || PublicVariable.size == 52) {
            floatingView[startId] = (ViewGroup) layoutInflater.inflate(R.layout.floating_category_medium, null, false);
            categorySize = 36;
        } else if (PublicVariable.size == 65 || PublicVariable.size == 78) {
            floatingView[startId] = (ViewGroup) layoutInflater.inflate(R.layout.floating_category_large, null, false);
            categorySize = 48;
        }

        wholeCategoryFloating = floatingView[startId].findViewById(R.id.wholeCategoryFloating);
        one = functionsClass.initShapesImage(floatingView[startId], R.id.one);
        two = functionsClass.initShapesImage(floatingView[startId], R.id.two);
        three = functionsClass.initShapesImage(floatingView[startId], R.id.three);
        four = functionsClass.initShapesImage(floatingView[startId], R.id.four);
        pin[startId] = functionsClass.initShapesImage(floatingView[startId], R.id.pin);
        notificationDot[startId] = functionsClass.initShapesImage(floatingView[startId],
                functionsClass.checkStickyEdge() ? R.id.notificationDotEnd : R.id.notificationDotStart);

        Drawable drawableBack = null;
        switch (functionsClass.shapesImageId()) {
            case 1:
                drawableBack = getDrawable(R.drawable.category_droplet_icon);
                break;
            case 2:
                drawableBack = getDrawable(R.drawable.category_circle_icon);
                break;
            case 3:
                drawableBack = getDrawable(R.drawable.category_square_icon);
                break;
            case 4:
                drawableBack = getDrawable(R.drawable.category_squircle_icon);
                break;
            case 0:
                drawableBack = null;
                break;
        }
        if (drawableBack != null) {
            drawableBack.setTint(PublicVariable.primaryColor);
            drawableBack.setAlpha(functionsClass.readDefaultPreference("autoTrans", 255));
        }
        wholeCategoryFloating.setBackground(drawableBack);

        if (categoryName[startId].equals(getString(R.string.remove_all_floatings))) {
            for (int r = 1; r < startId; r++) {
                try {
                    if (floatingView != null) {
                        if (floatingView[r].isShown()) {
                            try {
                                windowManager.removeView(floatingView[r]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                PublicVariable.floatingCounter = PublicVariable.floatingCounter - 1;

                                if (PublicVariable.floatingCounter == 0) {
                                    if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                            .getBoolean("stable", true) == false) {
                                        stopService(new Intent(getApplicationContext(), BindServices.class));
                                    }
                                }
                            }
                        } else if (PublicVariable.floatingCounter == 0) {
                            if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                    .getBoolean("stable", true) == false) {
                                stopService(new Intent(getApplicationContext(), BindServices.class));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            PublicVariable.FloatingCategories.clear();
            PublicVariable.categoriesCounter = -1;
            try {
                if (broadcastReceiver != null) {
                    try {
                        unregisterReceiver(broadcastReceiver);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            stopSelf();
            return START_NOT_STICKY;
        }
        mapCategoryNameStartId.put(categoryName[startId], startId);
        String[] categoryApps = functionsClass.readFileLine(categoryName[startId]);
        if (categoryApps != null) {
            if (categoryApps.length > 0) {
                for (int i = 0; i < categoryApps.length; i++) {
                    mapContentCategoryName.put(categoryApps[i], categoryName[startId]);
                    if (getFileStreamPath(categoryApps[i] + "_" + "Notification" + "Package").exists()) {
                        showNotificationDot = true;
                        notificationPackage = categoryApps[i];
                    }
                }
            }
            try {
                one.setImageDrawable(functionsClass.loadCustomIcons() ?
                        loadCustomIcons.getDrawableIconForPackage(categoryApps[0], functionsClass.shapedAppIcon(categoryApps[0]))
                        :
                        functionsClass.shapedAppIcon(categoryApps[0]));
                one.setImageAlpha(functionsClass.readDefaultPreference("autoTrans", 255));
            } catch (Exception e) {
                one.setImageDrawable(null);
            }
            try {
                two.setImageDrawable(functionsClass.loadCustomIcons() ?
                        loadCustomIcons.getDrawableIconForPackage(categoryApps[1], functionsClass.shapedAppIcon(categoryApps[1]))
                        :
                        functionsClass.shapedAppIcon(categoryApps[1]));
                two.setImageAlpha(functionsClass.readDefaultPreference("autoTrans", 255));
            } catch (Exception e) {
                two.setImageDrawable(null);
            }
            try {
                three.setImageDrawable(functionsClass.loadCustomIcons() ?
                        loadCustomIcons.getDrawableIconForPackage(categoryApps[2], functionsClass.shapedAppIcon(categoryApps[2]))
                        :
                        functionsClass.shapedAppIcon(categoryApps[2]));
                three.setImageAlpha(functionsClass.readDefaultPreference("autoTrans", 255));
            } catch (Exception e) {
                three.setImageDrawable(null);
            }
            try {
                four.setImageDrawable(functionsClass.loadCustomIcons() ?
                        loadCustomIcons.getDrawableIconForPackage(categoryApps[3], functionsClass.shapedAppIcon(categoryApps[3]))
                        :
                        functionsClass.shapedAppIcon(categoryApps[3]));
                four.setImageAlpha(functionsClass.readDefaultPreference("autoTrans", 255));
            } catch (Exception e) {
                four.setImageDrawable(null);
            }
        }

        int HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, categorySize * 2, this.getResources().getDisplayMetrics());
        String nameForPosition = categoryName[startId];
        sharedPrefPosition = getSharedPreferences(nameForPosition, MODE_PRIVATE);
        xInit = xInit + 13;
        yInit = yInit + 13;
        xPos = sharedPrefPosition.getInt("X", xInit);
        yPos = sharedPrefPosition.getInt("Y", yInit);

        layoutParams[startId] = functionsClass.normalLayoutParams(HW, xPos, yPos);
        windowManager.addView(floatingView[startId], layoutParams[startId]);

        final String className = Category_Unlimited_Gps.class.getSimpleName();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Split_Apps_Pair_" + className);
        intentFilter.addAction("Split_Apps_Single_" + className);
        intentFilter.addAction("Pin_App_" + className);
        intentFilter.addAction("Unpin_App_" + className);
        intentFilter.addAction("Remove_Category_" + className);
        intentFilter.addAction("Sticky_Edge");
        intentFilter.addAction("Sticky_Edge_No");
        intentFilter.addAction("Notification_Dot");
        intentFilter.addAction("Notification_Dot_No");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("Split_Apps_Pair_" + className) && PublicVariable.splitScreen == true) {
                    FunctionsClass.println("Split Apps Pair");
                    PublicVariable.splitScreen = false;

                    final String packageNameSplitOne, packageNameSplitTwo;
                    if (getFileStreamPath(PublicVariable.splitPairPackage + ".SplitOne").exists()
                            && getFileStreamPath(PublicVariable.splitPairPackage + ".SplitTwo").exists()) {
                        packageNameSplitOne = functionsClass.readFile(PublicVariable.splitPairPackage + ".SplitOne");
                        packageNameSplitTwo = functionsClass.readFile(PublicVariable.splitPairPackage + ".SplitTwo");
                    } else {
                        packageNameSplitOne = functionsClass.readFileLine(PublicVariable.splitPairPackage)[0];
                        packageNameSplitTwo = functionsClass.readFileLine(PublicVariable.splitPairPackage)[1];
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Intent spliteOne = getPackageManager().getLaunchIntentForPackage(packageNameSplitOne);
                                spliteOne.setFlags(
                                        Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT |
                                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                                final Intent spliteTwo = getPackageManager().getLaunchIntentForPackage(packageNameSplitTwo);
                                spliteTwo.setFlags(
                                        Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT |
                                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                                startActivity(spliteOne);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(spliteTwo);
                                        PublicVariable.splitScreen = true;
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                sendBroadcast(new Intent("split_pair_finish"));
                                            }
                                        }, 700);
                                    }
                                }, 200);

                                functionsClass.Toast(functionsClass.appName(packageNameSplitOne), Gravity.TOP);
                                functionsClass.Toast(functionsClass.appName(packageNameSplitTwo), Gravity.BOTTOM);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 700);
                } else if (intent.getAction().equals("Split_Apps_Single_" + className) && PublicVariable.splitScreen == true) {
                    FunctionsClass.println("Split Apps Single");
                    PublicVariable.splitScreen = false;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Intent splitSingle = getPackageManager().getLaunchIntentForPackage(PublicVariable.splitSinglePackage);
                                splitSingle.setFlags(
                                        Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT |
                                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                startActivity(splitSingle);
                                PublicVariable.splitScreen = true;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sendBroadcast(new Intent("split_single_finish"));
                                    }
                                }, 500);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 200);
                } else if (intent.getAction().equals("Pin_App_" + className)) {
                    allowMove[intent.getIntExtra("startId", 1)] = false;
                    Drawable drawableBack = null;
                    switch (functionsClass.shapesImageId()) {
                        case 1:
                            drawableBack = getDrawable(R.drawable.pin_droplet_icon);
                            drawableBack.setTint(context.getColor(R.color.red_transparent));
                            break;
                        case 2:
                            drawableBack = getDrawable(R.drawable.pin_circle_icon);
                            drawableBack.setTint(context.getColor(R.color.red_transparent));
                            break;
                        case 3:
                            drawableBack = getDrawable(R.drawable.pin_square_icon);
                            drawableBack.setTint(context.getColor(R.color.red_transparent));
                            break;
                        case 4:
                            drawableBack = getDrawable(R.drawable.pin_squircle_icon);
                            drawableBack.setTint(context.getColor(R.color.red_transparent));
                            break;
                        case 0:
                            drawableBack = getDrawable(R.drawable.pin_noshap);
                            drawableBack.setTint(context.getColor(R.color.red_transparent));
                            break;
                    }
                    pin[intent.getIntExtra("startId", 1)].setImageDrawable(drawableBack);
                } else if (intent.getAction().equals("Unpin_App_" + className)) {
                    allowMove[intent.getIntExtra("startId", 1)] = true;
                    pin[intent.getIntExtra("startId", 1)].setImageDrawable(null);
                } else if (intent.getAction().equals("Remove_Category_" + className)) {
                    try {
                        if (floatingView != null) {
                            if (floatingView[intent.getIntExtra("startId", 1)] == null) {
                                return;
                            }
                            if (floatingView[intent.getIntExtra("startId", 1)].isShown()) {
                                try {
                                    windowManager.removeView(floatingView[intent.getIntExtra("startId", 1)]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    PublicVariable.FloatingCategories.remove(categoryName[intent.getIntExtra("startId", 1)]);
                                    PublicVariable.floatingCounter = PublicVariable.floatingCounter - 1;
                                    PublicVariable.floatingCategoryCounter_gps = PublicVariable.floatingCategoryCounter_gps - 1;
                                    PublicVariable.categoriesCounter = PublicVariable.categoriesCounter - 1;

                                    if (PublicVariable.floatingCounter == 0) {
                                        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                                .getBoolean("stable", true) == false) {
                                            stopService(new Intent(getApplicationContext(), BindServices.class));
                                        }
                                    }
                                    if (PublicVariable.floatingCategoryCounter_gps == 0) {
                                        if (broadcastReceiver != null) {
                                            try {
                                                unregisterReceiver(broadcastReceiver);
                                            } catch (IllegalArgumentException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        stopSelf();
                                    }
                                }
                            } else if (PublicVariable.floatingCounter == 0) {
                                if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                        .getBoolean("stable", true) == false) {
                                    stopService(new Intent(getApplicationContext(), BindServices.class));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (intent.getAction().equals("Sticky_Edge")) {
                    for (int r = 1; r <= startId; r++) {
                        try {
                            if (floatingView != null) {
                                if (floatingView[r].isShown()) {
                                    try {
                                        StickyEdge[r] = true;
                                        StickyEdgeParams[r] = functionsClass.moveToEdge(categoryName[r], layoutParams[r].height);
                                        windowManager.updateViewLayout(floatingView[r], StickyEdgeParams[r]);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (intent.getAction().equals("Sticky_Edge_No")) {
                    for (int r = 1; r <= startId; r++) {
                        try {
                            if (floatingView != null) {
                                if (floatingView[r].isShown()) {
                                    try {
                                        try {
                                            sharedPrefPosition = getSharedPreferences((categoryName[r]), MODE_PRIVATE);

                                            StickyEdge[r] = false;
                                            xPos = sharedPrefPosition.getInt("X", xInit);
                                            yPos = sharedPrefPosition.getInt("Y", yInit);
                                            windowManager.updateViewLayout(floatingView[r], functionsClass.backFromEdge(layoutParams[r].height, xPos, yPos));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (intent.getAction().equals("Notification_Dot")) {
                    try {
                        String notificationPackage = intent.getStringExtra("NotificationPackage");
                        String categoryNameNotification = mapContentCategoryName.get(notificationPackage);
                        int StartIdNotification = mapCategoryNameStartId.get(categoryNameNotification);
                        if (floatingView[StartIdNotification] != null) {
                            if (floatingView[StartIdNotification].isShown()) {
                                /*add dot*/
                                Drawable dotDrawable =
                                        functionsClass.loadCustomIcons() ?
                                                loadCustomIcons.getDrawableIconForPackage(notificationPackage, functionsClass.shapedAppIcon(notificationPackage).mutate()).mutate()
                                                :
                                                functionsClass.shapedAppIcon(notificationPackage).mutate();

                                notificationDot[StartIdNotification].setImageDrawable(dotDrawable);
                                notificationDot[StartIdNotification].setVisibility(View.VISIBLE);
                                notificationDot[StartIdNotification].setTag(notificationPackage);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (intent.getAction().equals("Notification_Dot_No")) {
                    try {
                        String notificationPackage = intent.getStringExtra("NotificationPackage");
                        String categoryNameNotification = mapContentCategoryName.get(notificationPackage);
                        int StartIdNotification = mapCategoryNameStartId.get(categoryNameNotification);
                        if (floatingView[StartIdNotification] != null) {
                            if (floatingView[StartIdNotification].isShown()) {
                                /*remove dot*/
                                notificationDot[StartIdNotification].setVisibility(View.INVISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);

        if (!functionsClass.litePreferencesEnabled()) {
            flingAnimationX[startId] = new FlingAnimation(new FloatValueHolder())
                    .setFriction(functionsClass.readPreference("FlingSensitivity", "FlingSensitivityValue", 3.0f));
            flingAnimationY[startId] = new FlingAnimation(new FloatValueHolder())
                    .setFriction(functionsClass.readPreference("FlingSensitivity", "FlingSensitivityValue", 3.0f));

            simpleOnGestureListener[startId] = new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent motionEventFirst, MotionEvent motionEventLast, float velocityX, float velocityY) {

                    if (allowMove[startId]) {
                        flingAnimationX[startId].setStartVelocity(velocityX);
                        flingAnimationY[startId].setStartVelocity(velocityY);

                        flingAnimationX[startId].setStartValue(flingPositionX);
                        flingAnimationY[startId].setStartValue(flingPositionY);

                        try {
                            flingAnimationX[startId].addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
                                @Override
                                public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                                    layoutParams[startId].x = (int) value;     // X movePoint
                                    windowManager.updateViewLayout(floatingView[startId], layoutParams[startId]);
                                }
                            });
                            flingAnimationY[startId].addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
                                @Override
                                public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                                    layoutParams[startId].y = (int) value;     // Y movePoint
                                    windowManager.updateViewLayout(floatingView[startId], layoutParams[startId]);
                                }
                            });

                            flingAnimationX[startId].start();
                            flingAnimationY[startId].start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        openIt[startId] = false;
                    }

                    return false;
                }
            };

            flingAnimationX[startId].addEndListener(new DynamicAnimation.OnAnimationEndListener() {
                @Override
                public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                    openIt[startId] = true;
                }
            });

            flingAnimationY[startId].addEndListener(new DynamicAnimation.OnAnimationEndListener() {
                @Override
                public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                    openIt[startId] = true;
                }
            });

            gestureDetector[startId] = new GestureDetector(getApplicationContext(), simpleOnGestureListener[startId]);
        }

        floatingView[startId].setOnTouchListener(new View.OnTouchListener() {
            int initialX;
            int initialY;
            float initialTouchX;
            float initialTouchY;

            @Override
            public boolean onTouch(final View view, MotionEvent motionEvent) {
                try {
                    flingAnimationX[startId].cancel();
                    flingAnimationY[startId].cancel();
                    gestureDetector[startId].onTouchEvent(motionEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                WindowManager.LayoutParams layoutParamsOnTouch;
                if (StickyEdge[startId] == true) {
                    layoutParamsOnTouch = StickyEdgeParams[startId];
                    layoutParamsOnTouch.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                } else {
                    layoutParamsOnTouch = layoutParams[startId];
                }
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        initialX = layoutParamsOnTouch.x;
                        initialY = layoutParamsOnTouch.y;

                        initialTouchX = motionEvent.getRawX();
                        initialTouchY = motionEvent.getRawY();

                        xMove = Math.round(initialTouchX);
                        yMove = Math.round(initialTouchY);

                        touchingDelay[startId] = true;
                        runnablePressHold = new Runnable() {
                            @Override
                            public void run() {
                                if (touchingDelay[startId] == true) {
                                    functionsClass.PopupOptionCategory(
                                            view,
                                            categoryName[startId],
                                            className,
                                            startId,
                                            initialX,
                                            initialY + PublicVariable.statusBarHeight
                                    );
                                    openIt[startId] = false;
                                }
                            }
                        };
                        handlerPressHold.postDelayed(runnablePressHold, functionsClass.readDefaultPreference("delayPressHold", 333));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        touchingDelay[startId] = false;
                        handlerPressHold.removeCallbacks(runnablePressHold);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                openIt[startId] = true;
                            }
                        }, 113);

                        if (allowMove[startId] == true) {
                            layoutParamsOnTouch.x = initialX + (int) (motionEvent.getRawX() - initialTouchX);
                            layoutParamsOnTouch.y = initialY + (int) (motionEvent.getRawY() - initialTouchY);
                            FunctionsClass.println("X :: " + layoutParamsOnTouch.x + "\n" + " Y :: " + layoutParamsOnTouch.y);

                            String nameForPosition = categoryName[startId];
                            SharedPreferences sharedPrefPosition = getSharedPreferences(nameForPosition, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPrefPosition.edit();
                            editor.putInt("X", layoutParamsOnTouch.x);
                            editor.putInt("Y", layoutParamsOnTouch.y);
                            editor.apply();
                        } else {
                            if (!functionsClass.litePreferencesEnabled()) {
                                float initialTouchXBoundBack = getSharedPreferences((categoryName[startId]), MODE_PRIVATE).getInt("X", 0);
                                float initialTouchYBoundBack = getSharedPreferences((categoryName[startId]), MODE_PRIVATE).getInt("Y", 0);

                                SpringForce springForceX = new SpringForce()
                                        .setFinalPosition(initialTouchXBoundBack)
                                        .setStiffness(SpringForce.STIFFNESS_MEDIUM)
                                        .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);

                                SpringForce springForceY = new SpringForce()
                                        .setFinalPosition(initialTouchYBoundBack)
                                        .setStiffness(SpringForce.STIFFNESS_MEDIUM)
                                        .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);

                                SpringAnimation springAnimationX = new SpringAnimation(new FloatValueHolder())
                                        .setSpring(springForceX);
                                SpringAnimation springAnimationY = new SpringAnimation(new FloatValueHolder())
                                        .setSpring(springForceY);

                                springAnimationX.setStartValue(motionEvent.getRawX());
                                springAnimationX.setStartVelocity(-0f);
                                springAnimationX.setMaxValue(functionsClass.displayX());
                                springAnimationY.setStartValue(motionEvent.getRawY());
                                springAnimationY.setStartVelocity(-0f);
                                springAnimationY.setMaxValue(functionsClass.displayY());

                                springAnimationX.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                                        layoutParamsOnTouch.x = (int) value;     // X movePoint
                                        windowManager.updateViewLayout(floatingView[startId], layoutParamsOnTouch);
                                    }
                                });
                                springAnimationY.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                                        layoutParamsOnTouch.y = (int) value;     // Y movePoint
                                        windowManager.updateViewLayout(floatingView[startId], layoutParamsOnTouch);
                                    }
                                });

                                springAnimationX.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
                                    @Override
                                    public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                                        openIt[startId] = true;
                                    }
                                });
                                springAnimationY.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
                                    @Override
                                    public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                                        openIt[startId] = true;
                                    }
                                });

                                springAnimationX.start();
                                springAnimationY.start();
                            }
                        }

                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        if (allowMove[startId] == true) {
                            layoutParamsOnTouch.x = initialX + (int) (motionEvent.getRawX() - initialTouchX);
                            layoutParamsOnTouch.y = initialY + (int) (motionEvent.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(floatingView[startId], layoutParamsOnTouch);
                            xMove = layoutParamsOnTouch.x;
                            yMove = layoutParamsOnTouch.y;

                            int difMoveX = (int) (layoutParamsOnTouch.x - initialTouchX);
                            int difMoveY = (int) (layoutParamsOnTouch.y - initialTouchY);
                            if (Math.abs(difMoveX) > Math.abs(PublicVariable.HW + ((PublicVariable.HW * 70) / 100))
                                    || Math.abs(difMoveY) > Math.abs(PublicVariable.HW + ((PublicVariable.HW * 70) / 100))) {
                                openIt[startId] = false;
                                touchingDelay[startId] = false;
                                handlerPressHold.removeCallbacks(runnablePressHold);
                            }

                            flingPositionX = layoutParamsOnTouch.x;
                            flingPositionY = layoutParamsOnTouch.y;
                        } else {
                            if (!functionsClass.litePreferencesEnabled()) {
                                layoutParamsOnTouch.x = initialX + (int) (motionEvent.getRawX() - initialTouchX);     // X movePoint
                                layoutParamsOnTouch.y = initialY + (int) (motionEvent.getRawY() - initialTouchY);     // Y movePoint
                                windowManager.updateViewLayout(floatingView[startId], layoutParamsOnTouch);

                                int difMoveX = (int) (layoutParamsOnTouch.x - initialTouchX);
                                int difMoveY = (int) (layoutParamsOnTouch.y - initialTouchY);
                                if (Math.abs(difMoveX) > Math.abs(PublicVariable.HW + ((PublicVariable.HW * 70) / 100))
                                        || Math.abs(difMoveY) > Math.abs(PublicVariable.HW + ((PublicVariable.HW * 70) / 100))) {
                                    sendBroadcast(new Intent("Hide_PopupListView_Shortcuts"));

                                    openIt[startId] = false;
                                    touchingDelay[startId] = false;
                                    handlerPressHold.removeCallbacks(runnablePressHold);
                                }
                            }
                        }
                        break;
                    }
                }
                return false;
            }
        });
        floatingView[startId].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (openIt[startId]) {
                    functionsClass.PopupAppListCategory(
                            view,
                            categoryName[startId],
                            functionsClass.readFileLine(categoryName[startId]),
                            className,
                            startId,
                            xMove,
                            yMove,
                            layoutParams[startId].width
                    );
                } else {
                }
            }
        });
        notificationDot[startId].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionsClass.PopupNotificationShortcuts(
                        notificationDot[startId],
                        notificationDot[startId].getTag().toString(),
                        App_Unlimited_Shortcuts.class.getSimpleName(),
                        startId,
                        PublicVariable.primaryColor,
                        xMove,
                        yMove
                );
            }
        });
        notificationDot[startId].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (functionsClass.AccessibilityServiceEnabled() && functionsClass.SettingServiceRunning(InteractionObserver.class)) {
                    functionsClass.sendInteractionObserverEvent(view, notificationDot[startId].getTag().toString(), AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED, 66666);
                } else {
                    try {
                        Object sbservice = getSystemService("statusbar");
                        Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
                        Method showsb = statusbarManager.getMethod("expandNotificationsPanel");//expandNotificationsPanel
                        showsb.invoke(sbservice);
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            Object sbservice = getSystemService("statusbar");
                            Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
                            Method showsb = statusbarManager.getMethod("expand");//expandNotificationsPanel
                            showsb.invoke(sbservice);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                return true;
            }
        });

        if (showNotificationDot) {
            sendBroadcast(new Intent("Notification_Dot").putExtra("NotificationPackage", notificationPackage));
        }

        return functionsClass.serviceMode();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        functionsClass = new FunctionsClass(getApplicationContext());

        array = getApplicationContext().getPackageManager().getInstalledApplications(0).size() * 2;
        layoutParams = new WindowManager.LayoutParams[array];
        StickyEdgeParams = new WindowManager.LayoutParams[array];
        floatingView = new ViewGroup[array];
        categoryName = new String[array];
        pin = new ShapesImage[array];
        notificationDot = new ShapesImage[array];
        allowMove = new boolean[array];
        touchingDelay = new boolean[array];
        trans = new boolean[array];
        StickyEdge = new boolean[array];
        openIt = new boolean[array];
        if (!functionsClass.litePreferencesEnabled()) {
            simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener[array];
            gestureDetector = new GestureDetector[array];

            flingAnimationX = new FlingAnimation[array];
            flingAnimationY = new FlingAnimation[array];
        }

        mapContentCategoryName = new LinkedHashMap<String, String>();
        mapCategoryNameStartId = new LinkedHashMap<String, Integer>();

        if (functionsClass.loadCustomIcons()) {
            loadCustomIcons = new LoadCustomIcons(getApplicationContext(), functionsClass.customIconPackageName());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
