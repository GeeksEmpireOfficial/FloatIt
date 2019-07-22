package net.geekstools.floatshort.PRO;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.text.Html;

import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.ServerManagedPolicy;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;


public class LicenseValidator extends Service {

    private static final String BASE64_PUBLIC_KEY =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtsFX5NnAyAamT5UTKM4j5N0XlhSMtTSx/CQaHjqJWoNhvglCBxrs/qj7fuec9" +
                    "TeDCKDaR5t/tYQpI1bxTDdQSXPEW1C5+8YpY6+Uw4Lx3wlb8F+CJmx/SP7oBD0OHkAacFb9pAkKwk+zcxJ8VJOjJIqurFl0jZ" +
                    "wIAJc0xBjIQUNyoh6VY7t0EnQVlt3lYNWDd49xye/6ws015rWWJJjKTYfu6v9my6M+7TBHORFiguEEy9eGFwxDdpPfRS635cj" +
                    "FXTpw44TNdJAXTRQ+NYdhcg23zIhWK7nQI7Oag5Zei9kOOdL6kDuECnuKTWPM3dHbxWxeTh3aq/yEJZ//tgYiWwIDAQAB";

    private static final byte[] SALT = new byte[]{
            -16, -13, 30, -128, -103, -57, 74, -64, 53, 88, -97, -45, 77, -113, -36, -113, -11, 32, -64, 89
    };
    FunctionsClass functionsClass;
    LicenseChecker licenseChecker;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        if (functionsClass.returnAPI() < 26) {
            startForeground(111, bindServiceLOW());
        } else {
            startForeground(111, bindServiceHIGH());
        }

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        licenseChecker = new LicenseChecker(
                getApplicationContext(),
                new ServerManagedPolicy(getApplicationContext(), new AESObfuscator(SALT, getPackageName(), deviceId)),
                BASE64_PUBLIC_KEY
        );
        final LicenseCheckerCallback licenseCheckerCallback = new LicenseCheckerCallback() {
            @Override
            public void allow(int reason) {
                functionsClass.saveFileAppendLine(".License", String.valueOf(reason));
                stopSelf();
            }

            @Override
            public void dontAllow(int reason) {
                sendBroadcast(new Intent(getString(R.string.license)));
            }

            @Override
            public void applicationError(int errorCode) {
            }
        };
        licenseChecker.checkAccess(licenseCheckerCallback);

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        functionsClass = new FunctionsClass(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        licenseChecker.onDestroy();
    }

    protected Notification bindServiceHIGH() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(getPackageName(), getString(R.string.app_name), NotificationManager.IMPORTANCE_MAX);
        notificationManager.createNotificationChannel(notificationChannel);

        Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());
        mBuilder.setColor(getResources().getColor(R.color.default_color));
        mBuilder.setContentTitle(getString(R.string.license_info));
        mBuilder.setContentText(getString(R.string.license_info_desc));
        mBuilder.setContentTitle(Html.fromHtml("<b><font color='" + getResources().getColor(R.color.default_color_darker) + "'>" + getString(R.string.license_info) + "</font></b>"));
        mBuilder.setContentText(Html.fromHtml("<font color='" + getResources().getColor(R.color.default_color_darker) + "'>" + getString(R.string.license_info_desc) + "</font>"));
        mBuilder.setTicker(getResources().getString(R.string.updating_info));
        mBuilder.setSmallIcon(R.drawable.ic_notification);
        mBuilder.setAutoCancel(false);
        mBuilder.setProgress(0, 0, true);
        mBuilder.setChannelId(getPackageName());

        return mBuilder.build();
    }

    protected Notification bindServiceLOW() {
        Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());
        mBuilder.setColor(getResources().getColor(R.color.default_color));
        mBuilder.setContentTitle(getString(R.string.license_info));
        mBuilder.setContentText(getString(R.string.license_info_desc));
        mBuilder.setContentTitle(Html.fromHtml("<b><font color='" + getResources().getColor(R.color.default_color_darker) + "'>" + getString(R.string.license_info) + "</font></b>"));
        mBuilder.setContentText(Html.fromHtml("<font color='" + getResources().getColor(R.color.default_color_darker) + "'>" + getString(R.string.license_info_desc) + "</font>"));
        mBuilder.setTicker(getResources().getString(R.string.updating_info));
        mBuilder.setSmallIcon(R.drawable.ic_notification);
        mBuilder.setAutoCancel(false);
        mBuilder.setProgress(0, 0, true);

        return mBuilder.build();
    }
}
