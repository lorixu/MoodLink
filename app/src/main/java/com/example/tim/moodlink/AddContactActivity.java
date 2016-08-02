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
import android.widget.Toast;

public class AddContactActivity extends AppCompatActivity {

    private Spinner cat_list;
    private SQLLiteDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        db = new SQLLiteDBHelper(getApplicationContext());

        // Getting elements
        ImageButton return_button = (ImageButton) findViewById(R.id.return_button);
        cat_list = (Spinner) findViewById(R.id.spinner_category);
        ImageButton validate_button = (ImageButton) findViewById(R.id.validate_button);

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
        String[] categoryItemsList = getResources().getStringArray(R.array.cat_items_new_contact);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryItemsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat_list.setAdapter(adapter);
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
                String category = cat_list.getSelectedItem().toString();

                if(wellFormedEmail(email) && wellFormedPhone(phone)) {

                    db.addContactTuple(new ContactData(
                            name,
                            category,
                            phone,
                            email
                    ));



                    Intent intent = new Intent(AddContactActivity.this, EmergencyContactsActivity.class);
                    startActivity(intent);

                    finish();
                    }else{
                    Toast.makeText(AddContactActivity.this, "Phone or Email not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    boolean wellFormedEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    boolean wellFormedPhone(String phone) {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches();
    }
}


