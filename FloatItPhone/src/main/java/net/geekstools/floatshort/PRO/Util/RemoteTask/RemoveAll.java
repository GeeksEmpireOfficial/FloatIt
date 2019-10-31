package net.geekstools.floatshort.PRO.Util.RemoteTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import net.geekstools.floatshort.PRO.Folders_Unlimited_Bluetooth;
import net.geekstools.floatshort.PRO.Folders_Unlimited_Floating;
import net.geekstools.floatshort.PRO.Folders_Unlimited_Gps;
import net.geekstools.floatshort.PRO.Folders_Unlimited_Nfc;
import net.geekstools.floatshort.PRO.Folders_Unlimited_Time;
import net.geekstools.floatshort.PRO.Folders_Unlimited_Wifi;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;

public class RemoveAll extends Service {

    FunctionsClass functionsClass;

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        /*Apps*/
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
        /*Categories*/
        Intent Category_Unlimited_Category = new Intent(getApplicationContext(), Folders_Unlimited_Floating.class);
        Category_Unlimited_Category.putExtra("categoryName", getString(R.string.remove_all_floatings));
        startService(Category_Unlimited_Category);

        Intent Category_Unlimited_Wifi = new Intent(getApplicationContext(), Folders_Unlimited_Wifi.class);
        Category_Unlimited_Wifi.putExtra("categoryName", getString(R.string.remove_all_floatings));
        startService(Category_Unlimited_Wifi);

        Intent Category_Unlimited_Bluetooth = new Intent(getApplicationContext(), Folders_Unlimited_Bluetooth.class);
        Category_Unlimited_Bluetooth.putExtra("categoryName", getString(R.string.remove_all_floatings));
        startService(Category_Unlimited_Bluetooth);

        Intent Category_Unlimited_Gps = new Intent(getApplicationContext(), Folders_Unlimited_Gps.class);
        Category_Unlimited_Gps.putExtra("categoryName", getString(R.string.remove_all_floatings));
        startService(Category_Unlimited_Gps);

        Intent Category_Unlimited_Nfc = new Intent(getApplicationContext(), Folders_Unlimited_Nfc.class);
        Category_Unlimited_Nfc.putExtra("categoryName", getString(R.string.remove_all_floatings));
        startService(Category_Unlimited_Nfc);

        Intent Category_Unlimited_Time = new Intent(getApplicationContext(), Folders_Unlimited_Time.class);
        Category_Unlimited_Time.putExtra("categoryName", getString(R.string.remove_all_floatings));
        startService(Category_Unlimited_Time);
        /*HIS*/
        Intent App_Unlimited_HIS = new Intent(getApplicationContext(), net.geekstools.floatshort.PRO.App_Unlimited_HIS.class);
        App_Unlimited_HIS.putExtra("packageName", getString(R.string.remove_all_floatings));
        startService(App_Unlimited_HIS);
        /*Widgets*/
        Intent Widget_Unlimited_Floating = new Intent(getApplicationContext(), net.geekstools.floatshort.PRO.Widget_Unlimited_Floating.class);
        Widget_Unlimited_Floating.putExtra(getString(R.string.remove_all_floatings), getString(R.string.remove_all_floatings));
        startService(Widget_Unlimited_Floating);

        stopSelf();
        return functionsClass.serviceMode();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        functionsClass = new FunctionsClass(getApplicationContext());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
