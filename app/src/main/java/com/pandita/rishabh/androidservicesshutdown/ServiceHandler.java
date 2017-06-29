package com.pandita.rishabh.androidservicesshutdown;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rishabh on 12-06-2017.
 */

public class ServiceHandler extends Service implements AudioManager.OnAudioFocusChangeListener {

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Boolean wifiReq = intent.getExtras().getBoolean("wifiReq");
        Boolean bluetoothReq = intent.getExtras().getBoolean("bluetoothReq");
        Boolean taskKillReq = intent.getExtras().getBoolean("taskKillReq");
        Boolean silentPhnReq = intent.getExtras().getBoolean("silentPhnReq");
        Boolean lockScreenReq = intent.getExtras().getBoolean("lockScreenReq");
        Boolean killMusicReq = intent.getExtras().getBoolean("killMusicReq");


        if (wifiReq) {
            WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            if (wifiManager != null && wifiManager.isWifiEnabled())
                wifiManager.setWifiEnabled(false);
            Log.i("***", "Wifi Disabled");
        }

        if (bluetoothReq) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled())
                mBluetoothAdapter.disable();
            Log.i("***", "Bluetooth Disabled");
        }

        if (taskKillReq) {
            Log.i("***", "Kill Task Started");
            List<String> appInfos = getListOfLaunchableApps();
            killActiveAppsOnly(appInfos);
            killMusic();
            Log.i("***", "Kill Task Ended");
        }

        if (silentPhnReq) {
            Log.i("***", "Silent Phone Notification level 3 Started");
            putPhoneOnSilent();
            Log.i("***", "Silent Phone Notification level 3 ended");
        }

        if (lockScreenReq) {
            DevicePolicyManager deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName compName = new ComponentName(this, MyAdmin.class);
            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                Log.i("***", "Device has become admin");
                deviceManger.lockNow();
            } else {
                Log.i("***", "Device is not admin");
            }
        }

        if (killMusicReq) {
            Log.i("***", "Kill Music Started");
            killMusic();
            Log.i("***", "Kill Music ended");
        }

        Log.i("***", "KILLING SERVICE");
        stopService(new Intent(this, ServiceHandler.class));
        return super.onStartCommand(intent, flags, startId);
    }

    private void putPhoneOnSilent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null && notificationManager.isNotificationPolicyAccessGranted()) {
                int current_setting = notificationManager.getCurrentInterruptionFilter();
                Log.i("***", "Current Notification setting:" + current_setting);
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS);
                current_setting = notificationManager.getCurrentInterruptionFilter();
                Log.i("***", "After Modification Notification setting:" + current_setting);

            } else if (notificationManager != null && !notificationManager.isNotificationPolicyAccessGranted()) {
                Log.i("***", "Silent function wont work because permission not granted");
            } else if (notificationManager == null) {
                Log.i("***", "Notification Manager Object not found");
            }
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.i("***", "Audio Manager for <Marshmellow not tested");
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (am != null)
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            else
                Log.i("***", "Audio Manager Object not found");
        }
    }

    private void killMusic() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (am != null) {
            int result = am.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                Log.i("***", "Audio Manager Access granted");
                am.abandonAudioFocus(this);
            } else {
                Log.i("***", "Audio Manager Access not granted");
            }
        } else
            Log.i("***", "Audio Manager Object not found");
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Log.i("Audio ***", "Loss/Duck");
                break;
        }
    }

    private void killActiveAppsOnly(List<String> appInfos) {
        String currentApp = "NULL";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            PackageManager pm = getApplicationContext().getPackageManager();
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {

                for (UsageStats usageStats : appList) {
                    if (appInfos.contains(usageStats.getPackageName())) {
                        Log.i("***", "" + "RUNNING "+usageStats.getPackageName() + "=" + usm.isAppInactive(usageStats.getPackageName()));
                        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
                        am.killBackgroundProcesses(usageStats.getPackageName());
                    }
                }
            }
        } else {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
            Log.e("TODO adapter***", "INCOMPLETE CODE foreground is: " + currentApp);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public List<String> getListOfLaunchableApps() {

        final PackageManager pm = getPackageManager();
        Intent intend = new Intent(Intent.ACTION_MAIN, null);
        intend.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> ri = pm.queryIntentActivities(intend, 0);
        List<String> appInfos = new ArrayList<String>(0);
        for (ResolveInfo resolveInfo : ri) {
            try {
                ApplicationInfo a = pm.getApplicationInfo(resolveInfo.activityInfo.packageName, PackageManager.GET_META_DATA);
                appInfos.add(a.packageName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return appInfos;
    }
}