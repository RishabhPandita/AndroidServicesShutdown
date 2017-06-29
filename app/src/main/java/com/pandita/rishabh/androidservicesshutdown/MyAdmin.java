package com.pandita.rishabh.androidservicesshutdown;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by Rishabh on 28-06-2017.
 */

public class MyAdmin extends DeviceAdminReceiver {

    static SharedPreferences getSamplePreferences(Context context) {
        return context.getSharedPreferences(DeviceAdminReceiver.class.getName(), 0);
    }

    void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, " Device Admin: enabled");
    }
    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, " Device Admin: disabled");
    }
}
