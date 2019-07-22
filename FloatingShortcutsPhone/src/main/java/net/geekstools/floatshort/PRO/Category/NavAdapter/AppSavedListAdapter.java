package net.geekstools.floatshort.PRO.Category.NavAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;

import java.util.ArrayList;

public class AppSavedListAdapter extends BaseAdapter {

    FunctionsClass functionsClass;
    int splitNumber = 1, layoutInflater;
    private Context context;
    private Activity activity;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public AppSavedListAdapter(Activity activity, Context context, ArrayList<NavDrawerItem> navDrawerItems, int splitNumber) {
        this.activity = activity;
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        this.splitNumber = splitNumber;

        functionsClass = new FunctionsClass(context, activity);

        switch (functionsClass.shapesImageId()) {
            case 1:
                layoutInflater = R.layout.item_saved_app_droplet;
                break;
            case 2:
                layoutInflater = R.layout.item_saved_app_circle;
                break;
            case 3:
                layoutInflater = R.layout.item_saved_app_square;
                break;
            case 4:
                layoutInflater = R.layout.item_saved_app_squircle;
                break;
            case 0:
                layoutInflater = R.layout.item_saved_app_noshape;
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
            viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.iconItem);
            viewHolder.textAppName = (TextView) convertView.findViewById(R.id.itemAppName);
            viewHolder.deleteItem = (Button) convertView.findViewById(R.id.deleteItem);
            viewHolder.confirmItem = (Button) convertView.findViewById(R.id.confirmItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (functionsClass.returnAPI() < 24) {
            viewHolder.confirmItem.setVisibility(View.INVISIBLE);
        }

        LayerDrawable drawConfirm = (LayerDrawable) context.getDrawable(R.drawable.ripple_effect_confirm);
        GradientDrawable backConfirm = (GradientDrawable) drawConfirm.findDrawableByLayerId(R.id.backtemp);
        backConfirm.setColor(PublicVariable.primaryColorOpposite);
        viewHolder.confirmItem.setBackground(drawConfirm);
        viewHolder.textAppName.setTextColor(context.getColor(R.color.light));

        viewHolder.imgIcon.setImageDrawable(navDrawerItems.get(position).getAppIcon());
        viewHolder.textAppName.setText(navDrawerItems.get(position).getAppName());

        viewHolder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewHolder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.deleteFile(navDrawerItems.get(position).getPackageName()
                        + PublicVariable.categoryName);
                functionsClass.removeLine(PublicVariable.categoryName,
                        navDrawerItems.get(position).getPackageName());
                context.sendBroadcast(new Intent(context.getString(R.string.checkboxActionAdvance)));
                context.sendBroadcast(new Intent(context.getString(R.string.counterActionAdvance)));
            }
        });
        viewHolder.confirmItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (splitNumber == 1) {
                    functionsClass.saveFile(PublicVariable.categoryName + ".SplitOne", navDrawerItems.get(position).getPackageName());
                } else if (splitNumber == 2) {
                    functionsClass.saveFile(PublicVariable.categoryName + ".SplitTwo", navDrawerItems.get(position).getPackageName());
                }
                context.sendBroadcast(new Intent(context.getString(R.string.splitActionAdvance)));
                context.sendBroadcast(new Intent(context.getString(R.string.visibilityActionAdvance)));
            }
        });

        RippleDrawable rippleDrawableBack = (RippleDrawable) context.getDrawable(R.drawable.saved_app_shortcut_whole);
        GradientDrawable gradientDrawableBack = (GradientDrawable) rippleDrawableBack.findDrawableByLayerId(R.id.backtemp);
        GradientDrawable gradientDrawableBackMask = (GradientDrawable) rippleDrawableBack.findDrawableByLayerId(android.R.id.mask);

        rippleDrawableBack.setColor(ColorStateList.valueOf(PublicVariable.colorLightDark));
        gradientDrawableBack.setColor(PublicVariable.primaryColorOpposite);
        gradientDrawableBackMask.setColor(PublicVariable.colorLightDark);

        viewHolder.items.setBackground(rippleDrawableBack);

        return convertView;
    }

    static class ViewHolder {
        RelativeLayout items;
        ImageView imgIcon;
        TextView textAppName;
        Button deleteItem;
        Button confirmItem;
    }
}
