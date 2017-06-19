package com.pandita.rishabh.androidservicesshutdown;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Rishabh on 12-06-2017.
 */

public class UtilAlarmActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm ***", Toast.LENGTH_LONG).show();// For example

        Intent i = new Intent(context, ServiceHandler.class);
        i.putExtra("wifi", intent.getStringExtra("wifi"));
        i.putExtra("bluetooth", intent.getStringExtra("bluetooth"));
        i.putExtra("mdata", intent.getStringExtra("mdata"));
        i.putExtra("taskkill", intent.getStringExtra("taskkill"));
        i.putExtra("silentphn", intent.getStringExtra("silentphn"));
        i.putExtra("lockscreen", intent.getStringExtra("lockscreen"));
        context.startService(i);

    }

    public void setAlarm(Context context, String[] services, int duration) {
        //10 seconds later

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, duration);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, UtilAlarmActivity.class);

        i.putExtra("wifi", services[0]);
        i.putExtra("bluetooth", services[1]);
        i.putExtra("mdata", services[2]);
        i.putExtra("taskkill", services[3]);
        i.putExtra("silentphn", services[4]);
        i.putExtra("lockscreen", services[5]);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, UtilAlarmActivity.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        sender.cancel();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
