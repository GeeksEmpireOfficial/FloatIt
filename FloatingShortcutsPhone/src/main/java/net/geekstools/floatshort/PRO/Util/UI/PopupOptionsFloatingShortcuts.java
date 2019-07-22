package net.geekstools.floatshort.PRO.Util.UI;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Shortcuts.NavAdapter.PopupShortcutsOptionAdapter;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;

import java.util.ArrayList;
import java.util.List;

public class PopupOptionsFloatingShortcuts extends Service {

    FunctionsClass functionsClass;
    WindowManager windowManager;
    WindowManager.LayoutParams layoutParams;

    ViewGroup viewGroup;
    RelativeLayout wholeMenuView, itemMenuView;
    ListView popupOptionsItems;

    int HW, xPosition, yPosition, startIdCommand;
    int statusBarHeight, navBarHeight;
    String packageName, className, classNameCommand;

    PopupShortcutsOptionAdapter popupShortcutsOptionAdapter;
    LoadCustomIcons loadCustomIcons;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, final int startId) {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

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

        startIdCommand = intent.getIntExtra("startIdCommand", 0);
        classNameCommand = intent.getStringExtra("classNameCommand");
        packageName = intent.getStringExtra("packageName");
        HW = intent.getIntExtra("HW", 0);
        xPosition = intent.getIntExtra("X", 0);
        yPosition = intent.getIntExtra("Y", 0);

        viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.popup_options_shortcuts, null, false);
        wholeMenuView = (RelativeLayout) viewGroup.findViewById(R.id.wholeMenuView);
        itemMenuView = (RelativeLayout) viewGroup.findViewById(R.id.itemMenuView);
        popupOptionsItems = (ListView) viewGroup.findViewById(R.id.popupOptionsItems);

        try {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(50);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            ArrayList<NavDrawerItem> navDrawerItemsSaved = new ArrayList<NavDrawerItem>();
            navDrawerItemsSaved.clear();

            List<String> popupItems = new ArrayList<String>();
            popupItems.clear();

            LayerDrawable popupItemsIcon = (LayerDrawable) getDrawable(R.drawable.draw_popup_shortcuts);
            GradientDrawable popupItemsIconBack = (GradientDrawable) popupItemsIcon.findDrawableByLayerId(R.id.backtemp);
            popupItemsIconBack.setColor(functionsClass.extractVibrantColor(functionsClass.appIcon(packageName)));

            switch (functionsClass.displaySection(xPosition, yPosition)) {
                case FunctionsClass.DisplaySection.TopLeft:
                    if (functionsClass.returnAPI() < 24) {
                        if (functionsClass.UsageStatsEnabled()) {
                            popupItems.add(getString(R.string.close));
                        }
                        popupItems.add(getString(R.string.pin));
                        popupItems.add(getString(R.string.unpin));
                        popupItems.add(getString(R.string.remove));
                    } else {
                        if (functionsClass.addFloatItItem()) {
                            popupItems.add(getString(R.string.floatIt));
                        }
                        popupItems.add(getString(R.string.splitIt));
                        if (functionsClass.UsageStatsEnabled()) {
                            popupItems.add(getString(R.string.close));
                        }
                        popupItems.add(getString(R.string.pin));
                        popupItems.add(getString(R.string.unpin));
                        popupItems.add(getString(R.string.remove));
                    }

                    itemMenuView.setX(xPosition);
                    itemMenuView.setY(yPosition + HW);

                    break;
                case FunctionsClass.DisplaySection.TopRight:
                    if (functionsClass.returnAPI() < 24) {
                        if (functionsClass.UsageStatsEnabled()) {
                            popupItems.add(getString(R.string.close));
                        }
                        popupItems.add(getString(R.string.pin));
                        popupItems.add(getString(R.string.unpin));
                        popupItems.add(getString(R.string.remove));
                    } else {
                        if (functionsClass.addFloatItItem()) {
                            popupItems.add(getString(R.string.floatIt));
                        }
                        popupItems.add(getString(R.string.splitIt));
                        if (functionsClass.UsageStatsEnabled()) {
                            popupItems.add(getString(R.string.close));
                        }
                        popupItems.add(getString(R.string.pin));
                        popupItems.add(getString(R.string.unpin));
                        popupItems.add(getString(R.string.remove));
                    }

                    itemMenuView.setX(xPosition - (functionsClass.DpToInteger(177) - HW));
                    itemMenuView.setY(yPosition + HW);

                    break;
                case FunctionsClass.DisplaySection.BottomLeft:
                    if (functionsClass.returnAPI() < 24) {
                        popupItems.add(getString(R.string.remove));
                        popupItems.add(getString(R.string.unpin));
                        popupItems.add(getString(R.string.pin));
                        if (functionsClass.UsageStatsEnabled()) {
                            popupItems.add(getString(R.string.close));
                        }
                    } else {
                        popupItems.add(getString(R.string.remove));
                        popupItems.add(getString(R.string.unpin));
                        popupItems.add(getString(R.string.pin));
                        if (functionsClass.UsageStatsEnabled()) {
                            popupItems.add(getString(R.string.close));
                        }
                        popupItems.add(getString(R.string.splitIt));
                        if (functionsClass.addFloatItItem()) {
                            popupItems.add(getString(R.string.floatIt));
                        }
                    }

                    itemMenuView.setX(xPosition);
                    itemMenuView.setY(yPosition - ((functionsClass.DpToInteger(37) * popupItems.size()) + functionsClass.DpToInteger(3)));

                    break;
                case FunctionsClass.DisplaySection.BottomRight:
                    if (functionsClass.returnAPI() < 24) {
                        popupItems.add(getString(R.string.remove));
                        popupItems.add(getString(R.string.unpin));
                        popupItems.add(getString(R.string.pin));
                        if (functionsClass.UsageStatsEnabled()) {
                            popupItems.add(getString(R.string.close));
                        }
                    } else {
                        popupItems.add(getString(R.string.remove));
                        popupItems.add(getString(R.string.unpin));
                        popupItems.add(getString(R.string.pin));
                        if (functionsClass.UsageStatsEnabled()) {
                            popupItems.add(getString(R.string.close));
                        }
                        popupItems.add(getString(R.string.splitIt));
                        if (functionsClass.addFloatItItem()) {
                            popupItems.add(getString(R.string.floatIt));
                        }
                    }

                    itemMenuView.setX(xPosition - (functionsClass.DpToInteger(177) - HW));
                    itemMenuView.setY(yPosition - ((functionsClass.DpToInteger(37) * popupItems.size()) + functionsClass.DpToInteger(3)));

                    break;
                default:
                    if (functionsClass.returnAPI() < 24) {
                        if (functionsClass.UsageStatsEnabled()) {
                            popupItems.add(getString(R.string.close));
                        }
                        popupItems.add(getString(R.string.pin));
                        popupItems.add(getString(R.string.unpin));
                        popupItems.add(getString(R.string.remove));
                    } else {
                        if (functionsClass.addFloatItItem()) {
                            popupItems.add(getString(R.string.floatIt));
                        }
                        popupItems.add(getString(R.string.splitIt));
                        if (functionsClass.UsageStatsEnabled()) {
                            popupItems.add(getString(R.string.close));
                        }
                        popupItems.add(getString(R.string.pin));
                        popupItems.add(getString(R.string.unpin));
                        popupItems.add(getString(R.string.remove));
                    }

                    itemMenuView.setX(xPosition);
                    itemMenuView.setY(yPosition + HW);

                    break;
            }

            for (int i = 0; i < popupItems.size(); i++) {
                navDrawerItemsSaved.add(new NavDrawerItem(
                        popupItems.get(i),
                        popupItemsIcon));
            }


            if (className != null) {
                popupShortcutsOptionAdapter =
                        new PopupShortcutsOptionAdapter(getApplicationContext(), navDrawerItemsSaved,
                                classNameCommand, packageName, className, startIdCommand);
            } else {
                popupShortcutsOptionAdapter =
                        new PopupShortcutsOptionAdapter(getApplicationContext(), navDrawerItemsSaved,
                                classNameCommand, packageName, startIdCommand);
            }

            popupOptionsItems.setAdapter(popupShortcutsOptionAdapter);


        } catch (Exception e) {
            e.printStackTrace();
        }

        layoutParams = functionsClass.splashRevealParams(
                0,
                0
        );
        windowManager.addView(viewGroup, layoutParams);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    functionsClass.circularRevealViewScreen(
                            wholeMenuView,
                            xPosition,
                            yPosition,
                            true
                    );

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            itemMenuView.setVisibility(View.VISIBLE);
                        }
                    }, 130);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100);

        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopSelf();
                    }
                }, 113);
            }
        });

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        functionsClass = new FunctionsClass(getApplicationContext());

        if (functionsClass.loadCustomIcons()) {
            loadCustomIcons = new LoadCustomIcons(getApplicationContext(), functionsClass.customIconPackageName());
        }
    }

    @Override
    public void onDestroy() {
        try {
            functionsClass.circularRevealViewScreen(
                    wholeMenuView,
                    xPosition,
                    yPosition,
                    false
            );
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    windowManager.removeViewImmediate(viewGroup);
                }
            }, 333);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
