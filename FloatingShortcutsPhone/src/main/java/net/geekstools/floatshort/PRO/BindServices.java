package net.geekstools.floatshort.PRO;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.TypedValue;

import net.geekstools.floatshort.PRO.Automation.RecoveryGps;
import net.geekstools.floatshort.PRO.Automation.RecoveryNfc;
import net.geekstools.floatshort.PRO.Automation.RecoveryWifi;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;

public class BindServices extends Service {

    FunctionsClass functionsClass;

    BroadcastReceiver broadcastReceiverONOFF, broadcastReceiverAction;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (startId > 1) {

            return START_NOT_STICKY;
        }
        startForeground(333, functionsClass.bindServiceNotification());

        PublicVariable.contextStatic = getApplicationContext();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PublicVariable.size = functionsClass.readDefaultPreference("floatingSize", 39);
                PublicVariable.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.size, getApplicationContext().getResources().getDisplayMetrics());
            }
        }, 333);
        if (functionsClass.SystemCache()) {
            if (startId == 1) {
                IntentFilter intentFilterNewDataSet = new IntentFilter();
                intentFilterNewDataSet.addAction(Intent.ACTION_SCREEN_ON);
                intentFilterNewDataSet.addAction(Intent.ACTION_SCREEN_OFF);
                broadcastReceiverONOFF = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                            if (!PublicVariable.inMemory) {
                                Intent openBackground = new Intent(getApplicationContext(), Configurations.class);
                                openBackground.putExtra("goHome", true);
                                openBackground.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(openBackground);
                            }
                        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                            startService(new Intent(getApplicationContext(), BindServices.class));
                        }
                    }
                };
                try {
                    unregisterReceiver(broadcastReceiverONOFF);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                registerReceiver(broadcastReceiverONOFF, intentFilterNewDataSet);
            }
        }

        if (functionsClass.returnAPI() >= 26) {
            if (startId == 1) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
                intentFilter.addAction("android.location.PROVIDERS_CHANGED");
                intentFilter.addAction("android.nfc.action.ADAPTER_STATE_CHANGED");
                broadcastReceiverAction = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED") && PublicVariable.triggerWifiBroadcast) {
                            try {
                                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                if (wifiManager.isWifiEnabled() == true && PublicVariable.receiveWiFi == false) {
                                    if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                                        Intent wifiRecovery = new Intent(context, RecoveryWifi.class);
                                        wifiRecovery.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startService(wifiRecovery);

                                        PublicVariable.receiveWiFi = true;
                                    }
                                } else if (wifiManager.isWifiEnabled() == false) {
                                    if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
                                        Intent wifiShortcutsRemove = new Intent(context, App_Unlimited_Wifi.class);
                                        wifiShortcutsRemove.putExtra("pack", context.getString(R.string.remove_all_floatings));
                                        wifiShortcutsRemove.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startService(wifiShortcutsRemove);

                                        Intent wifiCategoryRemove = new Intent(context, Category_Unlimited_Wifi.class);
                                        wifiCategoryRemove.putExtra("categoryName", context.getString(R.string.remove_all_floatings));
                                        wifiCategoryRemove.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startService(wifiCategoryRemove);

                                        PublicVariable.receiveWiFi = false;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (intent.getAction().equals("android.location.PROVIDERS_CHANGED")) {
                            try {
                                final LocationManager locManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                                if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true && PublicVariable.receiverGPS == false) {
                                    Intent gpsRecovery = new Intent(context, RecoveryGps.class);
                                    gpsRecovery.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startService(gpsRecovery);

                                    PublicVariable.receiverGPS = true;
                                } else if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
                                    Intent gpsShortcutsRemove = new Intent(context, App_Unlimited_Gps.class);
                                    gpsShortcutsRemove.putExtra("pack", context.getString(R.string.remove_all_floatings));
                                    gpsShortcutsRemove.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startService(gpsShortcutsRemove);

                                    Intent gpsCategoryRemove = new Intent(context, Category_Unlimited_Gps.class);
                                    gpsCategoryRemove.putExtra("categoryName", context.getString(R.string.remove_all_floatings));
                                    gpsCategoryRemove.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startService(gpsCategoryRemove);

                                    PublicVariable.receiverGPS = false;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (intent.getAction().equals("android.nfc.action.ADAPTER_STATE_CHANGED")) {
                            try {
                                NfcManager nfcManager = (NfcManager) context.getApplicationContext().getSystemService(Context.NFC_SERVICE);
                                if (nfcManager.getDefaultAdapter().isEnabled() == true && PublicVariable.receiverNFC == false) {
                                    Intent nfcRecovery = new Intent(context, RecoveryNfc.class);
                                    nfcRecovery.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startService(nfcRecovery);

                                    PublicVariable.receiverNFC = true;
                                } else if (nfcManager.getDefaultAdapter().isEnabled() == false) {
                                    Intent nfcShortcutsRemove = new Intent(context, App_Unlimited_Nfc.class);
                                    nfcShortcutsRemove.putExtra("pack", context.getString(R.string.remove_all_floatings));
                                    nfcShortcutsRemove.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startService(nfcShortcutsRemove);

                                    Intent nfcCategoryRemove = new Intent(context, Category_Unlimited_Nfc.class);
                                    nfcCategoryRemove.putExtra("categoryName", context.getString(R.string.remove_all_floatings));
                                    nfcCategoryRemove.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startService(nfcCategoryRemove);

                                    PublicVariable.receiverNFC = false;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                try {
                    unregisterReceiver(broadcastReceiverAction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                registerReceiver(broadcastReceiverAction, intentFilter);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PublicVariable.triggerWifiBroadcast = true;
                    }
                }, 3000);
            }
        }

        return functionsClass.serviceMode();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        functionsClass = new FunctionsClass(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiverAction);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (functionsClass.SystemCache() || functionsClass.automationFeatureEnable()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(getApplicationContext(), BindServices.class));
                } else {
                    startService(new Intent(getApplicationContext(), BindServices.class));
                }
            } else {
                unregisterReceiver(broadcastReceiverONOFF);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
