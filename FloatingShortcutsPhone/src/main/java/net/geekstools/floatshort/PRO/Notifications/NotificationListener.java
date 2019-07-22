package net.geekstools.floatshort.PRO.Notifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;

public class NotificationListener extends NotificationListenerService {

    FunctionsClass functionsClass;

    String notificationTitle, notificationText, notificationPackage, notificationTime, notificationId;
    Drawable notificationIcon;

    @Override
    public void onCreate() {
        super.onCreate();
        functionsClass = new FunctionsClass(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        return START_STICKY;
    }

    @Override
    public void onNotificationPosted(final StatusBarNotification statusBarNotification) {
        if (statusBarNotification.getPackageName() != null && statusBarNotification.isClearable()) {
            if (PublicVariable.previousDuplicated == null) {
                PublicVariable.previousDuplicated = statusBarNotification.getPackageName();
            } else {
                if (PublicVariable.previousDuplicated.equals(notificationPackage)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            PublicVariable.previousDuplicated = null;
                        }
                    }, 500);
                    return;
                }
            }
            try {
                notificationPackage = statusBarNotification.getPackageName();
                notificationId = statusBarNotification.getKey();
                notificationTime = String.valueOf(statusBarNotification.getPostTime());
                Bundle extras = statusBarNotification.getNotification().extras;
                if (!extras.isEmpty()) {
                    try {
                        notificationTitle = extras.getString(Notification.EXTRA_TITLE);
                    } catch (Exception e) {
                        notificationTitle = functionsClass.appName(notificationPackage);
                    }
                    /*LOAD TEXT CONTENT*/
                    try {
                        notificationText = extras.getString(Notification.EXTRA_TEXT);
                    } catch (Exception e) {
                        notificationText = extras.getString(Notification.EXTRA_BIG_TEXT);
                    }
                    /*LOAD ICON*/
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            notificationIcon = statusBarNotification.getNotification().getLargeIcon().loadDrawable(getApplicationContext());

                            FunctionsClass.println("getLargeIcon");
                        } catch (Exception e) {
                            try {
                                notificationIcon = statusBarNotification.getNotification().getSmallIcon().loadDrawable(getApplicationContext());

                                FunctionsClass.println("getSmallIcon");
                            } catch (Exception e1) {
                                notificationIcon = functionsClass.appIcon(notificationPackage);

                                FunctionsClass.println("appIcon");
                            }
                        }
                    } else {
                        try {
                            notificationIcon = functionsClass.bitmapToDrawable(statusBarNotification.getNotification().largeIcon);
                        } catch (Exception e) {
                            notificationIcon = functionsClass.appIcon(notificationPackage);
                        }
                    }
                }

                FunctionsClass.println("::: Package ::: " + notificationPackage);
                FunctionsClass.println("::: Key ::: " + notificationId);
                FunctionsClass.println("::: Title ::: " + notificationTitle);
                FunctionsClass.println("::: Text ::: " + notificationText);
                FunctionsClass.println("::: Time ::: " + notificationTime);

                /*Save Temp Notification Files*/
                functionsClass.saveFileAppendLine(notificationPackage + "_" + "Notification" + "Package", notificationTime);
                functionsClass.saveFile(notificationTime + "_" + "Notification" + "Key", notificationId);
                functionsClass.saveFile(notificationTime + "_" + "Notification" + "Title", notificationTitle);
                functionsClass.saveFile(notificationTime + "_" + "Notification" + "Text", notificationText);
                functionsClass.saveBitmapIcon(notificationTime + "_" + "Notification" + "Icon", notificationIcon);
                PublicVariable.notificationIntent.put(notificationTime, statusBarNotification.getNotification().contentIntent);

                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("Remove_Notification_Key");
                BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        try {
                            if (intent.getAction().equals("Remove_Notification_Key")) {
                                NotificationListener.this.cancelNotification(intent.getStringExtra("notification_key"));

                                try {
                                    String notificationPackage = intent.getStringExtra("notification_package");
                                    String notificationTime = intent.getStringExtra("notification_time");

                                    FunctionsClass.println("::: Remove Package ::: " + notificationPackage);
                                    FunctionsClass.println("::: Remove Time ::: " + notificationTime);
                                    FunctionsClass.println("::: Broadcast Remove Key ::: " + intent.getStringExtra("notification_key"));

                                    deleteFile(notificationTime + "_" + "Notification" + "Key");
                                    deleteFile(notificationTime + "_" + "Notification" + "Title");
                                    deleteFile(notificationTime + "_" + "Notification" + "Text");
                                    deleteFile(notificationTime + "_" + "Notification" + "Icon");

                                    functionsClass.removeLine(notificationPackage + "_" + "Notification" + "Package", notificationTime);
                                    if (functionsClass.countLineInnerFile(notificationPackage + "_" + "Notification" + "Package") == 0) {
                                        deleteFile(notificationPackage + "_" + "Notification" + "Package");
                                        sendBroadcast(new Intent("Notification_Dot_No").putExtra("NotificationPackage", notificationPackage));
                                        PublicVariable.notificationIntent.clear();
                                    }
                                    PublicVariable.notificationIntent.remove(notificationTime);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                try {
                    registerReceiver(broadcastReceiver, intentFilter);
                } catch (AssertionError assertionError) {
                    assertionError.printStackTrace();
                }
                sendBroadcast(new Intent("Notification_Dot").putExtra("NotificationPackage", notificationPackage));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public StatusBarNotification[] getActiveNotifications() {
        return super.getActiveNotifications();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        try {
            if (statusBarNotification.getPackageName() != null && statusBarNotification.isClearable()) {
                String notificationPackage = statusBarNotification.getPackageName();
                String notificationTime = String.valueOf(statusBarNotification.getPostTime());

                FunctionsClass.println("::: Remove Package ::: " + notificationPackage);
                FunctionsClass.println("::: Remove Time ::: " + notificationTime);

                deleteFile(notificationTime + "_" + "Notification" + "Key");
                deleteFile(notificationTime + "_" + "Notification" + "Title");
                deleteFile(notificationTime + "_" + "Notification" + "Text");
                deleteFile(notificationTime + "_" + "Notification" + "Icon");

                functionsClass.removeLine(notificationPackage + "_" + "Notification" + "Package", notificationTime);
                if (functionsClass.countLineInnerFile(notificationPackage + "_" + "Notification" + "Package") == 0) {
                    deleteFile(notificationPackage + "_" + "Notification" + "Package");
                    sendBroadcast(new Intent("Notification_Dot_No").putExtra("NotificationPackage", notificationPackage));
                    PublicVariable.notificationIntent.clear();
                }
                PublicVariable.notificationIntent.remove(notificationTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
