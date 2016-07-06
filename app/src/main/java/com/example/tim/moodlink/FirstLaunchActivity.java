package com.example.tim.moodlink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

    // TODO : add a verification on the information filled by user before validating

public class FirstLaunchActivity extends AppCompatActivity {

    private Button validateButton = null;

    private EditText usernameEditText = null;
    private EditText practNameEditText = null;
    private EditText practPhoneEditText = null;
    private EditText practEmailEditText = null;
    private FileOutputStream output = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        usernameEditText = (EditText) findViewById(R.id.username_editText);
        practNameEditText = (EditText) findViewById(R.id.first_contact_name_editText);
        practPhoneEditText = (EditText) findViewById(R.id.first_contact_phone_editText);
        practEmailEditText = (EditText) findViewById(R.id.first_contact_email_editText);


        validateButton = (Button) findViewById(R.id.button_save);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToFiles();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(FirstLaunchActivity.this);
                SharedPreferences.Editor edit = prefs.edit();

                edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
                edit.commit();

                Intent intent = new Intent(FirstLaunchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void writeToFiles(){
        // Creation of the username file

        try {
            output = openFileOutput("USERNAME", MODE_PRIVATE);

            output.write(usernameEditText.getText().toString().getBytes());

            if(output != null)
                output.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creation of the contacts file

        try {
            output = openFileOutput("CONTACTS", MODE_PRIVATE);

            output.write(("Practitioner" + "," + practNameEditText.getText().toString() + "," + practPhoneEditText.getText().toString() + "," + practEmailEditText.getText().toString() + "\n" ).getBytes());

            if(output != null)
                output.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


