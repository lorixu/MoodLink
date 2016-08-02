package com.example.tim.moodlink;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    public static String TAG = "SETTING ACTIVITY";

    public SQLLiteDBHelper db;


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = new SQLLiteDBHelper(getApplicationContext());


        ImageButton return_button = (ImageButton) findViewById(R.id.return_button);

        return_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Services on/off switches

        CheckBox lightCheckBox = (CheckBox) findViewById(R.id.LightServiceCheckBox);
        CheckBox cameraCheckBox = (CheckBox) findViewById(R.id.CameraServiceCheckBox);
        CheckBox locationCheckBox = (CheckBox) findViewById(R.id.LocationServiceCheckBox);
        CheckBox phoneCheckBox = (CheckBox) findViewById(R.id.phoneCheckBox);
        CheckBox accelerometerCheckBox = (CheckBox) findViewById(R.id.AccelerometerServiceCheckBox);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);

        Pair<?, ?>[] prefsId =new Pair<?, ?>[]{
                new Pair<>(getString(R.string.PREF_CAMERA_SERVICE_STARTED), cameraCheckBox),
                new Pair<>(getString(R.string.PREF_LIGHT_SERVICE_STARTED), lightCheckBox),
                new Pair<>(getString(R.string.PREF_PHONE_RECEIVER_ACTIVATED), phoneCheckBox),
                new Pair<>(getString(R.string.PREF_LOCATION_SERVICE_ACTIVATED), locationCheckBox),
                new Pair<>(getString(R.string.PREF_ACCELEROMETER_SERVICE_STARTED), accelerometerCheckBox)};

        for (Pair<?,?> pair : prefsId) {
            String strId = (String) pair.first;
            CheckBox chkbx = (CheckBox) pair.second;
            if (prefs.getBoolean(strId, false)) {
                chkbx.setChecked(Boolean.TRUE);
            } else {
                chkbx.setChecked(Boolean.FALSE);
            }
        }

        lightCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Intent lightIntent = new Intent(SettingsActivity.this, SetAlarmManagerService.class);
                    lightIntent.setAction(SetAlarmManagerService.START_SERVICE);
                    lightIntent.putExtra(SetAlarmManagerService.SERVICE_ID, AlarmReceiver.LIGHT_SERVICE);
                    SettingsActivity.this.startService(lightIntent);
                    Log.d(TAG, "onCheckedChanged: Light Alarm manager set");
                    Toast.makeText(SettingsActivity.this, "Light Service Launched", Toast.LENGTH_SHORT).show();


                } else {

                    Intent lightIntent = new Intent(SettingsActivity.this, SetAlarmManagerService.class);

                    lightIntent.setAction(SetAlarmManagerService.STOP_SERVICE);
                    lightIntent.putExtra(SetAlarmManagerService.SERVICE_ID, AlarmReceiver.LIGHT_SERVICE);
                    SettingsActivity.this.startService(lightIntent);
                    Log.d(TAG, "onCheckedChanged: Light Alarm manager canceled");
                    Toast.makeText(SettingsActivity.this, "Light Service Stopped", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cameraCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {


                    Intent cameraIntent = new Intent(SettingsActivity.this, SetAlarmManagerService.class);
                    cameraIntent.setAction(SetAlarmManagerService.START_SERVICE);
                    cameraIntent.putExtra(SetAlarmManagerService.SERVICE_ID, AlarmReceiver.CAMERA_SERVICE);
                    SettingsActivity.this.startService(cameraIntent);
                    Log.d(TAG, "onCheckedChanged: Camera Alarm manager set");
                    Toast.makeText(SettingsActivity.this, "Camera Service Launched", Toast.LENGTH_SHORT).show();

                } else {

                    Intent cameraIntent = new Intent(SettingsActivity.this, SetAlarmManagerService.class);
                    cameraIntent.setAction(SetAlarmManagerService.STOP_SERVICE);
                    cameraIntent.putExtra(SetAlarmManagerService.SERVICE_ID, AlarmReceiver.CAMERA_SERVICE);
                    SettingsActivity.this.startService(cameraIntent);
                    Log.d(TAG, "onCheckedChanged: Camera Alarm manager canceled");
                    Toast.makeText(SettingsActivity.this, "Camera Service Stopped", Toast.LENGTH_SHORT).show();

                }
            }
        });

        locationCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    Intent locationIntent = new Intent(SettingsActivity.this, SetAlarmManagerService.class);
                    locationIntent.setAction(SetAlarmManagerService.START_SERVICE);
                    locationIntent.putExtra(SetAlarmManagerService.SERVICE_ID, AlarmReceiver.LOCATION_SERVICE);
                    SettingsActivity.this.startService(locationIntent);
                    Log.d(TAG, "onCheckedChanged: Location Alarm manager set");
                    Toast.makeText(SettingsActivity.this, "Location Service Launched", Toast.LENGTH_SHORT).show();

                } else {

                    Intent locationIntent = new Intent(SettingsActivity.this, SetAlarmManagerService.class);
                    locationIntent.setAction(SetAlarmManagerService.STOP_SERVICE);
                    locationIntent.putExtra(SetAlarmManagerService.SERVICE_ID, AlarmReceiver.LOCATION_SERVICE);
                    SettingsActivity.this.startService(locationIntent);
                    Log.d(TAG, "onCheckedChanged: Location Alarm manager canceled");
                    Toast.makeText(SettingsActivity.this, "Location Service Stopped", Toast.LENGTH_SHORT).show();

                }
            }
        });

        phoneCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                SharedPreferences.Editor edit = prefs.edit();
                if (b) {
                    PackageManager pm = SettingsActivity.this.getPackageManager();
                    ComponentName componentName = new ComponentName(SettingsActivity.this, PhoneReceiver.class);
                    pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    Toast.makeText(getApplicationContext(), "Phone Receiver activated", Toast.LENGTH_LONG).show();

                    edit.putBoolean(SettingsActivity.this.getString(R.string.PREF_PHONE_RECEIVER_ACTIVATED), Boolean.TRUE);
                    edit.apply();


                } else {

                    PackageManager pm = SettingsActivity.this.getPackageManager();
                    ComponentName componentName = new ComponentName(SettingsActivity.this, PhoneReceiver.class);
                    pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    Toast.makeText(getApplicationContext(), "Phone Receiver disabled", Toast.LENGTH_LONG).show();
                    edit.putBoolean(SettingsActivity.this.getString(R.string.PREF_PHONE_RECEIVER_ACTIVATED), Boolean.FALSE);
                    edit.apply();
                }
            }
        });

        accelerometerCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    Intent accelerometerIntent = new Intent(SettingsActivity.this, SetAlarmManagerService.class);
                    accelerometerIntent.setAction(SetAlarmManagerService.START_SERVICE);
                    accelerometerIntent.putExtra(SetAlarmManagerService.SERVICE_ID, AlarmReceiver.ACCELEROMETER_SERVICE);
                    SettingsActivity.this.startService(accelerometerIntent);
                    Log.d(TAG, "onCheckedChanged: Accelerometer Alarm manager set");
                    Toast.makeText(SettingsActivity.this, "Accelerometer Service Launched", Toast.LENGTH_SHORT).show();

                } else {

                    Intent accelerometerIntent = new Intent(SettingsActivity.this, SetAlarmManagerService.class);
                    accelerometerIntent.setAction(SetAlarmManagerService.STOP_SERVICE);
                    accelerometerIntent.putExtra(SetAlarmManagerService.SERVICE_ID, AlarmReceiver.ACCELEROMETER_SERVICE);
                    SettingsActivity.this.startService(accelerometerIntent);
                    Log.d(TAG, "onCheckedChanged: Accelerometer Alarm manager canceled");
                    Toast.makeText(SettingsActivity.this, "Accelerometer Service Stopped", Toast.LENGTH_SHORT).show();

                }
            }
        });


        // VALUES SAVING TEST

        //spinner
        Spinner valueSpinner = (Spinner) findViewById(R.id.spinnerValues);

        String[] valueItemsList = getResources().getStringArray(R.array.value_items_settings);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, valueItemsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valueSpinner.setAdapter(adapter);

        valueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                Button dataButton = (Button) findViewById(R.id.buttonPrintValues);
                Button deleteButton = (Button) findViewById(R.id.buttonDeleteValues);
                dataButton.setOnClickListener(null);
                deleteButton.setOnClickListener(null);

                TextView itemSelected = (TextView) view;
                Log.d(TAG, "onItemSelected: "+itemSelected.getText().toString());
                switch (itemSelected.getText().toString()) {
                    case "Luminosity":
                        setButtonOnLuminosity(dataButton, deleteButton);
                        break;
                    case "Accelerometer":
                        setButtonOnAccelerometer(dataButton, deleteButton);
                        break;
                    case "Location":
                        setButtonOnLocation(dataButton, deleteButton);
                        break;
                    case "Phone":
                        setButtonOnPhone(dataButton, deleteButton);
                        break;
                    case "Camera":
                        setButtonOnCamera(dataButton, deleteButton);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void setButtonOnLuminosity(Button dataButton, Button deleteButton) {
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<LightData> list = (ArrayList) db.getAllLightDatas();
                Log.d(TAG, "onClick: " + list.toString());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAllLightData();
            }
        });
    }

    private void setButtonOnAccelerometer(Button dataButton,Button deleteButton) {
         dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<AccelerometerData> list = (ArrayList) db.getAllAccelerometerDatas();
                Log.d(TAG, "onClick: " + list.toString());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAllAccelerometerData();
            }
        });
    }

    private void setButtonOnLocation(Button dataButton,Button deleteButton) {
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<LocationData> list = (ArrayList) db.getAllLocationDatas();
                Log.d(TAG, "onClick: " + list.toString());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAllLocationData();
            }
        });
    }

    private void setButtonOnPhone(Button dataButton,Button deleteButton) {
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<PhoneData> list = (ArrayList) db.getAllPhoneDatas();
                Log.d(TAG, "onClick: " + list.toString());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAllPhoneData();
            }
        });
    }

    private void setButtonOnCamera(Button dataButton,Button deleteButton) {
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<CameraData> list = (ArrayList) db.getAllCameraDatas();
                Log.d(TAG, "onClick: " + list.toString());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAllCameraData();
            }
        });
    }
}
