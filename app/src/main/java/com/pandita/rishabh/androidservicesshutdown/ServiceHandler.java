package com.pandita.rishabh.androidservicesshutdown;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rishabh on 12-06-2017.
 */

public class ServiceHandler extends Service implements AudioManager.OnAudioFocusChangeListener {
    public static final int REQUEST_ENABLE = 100;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SERVICE CLASS mData***", intent.getStringExtra("mdata"));
        String wifi = intent.getStringExtra("wifi");
        String bluetooth = intent.getStringExtra("bluetooth");
        String tkill = intent.getStringExtra("taskkill");
        String lock = intent.getStringExtra("lockscreen");

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        if (wifi.equals("yes") && wifiManager != null && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetooth.equals("yes") && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }


       /* DevicePolicyManager mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        KeyguardManager myKM = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);*/
        /*if(lock.equals("yes") *//*&& myKM!=null  && mDPM!=null*//* && !myKM.inKeyguardRestrictedInputMode()) {
           // ComponentName mAdminName = new ComponentName(this, LockAdminReq.class);
            if (!mDPM.isAdminActive(mAdminName)) {
                // try to become active – must happen here in this activity, to get result
                Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mAdminName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"Please make admin");
                startActivityForResult(intent,REQUEST_ENABLE);

            } else {
// Already is a device administrator, can do security operations now.
                mDPM.lockNow();
            }

        }*/


        if (tkill.equals("yes")) {
            List<String> appInfos = getListOfLaunchableApps();
            killActiveAppsOnly(appInfos);
            killMusic();
        }

        Log.i("***", "KILLING MAIN ACTIVITY SERVICE");
        stopService(new Intent(this, ServiceHandler.class));
        return super.onStartCommand(intent, flags, startId);
    }

    private void killMusic() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = am.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            am.abandonAudioFocus(this);
        }
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
                        Log.i("RUNNING APPS ***", "" + usageStats.getPackageName() + "=" + usm.isAppInactive(usageStats.getPackageName()));

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
