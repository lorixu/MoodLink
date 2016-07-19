package com.example.tim.moodlink;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddContactActivity extends AppCompatActivity {

    private ImageButton return_button;
    private Spinner cat_liste;
    private String[] categoryItemsList;
    private ImageButton validate_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Getting elements
        return_button = (ImageButton) findViewById(R.id.return_button);
        cat_liste = (Spinner) findViewById(R.id.spinner_category);
        validate_button = (ImageButton) findViewById(R.id.validate_button);

        // Setting actions on elements
            // Return Button
        return_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(AddContactActivity.this, EmergencyContactsActivity.class);
                startActivity(intent);
                finish();
            }
        });
            //Category spinner
        categoryItemsList = getResources().getStringArray(R.array.cat_items_new_contact);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryItemsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat_liste.setAdapter(adapter);
            // Validate Button
        validate_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                EditText name_editText = (EditText) findViewById(R.id.name_editText);
                EditText phone_editText = (EditText) findViewById(R.id.phone_editText);
                EditText email_editText = (EditText) findViewById(R.id.email_editText);

                // Getting strings

                String name = name_editText.getText().toString();
                String phone = phone_editText.getText().toString();
                String email = email_editText.getText().toString();
                String category = cat_liste.getSelectedItem().toString();

                if(wellFormedEmail(email) && wellFormedPhone(phone)) {

                    String toWrite = category + "," + name + "," + phone + "," + email + "\n";

                    // Creation of the contacts file
                    FileOutputStream output = null;

                    try {
                        output = openFileOutput(getString(R.string.contacts_file), MODE_APPEND);

                        output.write(toWrite.getBytes());
                        if (output != null)
                            output.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(AddContactActivity.this, EmergencyContactsActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    //TODO : ajouter Toast
                }
            }
        });
    }

    boolean wellFormedEmail(String email) {
        boolean result = true;
        if (TextUtils.isEmpty(email)) {
            result = false;
        } else {
            result = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return result;
    }

    boolean wellFormedPhone(String phone) {
        boolean result = true;
        if (TextUtils.isEmpty(phone)) {
            result = false;
        } else {
            result = Patterns.PHONE.matcher(phone).matches();
        }
        return result;
    }
}


