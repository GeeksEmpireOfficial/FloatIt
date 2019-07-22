package net.geekstools.floatshort.PRO.Shortcuts.NavAdapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.geekstools.floatshort.PRO.CheckPoint;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.InteractionObserver.InteractionObserver;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.ACCESSIBILITY_SERVICE;

public class PopupShortcutsOptionAdapter extends BaseAdapter {

    FunctionsClass functionsClass;
    private Context context;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private String packageName, className, classNameCommand;
    private int startId;

    public PopupShortcutsOptionAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems,
                                       String classNameCommand, String packageName, int startId) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        this.classNameCommand = classNameCommand;
        this.packageName = packageName;
        this.startId = startId;

        functionsClass = new FunctionsClass(context);
        FunctionsClass.println("*** Command ClassName ::: " + classNameCommand + " ***");
    }

    public PopupShortcutsOptionAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems,
                                       String classNameCommand, String packageName, String className, int startId) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        this.classNameCommand = classNameCommand;
        this.packageName = packageName;
        this.className = className;
        this.startId = startId;

        functionsClass = new FunctionsClass(context);
        FunctionsClass.println("*** Command ClassName ::: " + classNameCommand + " ***");
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
            convertView = mInflater.inflate(R.layout.item_popup_app, null);

            viewHolder = new ViewHolder();
            viewHolder.items = (RelativeLayout) convertView.findViewById(R.id.items);
            viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.iconItem);
            viewHolder.textAppName = (TextView) convertView.findViewById(R.id.itemAppName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imgIcon.setImageDrawable(navDrawerItems.get(position).getAppIcon());
        viewHolder.textAppName.setText(navDrawerItems.get(position).getPackageName());

        int itemsListColor = 0;
        if (functionsClass.appThemeTransparent() == true) {
            itemsListColor = functionsClass.setColorAlpha(PublicVariable.colorLightDark, 50);
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
                if (navDrawerItems.get(position).getPackageName().equals(context.getString(R.string.splitIt))) {
                    if (!functionsClass.AccessibilityServiceEnabled() && !functionsClass.SettingServiceRunning(InteractionObserver.class)) {
                        context.startActivity(new Intent(context, CheckPoint.class)
                                .putExtra(context.getString(R.string.splitIt), context.getPackageName())
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    } else {
                        final AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService(ACCESSIBILITY_SERVICE);
                        PublicVariable.splitSinglePackage = packageName;
                        PublicVariable.splitSingleClassName = className;
                        AccessibilityEvent event = AccessibilityEvent.obtain();
                        event.setSource(view);
                        event.setEventType(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
                        event.setAction(69201);
                        event.setClassName(classNameCommand);
                        event.getText().add(context.getPackageName());
                        accessibilityManager.sendAccessibilityEvent(event);
                    }
                } else if (navDrawerItems.get(position).getPackageName().equals(context.getString(R.string.pin))) {
                    context.sendBroadcast(new Intent("Pin_App_" + classNameCommand).putExtra("startId", startId));
                } else if (navDrawerItems.get(position).getPackageName().equals(context.getString(R.string.unpin))) {
                    context.sendBroadcast(new Intent("Unpin_App_" + classNameCommand).putExtra("startId", startId));
                } else if (navDrawerItems.get(position).getPackageName().equals(context.getString(R.string.floatIt))) {
                    context.sendBroadcast(new Intent("Float_It_" + classNameCommand).putExtra("startId", startId));
                } else if (navDrawerItems.get(position).getPackageName().equals(context.getString(R.string.close))) {
                    if (functionsClass.UsageStatsEnabled()) {
                        try {
                            UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                            List<UsageStats> queryUsageStats = mUsageStatsManager
                                    .queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                                            System.currentTimeMillis() - (1000 * 60),            //begin
                                            System.currentTimeMillis());                    //end
                            Collections.sort(queryUsageStats, new Comparator<UsageStats>() {
                                @Override
                                public int compare(UsageStats left, UsageStats right) {
                                    return Long.compare(right.getLastTimeUsed(), left.getLastTimeUsed());
                                }
                            });
                            String inFrontPackageName = queryUsageStats.get(0).getPackageName();
                            if (inFrontPackageName.contains(packageName)) {
                                Intent homeScreen = new Intent(Intent.ACTION_MAIN);
                                homeScreen.addCategory(Intent.CATEGORY_HOME);
                                homeScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(homeScreen,
                                        ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out).toBundle());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (navDrawerItems.get(position).getPackageName().equals(context.getString(R.string.remove))) {
                    context.sendBroadcast(new Intent("Remove_App_" + classNameCommand).putExtra("startId", startId));
                }
                context.sendBroadcast(new Intent("Hide_PopupListView_Shortcuts"));
            }
        });

        return convertView;
    }

    static class ViewHolder {
        RelativeLayout items;
        ImageView imgIcon;
        TextView textAppName;
    }
}
