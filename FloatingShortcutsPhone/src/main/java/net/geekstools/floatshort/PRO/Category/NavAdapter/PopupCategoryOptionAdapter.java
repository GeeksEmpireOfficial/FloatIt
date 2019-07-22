package net.geekstools.floatshort.PRO.Category.NavAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.geekstools.floatshort.PRO.Category.CategoryHandler;
import net.geekstools.floatshort.PRO.CheckPoint;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.InteractionObserver.InteractionObserver;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;
import net.geekstools.floatshort.PRO.Util.UI.FloatingSplash;
import net.geekstools.imageview.customshapes.ShapesImage;

import java.util.ArrayList;

import static android.content.Context.ACCESSIBILITY_SERVICE;

public class PopupCategoryOptionAdapter extends BaseAdapter {

    FunctionsClass functionsClass;
    Drawable splitOne = null, splitTwo = null;
    LoadCustomIcons loadCustomIcons;
    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private String className;
    private int startId, layoutInflater, xPosition, yPosition, HW;

    public PopupCategoryOptionAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems, String className, int startId) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        this.className = className;
        this.startId = startId;
        functionsClass = new FunctionsClass(context);

        PublicVariable.size = functionsClass.readDefaultPreference("floatingSize", 39);

        switch (functionsClass.shapesImageId()) {
            case 1:
                layoutInflater = R.layout.item_popup_category_droplet;
                break;
            case 2:
                layoutInflater = R.layout.item_popup_category_circle;
                break;
            case 3:
                layoutInflater = R.layout.item_popup_category_square;
                break;
            case 4:
                layoutInflater = R.layout.item_popup_category_squircle;
                break;
            case 0:
                layoutInflater = R.layout.item_popup_category_noshape;
                break;
        }

        if (functionsClass.loadCustomIcons()) {
            loadCustomIcons = new LoadCustomIcons(context, functionsClass.customIconPackageName());
        }
    }

    public PopupCategoryOptionAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems, String className, int startId,
                                      int xPosition, int yPosition, int HW) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        this.className = className;
        this.startId = startId;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.HW = HW;
        functionsClass = new FunctionsClass(context);

        PublicVariable.size = functionsClass.readDefaultPreference("floatingSize", 39);

        switch (functionsClass.shapesImageId()) {
            case 1:
                layoutInflater = R.layout.item_popup_category_droplet;
                break;
            case 2:
                layoutInflater = R.layout.item_popup_category_circle;
                break;
            case 3:
                layoutInflater = R.layout.item_popup_category_square;
                break;
            case 4:
                layoutInflater = R.layout.item_popup_category_squircle;
                break;
            case 0:
                layoutInflater = R.layout.item_popup_category_noshape;
                break;
        }

        if (functionsClass.loadCustomIcons()) {
            loadCustomIcons = new LoadCustomIcons(context, functionsClass.customIconPackageName());
        }
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(layoutInflater, null);

            viewHolder = new ViewHolder();
            viewHolder.items = (RelativeLayout) convertView.findViewById(R.id.items);
            viewHolder.imgIcon = (ShapesImage) convertView.findViewById(R.id.iconItem);
            viewHolder.split_one = (ShapesImage) convertView.findViewById(R.id.split_one);
            viewHolder.split_two = (ShapesImage) convertView.findViewById(R.id.split_two);
            viewHolder.textAppName = (TextView) convertView.findViewById(R.id.itemAppName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            if (context.getFileStreamPath(navDrawerItems.get(position).getPackageName() + ".SplitOne").exists()
                    && context.getFileStreamPath(navDrawerItems.get(position).getPackageName() + ".SplitTwo").exists()
                    && navDrawerItems.get(position).getAppName().equals(context.getString(R.string.splitIt))) {
                splitOne = functionsClass.loadCustomIcons() ?
                        loadCustomIcons.getDrawableIconForPackage(functionsClass.readFile(navDrawerItems.get(position).getPackageName() + ".SplitOne"), functionsClass.shapedAppIcon(functionsClass.readFile(navDrawerItems.get(position).getPackageName() + ".SplitOne")))
                        :
                        functionsClass.shapedAppIcon(functionsClass.readFile(navDrawerItems.get(position).getPackageName() + ".SplitOne"));
                splitTwo = functionsClass.loadCustomIcons() ?
                        loadCustomIcons.getDrawableIconForPackage(functionsClass.readFile(navDrawerItems.get(position).getPackageName() + ".SplitTwo"), functionsClass.shapedAppIcon(functionsClass.readFile(navDrawerItems.get(position).getPackageName() + ".SplitTwo")))
                        :
                        functionsClass.shapedAppIcon(functionsClass.readFile(navDrawerItems.get(position).getPackageName() + ".SplitTwo"));

                viewHolder.split_one.setImageDrawable(splitOne);
                viewHolder.split_two.setImageDrawable(splitTwo);

                viewHolder.split_one.setImageAlpha(functionsClass.readDefaultPreference("autoTrans", 255));
                viewHolder.split_two.setImageAlpha(functionsClass.readDefaultPreference("autoTrans", 255));
            } else if (navDrawerItems.get(position).getAppName().equals(context.getString(R.string.splitIt))) {
                splitOne = functionsClass.loadCustomIcons() ?
                        loadCustomIcons.getDrawableIconForPackage(functionsClass.readFileLine(navDrawerItems.get(position).getPackageName())[0], functionsClass.shapedAppIcon(functionsClass.readFileLine(navDrawerItems.get(position).getPackageName())[0]))
                        :
                        functionsClass.shapedAppIcon(functionsClass.readFileLine(navDrawerItems.get(position).getPackageName())[0]);
                splitTwo = functionsClass.loadCustomIcons() ?
                        loadCustomIcons.getDrawableIconForPackage(functionsClass.readFileLine(navDrawerItems.get(position).getPackageName())[1], functionsClass.shapedAppIcon(functionsClass.readFileLine(navDrawerItems.get(position).getPackageName())[1]))
                        :
                        functionsClass.shapedAppIcon(functionsClass.readFileLine(navDrawerItems.get(position).getPackageName())[1]);

                viewHolder.split_one.setImageDrawable(splitOne);
                viewHolder.split_two.setImageDrawable(splitTwo);

                viewHolder.split_one.setImageAlpha(functionsClass.readDefaultPreference("autoTrans", 255));
                viewHolder.split_two.setImageAlpha(functionsClass.readDefaultPreference("autoTrans", 255));
            } else {
                viewHolder.split_one.setImageDrawable(null);
                viewHolder.split_two.setImageDrawable(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.imgIcon.setImageDrawable(navDrawerItems.get(position).getAppIcon());
        viewHolder.textAppName.setText(navDrawerItems.get(position).getAppName());

        viewHolder.imgIcon.setImageAlpha(functionsClass.readDefaultPreference("autoTrans", 255));
        viewHolder.textAppName.setAlpha(functionsClass.readDefaultPreference("autoTrans", 255) < 130 ? 0.70f : 1.0f);

        int itemsListColor;
        if (functionsClass.appThemeTransparent() == true) {
            itemsListColor = functionsClass.setColorAlpha(PublicVariable.colorLightDark, 77);
        } else {
            itemsListColor = PublicVariable.colorLightDark;
        }

        LayerDrawable drawPopupShortcut = (LayerDrawable) context.getDrawable(R.drawable.popup_shortcut_whole);
        GradientDrawable backPopupShortcut = (GradientDrawable) drawPopupShortcut.findDrawableByLayerId(R.id.backtemp);
        backPopupShortcut.setColor(itemsListColor);
        viewHolder.items.setBackground(drawPopupShortcut);
        viewHolder.textAppName.setTextColor(PublicVariable.colorLightDarkOpposite);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (navDrawerItems.get(position).getAppName().contains(context.getString(R.string.edit_category))) {
                        context.startActivity(new Intent(context, CategoryHandler.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    } else if (navDrawerItems.get(position).getAppName().contains(context.getString(R.string.remove_category))) {
                        context.sendBroadcast(new Intent("Remove_Category_" + className).putExtra("startId", startId));
                    } else if (navDrawerItems.get(position).getAppName().contains(context.getString(R.string.unpin_category))) {
                        context.sendBroadcast(new Intent("Unpin_App_" + className).putExtra("startId", startId));
                    } else if (navDrawerItems.get(position).getAppName().contains(context.getString(R.string.pin_category))) {
                        context.sendBroadcast(new Intent("Pin_App_" + className).putExtra("startId", startId));
                    } else if (navDrawerItems.get(position).getAppName().contains(context.getString(R.string.splitIt))) {
                        if (!functionsClass.AccessibilityServiceEnabled() && !functionsClass.SettingServiceRunning(InteractionObserver.class)) {
                            context.startActivity(new Intent(context, CheckPoint.class)
                                    .putExtra(context.getString(R.string.splitIt), context.getPackageName())
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        } else {
                            final AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService(ACCESSIBILITY_SERVICE);
                            AccessibilityEvent event = AccessibilityEvent.obtain();
                            event.setSource(view);
                            event.setEventType(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
                            event.setAction(10296);
                            event.setClassName(className);
                            event.getText().add(context.getPackageName());
                            accessibilityManager.sendAccessibilityEvent(event);
                        }
                    } else {
                        if (functionsClass.splashReveal()) {
                            Intent splashReveal = new Intent(context, FloatingSplash.class);
                            splashReveal.putExtra("packageName", navDrawerItems.get(position).getPackageName());
                            splashReveal.putExtra("X", xPosition);
                            splashReveal.putExtra("Y", yPosition);
                            splashReveal.putExtra("HW", HW);
                            context.startService(splashReveal);
                        } else {
                            if (functionsClass.FreeForm()) {
                                functionsClass.openApplicationFreeForm(navDrawerItems.get(position).getPackageName(),
                                        xPosition,
                                        (functionsClass.displayX() / 2),
                                        yPosition,
                                        (functionsClass.displayY() / 2)
                                );
                            } else {
                                functionsClass.appsLaunchPad(navDrawerItems.get(position).getPackageName());
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                context.sendBroadcast(new Intent("Hide_PopupListView_Category"));
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (functionsClass.returnAPI() >= 24) {
                    if (!functionsClass.AccessibilityServiceEnabled() && !functionsClass.SettingServiceRunning(InteractionObserver.class)) {
                        context.startActivity(new Intent(context, CheckPoint.class)
                                .putExtra(context.getString(R.string.splitIt), context.getPackageName())
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    } else {
                        PublicVariable.splitSinglePackage = navDrawerItems.get(position).getPackageName();
                        if (functionsClass.appInstalledOrNot(PublicVariable.splitSinglePackage)) {
                            final AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService(ACCESSIBILITY_SERVICE);
                            AccessibilityEvent event = AccessibilityEvent.obtain();
                            event.setSource(view);
                            event.setEventType(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
                            event.setAction(69201);
                            event.setClassName(className);
                            event.getText().add(context.getPackageName());
                            accessibilityManager.sendAccessibilityEvent(event);
                        }
                    }
                }
                context.sendBroadcast(new Intent("Hide_PopupListView_Category"));
                return true;
            }
        });

        return convertView;
    }

    static class ViewHolder {
        RelativeLayout items;
        ShapesImage imgIcon;
        ShapesImage split_one;
        ShapesImage split_two;
        TextView textAppName;
    }
}
