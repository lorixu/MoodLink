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

public class ModifyContactActivity extends AppCompatActivity {

    public static final String TAG = "ModifyContact";

    private Spinner cat_list;
    private EditText name_editText;
    private EditText phone_editText;
    private EditText email_editText;

    private long id_modified = -1;

    private SQLLiteDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_contact);

        db = new SQLLiteDBHelper(getApplicationContext());

        // Getting elements
        ImageButton return_button = (ImageButton) findViewById(R.id.return_button);
        ImageButton validate_button = (ImageButton) findViewById(R.id.validate_button);

        id_modified = getIntent().getIntExtra("contact_id",-1);


        name_editText = (EditText) findViewById(R.id.name_editText);
        phone_editText = (EditText) findViewById(R.id.phone_editText);
        email_editText = (EditText) findViewById(R.id.email_editText);
        cat_list = (Spinner) findViewById(R.id.spinner_category);


        // Setting values
        ContactData contact = db.getContactData(id_modified);



        name_editText.setText(contact.getName());
        phone_editText.setText(contact.getPhone());
        email_editText.setText(contact.getEmail());

        // Setting actions on elements
            // Return Button
        return_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(ModifyContactActivity.this, EmergencyContactsActivity.class);
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

                if(wellFormedEmail(email_editText.getText().toString()) && wellFormedPhone(phone_editText.getText().toString())) {

                    db.updateContactData(id_modified,new ContactData(
                            name_editText.getText().toString(),
                            cat_list.getSelectedItem().toString(),
                            phone_editText.getText().toString(),
                            email_editText.getText().toString()
                    ));

                    Intent intent = new Intent(ModifyContactActivity.this, EmergencyContactsActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(ModifyContactActivity.this, "Phone or Email not valid", Toast.LENGTH_SHORT).show();
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
