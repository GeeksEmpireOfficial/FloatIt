package net.geekstools.floatshort.PRO.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcManager;

import net.geekstools.floatshort.PRO.App_Unlimited_Nfc;
import net.geekstools.floatshort.PRO.Automation.RecoveryNfc;
import net.geekstools.floatshort.PRO.Category_Unlimited_Nfc;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;

public class ReceiverNFC extends BroadcastReceiver {

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

            NfcManager nfcManager = (NfcManager) context.getApplicationContext().getSystemService(Context.NFC_SERVICE);
            if (nfcManager.getDefaultAdapter().isEnabled() == true && PublicVariable.receiverNFC == false) {
                Intent nfc = new Intent(context, RecoveryNfc.class);
                nfc.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(nfc);
                PublicVariable.receiverNFC = true;
            } else if (nfcManager.getDefaultAdapter().isEnabled() == false) {
                Intent w = new Intent(context, App_Unlimited_Nfc.class);
                w.putExtra("pack", context.getString(R.string.remove_all_floatings));
                w.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(w);

                Intent c = new Intent(context, Category_Unlimited_Nfc.class);
                c.putExtra("categoryName", context.getString(R.string.remove_all_floatings));
                c.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(c);

                PublicVariable.receiverNFC = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
