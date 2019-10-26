package net.geekstools.floatshort.PRO.Automation.Alarms;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;

import java.util.Calendar;

public class SetupAlarms extends Service {

    FunctionsClass functionsClass;
    Calendar newAlarmTime;

    String setTime, nextSetTime;
    int packagePosition, nextPosition;
    String[] timesClocks;

    int hrsDigits, minDigits;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        setTime = intent.getStringExtra("time");
        packagePosition = intent.getIntExtra("position", -1);

        timesClocks = functionsClass.readFileLine(".times.clocks");
        if ((packagePosition + 1) == functionsClass.countLineInnerFile(".times.clocks")) {
            //last one reset to first line
            String[] time = timesClocks[0].split(":");
            hrsDigits = Integer.parseInt(time[0]);
            minDigits = Integer.parseInt(time[1]);

            newAlarmTime.set(Calendar.HOUR_OF_DAY, hrsDigits);
            newAlarmTime.set(Calendar.MINUTE, minDigits);
            newAlarmTime.set(Calendar.SECOND, 0);

            nextSetTime = timesClocks[0];
            nextPosition = 0;
        } else {
            //set the next line
            try {
                String[] time = timesClocks[packagePosition + 1].split(":");
                hrsDigits = Integer.parseInt(time[0]);
                minDigits = Integer.parseInt(time[1]);

                newAlarmTime.set(Calendar.HOUR_OF_DAY, hrsDigits);
                newAlarmTime.set(Calendar.MINUTE, minDigits);
                newAlarmTime.set(Calendar.SECOND, 13);

                nextSetTime = timesClocks[packagePosition + 1];
                nextPosition = packagePosition + 1;
            } catch (Exception e) {
                e.printStackTrace();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    stopForeground(Service.STOP_FOREGROUND_REMOVE);
                    stopForeground(true);
                }
                stopSelf();
                return Service.START_NOT_STICKY;
            }

        }
        functionsClass.initialAlarm(newAlarmTime, nextSetTime, nextPosition);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(Service.STOP_FOREGROUND_REMOVE);
            stopForeground(true);
        }
        stopSelf();

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        functionsClass = new FunctionsClass(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(333, functionsClass.bindServiceNotification(), Service.STOP_FOREGROUND_REMOVE);
        } else {
            startForeground(333, functionsClass.bindServiceNotification());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(getApplicationContext(), BindServices.class));
        } else {
            startService(new Intent(getApplicationContext(), BindServices.class));
        }

        newAlarmTime = Calendar.getInstance();

        timesClocks = new String[functionsClass.countLineInnerFile(".times.clocks")];
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(Service.STOP_FOREGROUND_REMOVE);
            stopForeground(true);
        }
        stopSelf();
    }
}
