package com.example.tim.moodlink;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class FirstLaunchActivity extends AppCompatActivity {

    public static String TAG = "First Launch Activity";

    private EditText usernameEditText = null;
    private EditText userHomeEditText = null;
    private EditText userWorkEditText = null;
    private EditText practitionerNameEditText = null;
    private EditText practitionerPhoneEditText = null;
    private EditText practitionerEmailEditText = null;

    private SQLLiteDBHelper db;

    Thread createDBThread = null;

    Runnable createDB = new Runnable() {
        @Override
        public void run() {
            db = new SQLLiteDBHelper(getApplicationContext());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        createDBThread = new Thread(createDB);
        createDBThread.start();

        usernameEditText = (EditText) findViewById(R.id.username_editText);
        userHomeEditText = (EditText) findViewById(R.id.homeEditText);
        userWorkEditText = (EditText) findViewById(R.id.workEditText);
        practitionerNameEditText = (EditText) findViewById(R.id.first_contact_name_editText);
        practitionerPhoneEditText = (EditText) findViewById(R.id.first_contact_phone_editText);
        practitionerEmailEditText = (EditText) findViewById(R.id.first_contact_email_editText);


        ImageButton validateButton = (ImageButton) findViewById(R.id.button_save);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (createDBThread == null) {
                    Toast.makeText(FirstLaunchActivity.this, "DataBase in creation, please try again in a few seconds.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        createDBThread.join();

                        if (allFieldsFulled()) {
                            if (wellFormedEmail(practitionerEmailEditText.getText().toString()) && wellFormedPhone(practitionerPhoneEditText.getText().toString())) {

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
                                setAlarmManagerIntent.putExtra(SetAlarmManagerService.SERVICE_ID, AlarmReceiver.ALL_SERVICES);
                                FirstLaunchActivity.this.startService(setAlarmManagerIntent);

                                finish();
                            } else {
                                Toast.makeText(FirstLaunchActivity.this, "Phone or Email not valid", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(FirstLaunchActivity.this, "Please fulfil every field.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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

    private void createFiles() {
        // Creation of the username file

        if (userWorkEditText.getText().length() == 0) {
            db.addUserTuple(new UserData(usernameEditText.getText().toString(), getLocationFromAddress(FirstLaunchActivity.this, userHomeEditText.getText().toString())));
        } else {
            db.addUserTuple(new UserData(usernameEditText.getText().toString(), getLocationFromAddress(FirstLaunchActivity.this, userHomeEditText.getText().toString()), getLocationFromAddress(FirstLaunchActivity.this, userWorkEditText.getText().toString())));
        }
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

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            Log.d(TAG, "getLocationFromAddress: " + location.getLatitude());
            Log.d(TAG, "getLocationFromAddress: " + location.getLongitude());

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}


