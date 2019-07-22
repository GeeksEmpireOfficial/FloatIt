package net.geekstools.floatshort.PRO.Util.NavAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;

import java.util.ArrayList;

public class CustomIconsThemeAdapter extends RecyclerView.Adapter<CustomIconsThemeAdapter.ViewHolder> {

    FunctionsClass functionsClass;
    View view;
    ViewHolder viewHolder;
    private Context context;
    private Activity activity;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public CustomIconsThemeAdapter(Activity activity, Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.activity = activity;
        this.context = context;
        this.navDrawerItems = navDrawerItems;

        functionsClass = new FunctionsClass(context, activity);
    }

    @Override
    public CustomIconsThemeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.custom_icon_items, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolderBinder, final int position) {

        viewHolderBinder.appName.setText(navDrawerItems.get(position).getAppName());
        viewHolderBinder.appName.setTextColor(PublicVariable.colorLightDarkOpposite);

        viewHolderBinder.appIcon.setImageDrawable(navDrawerItems.get(position).getAppIcon());

        viewHolderBinder.customIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionsClass.saveDefaultPreference("customIcon", navDrawerItems.get(position).getPackageName());

                functionsClass.saveDefaultPreference("LitePreferences", false);
                if (functionsClass.loadCustomIcons()) {
                    LoadCustomIcons loadCustomIcons = new LoadCustomIcons(context, navDrawerItems.get(position).getPackageName());
                    loadCustomIcons.load();
                }

                functionsClass.saveDefaultPreference("LitePreferences", false);
                context.sendBroadcast(new Intent("CUSTOM_DIALOGUE_DISMISS"));
            }
        });

        viewHolderBinder.customIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (functionsClass.loadCustomIcons()) {
                    LoadCustomIcons loadCustomIcons = new LoadCustomIcons(context, navDrawerItems.get(position).getPackageName());
                    loadCustomIcons.load();


                    functionsClass.Toast(String.valueOf(loadCustomIcons.getTotalIcons()), Gravity.BOTTOM, PublicVariable.primaryColor);
                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return navDrawerItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout customIcon;
        TextView appName;
        ImageView appIcon;

        public ViewHolder(View view) {
            super(view);
            customIcon = (RelativeLayout) view.findViewById(R.id.customIcon);
            appName = (TextView) view.findViewById(R.id.appName);
            appIcon = (ImageView) view.findViewById(R.id.appIcon);
        }
    }
}
