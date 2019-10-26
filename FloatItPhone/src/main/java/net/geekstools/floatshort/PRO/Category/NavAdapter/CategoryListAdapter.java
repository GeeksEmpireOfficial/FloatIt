package net.geekstools.floatshort.PRO.Category.NavAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.geekstools.floatshort.PRO.Category.AppSelectionList;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;

import java.io.File;
import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    FunctionsClass functionsClass;
    Context context;
    Activity activity;
    ArrayList<NavDrawerItem> navDrawerItems;

    ImageView imageView;
    RelativeLayout freqLayout;

    String endEdited = "";

    View view;
    ViewHolder viewHolder;

    LoadCustomIcons loadCustomIcons;

    public CategoryListAdapter(Activity activity, Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.activity = activity;
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        functionsClass = new FunctionsClass(context);

        PublicVariable.size = functionsClass.readDefaultPreference("floatingSize", 39);
        PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, context.getResources().getDisplayMetrics());

        if (functionsClass.loadCustomIcons()) {
            loadCustomIcons = new LoadCustomIcons(context, functionsClass.customIconPackageName());
        }
    }

    @Override
    public CategoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolderBinder, final int position) {

        LayerDrawable drawCardCategory = (LayerDrawable) context.getDrawable(R.drawable.card_category);
        Drawable backCardCategory = drawCardCategory.findDrawableByLayerId(R.id.category_item);
        backCardCategory.setTint(PublicVariable.colorLightDark);
        drawCardCategory.setAlpha(7);
        viewHolderBinder.categoryItem.setBackground(drawCardCategory);

        viewHolderBinder.categoryName.setTextColor(PublicVariable.colorLightDarkOpposite);
        viewHolderBinder.categoryName.setHintTextColor(functionsClass.setColorAlpha(PublicVariable.colorLightDarkOpposite, 175));

        final String nameCategory = navDrawerItems.get(position).getCategory();
        final String[] categoryPackages = navDrawerItems.get(position).getPackName();

        if (functionsClass.loadRecoveryIndicatorCategory(nameCategory)) {
            viewHolderBinder.categoryName.setText(navDrawerItems.get(position).getCategory() + " " + "\uD83D\uDD04");
        } else {
            viewHolderBinder.categoryName.setText(navDrawerItems.get(position).getCategory());
        }
        viewHolderBinder.runCategory.setText(String.valueOf(nameCategory.charAt(0)).toUpperCase());

        if (nameCategory.equals(context.getPackageName())) {
            try {
                viewHolderBinder.runCategory.setText(context.getString(R.string.index_item));
                viewHolderBinder.categoryName.setText("");
                viewHolderBinder.addApp.setVisibility(View.INVISIBLE);
                viewHolderBinder.selectedApp.removeAllViews();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            File autoFile = context.getFileStreamPath(nameCategory);
            if (autoFile.exists() && autoFile.isFile()) {
                viewHolderBinder.selectedApp.removeAllViews();
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
                    viewHolderBinder.selectedApp.addView(freqLayout);
                    viewHolderBinder.addApp.setVisibility(View.VISIBLE);
                    Drawable addAppsDrawable = context.getDrawable(R.drawable.ic_add_apps);
                    addAppsDrawable.setTint(PublicVariable.primaryColorOpposite);
                    viewHolderBinder.addApp.setImageDrawable(addAppsDrawable);
                }
            }
        }

        viewHolderBinder.categoryName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    PublicVariable.categoryName = textView.getText().toString().replace(" \uD83D\uDD04", "");

                    File file = context.getFileStreamPath(navDrawerItems.get(position).getCategory());
                    if (file.exists() && file.isFile()) {//edit
                        if (navDrawerItems.get(position).getCategory().equals(PublicVariable.categoryName)) {
                            PublicVariable.categoryName = navDrawerItems.get(position).getCategory();
                            context.startActivity(new Intent(context, AppSelectionList.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        } else {//edit name
                            String[] appsContent = functionsClass.readFileLine(navDrawerItems.get(position).getCategory());
                            if (PublicVariable.categoryName.length() == 0) {
                                PublicVariable.categoryName = PublicVariable.categoryName + "_" + System.currentTimeMillis();
                            }
                            for (String appContent : appsContent) {
                                context.deleteFile(appContent + navDrawerItems.get(position).getCategory());
                                functionsClass.saveFileAppendLine(PublicVariable.categoryName, appContent);
                                functionsClass.saveFile(appContent + PublicVariable.categoryName, appContent);
                            }
                            if (functionsClass.loadRecoveryIndicatorCategory(nameCategory)) {
                                functionsClass.removeLine(".uCategory", nameCategory);

                                functionsClass.saveFileAppendLine(".uCategory", PublicVariable.categoryName);
                            }
                            functionsClass.removeLine(".categoryInfo", navDrawerItems.get(position).getCategory());
                            functionsClass.saveFileAppendLine(".categoryInfo", PublicVariable.categoryName);
                            context.deleteFile(navDrawerItems.get(position).getCategory());
                            context.startActivity(new Intent(context, AppSelectionList.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    } else {
                        if (PublicVariable.categoryName.length() == 0) {
                            PublicVariable.categoryName = PublicVariable.categoryName + "_" + System.currentTimeMillis();
                        }

                        functionsClass.saveFileAppendLine(".categoryInfo", PublicVariable.categoryName);
                        functionsClass.saveFileEmpty(PublicVariable.categoryName);
                        context.startActivity(new Intent(context, AppSelectionList.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }
                return true;
            }
        });
        viewHolderBinder.categoryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                endEdited = editable.toString();
                if (viewHolderBinder.addApp.isShown()) {
                    viewHolderBinder.addApp.setVisibility(View.INVISIBLE);
                }
            }
        });
        viewHolderBinder.addApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!navDrawerItems.get(position).getCategory().equals(context.getPackageName())) {
                    PublicVariable.categoryName = navDrawerItems.get(position).getCategory();
                } else {
                    if (endEdited.length() > 0) {
                        PublicVariable.categoryName = endEdited;
                    }
                }

                File file = context.getFileStreamPath(navDrawerItems.get(position).getCategory());
                if (file.exists() && file.isFile()) {
                    if (navDrawerItems.get(position).getCategory().equals(PublicVariable.categoryName)) {
                        PublicVariable.categoryName = navDrawerItems.get(position).getCategory();
                        context.startActivity(new Intent(context, AppSelectionList.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    } else {
                        String[] appsContent = functionsClass.readFileLine(navDrawerItems.get(position).getCategory());
                        if (PublicVariable.categoryName.length() == 0) {
                            PublicVariable.categoryName = PublicVariable.categoryName + "_" + System.currentTimeMillis();
                        }
                        for (String appContent : appsContent) {
                            context.deleteFile(appContent + navDrawerItems.get(position).getCategory());
                            functionsClass.saveFileAppendLine(PublicVariable.categoryName, appContent);
                            functionsClass.saveFile(appContent + PublicVariable.categoryName, appContent);
                        }
                        functionsClass.saveFileAppendLine(".categoryInfo", PublicVariable.categoryName);
                        functionsClass.removeLine(".categoryInfo", navDrawerItems.get(position).getCategory());
                        context.deleteFile(navDrawerItems.get(position).getCategory());
                        context.startActivity(new Intent(context, AppSelectionList.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                } else {
                    if (PublicVariable.categoryName.length() == 0) {
                        PublicVariable.categoryName = PublicVariable.categoryName + "_" + System.currentTimeMillis();
                    }

                    functionsClass.saveFileAppendLine(".categoryInfo", PublicVariable.categoryName);
                    functionsClass.saveFileEmpty(PublicVariable.categoryName);
                    context.startActivity(new Intent(context, AppSelectionList.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        });

        viewHolderBinder.selectedApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!navDrawerItems.get(position).getCategory().equals(context.getPackageName())) {
                    functionsClass
                            .runUnlimitedCategoryService(navDrawerItems.get(position).getCategory());
                }
            }
        });
        viewHolderBinder.selectedApp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!navDrawerItems.get(position).getCategory().equals(context.getPackageName())) {
                    PublicVariable.itemPosition = position;
                    String categoryName = navDrawerItems.get(position).getCategory();
                    functionsClass.popupOptionCategory(context, view, categoryName, position);
                }
                return true;
            }
        });
        viewHolderBinder.runCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!navDrawerItems.get(position).getCategory().equals(context.getPackageName())) {
                    functionsClass
                            .runUnlimitedCategoryService(navDrawerItems.get(position).getCategory());
                }
            }
        });
        viewHolderBinder.runCategory.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!navDrawerItems.get(position).getCategory().equals(context.getPackageName())) {
                    PublicVariable.itemPosition = position;
                    String categoryName = navDrawerItems.get(position).getCategory();
                    functionsClass.popupOptionCategory(context, view, categoryName, position);
                }
                return true;
            }
        });

        RippleDrawable drawItem = (RippleDrawable) context.getDrawable(R.drawable.ripple_effect_category_logo);
        drawItem.setDrawableByLayerId(R.id.category_logo_layer, functionsClass.shapesDrawablesCategory(viewHolderBinder.runCategory));
        drawItem.setDrawableByLayerId(android.R.id.mask, functionsClass.shapesDrawablesCategory(viewHolderBinder.runCategory));
        Drawable categoryLogoLayer = (Drawable) drawItem.findDrawableByLayerId(R.id.category_logo_layer);
        Drawable categoryMask = (Drawable) drawItem.findDrawableByLayerId(android.R.id.mask);
        categoryLogoLayer.setTint(PublicVariable.primaryColorOpposite);
        categoryMask.setTint(PublicVariable.primaryColor);
        viewHolderBinder.runCategory.setBackground(drawItem);
    }

    @Override
    public int getItemCount() {
        return navDrawerItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout categoryItem;
        LinearLayout selectedApp;
        EditText categoryName;
        ImageView addApp;
        TextView runCategory;

        public ViewHolder(View view) {
            super(view);
            categoryItem = (RelativeLayout) view.findViewById(R.id.categoryItem);
            selectedApp = (LinearLayout) view.findViewById(R.id.selectedApps);
            categoryName = (EditText) view.findViewById(R.id.categoryName);
            addApp = (ImageView) view.findViewById(R.id.addApps);
            runCategory = (TextView) view.findViewById(R.id.runCategory);
        }
    }
}
