package net.geekstools.floatshort.PRO.Category.NavAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.NavAdapter.NavDrawerItem;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;
import net.geekstools.imageview.customshapes.ShapesImage;

import java.io.File;
import java.util.ArrayList;

public class AppSelectionListAdapter extends RecyclerView.Adapter<AppSelectionListAdapter.ViewHolder> {

    FunctionsClass functionsClass;
    ImageView tempIcon;
    float fromX, fromY, toX, toY, dpHeight, dpWidth, systemUiHeight;
    int animationType, layoutInflater;
    CheckBox[] autoChoice;
    View view;
    ViewHolder viewHolder;
    LoadCustomIcons loadCustomIcons;
    private Context context;
    private Activity activity;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public AppSelectionListAdapter(Activity activity, Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.activity = activity;
        this.context = context;
        this.navDrawerItems = navDrawerItems;

        autoChoice = new CheckBox[navDrawerItems.size()];
        functionsClass = new FunctionsClass(context, activity);
        tempIcon = functionsClass.initShapesImage(activity, R.id.tempIcon);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels;
        dpWidth = displayMetrics.widthPixels;
        systemUiHeight = PublicVariable.actionBarHeight;
        fromX = toX = PublicVariable.confirmButtonX;
        toY = PublicVariable.confirmButtonY;
        animationType = Animation.ABSOLUTE;

        switch (functionsClass.shapesImageId()) {
            case 1:
                layoutInflater = R.layout.selection_item_card_list_droplet;
                break;
            case 2:
                layoutInflater = R.layout.selection_item_card_list_circle;
                break;
            case 3:
                layoutInflater = R.layout.selection_item_card_list_square;
                break;
            case 4:
                layoutInflater = R.layout.selection_item_card_list_squircle;
                break;
            case 0:
                layoutInflater = R.layout.selection_item_card_list_noshape;
                break;
        }

        if (functionsClass.loadCustomIcons()) {
            loadCustomIcons = new LoadCustomIcons(context, functionsClass.customIconPackageName());
        }
    }

    @Override
    public AppSelectionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(layoutInflater, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolderBinder, final int position) {
        try {
            autoChoice[position] = viewHolderBinder.autoChoice;
            final String packageName = navDrawerItems.get(position).getPackageName();
            File autoFile = context.getFileStreamPath(packageName + PublicVariable.categoryName);
            autoChoice[position].setChecked(false);
            if (autoFile.exists()) {
                autoChoice[position].setChecked(true);
            } else {
                autoChoice[position].setChecked(false);
            }

            if (PublicVariable.themeLightDark) {
                autoChoice[position].setButtonTintList(ColorStateList.valueOf(context.getColor(R.color.dark)));
            } else if (!PublicVariable.themeLightDark) {
                viewHolder.appName.setTextColor(context.getColor(R.color.light));
                autoChoice[position].setButtonTintList(ColorStateList.valueOf(context.getColor(R.color.light)));
            }

            viewHolder.appIcon.setImageDrawable(navDrawerItems.get(position).getAppIcon());
            viewHolder.appName.setText(navDrawerItems.get(position).getAppName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolderBinder.item.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        fromY = -((dpHeight - motionEvent.getRawY()) - (systemUiHeight));
                        break;
                    case MotionEvent.ACTION_UP:
                        final String pack = navDrawerItems.get(position).getPackageName();
                        File autoFile = context.getFileStreamPath(pack + PublicVariable.categoryName);
                        if (autoFile.exists()) {
                            context.deleteFile(pack + PublicVariable.categoryName);
                            functionsClass.removeLine(PublicVariable.categoryName, navDrawerItems.get(position).getPackageName());
                            autoChoice[position].setChecked(false);
                            context.sendBroadcast(new Intent(context.getString(R.string.counterActionAdvance)));

                            context.sendBroadcast(new Intent(context.getString(R.string.savedActionHideAdvance)));
                            context.sendBroadcast(new Intent(context.getString(R.string.visibilityActionAdvance)));
                        } else {
                            functionsClass.saveFile(
                                    pack + PublicVariable.categoryName, pack);
                            functionsClass.saveFileAppendLine(
                                    PublicVariable.categoryName, pack);
                            autoChoice[position].setChecked(true);

                            TranslateAnimation translateAnimation =
                                    new TranslateAnimation(animationType, fromX,
                                            animationType, toX,
                                            animationType, fromY,
                                            animationType, toY);
                            translateAnimation.setDuration((long) Math.abs(fromY));


                            tempIcon.setImageDrawable(functionsClass.loadCustomIcons() ?
                                    loadCustomIcons.getDrawableIconForPackage(navDrawerItems.get(position).getPackageName(), functionsClass.shapedAppIcon(navDrawerItems.get(position).getPackageName()))
                                    :
                                    functionsClass.shapedAppIcon(navDrawerItems.get(position).getPackageName()));
                            tempIcon.startAnimation(translateAnimation);
                            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    context.sendBroadcast(new Intent(context.getString(R.string.savedActionHideAdvance)));
                                    context.sendBroadcast(new Intent(context.getString(R.string.visibilityActionAdvance)));

                                    tempIcon.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    tempIcon.setVisibility(View.INVISIBLE);
                                    context.sendBroadcast(new Intent(context.getString(R.string.animtaionActionAdvance)));
                                    context.sendBroadcast(new Intent(context.getString(R.string.counterActionAdvance)));
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }
                            });
                        }
                        break;
                }
                return true;
            }
        });

        autoChoice[position].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

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
        CheckBox autoChoice;

        public ViewHolder(View view) {
            super(view);
            item = (RelativeLayout) view.findViewById(R.id.item);
            appIcon = (ShapesImage) view.findViewById(R.id.icon);
            appName = (TextView) view.findViewById(R.id.desc);
            autoChoice = (CheckBox) view.findViewById(R.id.autoChoice);
        }
    }
}
