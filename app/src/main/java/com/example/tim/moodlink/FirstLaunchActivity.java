package com.example.tim.moodlink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileOutputStream;

public class FirstLaunchActivity extends AppCompatActivity {

    private EditText usernameEditText = null;
    private EditText practitionerNameEditText = null;
    private EditText practitionerPhoneEditText = null;
    private EditText practitionerEmailEditText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        usernameEditText = (EditText) findViewById(R.id.username_editText);
        practitionerNameEditText = (EditText) findViewById(R.id.first_contact_name_editText);
        practitionerPhoneEditText = (EditText) findViewById(R.id.first_contact_phone_editText);
        practitionerEmailEditText = (EditText) findViewById(R.id.first_contact_email_editText);


        ImageButton validateButton = (ImageButton) findViewById(R.id.button_save);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(allFieldsFulled()){
                    createFiles();

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(FirstLaunchActivity.this);
                    SharedPreferences.Editor edit = prefs.edit();

                    edit.putBoolean(getString(R.string.PREF_PREVIOUSLY_STARTED), Boolean.TRUE);

                    edit.apply();

                    Intent intent = new Intent(FirstLaunchActivity.this, MainActivity.class);
                    startActivity(intent);

                    // Set the alarm manager

                    Intent setAlarmManagerIntent = new Intent(FirstLaunchActivity.this, SetAlarmManagerService.class);
                    setAlarmManagerIntent.setAction(SetAlarmManagerService.START_SERVICE);
                    setAlarmManagerIntent.putExtra(SetAlarmManagerService.SERVICE_ID,AlarmReceiver.ALL_SERVICES);
                    FirstLaunchActivity.this.startService(setAlarmManagerIntent);

                    finish();

                }else{

                    Toast.makeText(FirstLaunchActivity.this,"Please fulfil every field.",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private boolean allFieldsFulled() {
        return !(usernameEditText.getText().toString().matches("") ||
                practitionerNameEditText.getText().toString().matches("") ||
                practitionerPhoneEditText.getText().toString().matches("") ||
                practitionerEmailEditText.getText().toString().matches(""));
    }

    private void createFiles(){
        // Creation of the username file

        FileOutputStream output;
        try {
            output = openFileOutput(getString(R.string.username_file), MODE_PRIVATE);

            output.write(usernameEditText.getText().toString().getBytes());

            output.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Creation of the contacts file

        try {
            output = openFileOutput(getString(R.string.contacts_file), MODE_PRIVATE);

            output.write(("practitioner" + "," + practitionerNameEditText.getText().toString() + "," + practitionerPhoneEditText.getText().toString() + "," + practitionerEmailEditText.getText().toString() + "\n" ).getBytes());

            output.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            output = openFileOutput(getString(R.string.light_values_file), MODE_PRIVATE);

            output.write("".getBytes());

            output.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


