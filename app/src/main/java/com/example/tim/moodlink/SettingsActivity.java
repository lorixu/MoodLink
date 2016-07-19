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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    public static String TAG = "SETTING ACTIVITY";



    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton return_button = (ImageButton) findViewById(R.id.return_button);

        return_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        // VALUES SAVING TEST

        //spinner
        Spinner valueSpinner = (Spinner) findViewById(R.id.spinnerValues);

        String[] valueItemsList = getResources().getStringArray(R.array.value_items_settings);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, valueItemsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valueSpinner.setAdapter(adapter);

        valueSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView itemSelected = (TextView) view;

                switch (itemSelected.getText().toString()){
                    case "Luminosity" :
                        setButtonsOnLuminosity();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

// TODO : switches
        // Services on/off switch

        CheckBox lightCheckBox = (CheckBox) findViewById(R.id.LightServiceCheckBox);
        CheckBox cameraCheckBox = (CheckBox) findViewById(R.id.CameraServiceCheckBox);
        CheckBox phoneCheckBox = (CheckBox) findViewById(R.id.phoneCheckBox);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
        if(prefs.getBoolean(getString(R.string.PREF_CAMERA_SERVICE_STARTED),Boolean.FALSE)){
            Log.d(TAG, "onCreate: Camera switch = true");
            cameraCheckBox.setChecked(Boolean.TRUE);
        }else{
            Log.d(TAG, "onCreate: Camera switch = false");
            cameraCheckBox.setChecked(Boolean.FALSE);
        }
        if(prefs.getBoolean(getString(R.string.PREF_LIGHT_SERVICE_STARTED),Boolean.FALSE)){
            Log.d(TAG, "onCreate: Light switch = true");
            lightCheckBox.setChecked(Boolean.TRUE);
        }else{
            Log.d(TAG, "onCreate: Light switch = false");
            lightCheckBox.setChecked(Boolean.FALSE);
        }
        if(prefs.getBoolean(getString(R.string.PREF_PHONE_RECEIVER_ACTIVATED),Boolean.FALSE)){
            Log.d(TAG, "onCreate: Phone switch = true");
            phoneCheckBox.setChecked(Boolean.TRUE);
        }else{
            Log.d(TAG, "onCreate: Phone switch = false");
            phoneCheckBox.setChecked(Boolean.FALSE);
        }


        lightCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Intent lightIntent = new Intent(SettingsActivity.this,SetAlarmManagerService.class);
                    lightIntent.setAction(SetAlarmManagerService.START_SERVICE);
                    lightIntent.putExtra(SetAlarmManagerService.SERVICE_ID, AlarmReceiver.LIGHT_SERVICE);
                    SettingsActivity.this.startService(lightIntent);
                    Log.d(TAG, "onCheckedChanged: Light Alarm manager set");
                    Toast.makeText(SettingsActivity.this, "Light Service Launched", Toast.LENGTH_SHORT).show();


                }else{

                    Intent lightIntent = new Intent(SettingsActivity.this,SetAlarmManagerService.class);

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
                if (b){



                    Intent cameraIntent = new Intent(SettingsActivity.this,SetAlarmManagerService.class);
                    cameraIntent.setAction(SetAlarmManagerService.START_SERVICE);
                    cameraIntent.putExtra(SetAlarmManagerService.SERVICE_ID, AlarmReceiver.CAMERA_SERVICE);
                    SettingsActivity.this.startService(cameraIntent);
                    Log.d(TAG, "onCheckedChanged: Camera Alarm manager set");
                    Toast.makeText(SettingsActivity.this, "Camera Service Launched", Toast.LENGTH_SHORT).show();

                }else{

                    Intent cameraIntent = new Intent(SettingsActivity.this,SetAlarmManagerService.class);
                    cameraIntent.setAction(SetAlarmManagerService.STOP_SERVICE);
                    cameraIntent.putExtra(SetAlarmManagerService.SERVICE_ID, AlarmReceiver.CAMERA_SERVICE);
                    SettingsActivity.this.startService(cameraIntent);
                    Log.d(TAG, "onCheckedChanged: Camera Alarm manager canceled");
                    Toast.makeText(SettingsActivity.this, "Camera Service Stopped", Toast.LENGTH_SHORT).show();

                }
            }
        });
        phoneCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                SharedPreferences.Editor edit = prefs.edit();
                if (b){
                    PackageManager pm  = SettingsActivity.this.getPackageManager();
                    ComponentName componentName = new ComponentName(SettingsActivity.this, PhoneReceiver.class);
                    pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    Toast.makeText(getApplicationContext(), "Phone Receiver activated", Toast.LENGTH_LONG).show();

                    edit.putBoolean(SettingsActivity.this.getString(R.string.PREF_PHONE_RECEIVER_ACTIVATED),Boolean.TRUE);
                    edit.apply();



                }else{

                    PackageManager pm  = SettingsActivity.this.getPackageManager();
                    ComponentName componentName = new ComponentName(SettingsActivity.this, PhoneReceiver.class);
                    pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    Toast.makeText(getApplicationContext(), "Phone Receiver disabled", Toast.LENGTH_LONG).show();
                    edit.putBoolean(SettingsActivity.this.getString(R.string.PREF_PHONE_RECEIVER_ACTIVATED),Boolean.FALSE);
                    edit.apply();
                }
            }
        });
    }

    private void setButtonsOnLuminosity() {

        final Button dataButton = (Button) findViewById(R.id.buttonPrintValues);
        final Button dataResetButton = (Button) findViewById(R.id.buttonReset);

        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // File's reading
                FileInputStream input;

                try {
                    input = openFileInput("LIGHT_VALUES");
                    Log.d("FILE READING", "File opened");
                    int character;
                    String fileContent = "";
                    while ((character = input.read()) != -1) {
                        fileContent += (char) character;
                    }

                    Log.d("FILE READING", "Values = " + fileContent);
                    TextView dataText = (TextView) findViewById(R.id.dataPrintingTextView);

                    dataText.setText(fileContent);

                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        dataResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // File's writing
                FileOutputStream output;

                try {
                    output = openFileOutput("LIGHT_VALUES", MODE_PRIVATE);

                    output.write("".getBytes());

                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TextView dataText = (TextView) findViewById(R.id.dataPrintingTextView);

                dataText.setText("");
            }
        });
    }
}
