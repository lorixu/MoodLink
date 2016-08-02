package com.example.tim.moodlink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class FirstLaunchActivity extends AppCompatActivity {

    private EditText usernameEditText = null;
    private EditText practitionerNameEditText = null;
    private EditText practitionerPhoneEditText = null;
    private EditText practitionerEmailEditText = null;

    private SQLLiteDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        db = new SQLLiteDBHelper(getApplicationContext());

        usernameEditText = (EditText) findViewById(R.id.username_editText);
        practitionerNameEditText = (EditText) findViewById(R.id.first_contact_name_editText);
        practitionerPhoneEditText = (EditText) findViewById(R.id.first_contact_phone_editText);
        practitionerEmailEditText = (EditText) findViewById(R.id.first_contact_email_editText);


        ImageButton validateButton = (ImageButton) findViewById(R.id.button_save);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(allFieldsFulled()){
                    if(wellFormedEmail(practitionerEmailEditText.getText().toString()) && wellFormedPhone(practitionerPhoneEditText.getText().toString())) {

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
                        Toast.makeText(FirstLaunchActivity.this, "Phone or Email not valid", Toast.LENGTH_SHORT).show();
                    }

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

        db.addUsernameTuple(new UsernameData(usernameEditText.getText().toString()));

        // Creation of the contacts file

        db.addContactTuple(new ContactData(
                practitionerNameEditText.getText().toString(),
                ContactData.PRACTITIONER,
                practitionerPhoneEditText.getText().toString(),
                practitionerEmailEditText.getText().toString()
        ));
    }


    boolean wellFormedEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    boolean wellFormedPhone(String phone) {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}


