package net.geekstools.floatshort.PRO.Receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.geekstools.floatshort.PRO.App_Unlimited_Bluetooth;
import net.geekstools.floatshort.PRO.Automation.RecoveryBluetooth;
import net.geekstools.floatshort.PRO.Category_Unlimited_Bluetooth;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;

public class ReceiverBluetooth extends BroadcastReceiver {

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

            final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled() == true && PublicVariable.receiveBluetooth == false) {
                Intent bluetooth = new Intent(context, RecoveryBluetooth.class);
                bluetooth.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(bluetooth);
                PublicVariable.receiveBluetooth = true;
            } else if (bluetoothAdapter.isEnabled() == false) {
                Intent w = new Intent(context, App_Unlimited_Bluetooth.class);
                w.putExtra("pack", context.getString(R.string.remove_all_floatings));
                w.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(w);

                Intent c = new Intent(context, Category_Unlimited_Bluetooth.class);
                c.putExtra("categoryName", context.getString(R.string.remove_all_floatings));
                c.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(c);

                PublicVariable.receiveBluetooth = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
