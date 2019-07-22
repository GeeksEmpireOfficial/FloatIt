package net.geekstools.floatshort.PRO.Shortcuts.NavAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.imageview.customshapes.ShapesImage;

import java.util.ArrayList;

public class PopupShortcutsOptionAdapter extends BaseAdapter {

    FunctionsClass functionsClass;
    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private String packageName, className;
    private int startId;

    public PopupShortcutsOptionAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems,
                                       String className, String packageName, int startId) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        this.className = className;
        this.packageName = packageName;
        this.startId = startId;

        functionsClass = new FunctionsClass(context);
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
            convertView = mInflater.inflate(R.layout.item_popup_category_noshape, null);

            viewHolder = new ViewHolder();
            viewHolder.items = (RelativeLayout) convertView.findViewById(R.id.items);
            viewHolder.imgIcon = (ShapesImage) convertView.findViewById(R.id.iconItem);
            viewHolder.textAppName = (TextView) convertView.findViewById(R.id.itemAppName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imgIcon.setImageDrawable(navDrawerItems.get(position).getIcon());
        viewHolder.textAppName.setText(navDrawerItems.get(position).getTitleText());

        int itemsListColor = 0;
        if (functionsClass.setAppTransparency() == true) {
            itemsListColor = functionsClass.setColorAlpha(PublicVariable.colorLightDark, 50);
        } else {
            itemsListColor = PublicVariable.colorLightDark;
        }

        LayerDrawable drawPopupShortcut = (LayerDrawable) context.getResources().getDrawable(R.drawable.popup_shortcut_whole);
        GradientDrawable backPopupShortcut = (GradientDrawable) drawPopupShortcut.findDrawableByLayerId(R.id.backtemp);
        backPopupShortcut.setColor(itemsListColor);
        viewHolder.items.setBackground(drawPopupShortcut);
        viewHolder.textAppName.setTextColor(PublicVariable.colorLightDarkOpposite);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (navDrawerItems.get(position).getTitleText().equals(context.getString(R.string.pin))) {
                    context.sendBroadcast(new Intent("Pin_App_" + className).putExtra("startId", startId));
                } else if (navDrawerItems.get(position).getTitleText().equals(context.getString(R.string.unpin))) {
                    context.sendBroadcast(new Intent("Unpin_App_" + className).putExtra("startId", startId));
                } else if (navDrawerItems.get(position).getTitleText().equals(context.getString(R.string.remove))) {
                    context.sendBroadcast(new Intent("Remove_App_" + className).putExtra("startId", startId));
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
