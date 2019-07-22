package net.geekstools.floatshort.PRO.Automation.Categories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import net.geekstools.floatshort.PRO.Automation.Alarms.TimeDialogue;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;

import java.io.File;
import java.util.ArrayList;

public class CategoryAutoListAdapter extends RecyclerView.Adapter<CategoryAutoListAdapter.ViewHolder> {

    FunctionsClass functionsClass;
    ImageView imageView;
    RelativeLayout freqLayout;
    LinearLayout[] selectedApps;
    TextView[] timeView;
    CheckBox[] autoChoice;
    EditText[] categoryName;
    String autoIdAppend;
    View view;
    ViewHolder viewHolder;
    LoadCustomIcons loadCustomIcons;
    private Context context;
    private Activity activity;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public CategoryAutoListAdapter(Activity activity, Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.activity = activity;
        this.context = context;
        this.navDrawerItems = navDrawerItems;

        functionsClass = new FunctionsClass(context);

        selectedApps = new LinearLayout[navDrawerItems.size()];
        categoryName = new EditText[navDrawerItems.size()];
        timeView = new TextView[navDrawerItems.size()];
        autoChoice = new CheckBox[navDrawerItems.size()];

        if (PublicVariable.autoID != null) {
            if (PublicVariable.autoID.equals(context.getString(R.string.wifi_category))) {
                autoIdAppend = "Wifi";
            } else if (PublicVariable.autoID.equals(context.getString(R.string.bluetooth_category))) {
                autoIdAppend = "Bluetooth";
            } else if (PublicVariable.autoID.equals(context.getString(R.string.gps_category))) {
                autoIdAppend = "Gps";
            } else if (PublicVariable.autoID.equals(context.getString(R.string.nfc_category))) {
                autoIdAppend = "Nfc";
            } else if (PublicVariable.autoID.equals(context.getString(R.string.time_category))) {
                autoIdAppend = "Time";
            }
        }

        if (functionsClass.loadCustomIcons()) {
            loadCustomIcons = new LoadCustomIcons(context, functionsClass.customIconPackageName());
        }
    }

    @Override
    public CategoryAutoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.auto_categories_items, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolderBinder, final int position) {

        RelativeLayout categoryItem = viewHolderBinder.categoryItem;
        selectedApps[position] = viewHolderBinder.selectedApp;
        categoryName[position] = viewHolderBinder.categoryName;
        timeView[position] = viewHolderBinder.timeView;
        autoChoice[position] = viewHolderBinder.autoChoice;

        categoryName[position].setTextColor(PublicVariable.colorLightDarkOpposite);
        categoryName[position].setHintTextColor(functionsClass.setColorAlpha(PublicVariable.colorLightDarkOpposite, 175));

        autoChoice[position].setButtonTintList(ColorStateList.valueOf(PublicVariable.colorLightDarkOpposite));

        final String nameCategory = navDrawerItems.get(position).getCategory();
        final String[] categoryPackages = navDrawerItems.get(position).getPackName();

        categoryName[position].setText(navDrawerItems.get(position).getCategory());
        timeView[position].setText(String.valueOf(nameCategory.charAt(0)).toUpperCase());

        if (nameCategory.equals(context.getPackageName())) {
            try {
                timeView[position].setText(context.getString(R.string.index_item));
                categoryName[position].setText("");
                selectedApps[position].removeAllViews();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            File autoFile = context.getFileStreamPath(nameCategory);
            if (autoFile.exists() && autoFile.isFile()) {
                selectedApps[position].removeAllViews();
                int previewItems = 7;
                if (categoryPackages.length < 7) {
                    previewItems = categoryPackages.length;
                }
                for (int i = 0; i < previewItems; i++) {
                    freqLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.selected_apps_item, null);
                    imageView = functionsClass.initShapesImage(freqLayout, R.id.appSelectedItem);
                    imageView.setImageDrawable(functionsClass.loadCustomIcons() ?
                            loadCustomIcons.getDrawableIconForPackage(categoryPackages[i], functionsClass.shapedAppIcon(categoryPackages[i]))
                            :
                            functionsClass.shapedAppIcon(categoryPackages[i]));
                    selectedApps[position].addView(freqLayout);
                }
            }
        }

        try {
            if (PublicVariable.autoID.equals(context.getString(R.string.time_category))) {
                File autoFile = context.getFileStreamPath(nameCategory + ".Time");
                autoChoice[position].setChecked(false);
                timeView[position].setTextSize(50);
                if (autoFile.exists()) {
                    autoChoice[position].setChecked(true);
                    timeView[position].setTextSize(15);
                    timeView[position].setText(navDrawerItems.get(position).getTimes());
                    timeView[position].setVisibility(View.VISIBLE);
                } else {
                    autoChoice[position].setChecked(false);
                    timeView[position].setTextSize(50);
                }
            } else {
                File autoFile = context.getFileStreamPath(nameCategory + "." + autoIdAppend);
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

        categoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PublicVariable.autoID == null) {
                    Toast.makeText(context, context.getString(R.string.retry), Toast.LENGTH_LONG).show();
                } else {
                    if (PublicVariable.autoID.equals(context.getString(R.string.time_category))) {
                        final String nameCategory = navDrawerItems.get(position).getCategory();

                        File autoFile = context.getFileStreamPath(nameCategory + "." + autoIdAppend);
                        if (autoFile.exists()) {
                            context.deleteFile(
                                    navDrawerItems.get(position).getCategory() + "." + autoIdAppend);
                            functionsClass.removeLine(
                                    navDrawerItems.get(position).getTimes(),
                                    navDrawerItems.get(position).getCategory());
                            if (functionsClass.countLineInnerFile(navDrawerItems.get(position).getCategory()) == 0) {
                                context.deleteFile(navDrawerItems.get(position).getTimes());
                            }

                            functionsClass.removeLine(".times.clocks", navDrawerItems.get(position).getTimes());
                            autoChoice[position].setChecked(false);
                            timeView[position].setTextSize(50);
                            timeView[position].setText(String.valueOf(navDrawerItems.get(position).getCategory().charAt(0)).toUpperCase());
                        } else {
                            autoChoice[position].setChecked(true);
                            context.startActivity(
                                    new Intent(context, TimeDialogue.class)
                                            .putExtra("content", navDrawerItems.get(position).getCategory())
                                            .putExtra("type", "CATEGORY")
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    } else {
                        final String nameCategory = navDrawerItems.get(position).getCategory();
                        File autoFile = context.getFileStreamPath(nameCategory + "." + autoIdAppend);
                        if (autoFile.exists()) {
                            context.deleteFile(
                                    navDrawerItems.get(position).getCategory() + "." + autoIdAppend);
                            functionsClass.removeLine(".auto" + autoIdAppend + "Category", navDrawerItems.get(position).getCategory());
                            autoChoice[position].setChecked(false);
                        } else {
                            functionsClass.saveFile(
                                    navDrawerItems.get(position).getCategory() + "." + autoIdAppend,
                                    navDrawerItems.get(position).getCategory());
                            functionsClass.saveFileAppendLine(
                                    ".auto" + autoIdAppend + "Category",
                                    navDrawerItems.get(position).getCategory());
                            autoChoice[position].setChecked(true);
                        }
                    }
                }
            }
        });

        RippleDrawable drawItem = (RippleDrawable) context.getDrawable(R.drawable.ripple_effect_category_logo);
        drawItem.setDrawableByLayerId(R.id.category_logo_layer, functionsClass.shapesDrawablesCategory(timeView[position]));
        drawItem.setDrawableByLayerId(android.R.id.mask, functionsClass.shapesDrawablesCategory(timeView[position]));
        Drawable categoryLogoLayer = (Drawable) drawItem.findDrawableByLayerId(R.id.category_logo_layer);
        Drawable categoryMask = (Drawable) drawItem.findDrawableByLayerId(android.R.id.mask);
        categoryLogoLayer.setTint(PublicVariable.primaryColorOpposite);
        categoryMask.setTint(PublicVariable.primaryColor);
        timeView[position].setBackground(drawItem);

        RippleDrawable drawCategories = (RippleDrawable) context.getDrawable(R.drawable.auto_category);
        GradientDrawable backCategories = (GradientDrawable) drawCategories.findDrawableByLayerId(R.id.category_item);
        GradientDrawable backCategoriesRipple = (GradientDrawable) drawCategories.findDrawableByLayerId(android.R.id.mask);
        backCategories.setColor(PublicVariable.colorLightDark);
        backCategoriesRipple.setColor(PublicVariable.colorLightDarkOpposite);
        drawCategories.setColor(ColorStateList.valueOf(PublicVariable.primaryColorOpposite));
        drawCategories.setAlpha(7);
        categoryItem.setBackground(drawCategories);
    }

    @Override
    public int getItemCount() {
        return navDrawerItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout categoryItem;
        LinearLayout selectedApp;
        EditText categoryName;
        TextView timeView;
        CheckBox autoChoice;

        public ViewHolder(View view) {
            super(view);
            categoryItem = (RelativeLayout) view.findViewById(R.id.categoryItem);
            selectedApp = (LinearLayout) view.findViewById(R.id.selectedApps);
            categoryName = (EditText) view.findViewById(R.id.categoryName);
            timeView = (TextView) view.findViewById(R.id.time);
            autoChoice = (CheckBox) view.findViewById(R.id.autoChoice);
        }
    }
}
