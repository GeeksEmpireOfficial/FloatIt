package net.geekstools.floatshort.PRO;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.text.Html;
import android.widget.RemoteViews;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;

public class BindServices extends Service {

    RemoteViews remoteNotfication;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PublicVariable.contextStatic = getApplicationContext();
        FunctionsClass functionsClass = new FunctionsClass(getApplicationContext());

        startForeground(333, (functionsClass.returnAPI() <= 25) ? bindServiceLow() : bindServiceHigh());

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected Notification bindServiceLow() {
        Notification.Builder mBuilder = new Notification.Builder(this);

        Bitmap bM = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

        mBuilder.setContent(remoteNotfication);
        mBuilder.setContentTitle(Html.fromHtml("<b><font color='" + getColor(R.color.light) + "'>" + getResources().getString(R.string.bindTitle) + "</font></b>"));
        mBuilder.setContentText(Html.fromHtml("<font color='" + getColor(R.color.light) + "'>" + getResources().getString(R.string.bindDesc) + "</font>"));
        mBuilder.setTicker(getResources().getString(R.string.app_name));
        mBuilder.setSmallIcon(R.drawable.ic_notification);
        mBuilder.setLargeIcon(bM);
        mBuilder.setAutoCancel(false);
        mBuilder.setColor(getColor(R.color.default_color));
        mBuilder.setPriority(Notification.PRIORITY_MAX);

        Intent Main = new Intent(this, Configurations.class);
        PendingIntent mainPV = PendingIntent.getActivity(this, 0, Main, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(mainPV);

        return mBuilder.build();
    }

    protected Notification bindServiceHigh() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = new NotificationChannel(getPackageName(), getString(R.string.app_name), NotificationManager.IMPORTANCE_MAX);
        notificationManager.createNotificationChannel(notificationChannel);

        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setContent(remoteNotfication);
        mBuilder.setContentTitle(Html.fromHtml("<b><font color='" + getColor(R.color.light) + "'>" + getResources().getString(R.string.bindTitle) + "</font></b>"));
        mBuilder.setContentText(Html.fromHtml("<font color='" + getColor(R.color.light) + "'>" + getResources().getString(R.string.bindDesc) + "</font>"));
        mBuilder.setTicker(getResources().getString(R.string.app_name));
        mBuilder.setSmallIcon(R.drawable.ic_notification);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        mBuilder.setAutoCancel(false);
        mBuilder.setColor(getColor(R.color.default_color));
        mBuilder.setChannelId(getPackageName());
        mBuilder.setContentIntent(PendingIntent.getActivity(this, 0,
                new Intent(this, Configurations.class),
                PendingIntent.FLAG_UPDATE_CURRENT));

        //notificationManager.notify(666, mBuilder.build());
        return mBuilder.build();
    }
}
