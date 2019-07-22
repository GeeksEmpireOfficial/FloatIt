package net.geekstools.floatshort.PRO.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import net.geekstools.floatshort.PRO.App_Unlimited_Gps;
import net.geekstools.floatshort.PRO.Automation.RecoveryGps;
import net.geekstools.floatshort.PRO.Category_Unlimited_Gps;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;

public class ReceiverGPS extends BroadcastReceiver {

    FunctionsClass functionsClass;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            functionsClass = new FunctionsClass(context);

            if (functionsClass.loadCustomIcons()) {
                LoadCustomIcons loadCustomIcons = new LoadCustomIcons(context, functionsClass.customIconPackageName());
                loadCustomIcons.load();
                FunctionsClass.println("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIcons());
            }

            final LocationManager locManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true && PublicVariable.receiverGPS == false) {
                Intent gps = new Intent(context, RecoveryGps.class);
                gps.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(gps);
                PublicVariable.receiverGPS = true;
            } else if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
                Intent w = new Intent(context, App_Unlimited_Gps.class);
                w.putExtra("pack", context.getString(R.string.remove_all_floatings));
                w.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(w);

                Intent c = new Intent(context, Category_Unlimited_Gps.class);
                c.putExtra("categoryName", context.getString(R.string.remove_all_floatings));
                c.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(c);

                PublicVariable.receiverGPS = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
