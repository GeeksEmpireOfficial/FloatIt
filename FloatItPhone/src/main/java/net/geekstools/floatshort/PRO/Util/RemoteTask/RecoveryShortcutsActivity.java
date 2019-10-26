package net.geekstools.floatshort.PRO.Util.RemoteTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Actions;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;

public class RecoveryShortcutsActivity extends Activity {

    FunctionsClass functionsClass;

    Uri BASE_URL =
            Uri.parse("https://www.geeksempire.net/createshortcuts.html/");
    String packageName, setAppIndex, setAppIndexUrl;
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
                LayerDrawable drawCategory = (LayerDrawable) getDrawable(R.drawable.draw_widget_shortcuts);
                drawCategory.setDrawableByLayerId(R.id.backtemp, shapeTempDrawable);
                final Bitmap shortcutApp = Bitmap
                        .createBitmap(drawCategory.getIntrinsicWidth(), drawCategory.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                drawCategory.setBounds(0, 0, drawCategory.getIntrinsicWidth(), drawCategory.getIntrinsicHeight());
                drawCategory.draw(new Canvas(shortcutApp));

                Intent setCategoryRecovery = new Intent(getApplicationContext(), RecoveryShortcutsActivity.class);
                setCategoryRecovery.setAction("Remote_Recover_Shortcuts");
                setCategoryRecovery.addCategory(Intent.CATEGORY_DEFAULT);
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, setCategoryRecovery);
                intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.recoveryShortcuts));
                intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, shortcutApp);
                setResult(RESULT_OK, intent);
            } else if (getIntent().getAction().equals(Intent.ACTION_MAIN) || getIntent().getAction().equals(Intent.ACTION_VIEW)
                    || getIntent().getAction().equals("Remote_Recover_Shortcuts")) {

                Intent intent = new Intent(getApplicationContext(), RecoveryShortcuts.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent);
                } else {
                    startService(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            Intent intent = new Intent(getApplicationContext(), RecoveryShortcuts.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        }
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        endIndexAppInfo();
    }

    public void IndexAppInfo(final String setAppIndex, String setAppIndexUrl) throws Exception {
        Indexable articleToIndex = new Indexable.Builder()
                .setName(setAppIndex)
                .setUrl(setAppIndexUrl)
                .build();

        Task<Void> updateTask = FirebaseAppIndex.getInstance().update(articleToIndex);
        updateTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
        updateTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

        Task<Void> startTask = FirebaseUserActions.getInstance().start(getAction(setAppIndex, setAppIndexUrl));
        startTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
        startTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void endIndexAppInfo() {
        try {
            FirebaseUserActions.getInstance().end(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private com.google.firebase.appindexing.Action getAction(String titleForAction, String urlForAction) {
        return
                Actions.newView(titleForAction, urlForAction);
    }
}
