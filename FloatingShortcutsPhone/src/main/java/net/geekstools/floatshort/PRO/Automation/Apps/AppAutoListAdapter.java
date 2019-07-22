package net.geekstools.floatshort.PRO.Automation.Apps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.geekstools.floatshort.PRO.Automation.Alarms.TimeDialogue;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.imageview.customshapes.ShapesImage;

import java.io.File;
import java.util.ArrayList;

public class AppAutoListAdapter extends BaseAdapter {

    FunctionsClass functionsClass;
    TextView[] timeView;
    CheckBox[] autoChoice;
    String autoIdAppend;
    int layoutInflater;
    private Context context;
    private Activity activity;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public AppAutoListAdapter(Activity activity, Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.activity = activity;
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        functionsClass = new FunctionsClass(context);

        autoChoice = new CheckBox[navDrawerItems.size()];
        timeView = new TextView[navDrawerItems.size()];

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
            viewHolder.item = (RelativeLayout) convertView.findViewById(R.id.item);
            viewHolder.imgIcon = (ShapesImage) convertView.findViewById(R.id.icon);
            viewHolder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        timeView[position] = (TextView) convertView.findViewById(R.id.time);
        timeView[position].setTextColor(functionsClass.setColorAlpha(PublicVariable.colorLightDarkOpposite, 113));
        autoChoice[position] = (CheckBox) convertView.findViewById(R.id.autoChoice);

        if (PublicVariable.themeLightDark) {
            autoChoice[position].setButtonTintList(ColorStateList.valueOf(context.getColor(R.color.dark)));
        } else if (!PublicVariable.themeLightDark) {
            viewHolder.txtDesc.setTextColor(context.getColor(R.color.light));
            autoChoice[position].setButtonTintList(ColorStateList.valueOf(context.getColor(R.color.light)));
        }

        viewHolder.imgIcon.setImageDrawable(navDrawerItems.get(position).getAppIcon());
        viewHolder.txtDesc.setText(navDrawerItems.get(position).getAppName());

        try {
            if (PublicVariable.autoID.equals(context.getString(R.string.time))) {
                final String pack = navDrawerItems.get(position).getPackageName();
                File autoFile = context.getFileStreamPath(pack + ".Time");
                autoChoice[position].setChecked(false);
                timeView[position].setVisibility(View.INVISIBLE);
                if (autoFile.exists()) {
                    autoChoice[position].setChecked(true);
                    timeView[position].setText(navDrawerItems.get(position).getTimes());
                    timeView[position].setVisibility(View.VISIBLE);
                } else {
                    autoChoice[position].setChecked(false);
                    timeView[position].setVisibility(View.INVISIBLE);
                }
            } else {
                final String pack = navDrawerItems.get(position).getPackageName();
                File autoFile = context.getFileStreamPath(pack + autoIdAppend);
                autoChoice[position].setChecked(false);
                if (autoFile.exists()) {
                    autoChoice[position].setChecked(true);
                } else {
                    autoChoice[position].setChecked(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
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
                            autoChoice[position].setChecked(false);
                        } else {
                            functionsClass.saveFile(
                                    navDrawerItems.get(position).getPackageName() + ".Wifi",
                                    navDrawerItems.get(position).getPackageName());
                            functionsClass.saveFileAppendLine(
                                    ".autoWifi",
                                    navDrawerItems.get(position).getPackageName());
                            autoChoice[position].setChecked(true);
                        }
                    } else if (PublicVariable.autoID.equals(context.getString(R.string.bluetooth))) {
                        final String pack = navDrawerItems.get(position).getPackageName();
                        File autoFile = context.getFileStreamPath(pack + ".Bluetooth");
                        if (autoFile.exists()) {
                            context.deleteFile(
                                    navDrawerItems.get(position).getPackageName() + ".Bluetooth");
                            functionsClass.removeLine(".autoBluetooth", navDrawerItems.get(position).getPackageName());
                            autoChoice[position].setChecked(false);
                        } else {
                            functionsClass.saveFile(
                                    navDrawerItems.get(position).getPackageName() + ".Bluetooth",
                                    navDrawerItems.get(position).getPackageName());
                            functionsClass.saveFileAppendLine(
                                    ".autoBluetooth",
                                    navDrawerItems.get(position).getPackageName());
                            autoChoice[position].setChecked(true);
                        }
                    } else if (PublicVariable.autoID.equals(context.getString(R.string.gps))) {
                        final String pack = navDrawerItems.get(position).getPackageName();
                        File autoFile = context.getFileStreamPath(pack + ".Gps");
                        if (autoFile.exists()) {
                            context.deleteFile(
                                    navDrawerItems.get(position).getPackageName() + ".Gps");
                            functionsClass.removeLine(".autoGps", navDrawerItems.get(position).getPackageName());
                            autoChoice[position].setChecked(false);
                        } else {
                            functionsClass.saveFile(
                                    navDrawerItems.get(position).getPackageName() + ".Gps",
                                    navDrawerItems.get(position).getPackageName());
                            functionsClass.saveFileAppendLine(
                                    ".autoGps",
                                    navDrawerItems.get(position).getPackageName());
                            autoChoice[position].setChecked(true);
                        }
                    } else if (PublicVariable.autoID.equals(context.getString(R.string.nfc))) {
                        final String pack = navDrawerItems.get(position).getPackageName();
                        File autoFile = context.getFileStreamPath(pack + ".Nfc");
                        if (autoFile.exists()) {
                            context.deleteFile(
                                    navDrawerItems.get(position).getPackageName() + ".Nfc");
                            functionsClass.removeLine(".autoNfc", navDrawerItems.get(position).getPackageName());
                            autoChoice[position].setChecked(false);
                        } else {
                            functionsClass.saveFile(
                                    navDrawerItems.get(position).getPackageName() + ".Nfc",
                                    navDrawerItems.get(position).getPackageName());
                            functionsClass.saveFileAppendLine(
                                    ".autoNfc",
                                    navDrawerItems.get(position).getPackageName());
                            autoChoice[position].setChecked(true);
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
                            autoChoice[position].setChecked(false);
                            timeView[position].setText("");
                            timeView[position].setVisibility(View.INVISIBLE);
                        } else {
                            autoChoice[position].setChecked(true);
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
        GradientDrawable gradientDrawable = (GradientDrawable) drawItem.findDrawableByLayerId(android.R.id.mask);
        gradientDrawable.setColor(PublicVariable.primaryColorOpposite);
        drawItem.setColor(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));
        viewHolder.item.setBackground(drawItem);

        return convertView;
    }

    static class ViewHolder {
        RelativeLayout item;
        ShapesImage imgIcon;
        TextView txtDesc;
    }
}
