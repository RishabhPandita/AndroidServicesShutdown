package com.pandita.rishabh.androidservicesshutdown;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public static Activity contextActivity;
    public boolean start = true;
    public String[] services = new String[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Switch wifisw = (Switch) findViewById(R.id.wifiSwitch);
        Switch bluetoothsw = (Switch) findViewById(R.id.bluetoothSwitch);
        Switch killsw = (Switch) findViewById(R.id.killBackTaskSwitch);
        final Switch silentPhn = (Switch) findViewById(R.id.silentRingSwitch);
        Switch lockScreen = (Switch) findViewById(R.id.screenLockSwitch);
        Switch killMusic = (Switch) findViewById(R.id.killMusicSwitch);
        final EditText timerText = (EditText) findViewById(R.id.timerEditText);
        final Button startbutton = (Button) findViewById(R.id.startStopButton);

        services[0] = "no";
        wifisw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    Toast.makeText(getApplicationContext(), "Wifi unselected", Toast.LENGTH_SHORT).show();
                    services[0] = "no";
                } else {
                    Toast.makeText(getApplicationContext(), "Wifi selected", Toast.LENGTH_SHORT).show();
                    services[0] = "yes";

                }
            }
        });

        services[1] = "no";
        bluetoothsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    services[1] = "no";
                    Toast.makeText(getApplicationContext(), "Bluetoth unselected", Toast.LENGTH_SHORT).show();
                } else {
                    services[1] = "yes";
                    Toast.makeText(getApplicationContext(), "Bluetooth selected", Toast.LENGTH_SHORT).show();

                }
            }
        });
        services[2] = "no";
        services[3] = "no";
        killsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    services[3] = "no";
                    Toast.makeText(getApplicationContext(), "Dont Kill ALL Task", Toast.LENGTH_SHORT).show();
                } else {
                    services[3] = "yes";
                    Toast.makeText(getApplicationContext(), "Kill All Task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        services[4] = "no";
        silentPhn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    services[4] = "no";
                } else {
                    if (doNotDisturnSettingAccess()) {
                        services[4] = "yes";
                        Toast.makeText(getApplicationContext(), "Mutes all notification sounds except one marked alarm", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "This functionality needs Do Not Disturb permission to work", Toast.LENGTH_LONG).show();
                        silentPhn.setChecked(false);
                    }
                }
            }
        });

        services[5] = "no";
        lockScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    services[5] = "no";
                    Toast.makeText(getApplicationContext(), "Lock Screen unselected", Toast.LENGTH_SHORT).show();
                } else {
                    services[5] = "yes";
                    Toast.makeText(getApplicationContext(), "Lock Screen selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        services[6] = "no";
        killMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    services[6] = "no";
                    Toast.makeText(getApplicationContext(), "Kill Music unselected", Toast.LENGTH_SHORT).show();
                } else {
                    services[6] = "yes";
                    Toast.makeText(getApplicationContext(), "Kill Music selected", Toast.LENGTH_SHORT).show();
                }
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
                        am.setAlarm(getApplicationContext(), services, duration);
                        start = false;
                        startbutton.setText("STOP");
                        timerText.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "Timer Started", Toast.LENGTH_LONG).show();
                    } else {
                        start = true;
                        startbutton.setText("START");
                        am.cancelAlarm(getApplicationContext());
                        timerText.setEnabled(true);
                        Toast.makeText(getApplicationContext(), "Timer Cancelled", Toast.LENGTH_LONG).show();
                    }
                } else {
                    timerText.setText("");
                    Toast.makeText(getApplicationContext(), "Enter correct duration", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    private boolean doNotDisturnSettingAccess() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

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
                            }
                        }
                    });
                }
            }
        }
        return true;
    }
}
