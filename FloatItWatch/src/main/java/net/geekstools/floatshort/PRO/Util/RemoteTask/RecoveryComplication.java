package net.geekstools.floatshort.PRO.Util.RemoteTask;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.support.wearable.complications.ComplicationData;
import android.support.wearable.complications.ComplicationManager;
import android.support.wearable.complications.ComplicationProviderService;
import android.support.wearable.complications.ComplicationText;

import net.geekstools.floatshort.PRO.Automation.RecoveryShortcuts;
import net.geekstools.floatshort.PRO.BuildConfig;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;

public class RecoveryComplication extends ComplicationProviderService {

    FunctionsClass functionsClass;

    @Override
    public void onComplicationActivated(int complicationId, int dataType, ComplicationManager complicationManager) {
        if (BuildConfig.DEBUG) {
            System.out.println("onComplicationActivated " + dataType);
        }
        functionsClass = new FunctionsClass(getApplicationContext());
        functionsClass.savePreference("ComplicationProviderService", "ComplicationedId", complicationId);
    }

    @Override
    public void onComplicationUpdate(int complicationId, int type, ComplicationManager manager) {
        if (type == ComplicationData.TYPE_LARGE_IMAGE || type == ComplicationData.TYPE_ICON || type == ComplicationData.TYPE_RANGED_VALUE) {
            functionsClass = new FunctionsClass(getApplicationContext());
            functionsClass.savePreference("ComplicationProviderService", "ComplicationedId", complicationId);

            Intent recoveryIntent = new Intent(getApplicationContext(), RecoveryShortcuts.class);
            recoveryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent complicationTogglePendingIntent = PendingIntent
                    .getService(getApplicationContext(), 666, recoveryIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            ComplicationData complicationData;
            switch (type) {
                case ComplicationData.TYPE_LARGE_IMAGE://8
                    complicationData = new ComplicationData.Builder(ComplicationData.TYPE_LARGE_IMAGE)
                            .setLargeImage(Icon.createWithResource(this, R.drawable.draw_recovery))
                            .setTapAction(complicationTogglePendingIntent)
                            .build();
                    break;
                case ComplicationData.TYPE_ICON://6
                    complicationData = new ComplicationData.Builder(ComplicationData.TYPE_ICON)
                            .setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.w_recovery_indicator))
                            .setTapAction(complicationTogglePendingIntent)
                            .build();
                    break;
                case ComplicationData.TYPE_RANGED_VALUE://5
                    complicationData = new ComplicationData.Builder(ComplicationData.TYPE_RANGED_VALUE)
                            .setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_notification))
                            .setShortText(ComplicationText.plainText(getString(R.string.recoveryEmoji)))
                            .setMinValue(0)
                            .setMaxValue(functionsClass.readPreference("InstalledApps", "countApps", getPackageManager().getInstalledApplications(0).size()))
                            .setValue(functionsClass.countLine(".uFile"))
                            .setTapAction(complicationTogglePendingIntent)
                            .build();
                    break;
                default://6
                    complicationData = new ComplicationData.Builder(ComplicationData.TYPE_ICON)
                            .setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.w_recovery_indicator))
                            .setTapAction(complicationTogglePendingIntent)
                            .build();
                    break;
            }
            manager.updateComplicationData(complicationId, complicationData);
        }
    }

    @Override
    public void onComplicationDeactivated(int complicationId) {
        System.out.println("onComplicationDeactivated");
    }
}