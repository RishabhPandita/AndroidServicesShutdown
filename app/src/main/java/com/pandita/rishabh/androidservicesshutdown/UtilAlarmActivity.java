package com.pandita.rishabh.androidservicesshutdown;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

/**
 * Created by Rishabh on 12-06-2017.
 */

public class UtilAlarmActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ServiceHandler.class);

        i.putExtra("wifiReq", intent.getExtras().getBoolean("wifiReq"));
        i.putExtra("bluetoothReq", intent.getExtras().getBoolean("bluetoothReq"));
        i.putExtra("taskKillReq", intent.getExtras().getBoolean("taskKillReq"));
        i.putExtra("silentPhnReq", intent.getExtras().getBoolean("silentPhnReq"));
        i.putExtra("lockScreenReq", intent.getExtras().getBoolean("lockScreenReq"));
        i.putExtra("killMusicReq",intent.getExtras().getBoolean("killMusicReq"));

        context.startService(i);

    }

    public void setAlarm(Context context, int duration, boolean[] serviceReq) {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, duration);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, UtilAlarmActivity.class);

        i.putExtra("wifiReq", serviceReq[0]);
        i.putExtra("bluetoothReq", serviceReq[1]);
        i.putExtra("taskKillReq", serviceReq[2]);
        i.putExtra("silentPhnReq", serviceReq[3]);
        i.putExtra("lockScreenReq", serviceReq[4]);
        i.putExtra("killMusicReq", serviceReq[5]);

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