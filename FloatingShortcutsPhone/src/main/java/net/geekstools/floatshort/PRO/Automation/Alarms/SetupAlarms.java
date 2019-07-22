package net.geekstools.floatshort.PRO.Automation.Alarms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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

                stopSelf();
                return Service.START_NOT_STICKY;
            }

        }
        functionsClass.initialAlarm(newAlarmTime, nextSetTime, nextPosition);

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        functionsClass = new FunctionsClass(getApplicationContext());
        if (functionsClass.returnAPI() >= 26) {
            startForeground(333, functionsClass.bindServiceNotification());
        }
        newAlarmTime = Calendar.getInstance();

        timesClocks = new String[functionsClass.countLineInnerFile(".times.clocks")];
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
