package net.geekstools.floatshort.PRO.Widget.NavAdapter;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.android.material.textfield.TextInputEditText;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Widget.RoomDatabase.WidgetDataDAO;
import net.geekstools.floatshort.PRO.Widget.RoomDatabase.WidgetDataInterface;
import net.geekstools.floatshort.PRO.Widget.WidgetConfigurations;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ConfiguredWidgetsAdapter extends RecyclerView.Adapter<ConfiguredWidgetsAdapter.ViewHolder> {

    Activity activity;
    Context context;

    FunctionsClass functionsClass;

    ArrayList<NavDrawerItem> navDrawerItems;

    View view;
    ViewHolder viewHolder;

    AppWidgetManager appWidgetManager;
    AppWidgetHost appWidgetHost;

    public ConfiguredWidgetsAdapter(Activity activity, Context context, ArrayList<NavDrawerItem> navDrawerItems, AppWidgetManager appWidgetManager, AppWidgetHost appWidgetHost) {
        this.context = context;
        this.activity = activity;

        this.navDrawerItems = navDrawerItems;

        this.appWidgetManager = appWidgetManager;
        this.appWidgetHost = appWidgetHost;

        functionsClass = new FunctionsClass(context);
    }

    @Override
    public ConfiguredWidgetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.widget_configured_items, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolderBinder, final int position) {
        AppWidgetProviderInfo appWidgetProviderInfo = navDrawerItems.get(position).getAppWidgetProviderInfo();
        int appWidgetId = navDrawerItems.get(position).getAppWidgetId();

        WidgetConfigurations.createWidget(activity, viewHolder.widgetPreview,
                appWidgetManager, appWidgetHost,
                appWidgetProviderInfo, appWidgetId);

        viewHolder.widgetLabel.setText(navDrawerItems.get(position).getAddedWidgetRecovery() ? navDrawerItems.get(position).getWidgetLabel() + " " + "\uD83D\uDD04" : navDrawerItems.get(position).getWidgetLabel());
        viewHolder.widgetLabel.setTextColor(PublicVariable.themeLightDark ? context.getColor(R.color.dark) : context.getColor(R.color.light));

        LayerDrawable drawFloatTheWidget = (LayerDrawable) context.getDrawable(R.drawable.draw_open);
        Drawable backFloatTheWidget = drawFloatTheWidget.findDrawableByLayerId(R.id.backtemp);
        backFloatTheWidget.setTint(functionsClass.extractDominantColor(functionsClass.appIcon(navDrawerItems.get(position).getPackageName())));
        viewHolder.floatTheWidget.setImageDrawable(drawFloatTheWidget);

        viewHolder.floatTheWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionsClass.runUnlimitedWidgetService(navDrawerItems.get(position).getAppWidgetId(), navDrawerItems.get(position).getWidgetLabel());
            }
        });
        viewHolder.floatTheWidget.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                functionsClass.doVibrate(77);

                functionsClass.popupOptionWidget(activity, context, view,
                        navDrawerItems.get(position).getPackageName(),
                        navDrawerItems.get(position).getClassNameProviderWidget(),
                        navDrawerItems.get(position).getWidgetLabel(),
                        appWidgetProviderInfo.loadPreviewImage(context, DisplayMetrics.DENSITY_LOW) != null ?
                                appWidgetProviderInfo.loadPreviewImage(context, DisplayMetrics.DENSITY_HIGH) : appWidgetProviderInfo.loadIcon(context, DisplayMetrics.DENSITY_HIGH),
                        navDrawerItems.get(position).getAddedWidgetRecovery());

                return true;
            }
        });

        viewHolder.widgetLabel.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (textView.length() > 0) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                WidgetDataInterface widgetDataInterface = Room.databaseBuilder(context, WidgetDataInterface.class, PublicVariable.WIDGET_DATA_DATABASE_NAME)
                                        .fallbackToDestructiveMigration()
                                        .addCallback(new RoomDatabase.Callback() {
                                            @Override
                                            public void onCreate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                                                super.onCreate(supportSQLiteDatabase);
                                            }

                                            @Override
                                            public void onOpen(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                                                super.onOpen(supportSQLiteDatabase);
                                            }
                                        })
                                        .build();
                                WidgetDataDAO widgetDataDAO = widgetDataInterface.initDataAccessObject();
                                widgetDataDAO.updateWidgetLabelByWidgetId(navDrawerItems.get(position).getAppWidgetId(), textView.getText().toString().replace("\uD83D\uDD04", ""));
                                widgetDataInterface.close();

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        context.sendBroadcast(new Intent("FORCE_RELOAD"));
                                    }
                                });
                            }
                        }).start();
                    }

                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return navDrawerItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout widgetItem, widgetPreview;
        ImageView floatTheWidget;
        TextInputEditText widgetLabel;

        public ViewHolder(View view) {
            super(view);
            widgetItem = (RelativeLayout) view.findViewById(R.id.widgetItem);
            widgetPreview = (RelativeLayout) view.findViewById(R.id.widgetPreview);
            widgetLabel = (TextInputEditText) view.findViewById(R.id.widgetLabel);
            floatTheWidget = (ImageView) view.findViewById(R.id.floatTheWidget);
        }
    }
}
