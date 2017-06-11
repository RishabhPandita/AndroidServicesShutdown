package com.pandita.rishabh.androidservicesshutdown;

import android.app.ActionBar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Switch wifisw = (Switch) findViewById(R.id.wifiSwitch);
        Switch bluetoothsw = (Switch) findViewById(R.id.bluetoothSwitch);
        Switch mobileDatasw = (Switch) findViewById(R.id.mobileDataSwitch);
        Switch killsw = (Switch) findViewById(R.id.killBackTaskSwitch);
        Switch silentPhn = (Switch) findViewById(R.id.silentRingSwitch);
        Switch lockScreen = (Switch) findViewById(R.id.screenLockSwitch);
        EditText timerText = (EditText) findViewById(R.id.timerEditText);

        if (flag == true) {

            wifisw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (!isChecked) {
                        Toast.makeText(getApplicationContext(), "Wifi unselected", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Wifi selected", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            bluetoothsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (!isChecked) {
                        Toast.makeText(getApplicationContext(), "Wifi unselected", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Wifi selected", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            mobileDatasw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (!isChecked) {
                        Toast.makeText(getApplicationContext(), "Wifi unselected", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Wifi selected", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            killsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (!isChecked) {
                        Toast.makeText(getApplicationContext(), "Wifi unselected", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Wifi selected", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            silentPhn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (!isChecked) {
                        Toast.makeText(getApplicationContext(), "Wifi unselected", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Wifi selected", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            lockScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (!isChecked) {
                        Toast.makeText(getApplicationContext(), "Wifi unselected", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Wifi selected", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        } else {
            lockScreen.setClickable(false);
            silentPhn.setClickable(false);
            killsw.setClickable(false);
            mobileDatasw.setClickable(false);
            bluetoothsw.setClickable(false);
            wifisw.setClickable(false);
            timerText.setEnabled(false);
        }
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
                    mainSwitchOnOffSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Switch wifisw = (Switch) findViewById(R.id.wifiSwitch);
                            Switch bluetoothsw = (Switch) findViewById(R.id.bluetoothSwitch);
                            Switch mobileDatasw = (Switch) findViewById(R.id.mobileDataSwitch);
                            Switch killsw = (Switch) findViewById(R.id.killBackTaskSwitch);
                            Switch silentPhn = (Switch) findViewById(R.id.silentRingSwitch);
                            Switch lockScreen = (Switch) findViewById(R.id.screenLockSwitch);
                            EditText timerText = (EditText) findViewById(R.id.timerEditText);


                            if (!isChecked) {
                                lockScreen.setClickable(false);
                                lockScreen.setChecked(false);
                                silentPhn.setClickable(false);
                                silentPhn.setChecked(false);
                                killsw.setClickable(false);
                                killsw.setChecked(false);
                                mobileDatasw.setClickable(false);
                                mobileDatasw.setChecked(false);
                                bluetoothsw.setClickable(false);
                                bluetoothsw.setChecked(false);
                                wifisw.setClickable(false);
                                wifisw.setChecked(false);
                                timerText.setEnabled(false);

                            } else {
                                flag = true;
                                lockScreen.setClickable(true);
                                silentPhn.setClickable(true);
                                killsw.setClickable(true);
                                mobileDatasw.setClickable(true);
                                bluetoothsw.setClickable(true);
                                wifisw.setClickable(true);
                                timerText.setEnabled(true);
                            }
                        }
                    });
                }
            }
        }
        return true;
    }

}
