package com.example.tim.moodlink;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class EmergencyContactsActivity extends AppCompatActivity {

    public static final String TAG = "EmergencyContacts";

    private FileInputStream input = null;

    private ArrayList<HashMap<String, String>> contactsList = new ArrayList<>();

    private SQLLiteDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        db = new SQLLiteDBHelper(getApplicationContext());

        // Display of contacts

        displayContacts();


        // Handling of the action bar buttons

        ImageButton return_button = (ImageButton) findViewById(R.id.return_button);
        ImageButton add_contact_button = (ImageButton) findViewById(R.id.add_contact_button);

        return_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EmergencyContactsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        add_contact_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EmergencyContactsActivity.this, AddContactActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void displayContacts() {

        ArrayList<ContactData> contactItemsList = (ArrayList) db.getAllContactDatas();

        contactsList.clear();

        for (ContactData contactItem : contactItemsList) {
            HashMap<String, String> element = new HashMap<>();

            element.put("category", contactItem.getCategory());
            element.put("name", contactItem.getName());
            element.put("phone", contactItem.getPhone());
            element.put("email", contactItem.getEmail());
            element.put("ID",Long.toString(contactItem.getId()));

            contactsList.add((HashMap<String, String>) element.clone());
        }


        ListAdapter adapter = new SimpleAdapter(this,
                contactsList,
                R.layout.contact_item,
                new String[]{"name","ID", "category", "phone", "email"},
                new int[]{R.id.text1, R.id.contactIDInvisibleTextView, R.id.text2, R.id.text3, R.id.text4});

        ListView myList = (ListView) findViewById(R.id.contacts_listView);

        myList.setAdapter(adapter);

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {

                TextView contactIDTextView = ((TextView) view.findViewById(R.id.contactIDInvisibleTextView));
                Log.d(TAG, "onItemLongClick: contact id = "+contactIDTextView.getText().toString() );
                final long contactID  = Long.valueOf(contactIDTextView.getText().toString()).longValue();

                AlertDialog.Builder builder = new AlertDialog.Builder(EmergencyContactsActivity.this);
                builder.setItems(R.array.contacts_longclick_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 1) { //Delete click performed

                            db.deleteContact(contactID);
                            displayContacts();
                        } else if (which == 0) {
                            Intent intent = new Intent(EmergencyContactsActivity.this, ModifyContactActivity.class);
                            intent.putExtra("contact_id", contactID);
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
    }

}
