package net.geekstools.floatshort.PRO.Automation.Apps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import net.geekstools.floatshort.PRO.Automation.Alarms.TimeDialogue;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.imageview.customshapes.ShapesImage;

import java.io.File;
import java.util.ArrayList;

public class AppAutoListAdapter extends RecyclerView.Adapter<AppAutoListAdapter.ViewHolder> {

    private Context context;
    private Activity activity;

    FunctionsClass functionsClass;

    View view;
    ViewHolder viewHolder;

    String autoIdAppend;
    int layoutInflater;

    private ArrayList<NavDrawerItem> navDrawerItems;

    public AppAutoListAdapter(Activity activity, Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.activity = activity;
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        functionsClass = new FunctionsClass(context);

        switch (functionsClass.shapesImageId()) {
            case 1:
                layoutInflater = R.layout.auto_apps_items_droplet;
                break;
            case 2:
                layoutInflater = R.layout.auto_apps_items_circle;
                break;
            case 3:
                layoutInflater = R.layout.auto_apps_items_square;
                break;
            case 4:
                layoutInflater = R.layout.auto_apps_items_squircle;
                break;
            case 0:
                layoutInflater = R.layout.auto_apps_items_noshape;
                break;
        }

        if (PublicVariable.autoID != null) {
            if (PublicVariable.autoID.equals(context.getString(R.string.wifi))) {
                autoIdAppend = ".Wifi";
            } else if (PublicVariable.autoID.equals(context.getString(R.string.bluetooth))) {
                autoIdAppend = ".Bluetooth";
            } else if (PublicVariable.autoID.equals(context.getString(R.string.gps))) {
                autoIdAppend = ".Gps";
            } else if (PublicVariable.autoID.equals(context.getString(R.string.nfc))) {
                autoIdAppend = ".Nfc";
            } else if (PublicVariable.autoID.equals(context.getString(R.string.time))) {
                autoIdAppend = ".Time";
            }
        }
    }

    @Override
    public AppAutoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(layoutInflater, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolderBinder, final int position) {
        viewHolderBinder.time.setTextColor(functionsClass.setColorAlpha(PublicVariable.colorLightDarkOpposite, 113));

        if (PublicVariable.themeLightDark) {
            viewHolderBinder.autoChoice.setButtonTintList(ColorStateList.valueOf(context.getColor(R.color.dark)));
        } else if (!PublicVariable.themeLightDark) {
            viewHolderBinder.desc.setTextColor(context.getColor(R.color.light));
            viewHolderBinder.autoChoice.setButtonTintList(ColorStateList.valueOf(context.getColor(R.color.light)));
        }

        viewHolderBinder.icon.setImageDrawable(navDrawerItems.get(position).getAppIcon());
        viewHolderBinder.desc.setText(navDrawerItems.get(position).getAppName());

        try {
            if (PublicVariable.autoID.equals(context.getString(R.string.time))) {
                final String pack = navDrawerItems.get(position).getPackageName();
                File autoFile = context.getFileStreamPath(pack + ".Time");
                viewHolderBinder.autoChoice.setChecked(false);
                viewHolderBinder.time.setVisibility(View.INVISIBLE);
                if (autoFile.exists()) {
                    viewHolderBinder.autoChoice.setChecked(true);
                    viewHolderBinder.time.setText(navDrawerItems.get(position).getTimes());
                    viewHolderBinder.time.setVisibility(View.VISIBLE);
                } else {
                    viewHolderBinder.autoChoice.setChecked(false);
                    viewHolderBinder.time.setVisibility(View.INVISIBLE);
                }
            } else {
                final String pack = navDrawerItems.get(position).getPackageName();
                File autoFile = context.getFileStreamPath(pack + autoIdAppend);
                viewHolderBinder.autoChoice.setChecked(false);
                if (autoFile.exists()) {
                    viewHolderBinder.autoChoice.setChecked(true);
                } else {
                    viewHolderBinder.autoChoice.setChecked(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolderBinder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PublicVariable.autoID == null) {
                    Toast.makeText(context, context.getString(R.string.retry), Toast.LENGTH_LONG).show();
                } else {
                    if (PublicVariable.autoID.equals(context.getString(R.string.wifi))) {
                        final String pack = navDrawerItems.get(position).getPackageName();
                        File autoFile = context.getFileStreamPath(pack + ".Wifi");
                        if (autoFile.exists()) {
                            context.deleteFile(
                                    navDrawerItems.get(position).getPackageName() + ".Wifi");
                            functionsClass.removeLine(".autoWifi", navDrawerItems.get(position).getPackageName());
                            viewHolderBinder.autoChoice.setChecked(false);
                        } else {
                            functionsClass.saveFile(
                                    navDrawerItems.get(position).getPackageName() + ".Wifi",
                                    navDrawerItems.get(position).getPackageName());
                            functionsClass.saveFileAppendLine(
                                    ".autoWifi",
                                    navDrawerItems.get(position).getPackageName());
                            viewHolderBinder.autoChoice.setChecked(true);
                        }
                    } else if (PublicVariable.autoID.equals(context.getString(R.string.bluetooth))) {
                        final String pack = navDrawerItems.get(position).getPackageName();
                        File autoFile = context.getFileStreamPath(pack + ".Bluetooth");
                        if (autoFile.exists()) {
                            context.deleteFile(
                                    navDrawerItems.get(position).getPackageName() + ".Bluetooth");
                            functionsClass.removeLine(".autoBluetooth", navDrawerItems.get(position).getPackageName());
                            viewHolderBinder.autoChoice.setChecked(false);
                        } else {
                            functionsClass.saveFile(
                                    navDrawerItems.get(position).getPackageName() + ".Bluetooth",
                                    navDrawerItems.get(position).getPackageName());
                            functionsClass.saveFileAppendLine(
                                    ".autoBluetooth",
                                    navDrawerItems.get(position).getPackageName());
                            viewHolderBinder.autoChoice.setChecked(true);
                        }
                    } else if (PublicVariable.autoID.equals(context.getString(R.string.gps))) {
                        final String pack = navDrawerItems.get(position).getPackageName();
                        File autoFile = context.getFileStreamPath(pack + ".Gps");
                        if (autoFile.exists()) {
                            context.deleteFile(
                                    navDrawerItems.get(position).getPackageName() + ".Gps");
                            functionsClass.removeLine(".autoGps", navDrawerItems.get(position).getPackageName());
                            viewHolderBinder.autoChoice.setChecked(false);
                        } else {
                            functionsClass.saveFile(
                                    navDrawerItems.get(position).getPackageName() + ".Gps",
                                    navDrawerItems.get(position).getPackageName());
                            functionsClass.saveFileAppendLine(
                                    ".autoGps",
                                    navDrawerItems.get(position).getPackageName());
                            viewHolderBinder.autoChoice.setChecked(true);
                        }
                    } else if (PublicVariable.autoID.equals(context.getString(R.string.nfc))) {
                        final String pack = navDrawerItems.get(position).getPackageName();
                        File autoFile = context.getFileStreamPath(pack + ".Nfc");
                        if (autoFile.exists()) {
                            context.deleteFile(
                                    navDrawerItems.get(position).getPackageName() + ".Nfc");
                            functionsClass.removeLine(".autoNfc", navDrawerItems.get(position).getPackageName());
                            viewHolderBinder.autoChoice.setChecked(false);
                        } else {
                            functionsClass.saveFile(
                                    navDrawerItems.get(position).getPackageName() + ".Nfc",
                                    navDrawerItems.get(position).getPackageName());
                            functionsClass.saveFileAppendLine(
                                    ".autoNfc",
                                    navDrawerItems.get(position).getPackageName());
                            viewHolderBinder.autoChoice.setChecked(true);
                        }
                    } else if (PublicVariable.autoID.equals(context.getString(R.string.time))) {
                        final String pack = navDrawerItems.get(position).getPackageName();

                        File autoFile = context.getFileStreamPath(pack + ".Time");
                        if (autoFile.exists()) {
                            context.deleteFile(
                                    navDrawerItems.get(position).getPackageName() + ".Time");
                            functionsClass.removeLine(
                                    navDrawerItems.get(position).getTimes(),
                                    navDrawerItems.get(position).getPackageName());
                            if (functionsClass.countLineInnerFile(navDrawerItems.get(position).getPackageName()) == 0) {
                                context.deleteFile(navDrawerItems.get(position).getTimes());
                            }

                            functionsClass.removeLine(".times.clocks", navDrawerItems.get(position).getTimes());
                            viewHolderBinder.autoChoice.setChecked(false);
                            viewHolderBinder.time.setText("");
                            viewHolderBinder.time.setVisibility(View.INVISIBLE);
                        } else {
                            viewHolderBinder.autoChoice.setChecked(true);
                            context.startActivity(
                                    new Intent(context, TimeDialogue.class)
                                            .putExtra("content", navDrawerItems.get(position).getPackageName())
                                            .putExtra("type", "APP")
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    } else {
                        Toast.makeText(context, context.getString(R.string.retry), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        RippleDrawable drawItem = (RippleDrawable) context.getDrawable(R.drawable.ripple_effect);
        Drawable gradientDrawable = drawItem.findDrawableByLayerId(android.R.id.mask);
        gradientDrawable.setTint(PublicVariable.primaryColorOpposite);
        drawItem.setColor(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));
        viewHolder.item.setBackground(drawItem);
    }

    @Override
    public int getItemCount() {
        return navDrawerItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout item;
        CheckBox autoChoice;
        ShapesImage icon;
        TextView desc;
        TextView time;

        public ViewHolder(View view) {
            super(view);
            item = (RelativeLayout) view.findViewById(R.id.item);
            autoChoice = (CheckBox) view.findViewById(R.id.autoChoice);
            icon = (ShapesImage) view.findViewById(R.id.icon);
            desc = (TextView) view.findViewById(R.id.desc);
            time = (TextView) view.findViewById(R.id.time);
        }
    }
}
