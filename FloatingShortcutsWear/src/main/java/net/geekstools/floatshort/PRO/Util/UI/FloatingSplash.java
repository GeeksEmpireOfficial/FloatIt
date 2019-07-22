package net.geekstools.floatshort.PRO.Util.UI;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.imageview.customshapes.ShapesImage;

public class FloatingSplash extends Service {

    public static int xPostionRemoval, yPositionRemoval, HWRemoval;
    FunctionsClass functionsClass;
    WindowManager windowManager;
    WindowManager.LayoutParams layoutParams;
    ViewGroup viewGroup;
    RelativeLayout splashView;
    ShapesImage shapesImage;
    int xPosition, yPosition, appColor;
    int statusBarHeight, navBarHeight;
    String packageName, className;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, final int startId) {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        navBarHeight = 0;
        int resourceIdNav = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceIdNav > 0) {
            navBarHeight = getResources().getDimensionPixelSize(resourceIdNav);
        }
        statusBarHeight = 0;
        int resourceIdStatus = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceIdStatus > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceIdStatus);
        }

        packageName = intent.getStringExtra("packageName");
        Drawable appIcon = functionsClass.shapedAppIcon(packageName).mutate();
        try {
            if (intent.hasExtra("className")) {
                className = intent.getStringExtra("className");
                appIcon = functionsClass.shapedAppIcon(getPackageManager().getActivityInfo(new ComponentName(packageName, className), 0)).mutate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        appColor = functionsClass.extractDominantColor(appIcon);
        final int HW = intent.getIntExtra("HW", 0);
        xPosition = intent.getIntExtra("X", 0);
        yPosition = intent.getIntExtra("Y", 0);

        xPostionRemoval = xPosition;
        yPositionRemoval = yPosition + statusBarHeight;
        HWRemoval = HW;

        viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.reveal_splash, null, false);
        splashView = (RelativeLayout) viewGroup.findViewById(R.id.splashView);
        shapesImage = functionsClass.initShapesImage(viewGroup, R.id.shapedIcon);
        RelativeLayout.LayoutParams layoutParamsRelativeLayout = new RelativeLayout.LayoutParams(
                HW,
                HW
        );
        shapesImage.setX(xPosition);
        shapesImage.setY(yPosition + statusBarHeight);
        shapesImage.setLayoutParams(layoutParamsRelativeLayout);
        shapesImage.setImageDrawable(appIcon);
        layoutParams = functionsClass.splashRevealParams(
                0,
                0
        );
        windowManager.addView(viewGroup, layoutParams);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    functionsClass.circularRevealSplashScreen(
                            splashView,
                            shapesImage,
                            xPosition,
                            yPosition + statusBarHeight,
                            appColor,
                            packageName,
                            className,
                            true
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    if (className != null) {
                        functionsClass.appsLaunchPad(packageName, className);
                    } else {
                        functionsClass.appsLaunchPad(packageName);
                    }
                }
            }
        }, 100);

        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeScreen = new Intent(Intent.ACTION_MAIN);
                homeScreen.addCategory(Intent.CATEGORY_HOME);
                homeScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeScreen);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopSelf();
                    }
                }, 113);
            }
        });

        return functionsClass.serviceMode();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        functionsClass = new FunctionsClass(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            layoutParams.windowAnimations = android.R.style.Animation_Dialog;
            windowManager.updateViewLayout(viewGroup, layoutParams);

            sendBroadcast(new Intent("FloatingSplashRemoval"));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    windowManager.removeViewImmediate(viewGroup);
                }
            }, 555);
        } catch (Exception e) {
            e.printStackTrace();
            windowManager.removeViewImmediate(viewGroup);
        }
    }
}
