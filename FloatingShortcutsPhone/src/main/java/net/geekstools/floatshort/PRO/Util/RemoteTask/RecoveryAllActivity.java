package net.geekstools.floatshort.PRO.Util.RemoteTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;

public class RecoveryAllActivity extends Activity {

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
                LayerDrawable drawCategory = (LayerDrawable) getDrawable(R.drawable.draw_recovery);
                drawCategory.setDrawableByLayerId(R.id.backtemp, shapeTempDrawable);
                final Bitmap shortcutApp = Bitmap
                        .createBitmap(drawCategory.getIntrinsicWidth(), drawCategory.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                drawCategory.setBounds(0, 0, drawCategory.getIntrinsicWidth(), drawCategory.getIntrinsicHeight());
                drawCategory.draw(new Canvas(shortcutApp));

                Intent setRecoveryAll = new Intent(getApplicationContext(), RecoveryAllActivity.class);
                setRecoveryAll.setAction("Remote_Recover_All");
                setRecoveryAll.addCategory(Intent.CATEGORY_DEFAULT);
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, setRecoveryAll);
                intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.recover_all));
                intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, shortcutApp);
                setResult(RESULT_OK, intent);
            } else if (getIntent().getAction().equals(Intent.ACTION_MAIN) || getIntent().getAction().equals(Intent.ACTION_VIEW)
                    || getIntent().getAction().equals("Remote_Recover_All")) {
                startActivity(new Intent(getApplicationContext(), RecoveryShortcutsActivity.class));
                startActivity(new Intent(getApplicationContext(), RecoveryCategoryActivity.class));
                startService(new Intent(getApplicationContext(), RecoveryWidgets.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
            startActivity(new Intent(getApplicationContext(), RecoveryShortcutsActivity.class));
            startActivity(new Intent(getApplicationContext(), RecoveryCategoryActivity.class));
            startService(new Intent(getApplicationContext(), RecoveryWidgets.class));
        }

        finish();
    }
}
