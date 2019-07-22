package net.geekstools.floatshort.PRO.Category.NavAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.UI.FloatingSplash;
import net.geekstools.imageview.customshapes.ShapesImage;

import java.util.ArrayList;

public class PopupCategoryOptionAdapter extends BaseAdapter {

    FunctionsClass functionsClass;
    Context context;
    ArrayList<NavDrawerItem> navDrawerItems;
    String className;
    int startId, layoutInflater, xPosition, yPosition, HW;


    public PopupCategoryOptionAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems, String className, int startId) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        this.className = className;
        this.startId = startId;

        functionsClass = new FunctionsClass(context);

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
            case 5:
                layoutInflater = R.layout.item_popup_category_cut_circle;
                break;
            case 0:
                layoutInflater = R.layout.item_popup_category_noshape;
                break;
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
            case 5:
                layoutInflater = R.layout.item_popup_category_cut_circle;
                break;
            case 0:
                layoutInflater = R.layout.item_popup_category_noshape;
                break;
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
            viewHolder.textAppName = (TextView) convertView.findViewById(R.id.itemAppName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imgIcon.setImageDrawable(navDrawerItems.get(position).getIcon());
        viewHolder.textAppName.setText(navDrawerItems.get(position).getAppName());

        viewHolder.imgIcon.setImageAlpha(PublicVariable.hide ? 157 : 250);
        viewHolder.textAppName.setAlpha(PublicVariable.hide ? 0.77f : 1.0f);

        int itemsListColor;
        if (functionsClass.setAppTransparency() == true) {
            itemsListColor = functionsClass.setColorAlpha(PublicVariable.colorLightDark, 157);
        } else {
            itemsListColor = PublicVariable.colorLightDark;
        }

        LayerDrawable drawPopupShortcut = (LayerDrawable) context.getResources().getDrawable(R.drawable.popup_shortcut_whole);
        GradientDrawable backPopupShortcut = (GradientDrawable) drawPopupShortcut.findDrawableByLayerId(R.id.backtemp);
        backPopupShortcut.setColor(itemsListColor);
        viewHolder.items.setBackground(drawPopupShortcut);
        viewHolder.textAppName.setTextColor(PublicVariable.colorLightDarkOpposite);

        viewHolder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (navDrawerItems.get(position).getAppName().contains(context.getString(R.string.remove_category))) {
                    context.sendBroadcast(new Intent("Remove_Category_" + className).putExtra("startId", startId));
                } else if (navDrawerItems.get(position).getAppName().contains(context.getString(R.string.pin_category))) {
                    context.sendBroadcast(new Intent("Pin_App_" + className).putExtra("startId", startId));
                } else if (navDrawerItems.get(position).getAppName().contains(context.getString(R.string.unpin_category))) {
                    context.sendBroadcast(new Intent("Unpin_App_" + className).putExtra("startId", startId));
                } else {
                    if (functionsClass.splashReveal()) {
                        Intent splashReveal = new Intent(context, FloatingSplash.class);
                        splashReveal.putExtra("packageName", navDrawerItems.get(position).getPackageName());
                        splashReveal.putExtra("X", xPosition);
                        splashReveal.putExtra("Y", yPosition);
                        splashReveal.putExtra("HW", HW);
                        context.startService(splashReveal);
                    } else {
                        functionsClass.appsLaunchPad(navDrawerItems.get(position).getPackageName());
                    }
                }
                context.sendBroadcast(new Intent("Hide_PopupListView_Category"));
            }
        });

        return convertView;
    }

    static class ViewHolder {
        RelativeLayout items;
        ShapesImage imgIcon;
        TextView textAppName;
    }
}
