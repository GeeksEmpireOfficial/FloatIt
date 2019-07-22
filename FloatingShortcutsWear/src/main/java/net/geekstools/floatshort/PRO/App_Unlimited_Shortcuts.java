package net.geekstools.floatshort.PRO;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.UI.FloatingSplash;
import net.geekstools.imageview.customshapes.ShapesImage;

public class App_Unlimited_Shortcuts extends Service {

    FunctionsClass functionsClass;

    WindowManager windowManager;
    WindowManager.LayoutParams[] params;
    WindowManager.LayoutParams moveDetection;

    int array, xPos, yPos, xInit = 13, yInit = 13, xMove, yMove;

    String[] packages;
    Drawable[] appIcon;
    int[] iconColor;
    boolean[] allowMove, remove, touchingDelay, openIt;

    ViewGroup[] floatingView;
    ShapesImage[] shapedIcon, controlIcon;

    Runnable delayRunnable = null, getbackRunnable = null, runnablePressHold = null;
    Handler delayHandler = new Handler(), getbackHandler = new Handler(), handlerPressHold = new Handler();

    BroadcastReceiver broadcastReceiver;
    SharedPreferences sharedPrefPosition;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, final int startId) {
        System.out.println(this.getClass().getSimpleName() + " ::: StartId ::: " + startId);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            packages[startId] = intent.getStringExtra("pack");

            floatingView[startId] = (ViewGroup) layoutInflater.inflate(R.layout.floating_shortcuts, null, false);
            controlIcon[startId] = functionsClass.initShapesImage(floatingView[startId], R.id.controlIcon);
            shapedIcon[startId] = functionsClass.initShapesImage(floatingView[startId], R.id.shapedIcon);

            touchingDelay[startId] = false;
            allowMove[startId] = true;
            openIt[startId] = true;
        } catch (Exception e) {
            e.printStackTrace();
            return Service.START_NOT_STICKY;
        }

        if (packages[startId].equals(getString(R.string.remove_all_shortcuts))) {
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
                                    stopService(new Intent(getApplicationContext(), BindServices.class));
                                }
                            }
                        } else if (PublicVariable.floatingCounter == 0) {
                            stopService(new Intent(getApplicationContext(), BindServices.class));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            PublicVariable.FloatingShortcuts.clear();
            PublicVariable.shortcutsCounter = -1;
            if (broadcastReceiver != null) {
                unregisterReceiver(broadcastReceiver);
            }
            stopSelf();
            return START_NOT_STICKY;
        }

        if (functionsClass.appInstalledOrNot(packages[startId]) == false) {
            return START_NOT_STICKY;
        }
        functionsClass.saveUnlimitedShortcutsService(packages[startId]);
        functionsClass.updateRecoverShortcuts();

        appIcon[startId] = functionsClass.shapedAppIcon(packages[startId]);
        iconColor[startId] = functionsClass.extractDominantColor(functionsClass.appIcon(packages[startId]));
        shapedIcon[startId].setImageDrawable(functionsClass.shapedAppIcon(packages[startId]));

        try {
            sharedPrefPosition = getSharedPreferences((packages[startId]), MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        xInit = (functionsClass.displayX() / 3);
        yInit = yInit + 13;
        xPos = sharedPrefPosition.getInt("X", xInit);
        yPos = sharedPrefPosition.getInt("Y", yInit);
        params[startId] = functionsClass.normalLayoutParams(PublicVariable.HW, xPos, yPos);
        windowManager.addView(floatingView[startId], params[startId]);

        if (PublicVariable.hide == true) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    shapedIcon[startId].setImageAlpha(PublicVariable.alpha);
                }
            }, 1000);
        }

        floatingView[startId].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });
        floatingView[startId].setOnTouchListener(new View.OnTouchListener() {
            int initialX, initialY;
            float initialTouchX, initialTouchY;

            @Override
            public boolean onTouch(final View view, MotionEvent event) {
                WindowManager.LayoutParams paramsF;
                paramsF = params[startId];

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = paramsF.x;
                        initialY = paramsF.y;

                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        xMove = Math.round(initialTouchX);
                        yMove = Math.round(initialTouchY);

                        touchingDelay[startId] = true;
                        delayRunnable = new Runnable() {
                            @Override
                            public void run() {
                                if (touchingDelay[startId] == true) {
                                    remove[startId] = true;
                                    LayerDrawable drawClose = (LayerDrawable) getResources().getDrawable(R.drawable.draw_close_service);
                                    GradientDrawable backPref = (GradientDrawable) drawClose.findDrawableByLayerId(R.id.backtemp);
                                    backPref.setColor(iconColor[startId]);
                                    controlIcon[startId].setImageDrawable(drawClose);

                                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                    vibrator.vibrate(100);
                                    sendBroadcast(new Intent("Hide_PopupListView_Shortcuts"));

                                    getbackRunnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (remove[startId] == true) {
                                                remove[startId] = false;
                                                controlIcon[startId].setImageDrawable(null);
                                            }
                                        }
                                    };
                                    getbackHandler.postDelayed(getbackRunnable, 3333);
                                }
                            }
                        };
                        delayHandler.postDelayed(delayRunnable, 3333);

                        runnablePressHold = new Runnable() {
                            @Override
                            public void run() {
                                if (touchingDelay[startId] == true) {
                                    functionsClass.PopupShortcuts(
                                            floatingView[startId],
                                            packages[startId],
                                            App_Unlimited_Shortcuts.class.getSimpleName(),
                                            startId,
                                            initialX,
                                            initialY
                                    );
                                    openIt[startId] = false;
                                }
                            }
                        };
                        handlerPressHold.postDelayed(runnablePressHold, 555);

                        break;
                    case MotionEvent.ACTION_UP:
                        touchingDelay[startId] = false;
                        delayHandler.removeCallbacks(delayRunnable);
                        handlerPressHold.removeCallbacks(runnablePressHold);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                openIt[startId] = true;
                            }
                        }, 113);

                        if (allowMove[startId] == true) {
                            paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                            paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                            System.out.println("X :: " + paramsF.x + "\n" + " Y :: " + paramsF.y);

                            SharedPreferences sharedPrefPosition = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            try {
                                sharedPrefPosition = getSharedPreferences((packages[startId]), MODE_PRIVATE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            SharedPreferences.Editor editor = sharedPrefPosition.edit();
                            editor.putInt("X", paramsF.x);
                            editor.putInt("Y", paramsF.y);
                            editor.apply();
                        }
                        moveDetection = paramsF;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (allowMove[startId] == true) {
                            paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                            paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(floatingView[startId], paramsF);
                            moveDetection = paramsF;

                            int difMoveX = (int) (paramsF.x - initialTouchX);
                            int difMoveY = (int) (paramsF.y - initialTouchY);
                            if (Math.abs(difMoveX) > Math.abs(PublicVariable.HW + ((PublicVariable.HW * 70) / 100))
                                    || Math.abs(difMoveY) > Math.abs(PublicVariable.HW + ((PublicVariable.HW * 70) / 100))) {
                                sendBroadcast(new Intent("Hide_PopupListView_Shortcuts"));

                                openIt[startId] = false;
                                touchingDelay[startId] = false;
                                delayHandler.removeCallbacks(delayRunnable);
                                handlerPressHold.removeCallbacks(runnablePressHold);
                            }
                        }
                        break;
                }
                return false;
            }
        });
        floatingView[startId].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (remove[startId] == true) {
                    if (floatingView[startId] == null) {
                        return;
                    }
                    if (floatingView[startId].isShown()) {
                        try {
                            windowManager.removeView(floatingView[startId]);
                            getbackHandler.removeCallbacks(getbackRunnable);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            PublicVariable.FloatingShortcuts.remove(packages[startId]);
                            PublicVariable.floatingCounter = PublicVariable.floatingCounter - 1;
                            PublicVariable.shortcutsCounter = PublicVariable.shortcutsCounter - 1;

                            if (PublicVariable.floatingCounter == 0) {
                                stopService(new Intent(getApplicationContext(), BindServices.class));
                                if (broadcastReceiver != null) {
                                    unregisterReceiver(broadcastReceiver);
                                }
                                stopSelf();
                            }
                        }
                    }
                } else {
                    if (openIt[startId]) {
                        if (functionsClass.splashReveal()) {
                            Intent splashReveal = new Intent(getApplicationContext(), FloatingSplash.class);
                            splashReveal.putExtra("packageName", packages[startId]);
                            if (moveDetection != null) {
                                splashReveal.putExtra("X", moveDetection.x);
                                splashReveal.putExtra("Y", moveDetection.y);
                            } else {
                                splashReveal.putExtra("X", params[startId].x);
                                splashReveal.putExtra("Y", params[startId].y);
                            }
                            splashReveal.putExtra("HW", params[startId].width);
                            startService(splashReveal);
                        } else {
                            functionsClass.appsLaunchPad(packages[startId]);
                        }
                    } else {
                    }
                }
            }
        });

        final String className = App_Unlimited_Shortcuts.class.getSimpleName();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Pin_App_" + className);
        intentFilter.addAction("Unpin_App_" + className);
        intentFilter.addAction("Remove_App_" + className);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("Pin_App_" + className)) {
                    System.out.println(functionsClass.appName(packages[intent.getIntExtra("startId", 0)]));
                    allowMove[intent.getIntExtra("startId", 0)] = false;

                    Drawable pinDrawable = null;
                    switch (functionsClass.shapesImageId()) {
                        case 1:
                            pinDrawable = getDrawable(R.drawable.pin_droplet_icon);
                            controlIcon[intent.getIntExtra("startId", 1)].setPadding(-3, -3, -3, -3);
                            break;
                        case 2:
                            pinDrawable = getDrawable(R.drawable.pin_circle_icon);
                            break;
                        case 3:
                            pinDrawable = getDrawable(R.drawable.pin_square_icon);
                            break;
                        case 4:
                            pinDrawable = getDrawable(R.drawable.pin_squircle_icon);
                            break;
                        case 0:
                            pinDrawable = functionsClass.appIcon(packages[intent.getIntExtra("startId", 0)]).mutate();
                            break;
                    }
                    pinDrawable.setTint(functionsClass.setColorAlpha(Color.RED, 175));
                    if (functionsClass.returnAPI() >= 26) {
                        pinDrawable.setAlpha(175);

                        pinDrawable.setTint(Color.RED);
                        controlIcon[intent.getIntExtra("startId", 1)].setAlpha(0.50f);
                    }
                    controlIcon[intent.getIntExtra("startId", 0)].setImageDrawable(pinDrawable);
                } else if (intent.getAction().equals("Unpin_App_" + className)) {
                    System.out.println(functionsClass.appName(packages[intent.getIntExtra("startId", 0)]));
                    allowMove[intent.getIntExtra("startId", 0)] = true;
                    controlIcon[intent.getIntExtra("startId", 0)].setImageDrawable(null);
                } else if (intent.getAction().equals("Remove_App_" + className)) {
                    if (floatingView[intent.getIntExtra("startId", 0)] == null) {
                        return;
                    }
                    if (floatingView[intent.getIntExtra("startId", 0)].isShown()) {
                        try {
                            windowManager.removeView(floatingView[intent.getIntExtra("startId", 0)]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            PublicVariable.FloatingShortcuts.remove(packages[intent.getIntExtra("startId", 0)]);
                            PublicVariable.floatingCounter = PublicVariable.floatingCounter - 1;
                            PublicVariable.shortcutsCounter = PublicVariable.shortcutsCounter - 1;

                            if (PublicVariable.floatingCounter == 0) {
                                stopService(new Intent(getApplicationContext(), BindServices.class));
                                if (broadcastReceiver != null) {
                                    unregisterReceiver(broadcastReceiver);
                                }
                                stopSelf();
                            }
                        }
                    }
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);

        return functionsClass.serviceMode();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        functionsClass = new FunctionsClass(getApplicationContext());

        array = getApplicationContext().getPackageManager().getInstalledApplications(0).size() * 2;
        params = new WindowManager.LayoutParams[array];
        packages = new String[array];
        appIcon = new Drawable[array];
        iconColor = new int[array];
        floatingView = new ViewGroup[array];
        controlIcon = new ShapesImage[array];
        shapedIcon = new ShapesImage[array];
        allowMove = new boolean[array];
        remove = new boolean[array];
        touchingDelay = new boolean[array];
        openIt = new boolean[array];
    }
}
