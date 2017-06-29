package com.pandita.rishabh.androidservicesshutdown;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public static Activity contextActivity;
    public boolean start = true;
    public boolean[] servicesReq = new boolean[6];
    static final int RESULT_ENABLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(), "Welcome,Luke !!!", Toast.LENGTH_LONG).show();
        final Switch wifisw = (Switch) findViewById(R.id.wifiSwitch);
        final Switch bluetoothsw = (Switch) findViewById(R.id.bluetoothSwitch);
        final Switch killsw = (Switch) findViewById(R.id.killBackTaskSwitch);
        final Switch silentPhn = (Switch) findViewById(R.id.silentRingSwitch);
        final Switch lockScreen = (Switch) findViewById(R.id.screenLockSwitch);
        final Switch killMusic = (Switch) findViewById(R.id.killMusicSwitch);
        final EditText timerText = (EditText) findViewById(R.id.timerEditText);
        final Button startbutton = (Button) findViewById(R.id.startStopButton);

        servicesReq[0] = false;
        wifisw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                    servicesReq[0] = false;
                else
                    servicesReq[0] = true;

            }
        });

        servicesReq[1] = false;
        bluetoothsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                    servicesReq[1] = false;
                else
                    servicesReq[1] = true;
            }
        });

        servicesReq[2] = false;
        killsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    servicesReq[2] = false;
                }
                else {
                    if(isAppusgae())
                        servicesReq[2] = true;
                    else{
                        killsw.setChecked(false);
                        Toast.makeText(getApplicationContext(), "Please Enable App Usage", Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "In order to kill background applications please turn App Usage on for Android Services Shutdown", Toast.LENGTH_LONG).show();
                        openAppUsageSettings();
                    }
                }

            }
        });

        servicesReq[3] = false;
        silentPhn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    servicesReq[3] = false;
                } else {
                    if (doNotDisturnSettingAccess()) {
                        servicesReq[3] = true;
                        Toast.makeText(getApplicationContext(), "Mutes all notification sounds except one marked alarm, Luke.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "This functionality needs Do Not Disturb permission to work, Luke.", Toast.LENGTH_LONG).show();
                        silentPhn.setChecked(false);
                    }
                }
            }
        });

        servicesReq[4] = false;
        lockScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    servicesReq[4] = false;
                } else {
                    if (isDeviceAdmin()) {
                        servicesReq[4] = true;
                        Toast.makeText(getApplicationContext(), "Locks screen after specified time, Luke.", Toast.LENGTH_SHORT).show();
                    } else {
                        getDeviceAdminPermission();
                        lockScreen.setChecked(false);
                        Toast.makeText(getApplicationContext(), "This is a one time activity, Luke.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        servicesReq[5] = false;
        killMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                    servicesReq[5] = false;
                else
                    servicesReq[5] = true;
            }
        });

        contextActivity = this;
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable timerCount = timerText.getText();
                if (validateTime(timerCount.toString())) {
                    int duration = Integer.parseInt(timerCount.toString());
                    UtilAlarmActivity am = new UtilAlarmActivity();
                    if (start) {
                        am.setAlarm(getApplicationContext(), duration, servicesReq);
                        start = false;
                        startbutton.setText("STOP");
                        timerText.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "Timer Started, Luke.", Toast.LENGTH_SHORT).show();
                    } else {
                        am.cancelAlarm(getApplicationContext());
                        start = true;
                        startbutton.setText("START");
                        timerText.setEnabled(true);
                        lockScreen.setChecked(false);
                        silentPhn.setChecked(false);
                        killsw.setChecked(false);
                        bluetoothsw.setChecked(false);
                        wifisw.setChecked(false);
                        killMusic.setChecked(false);
                        Toast.makeText(getApplicationContext(), "Timer Cancelled, Luke.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    timerText.setText("");
                    Toast.makeText(getApplicationContext(), "Just the numbers, Luke !!!", Toast.LENGTH_LONG).show();
                }

            }
        });
        final TextView about = (TextView) findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String helpmsg="“Use the force, Luke.”";
                Toast.makeText(getApplicationContext(), helpmsg, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void openAppUsageSettings() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }


    private boolean doNotDisturnSettingAccess() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
            return notificationManager.isNotificationPolicyAccessGranted();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Toast.makeText(getApplicationContext(), "Since Android is below MarshMellow No Setting required", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public boolean validateTime(String duration) {
        for (int i = 0; i < duration.length(); i++) {
            if ((duration.charAt(i) < 48) || (duration.charAt(i) > 57)) {
                return false;
            }
        }
        if (duration.length() == 0 || duration.length() > 3)
            return false;
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == R.id.myswitch) {
                View view = MenuItemCompat.getActionView(item);
                if (view != null) {
                    SwitchCompat mainSwitchOnOffSw = (SwitchCompat) view.findViewById(R.id.switchForActionBar);
                    mainSwitchOnOffSw.setChecked(true);
                    mainSwitchOnOffSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Switch wifisw = (Switch) findViewById(R.id.wifiSwitch);
                            Switch bluetoothsw = (Switch) findViewById(R.id.bluetoothSwitch);
                            Switch killsw = (Switch) findViewById(R.id.killBackTaskSwitch);
                            Switch silentPhn = (Switch) findViewById(R.id.silentRingSwitch);
                            Switch lockScreen = (Switch) findViewById(R.id.screenLockSwitch);
                            EditText timerText = (EditText) findViewById(R.id.timerEditText);
                            Button startbutton1 = (Button) findViewById(R.id.startStopButton);
                            final Switch killMusic = (Switch) findViewById(R.id.killMusicSwitch);

                            if (!isChecked) {
                                lockScreen.setClickable(false);
                                lockScreen.setChecked(false);
                                silentPhn.setClickable(false);
                                silentPhn.setChecked(false);
                                killsw.setClickable(false);
                                killsw.setChecked(false);
                                bluetoothsw.setClickable(false);
                                bluetoothsw.setChecked(false);
                                wifisw.setClickable(false);
                                wifisw.setChecked(false);
                                killMusic.setClickable(false);
                                killMusic.setChecked(false);
                                timerText.setEnabled(false);
                                startbutton1.setEnabled(false);

                            } else {
                                lockScreen.setClickable(true);
                                silentPhn.setClickable(true);
                                killsw.setClickable(true);
                                bluetoothsw.setClickable(true);
                                wifisw.setClickable(true);
                                timerText.setEnabled(true);
                                startbutton1.setEnabled(true);
                                killMusic.setEnabled(true);
                            }
                        }
                    });
                }
            }
        }
        return true;
    }

    public boolean isDeviceAdmin() {
        DevicePolicyManager deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName compName = new ComponentName(this, MyAdmin.class);
        return deviceManger.isAdminActive(compName);
    }

    public void getDeviceAdminPermission() {
        ComponentName compName = new ComponentName(this, MyAdmin.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "In order to lock the screen it is necessary to make application device admin," +
                " you can undo it in settings later if you want");
        startActivityForResult(intent, RESULT_ENABLE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i("***", "Admin enabled!");
                } else {
                    Log.i("***", "Admin enable FAILED!");
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isAppusgae() {
        try {
            PackageManager packageManager = this.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}