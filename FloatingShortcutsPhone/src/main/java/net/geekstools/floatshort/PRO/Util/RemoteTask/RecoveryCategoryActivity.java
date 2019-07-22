package net.geekstools.floatshort.PRO.Util.RemoteTask;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.TypedValue;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;

public class RecoveryCategoryActivity extends Activity {

    String categoryName;
    String[] appData;

    boolean runService = true;

    @Override
    protected void onCreate(Bundle Saved) {
        super.onCreate(Saved);
        try {
            if (getIntent().getAction().equals(Intent.ACTION_CREATE_SHORTCUT)) {
                FunctionsClass functionsClass = new FunctionsClass(getApplicationContext(), this);

                Drawable shapeTempDrawable = functionsClass.shapesDrawables();
                if (shapeTempDrawable != null) {
                    shapeTempDrawable.setTint(PublicVariable.primaryColor);
                }
                LayerDrawable drawCategory = (LayerDrawable) getDrawable(R.drawable.draw_widget_categories);
                drawCategory.setDrawableByLayerId(R.id.backtemp, shapeTempDrawable);
                final Bitmap shortcutApp = Bitmap
                        .createBitmap(drawCategory.getIntrinsicWidth(), drawCategory.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                drawCategory.setBounds(0, 0, drawCategory.getIntrinsicWidth(), drawCategory.getIntrinsicHeight());
                drawCategory.draw(new Canvas(shortcutApp));

                Intent setCategoryRecovery = new Intent(getApplicationContext(), RecoveryCategoryActivity.class);
                setCategoryRecovery.setAction("Remote_Recover_Categories");
                setCategoryRecovery.addCategory(Intent.CATEGORY_DEFAULT);
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, setCategoryRecovery);
                intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.recover_category));
                intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, shortcutApp);
                setResult(RESULT_OK, intent);
            } else if (getIntent().getAction().equals(Intent.ACTION_MAIN) || getIntent().getAction().equals(Intent.ACTION_VIEW)
                    || getIntent().getAction().equals("Remote_Recover_Categories")) {
                FunctionsClass functionsClass = new FunctionsClass(getApplicationContext());

                PublicVariable.size = functionsClass.readDefaultPreference("floatingSize", 39);
                PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, this.getResources().getDisplayMetrics());

                try {
                    appData = functionsClass.readFileLine(".uCategory");

                    if (functionsClass.loadCustomIcons()) {
                        LoadCustomIcons loadCustomIcons = new LoadCustomIcons(getApplicationContext(), functionsClass.customIconPackageName());
                        loadCustomIcons.load();
                        FunctionsClass.println("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIcons());
                    }

                    for (String anAppData : appData) {
                        runService = true;
                        if (PublicVariable.FloatingCategories != null) {
                            for (int check = 0; check < PublicVariable.FloatingCategories.size(); check++) {
                                if (anAppData.equals(PublicVariable.FloatingCategories.get(check))) {
                                    runService = false;
                                }
                            }
                        }

                        if (runService == true) {
                            try {
                                categoryName = anAppData;
                                functionsClass.runUnlimitedCategoryService(categoryName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            FunctionsClass functionsClass = new FunctionsClass(getApplicationContext());

            PublicVariable.size = functionsClass.readDefaultPreference("floatingSize", 39);
            PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, this.getResources().getDisplayMetrics());

            try {
                appData = functionsClass.readFileLine(".uCategory");

                if (functionsClass.loadCustomIcons()) {
                    LoadCustomIcons loadCustomIcons = new LoadCustomIcons(getApplicationContext(), functionsClass.customIconPackageName());
                    loadCustomIcons.load();
                    FunctionsClass.println("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIcons());
                }

                for (String anAppData : appData) {
                    runService = true;
                    if (PublicVariable.FloatingCategories != null) {
                        for (int check = 0; check < PublicVariable.FloatingCategories.size(); check++) {
                            if (anAppData.equals(PublicVariable.FloatingCategories.get(check))) {
                                runService = false;
                            }
                        }
                    }

                    if (runService == true) {
                        try {
                            categoryName = anAppData;
                            functionsClass.runUnlimitedCategoryService(categoryName);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        finish();
    }
}
