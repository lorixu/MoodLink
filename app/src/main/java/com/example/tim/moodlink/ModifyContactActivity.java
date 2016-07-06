package com.example.tim.moodlink;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModifyContactActivity extends AppCompatActivity {

    private ImageButton return_button;
    private Spinner cat_liste;
    private String[] categoryItemsList;
    private ImageButton validate_button;
    private EditText name_editText;
    private EditText phone_editText;
    private EditText email_editText;

    private FileInputStream input = null;
    private int position_modified = -1;
    List<HashMap<String, String>> contactsList = new ArrayList<HashMap<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_contact);

        // Getting elements
        return_button = (ImageButton) findViewById(R.id.return_button);
        validate_button = (ImageButton) findViewById(R.id.validate_button);

        position_modified = getIntent().getIntExtra("position",-1);


        name_editText = (EditText) findViewById(R.id.name_editText);
        phone_editText = (EditText) findViewById(R.id.phone_editText);
        email_editText = (EditText) findViewById(R.id.email_editText);
        cat_liste = (Spinner) findViewById(R.id.spinner_category);

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
        categoryItemsList = getResources().getStringArray(R.array.cat_items_new_contact);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryItemsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat_liste.setAdapter(adapter);


            // Validate Button
        validate_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // TODO : Test if entries are valid
                deleteContact(position_modified);
                String toWrite = cat_liste.getSelectedItem().toString() + "," + name_editText.getText() + "," + phone_editText.getText() + "," + email_editText.getText() + "\n";

                // Creation of the contacts file
                FileOutputStream output = null;

                try {
                    output = openFileOutput("CONTACTS", MODE_APPEND);

                    output.write(toWrite.getBytes());
                    if(output != null)
                        output.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(ModifyContactActivity.this, EmergencyContactsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Setting values


        try {
            input = openFileInput ("CONTACTS");
            int character;
            String info = "";
            HashMap<String, String> element = new HashMap<String, String>();;
            ArrayList<String> contact = new ArrayList<String>();
            contactsList.clear();

            while ((character = input.read()) != -1) {
                if (((char) character) != '\n') {
                    if (((char) character) != ',') {
                        info += (char) character;
                    }else {
                        contact.add(info);
                        info = "";
                    }
                }else{
                    element.put("category", contact.get(0));
                    element.put("name", contact.get(1));
                    element.put("phone", contact.get(2));
                    element.put("email", info);
                    contactsList.add((HashMap<String,String>) element.clone());
                    info = "";
                    contact.clear();
                    element.clear();
                }
            }

            name_editText.setText(contactsList.get(position_modified).get("name"));
            phone_editText.setText(contactsList.get(position_modified).get("phone"));
            email_editText.setText(contactsList.get(position_modified).get("email"));

            if (input != null) {
                input.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteContact(int pos){
        try {
            input = openFileInput ("CONTACTS");
            int character;
            String info = "";
            HashMap<String, String> element = new HashMap<String, String>();;
            ArrayList<String> contact = new ArrayList<String>();
            contactsList.clear();

            while ((character = input.read()) != -1) {
                if (((char) character) != '\n') {
                    if (((char) character) != ',') {
                        info += (char) character;
                    }else {
                        contact.add(info);
                        info = "";
                    }
                }else{
                    element.put("category", contact.get(0));
                    element.put("name", contact.get(1));
                    element.put("phone", contact.get(2));
                    element.put("email", info);
                    contactsList.add((HashMap<String,String>) element.clone());
                    info = "";
                    contact.clear();
                    element.clear();

                }
            }


            if (input != null) {
                input.close();
            }

            deleteFile("CONTACTS");
            OutputStream output = openFileOutput("CONTACTS",MODE_PRIVATE);

            for (int i = 0; i < contactsList.size(); i ++){
                if (i != pos) {
                    String toWrite = contactsList.get(i).get("category") + "," + contactsList.get(i).get("name") + "," + contactsList.get(i).get("phone") + "," + contactsList.get(i).get("email") + "\n";
                    output.write(toWrite.getBytes());
                }
            }

            if (output != null) {
                output.close();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
