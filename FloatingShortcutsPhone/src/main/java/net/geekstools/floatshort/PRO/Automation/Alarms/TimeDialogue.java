package net.geekstools.floatshort.PRO.Automation.Alarms;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TimePicker;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClassDebug;

import java.util.Calendar;

public class TimeDialogue extends Activity {

    FunctionsClass functionsClass;

    Calendar newAlarmTime;
    TimePickerDialog timePickerDialog;

    String content, type;
    int position = 0;

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        functionsClass = new FunctionsClass(getApplicationContext(), this);
        newAlarmTime = Calendar.getInstance();

        content = getIntent().getStringExtra("content");
        type = getIntent().getStringExtra("type");

        timePickerDialog =
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                        newAlarmTime.set(Calendar.HOUR_OF_DAY, hours);
                        newAlarmTime.set(Calendar.MINUTE, minutes);
                        newAlarmTime.set(Calendar.SECOND, 13);

                        String setTime = hours + ":" + minutes;
                        FunctionsClassDebug.Companion.PrintDebug("*** " + setTime);
                        functionsClass.saveFile(
                                content + ".Time",
                                setTime);
                        functionsClass.removeLine(".times.clocks", setTime);
                        functionsClass.saveFileAppendLine(
                                ".times.clocks",
                                setTime);
                        functionsClass.saveFileAppendLine(
                                setTime,
                                content + type);
                        functionsClass.initialAlarm(newAlarmTime, setTime, position);

                        timePickerDialog.dismiss();
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        Calendar.getInstance().get(Calendar.MINUTE),
                        true);
        timePickerDialog.setCancelable(false);
        timePickerDialog.show();

        timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
    }
}
