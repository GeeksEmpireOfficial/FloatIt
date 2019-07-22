package net.geekstools.floatshort.PRO.Util.RemoteTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import net.geekstools.floatshort.PRO.Category_Unlimited_Floating;
import net.geekstools.floatshort.PRO.R;

public class RemoveAllActivity extends Activity {

    @Override
    protected void onCreate(Bundle Saved) {
        super.onCreate(Saved);

        Intent App_Unlimited_Shortcuts = new Intent(getApplicationContext(), net.geekstools.floatshort.PRO.App_Unlimited_Shortcuts.class);
        App_Unlimited_Shortcuts.putExtra("pack", getString(R.string.remove_all_floatings));
        startService(App_Unlimited_Shortcuts);

        Intent App_Unlimited_Wifi = new Intent(getApplicationContext(), net.geekstools.floatshort.PRO.App_Unlimited_Wifi.class);
        App_Unlimited_Wifi.putExtra("pack", getString(R.string.remove_all_floatings));
        startService(App_Unlimited_Wifi);

        Intent App_Unlimited_Bluetooth = new Intent(getApplicationContext(), net.geekstools.floatshort.PRO.App_Unlimited_Bluetooth.class);
        App_Unlimited_Bluetooth.putExtra("pack", getString(R.string.remove_all_floatings));
        startService(App_Unlimited_Bluetooth);

        Intent App_Unlimited_Gps = new Intent(getApplicationContext(), net.geekstools.floatshort.PRO.App_Unlimited_Gps.class);
        App_Unlimited_Gps.putExtra("pack", getString(R.string.remove_all_floatings));
        startService(App_Unlimited_Gps);

        Intent App_Unlimited_Nfc = new Intent(getApplicationContext(), net.geekstools.floatshort.PRO.App_Unlimited_Nfc.class);
        App_Unlimited_Nfc.putExtra("pack", getString(R.string.remove_all_floatings));
        startService(App_Unlimited_Nfc);

        Intent App_Unlimited_Time = new Intent(getApplicationContext(), net.geekstools.floatshort.PRO.App_Unlimited_Time.class);
        App_Unlimited_Time.putExtra("pack", getString(R.string.remove_all_floatings));
        startService(App_Unlimited_Time);
        /**/
        Intent Category_Unlimited_Category = new Intent(getApplicationContext(), Category_Unlimited_Floating.class);
        Category_Unlimited_Category.putExtra("categoryName", getString(R.string.remove_all_floatings));
        startService(Category_Unlimited_Category);

        Intent Category_Unlimited_Wifi = new Intent(getApplicationContext(), net.geekstools.floatshort.PRO.Category_Unlimited_Wifi.class);
        Category_Unlimited_Wifi.putExtra("categoryName", getString(R.string.remove_all_floatings));
        startService(Category_Unlimited_Wifi);

        Intent Category_Unlimited_Bluetooth = new Intent(getApplicationContext(), net.geekstools.floatshort.PRO.Category_Unlimited_Bluetooth.class);
        Category_Unlimited_Bluetooth.putExtra("categoryName", getString(R.string.remove_all_floatings));
        startService(Category_Unlimited_Bluetooth);

        Intent Category_Unlimited_Gps = new Intent(getApplicationContext(), net.geekstools.floatshort.PRO.Category_Unlimited_Gps.class);
        Category_Unlimited_Gps.putExtra("categoryName", getString(R.string.remove_all_floatings));
        startService(Category_Unlimited_Gps);

        Intent Category_Unlimited_Nfc = new Intent(getApplicationContext(), net.geekstools.floatshort.PRO.Category_Unlimited_Nfc.class);
        Category_Unlimited_Nfc.putExtra("categoryName", getString(R.string.remove_all_floatings));
        startService(Category_Unlimited_Nfc);

        Intent Category_Unlimited_Time = new Intent(getApplicationContext(), net.geekstools.floatshort.PRO.Category_Unlimited_Time.class);
        Category_Unlimited_Time.putExtra("categoryName", getString(R.string.remove_all_floatings));
        startService(Category_Unlimited_Time);

        finish();
    }
}
