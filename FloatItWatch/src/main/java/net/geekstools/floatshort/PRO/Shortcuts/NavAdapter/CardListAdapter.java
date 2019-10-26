package net.geekstools.floatshort.PRO.Shortcuts.NavAdapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.wearable.complications.ProviderUpdateRequester;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryComplication;
import net.geekstools.imageview.customshapes.ShapesImage;

import java.util.ArrayList;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    FunctionsClass functionsClass;
    Context context;
    Activity activity;
    ArrayList<NavDrawerItem> navDrawerItems;

    String PackageName;
    int layoutInflater;

    View view;
    ViewHolder viewHolder;

    public CardListAdapter(Activity activity, Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.activity = activity;
        this.context = context;
        this.navDrawerItems = navDrawerItems;

        functionsClass = new FunctionsClass(context, activity);

        switch (functionsClass.shapesImageId()) {
            case 1:
                layoutInflater = R.layout.item_card_list_droplet;
                break;
            case 2:
                layoutInflater = R.layout.item_card_list_circle;
                break;
            case 3:
                layoutInflater = R.layout.item_card_list_square;
                break;
            case 4:
                layoutInflater = R.layout.item_card_list_squircle;
                break;
            case 5:
                layoutInflater = R.layout.item_card_list_cut_circle;
                break;
            case 0:
                layoutInflater = R.layout.item_card_list_noshape;
                break;
        }
    }

    @Override
    public CardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(layoutInflater, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolderBinder, final int position) {
        try {
            viewHolderBinder.recoveryIndicator = viewHolderBinder.recoveryIndicator;
            viewHolderBinder.recoveryIndicator.setVisibility(View.INVISIBLE);
            if (functionsClass.loadRecoveryIndicator(navDrawerItems.get(position).getPackageName()) == true) {
                viewHolderBinder.recoveryIndicator.setVisibility(View.VISIBLE);
            } else {
                viewHolderBinder.recoveryIndicator.setVisibility(View.INVISIBLE);
            }
            viewHolderBinder.recoveryIndicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    functionsClass.removeLine(".uFile", navDrawerItems.get(position).getPackageName());
                    viewHolderBinder.recoveryIndicator.setVisibility(View.INVISIBLE);
                    functionsClass.updateRecoverShortcuts();

                    try {
                        ProviderUpdateRequester requester = new ProviderUpdateRequester(context, new ComponentName(context, RecoveryComplication.class));
                        requester.requestUpdate(functionsClass.readPreference("ComplicationProviderService", "ComplicationedId", 0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (functionsClass.checkThemeLightDark()) {
            viewHolderBinder.appName.setTextColor(context.getResources().getColor(R.color.dark));
        } else if (!functionsClass.checkThemeLightDark()) {
            viewHolderBinder.appName.setTextColor(context.getResources().getColor(R.color.light));
        }

        viewHolderBinder.appIcon.setImageDrawable(navDrawerItems.get(position).getIcon());
        viewHolderBinder.appName.setText(navDrawerItems.get(position).getAppName());

        SharedPreferences sharedPrefs = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
        PublicVariable.alpha = 133;
        PublicVariable.opacity = 255;
        if (sharedPrefs.getBoolean("hide", false) == false) {
            PublicVariable.hide = false;
        } else if (sharedPrefs.getBoolean("hide", false) == true) {
            PublicVariable.hide = true;
        }
        PublicVariable.size = 33;
        PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, context.getResources().getDisplayMetrics());

        viewHolderBinder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageName = navDrawerItems.get(position).getPackageName();
                functionsClass.runUnlimitedShortcutsService(PackageName);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            viewHolderBinder.recoveryIndicator.setVisibility(View.VISIBLE);

                            ProviderUpdateRequester requester = new ProviderUpdateRequester(context, new ComponentName(context, RecoveryComplication.class));
                            requester.requestUpdate(functionsClass.readPreference("ComplicationProviderService", "ComplicationedId", 0));

                            notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 333);
            }
        });

        viewHolderBinder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PackageName = navDrawerItems.get(position).getPackageName();
                functionsClass.appsLaunchPad(PackageName);

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return navDrawerItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout item;
        ShapesImage appIcon;
        TextView appName;
        Button recoveryIndicator;

        public ViewHolder(View view) {
            super(view);
            item = (RelativeLayout) view.findViewById(R.id.item);
            appIcon = (ShapesImage) view.findViewById(R.id.icon);
            appName = (TextView) view.findViewById(R.id.desc);
            recoveryIndicator = (Button) view.findViewById(R.id.recoveryIndicator);
        }
    }
}
