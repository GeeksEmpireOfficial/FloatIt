package net.geekstools.floatshort.PRO;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.imageview.customshapes.ShapesImage;

public class Category_Unlimited_Category extends Service {

    public static boolean running = false;
    FunctionsClass functionsClass;
    WindowManager windowManager;
    WindowManager.LayoutParams[] params;
    ViewGroup[] floatingView;
    RelativeLayout wholeCategoryFloating;
    ShapesImage one, two, three, four;
    ShapesImage[] pin;
    int xPos, yPos, xInit = 19, yInit = 19, xMove, yMove;
    int array;
    String[] categoryName;
    boolean[] allowMove, touchingDelay, trans, openIt;
    BroadcastReceiver broadcastReceiver;
    SharedPreferences sharedPrefPosition;
    Runnable runnablePressHold = null;
    Handler handlerPressHold = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        System.out.println(this.getClass().getSimpleName() + " ::: StartId ::: " + startId);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        try {
            categoryName[startId] = intent.getStringExtra("categoryName");

            openIt[startId] = true;
            allowMove[startId] = true;
        } catch (Exception e) {
            e.printStackTrace();
            return Service.START_NOT_STICKY;
        }

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        floatingView[startId] = (ViewGroup) layoutInflater.inflate(R.layout.category_floating, null, false);

        wholeCategoryFloating = (RelativeLayout) floatingView[startId].findViewById(R.id.wholeCategoryFloating);
        one = functionsClass.initShapesImage(floatingView[startId], R.id.one);
        two = functionsClass.initShapesImage(floatingView[startId], R.id.two);
        three = functionsClass.initShapesImage(floatingView[startId], R.id.three);
        four = functionsClass.initShapesImage(floatingView[startId], R.id.four);
        pin[startId] = functionsClass.initShapesImage(floatingView[startId], R.id.pin);

        int categoryFloatingColor;
        if (PublicVariable.hide == true) {
            categoryFloatingColor = functionsClass.setColorAlpha(PublicVariable.primaryColor, 50);
        } else {
            categoryFloatingColor = PublicVariable.primaryColor;
        }

        Drawable drawableBack = null;
        switch (functionsClass.shapesImageId()) {
            case 1:
                drawableBack = getDrawable(R.drawable.category_droplet_icon);
                drawableBack.setTint(categoryFloatingColor);
                break;
            case 2:
                drawableBack = getDrawable(R.drawable.category_circle_icon);
                drawableBack.setTint(categoryFloatingColor);
                break;
            case 3:
                drawableBack = getDrawable(R.drawable.category_square_icon);
                drawableBack.setTint(categoryFloatingColor);
                break;
            case 4:
                drawableBack = getDrawable(R.drawable.category_squircle_icon);
                drawableBack.setTint(categoryFloatingColor);
                break;
            case 5:
                drawableBack = getDrawable(R.drawable.category_cut_circle_icon);
                drawableBack.setTint(categoryFloatingColor);
                break;
            case 0:
                drawableBack = null;
                break;
        }
        wholeCategoryFloating.setBackground(drawableBack);

        if (categoryName[startId].equals(getString(R.string.remove_all_shortcuts))) {
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
            PublicVariable.FloatingCategories.clear();
            PublicVariable.categoriesCounter = -1;
            if (broadcastReceiver != null) {
                unregisterReceiver(broadcastReceiver);
            }
            running = false;
            stopSelf();
            return START_NOT_STICKY;
        }

        String[] appsCategory = functionsClass.readFileLine(".uFile");
        if (appsCategory != null) {
            try {
                one.setImageDrawable(functionsClass.shapedAppIcon(appsCategory[0]));
                one.setImageAlpha(PublicVariable.hide ? 157 : 250);
            } catch (Exception e) {
                one.setImageDrawable(null);
            }
            try {
                two.setImageDrawable(functionsClass.shapedAppIcon(appsCategory[1]));
                two.setImageAlpha(PublicVariable.hide ? 157 : 250);
            } catch (Exception e) {
                two.setImageDrawable(null);
            }
            try {
                three.setImageDrawable(functionsClass.shapedAppIcon(appsCategory[2]));
                three.setImageAlpha(PublicVariable.hide ? 157 : 250);
            } catch (Exception e) {
                three.setImageDrawable(null);
            }
            try {
                four.setImageDrawable(functionsClass.shapedAppIcon(appsCategory[3]));
                four.setImageAlpha(PublicVariable.hide ? 157 : 250);
            } catch (Exception e) {
                four.setImageDrawable(null);
            }
        }


        int HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 41, this.getResources().getDisplayMetrics());

        String nameForPosition = categoryName[startId];
        sharedPrefPosition = getSharedPreferences(nameForPosition, MODE_PRIVATE);
        xInit = xInit + 13;
        yInit = yInit + 13;
        xPos = sharedPrefPosition.getInt("X", xInit);
        yPos = sharedPrefPosition.getInt("Y", yInit);

        params[startId] = functionsClass.normalLayoutParams(HW, xPos, yPos);
        windowManager.addView(floatingView[startId], params[startId]);

        final String className = Category_Unlimited_Category.class.getSimpleName();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Split_Apps_Pair_" + className);
        intentFilter.addAction("Split_Apps_Single_" + className);
        intentFilter.addAction("Pin_App_" + className);
        intentFilter.addAction("Unpin_App_" + className);
        intentFilter.addAction("Remove_Category_" + className);
        intentFilter.addAction("Sticky_Edge");
        intentFilter.addAction("Sticky_Edge_No");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("Pin_App_" + className)) {
                    allowMove[intent.getIntExtra("startId", 0)] = false;
                    Drawable drawableBack = null;
                    switch (functionsClass.shapesImageId()) {
                        case 1:
                            drawableBack = getDrawable(R.drawable.pin_droplet_icon);
                            drawableBack.setTint(context.getResources().getColor(R.color.red_trans));
                            break;
                        case 2:
                            drawableBack = getDrawable(R.drawable.pin_circle_icon);
                            drawableBack.setTint(context.getResources().getColor(R.color.red_trans));
                            break;
                        case 3:
                            drawableBack = getDrawable(R.drawable.pin_square_icon);
                            drawableBack.setTint(context.getResources().getColor(R.color.red_trans));
                            break;
                        case 4:
                            drawableBack = getDrawable(R.drawable.pin_squircle_icon);
                            drawableBack.setTint(context.getResources().getColor(R.color.red_trans));
                            break;
                        case 5:
                            drawableBack = getDrawable(R.drawable.pin_cut_circle_icon);
                            drawableBack.setTint(context.getResources().getColor(R.color.red_trans));
                            break;
                        case 0:
                            drawableBack = getDrawable(R.drawable.pin_noshap);
                            drawableBack.setTint(context.getResources().getColor(R.color.red_trans));
                            break;
                    }
                    pin[intent.getIntExtra("startId", 0)].setImageDrawable(drawableBack);
                } else if (intent.getAction().equals("Unpin_App_" + className)) {
                    allowMove[intent.getIntExtra("startId", 0)] = true;
                    pin[intent.getIntExtra("startId", 0)].setImageDrawable(null);
                } else if (intent.getAction().equals("Remove_Category_" + className)) {
                    try {
                        if (floatingView != null) {
                            if (floatingView[intent.getIntExtra("startId", 0)] == null) {
                                return;
                            }
                            if (floatingView[intent.getIntExtra("startId", 0)].isShown()) {
                                try {
                                    windowManager.removeView(floatingView[intent.getIntExtra("startId", 0)]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    PublicVariable.FloatingCategories.remove(categoryName[intent.getIntExtra("startId", 0)]);
                                    PublicVariable.floatingCounter = PublicVariable.floatingCounter - 1;
                                    PublicVariable.floatingCategoryCounter_category = PublicVariable.floatingCategoryCounter_category - 1;
                                    PublicVariable.categoriesCounter = PublicVariable.categoriesCounter - 1;

                                    if (PublicVariable.floatingCounter == 0) {
                                        stopService(new Intent(getApplicationContext(), BindServices.class));
                                    }
                                    if (PublicVariable.floatingCategoryCounter_category == 0) {
                                        if (broadcastReceiver != null) {
                                            unregisterReceiver(broadcastReceiver);
                                        }
                                        running = false;
                                        stopSelf();
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
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);

        floatingView[startId].setOnTouchListener(new View.OnTouchListener() {
            int initialX;
            int initialY;
            float initialTouchX;
            float initialTouchY;

            @Override
            public boolean onTouch(final View view, MotionEvent event) {
                WindowManager.LayoutParams paramsF;
                paramsF = params[startId];

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        initialX = paramsF.x;
                        initialY = paramsF.y;

                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        xMove = paramsF.x;
                        yMove = paramsF.y;

                        touchingDelay[startId] = true;
                        runnablePressHold = new Runnable() {
                            @Override
                            public void run() {
                                if (touchingDelay[startId] == true) {
                                    functionsClass.PopupOptionCategory(
                                            view,
                                            ".uFile",
                                            className,
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
                            paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                            paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                            System.out.println("X :: " + paramsF.x + "\n" + " Y :: " + paramsF.y);

                            String nameForPosition = categoryName[startId];
                            SharedPreferences sharedPrefPosition = getSharedPreferences(nameForPosition, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPrefPosition.edit();
                            editor.putInt("X", paramsF.x);
                            editor.putInt("Y", paramsF.y);
                            editor.apply();
                        }
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        if (allowMove[startId] == true) {
                            paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                            paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(floatingView[startId], paramsF);
                            xMove = paramsF.x;
                            yMove = paramsF.y;

                            int difMoveX = (int) (paramsF.x - initialTouchX);
                            int difMoveY = (int) (paramsF.y - initialTouchY);
                            if (Math.abs(difMoveX) > Math.abs(PublicVariable.HW + ((PublicVariable.HW * 70) / 100))
                                    || Math.abs(difMoveY) > Math.abs(PublicVariable.HW + ((PublicVariable.HW * 70) / 100))) {
                                openIt[startId] = false;
                                touchingDelay[startId] = false;
                                handlerPressHold.removeCallbacks(runnablePressHold);
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
                    functionsClass.PopupListCategory(
                            view,
                            ".uFile",
                            functionsClass.readFileLine(".uFile"),
                            className,
                            startId,
                            xMove,
                            yMove,
                            params[startId].width
                    );
                }
            }
        });

        return functionsClass.serviceMode();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        functionsClass = new FunctionsClass(getApplicationContext());

        array = getApplicationContext().getPackageManager().getInstalledApplications(0).size() * 2;
        params = new WindowManager.LayoutParams[array];
        floatingView = new ViewGroup[array];
        categoryName = new String[array];
        pin = new ShapesImage[array];
        allowMove = new boolean[array];
        touchingDelay = new boolean[array];
        trans = new boolean[array];
        openIt = new boolean[array];

        running = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
