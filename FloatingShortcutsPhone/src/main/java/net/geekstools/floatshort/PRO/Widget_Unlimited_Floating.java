package net.geekstools.floatshort.PRO;

import android.app.Service;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;

public class Widget_Unlimited_Floating extends Service {

    FunctionsClass functionsClass;

    WindowManager windowManager;
    WindowManager.LayoutParams[] layoutParams;

    ViewGroup[] floatingView, widgetLayout;
    RelativeLayout[] wholeViewWidget;
    TextView[] widgetLabel;
    ImageView[] widgetMoveButton, widgetCloseButton;
    Button[] widgetResizeControl;

    AppWidgetManager appWidgetManager;
    AppWidgetProviderInfo[] appWidgetProviderInfo;
    AppWidgetHost[] appWidgetHosts;
    AppWidgetHostView[] appWidgetHostView;

    int array;

    int[] appWidgetId, widgetColor;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        FunctionsClass.println("StartID ::: " + startId);
        if (startId >= array) {
            return START_NOT_STICKY;
        }
        appWidgetId[startId] = intent.getIntExtra("WidgetId", -1);

        appWidgetProviderInfo[startId] = appWidgetManager.getAppWidgetInfo(appWidgetId[startId]);
        appWidgetHosts[startId] = new AppWidgetHost(this, (int) System.currentTimeMillis());
        appWidgetHosts[startId].startListening();

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        floatingView[startId] = (ViewGroup) layoutInflater.inflate(R.layout.floating_widgets, null, false);

        widgetLayout[startId] = (ViewGroup) floatingView[startId].findViewById(R.id.widgetViewGroup);
        wholeViewWidget[startId] = (RelativeLayout) floatingView[startId].findViewById(R.id.wholeViewWidget);
        widgetLabel[startId] = (TextView) floatingView[startId].findViewById(R.id.widgetLabel);
        widgetMoveButton[startId] = (ImageView) floatingView[startId].findViewById(R.id.widgetMoveButton);
        widgetCloseButton[startId] = (ImageView) floatingView[startId].findViewById(R.id.widgetCloseButton);
        widgetResizeControl[startId] = (Button) floatingView[startId].findViewById(R.id.widgetResizeControl);

        if (intent.hasExtra(getString(R.string.remove_all_floatings))) {
            if (intent.getStringExtra(getString(R.string.remove_all_floatings)).equals(getString(R.string.remove_all_floatings))) {
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
                PublicVariable.FloatingWidgets.clear();
                PublicVariable.widgetsCounter = -1;

                stopSelf();
                return START_NOT_STICKY;
            }
        }

        if (PublicVariable.themeLightDark) {
            wholeViewWidget[startId].setBackgroundTintList(ColorStateList.valueOf(functionsClass.appThemeTransparent() ? getColor(R.color.light_transparent) : getColor(R.color.light)));
            widgetLabel[startId].setBackgroundColor(getColor(R.color.light_transparent_high));

            widgetLabel[startId].setTextColor(getColor(R.color.dark));
        } else if (!PublicVariable.themeLightDark) {
            wholeViewWidget[startId].setBackgroundTintList(ColorStateList.valueOf(functionsClass.appThemeTransparent() ? getColor(R.color.dark_transparent) : getColor(R.color.dark)));
            widgetLabel[startId].setBackgroundColor(getColor(R.color.dark_transparent_high));

            widgetLabel[startId].setTextColor(getColor(R.color.light));
        }

        try {
            widgetColor[startId] = functionsClass.extractVibrantColor(appWidgetProviderInfo[startId].loadIcon(getApplicationContext(), DisplayMetrics.DENSITY_LOW));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (intent.hasExtra("WidgetLabel")) {
            String widgetLabelText = intent.getStringExtra("WidgetLabel");
            widgetLabel[startId].setText(widgetLabelText);
        } else {
            String widgetLabelText =
                    appWidgetProviderInfo[startId].loadLabel(getPackageManager()) == null ?
                            getString(R.string.widgetHint) : appWidgetProviderInfo[startId].loadLabel(getPackageManager());
            widgetLabel[startId].setText(widgetLabelText);
        }
        widgetLabel[startId].setTextColor(widgetColor[startId]);

        LayerDrawable moveLayerDrawable = (LayerDrawable) getDrawable(R.drawable.draw_move);
        GradientDrawable moveBackgroundLayerDrawable = (GradientDrawable) moveLayerDrawable.findDrawableByLayerId(R.id.backtemp);
        moveBackgroundLayerDrawable.setColor(widgetColor[startId]);

        LayerDrawable closeLayerDrawable = (LayerDrawable) getDrawable(R.drawable.draw_close_service);
        GradientDrawable closeBackgroundLayerDrawable = (GradientDrawable) closeLayerDrawable.findDrawableByLayerId(R.id.backtemp);
        closeBackgroundLayerDrawable.setColor(widgetColor[startId]);

        Drawable resizeDrawable = getDrawable(R.drawable.w_resize).mutate();
        resizeDrawable.setTint(widgetColor[startId]);

        widgetMoveButton[startId].setImageDrawable(moveLayerDrawable);
        widgetCloseButton[startId].setImageDrawable(closeLayerDrawable);
        widgetResizeControl[startId].setBackground(resizeDrawable);

        appWidgetHostView[startId] = appWidgetHosts[startId].createView(this, appWidgetId[startId], appWidgetProviderInfo[startId]);

        int initWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 213, this.getResources().getDisplayMetrics());
        int initHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 133, this.getResources().getDisplayMetrics());

        Bundle bundle = new Bundle();
        bundle.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, initWidth);
        bundle.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, initHeight);
        bundle.putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, functionsClass.displayX() / 2);
        bundle.putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, functionsClass.displayY() / 2);
        appWidgetManager.bindAppWidgetIdIfAllowed(appWidgetId[startId], appWidgetProviderInfo[startId].provider, bundle);

        appWidgetHostView[startId].setAppWidget(appWidgetId[startId], appWidgetProviderInfo[startId]);
        appWidgetHostView[startId].setMinimumHeight(initHeight);
        appWidgetHostView[startId].setMinimumWidth(initWidth);
        widgetLayout[startId].addView(appWidgetHostView[startId]);

        final RelativeLayout.LayoutParams widgetRelativeLayout = new RelativeLayout.LayoutParams(initWidth, initHeight);
        wholeViewWidget[startId].setElevation(19);
        wholeViewWidget[startId].setLayoutParams(widgetRelativeLayout);
        wholeViewWidget[startId].requestLayout();

        layoutParams[startId] = functionsClass.normalWidgetLayoutParams(appWidgetProviderInfo[startId].provider.getPackageName(),
                appWidgetId[startId],
                initWidth, initHeight);
        layoutParams[startId].windowAnimations = android.R.style.Animation_Dialog;

        windowManager.addView(floatingView[startId], layoutParams[startId]);

        widgetLabel[startId].setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams layoutParamsTouch = layoutParams[startId];
            int initialX;
            int initialY;

            float initialTouchX;
            float initialTouchY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = layoutParamsTouch.x;
                        initialY = layoutParamsTouch.y;

                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        layoutParamsTouch.x = initialX + (int) (event.getRawX() - initialTouchX);
                        layoutParamsTouch.y = initialY + (int) (event.getRawY() - initialTouchY);

                        functionsClass.savePreference(appWidgetId[startId] + appWidgetProviderInfo[startId].provider.getPackageName(),
                                "X", layoutParamsTouch.x);
                        functionsClass.savePreference(appWidgetId[startId] + appWidgetProviderInfo[startId].provider.getPackageName(),
                                "Y", layoutParamsTouch.y);

                        break;
                    case MotionEvent.ACTION_MOVE:
                        layoutParamsTouch.x = initialX + (int) (event.getRawX() - initialTouchX);
                        layoutParamsTouch.y = initialY + (int) (event.getRawY() - initialTouchY);
                        try {
                            windowManager.updateViewLayout(floatingView[startId], layoutParamsTouch);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }

                        break;
                }
                return false;
            }
        });
        widgetLabel[startId].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        widgetMoveButton[startId].setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams layoutParamsTouch = layoutParams[startId];
            int initialX;
            int initialY;

            float initialTouchX;
            float initialTouchY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = layoutParamsTouch.x;
                        initialY = layoutParamsTouch.y;

                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        layoutParamsTouch.x = initialX + (int) (event.getRawX() - initialTouchX);
                        layoutParamsTouch.y = initialY + (int) (event.getRawY() - initialTouchY);

                        functionsClass.savePreference(appWidgetId[startId] + appWidgetProviderInfo[startId].provider.getPackageName(),
                                "X", layoutParamsTouch.x);
                        functionsClass.savePreference(appWidgetId[startId] + appWidgetProviderInfo[startId].provider.getPackageName(),
                                "Y", layoutParamsTouch.y);

                        break;
                    case MotionEvent.ACTION_MOVE:
                        layoutParamsTouch.x = initialX + (int) (event.getRawX() - initialTouchX);
                        layoutParamsTouch.y = initialY + (int) (event.getRawY() - initialTouchY);
                        try {
                            windowManager.updateViewLayout(floatingView[startId], layoutParamsTouch);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }

                        break;
                }
                return false;
            }
        });
        widgetMoveButton[startId].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        widgetCloseButton[startId].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    windowManager.removeView(floatingView[startId]);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    PublicVariable.FloatingWidgets.remove((Object) appWidgetId[startId]);
                    PublicVariable.floatingCounter = PublicVariable.floatingCounter - 1;
                    PublicVariable.widgetsCounter = PublicVariable.widgetsCounter - 1;

                    if (PublicVariable.floatingCounter == 0) {
                        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                .getBoolean("stable", true) == false) {
                            stopService(new Intent(getApplicationContext(), BindServices.class));
                        }

                        stopSelf();
                    }
                }
            }
        });

        widgetResizeControl[startId].setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams layoutParamsTouch = layoutParams[startId];

            int initialWidth;
            int initialHeight;

            float initialTouchX;
            float initialTouchY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialWidth = layoutParamsTouch.width;
                        initialHeight = layoutParamsTouch.height;

                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:

                        if (layoutParamsTouch.width < initWidth || layoutParamsTouch.height < initHeight) {

                        } else {
                            functionsClass.savePreference(appWidgetId[startId] + appWidgetProviderInfo[startId].provider.getPackageName(),
                                    "WidgetWidth", layoutParamsTouch.width);
                            functionsClass.savePreference(appWidgetId[startId] + appWidgetProviderInfo[startId].provider.getPackageName(),
                                    "WidgetHeight", layoutParamsTouch.height);
                        }

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int xWidthMove = initialWidth + (int) (event.getRawX() - initialTouchX);
                        int yHeightMove = initialHeight + (int) (event.getRawY() - initialTouchY);

                        layoutParamsTouch.width = xWidthMove;
                        layoutParamsTouch.height = yHeightMove;

                        /*if (xWidthMove < initWidth || yHeightMove < initHeight) {

                        } else {

                        }*/

                        appWidgetHostView[startId].updateAppWidgetSize(new Bundle(), layoutParamsTouch.width, layoutParamsTouch.height, layoutParamsTouch.width, layoutParamsTouch.height);
                        windowManager.updateViewLayout(floatingView[startId], layoutParamsTouch);

                        break;
                }
                return false;
            }
        });

        return functionsClass.serviceMode();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        functionsClass = new FunctionsClass(getApplicationContext());

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

        array = getApplicationContext().getPackageManager().getInstalledApplications(0).size() * 2;
        layoutParams = new WindowManager.LayoutParams[array];
        appWidgetHosts = new AppWidgetHost[array];
        appWidgetHostView = new AppWidgetHostView[array];
        appWidgetProviderInfo = new AppWidgetProviderInfo[array];
        floatingView = new ViewGroup[array];
        widgetLayout = new ViewGroup[array];
        wholeViewWidget = new RelativeLayout[array];
        widgetLabel = new TextView[array];
        widgetMoveButton = new ImageView[array];
        widgetCloseButton = new ImageView[array];
        widgetResizeControl = new Button[array];
        appWidgetId = new int[array];
        widgetColor = new int[array];
    }
}
