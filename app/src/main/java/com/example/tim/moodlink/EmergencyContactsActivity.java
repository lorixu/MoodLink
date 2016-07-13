package com.example.tim.moodlink;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmergencyContactsActivity extends AppCompatActivity {

    private ImageButton return_button;
    private ImageButton add_contact_button;

    private FileInputStream input = null;

    private List<HashMap<String, String>> contactsList = new ArrayList<HashMap<String, String>>();
    private ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        // Display of contacts

        displayContacts();


        // Handling of the action bar buttons

        return_button = (ImageButton) findViewById(R.id.return_button);
        add_contact_button = (ImageButton) findViewById(R.id.add_contact_button);

        return_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(EmergencyContactsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        add_contact_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(EmergencyContactsActivity.this, AddContactActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void displayContacts(){
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
                    element.put("text1", contact.get(0));
                    element.put("text2", contact.get(1));
                    element.put("text3", contact.get(2));
                    element.put("text4", info);
                    contactsList.add((HashMap<String,String>) element.clone());
                    info = "";
                    contact.clear();
                    element.clear();

                }
            }


            ListAdapter adapter = new SimpleAdapter(this,
                    contactsList,
                    R.layout.contact_item,
                    new String[] {"text1", "text2","text3","text4"},
                    new int[] {R.id.text2, R.id.text1,R.id.text3, R.id.text4});

            myList = (ListView) findViewById(R.id.contacts_listView);

            myList.setAdapter(adapter);

            myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(EmergencyContactsActivity.this);
                    builder.setItems(R.array.contacts_longclick_options, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 1) { //Delete click performed
                                deleteContact(pos);
                            }else if (which == 0) {
                                Intent intent = new Intent(EmergencyContactsActivity.this, ModifyContactActivity.class);
                                intent.putExtra("position", pos);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return true;
                }
            });

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
        displayContacts();
    }
}
