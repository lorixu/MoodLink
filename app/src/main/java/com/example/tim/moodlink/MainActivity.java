package com.example.tim.moodlink;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {

    private DrawerLayout myDrawer;
    private String[] drawerItemsList;
    private ListView myDrawerList;
    private ImageButton drawerOpenButton;
    private ImageButton drawerCloseButton;
    private LinearLayout emergencyButton;
    private LinearLayout graphButton;
    private LinearLayout settingsButton;
    private int  MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Starting Service

        Intent serviceIntent = new Intent(this,MyIntentService.class);
        this.startService(serviceIntent);

        //Permission for accessing the camera (and the external storage)

        int[] permissionsCheck= new int[]{
                ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA),
                ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)};

        if (permissionsCheck[1] == PackageManager.PERMISSION_DENIED || permissionsCheck[1] == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        // Checking if it's a first launch
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            Intent intent = new Intent(MainActivity.this, FirstLaunchActivity.class);
            startActivity(intent);
            finish();
        }

        // Lecture du fichier
        FileInputStream input = null;

        try {
            input = openFileInput ("USERNAME");
            int character;
            String fileContent = "";
            while ((character = input.read()) != -1) {
                fileContent += (char) character;
            }

            TextView welcomeText = (TextView) findViewById(R.id.welcome_textView);
            Resources res = getResources();
            welcomeText.setText(res.getString(R.string.welcome,fileContent));

            if (input != null) {
                input.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Drawer launch
        drawerItemsList = getResources().getStringArray(R.array.items_drawer);
        myDrawerList = (ListView) findViewById(R.id.my_drawer);
        myDrawerList.setAdapter(new ArrayAdapter<String>(this,
               R.layout.drawer_item,R.id.text1, drawerItemsList));

        // Drawer Buttons

        drawerOpenButton = (ImageButton) findViewById(R.id.imageButton_DrawerClosed);

        drawerOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSideDrawer();
            }
        });

        Log.d("Debug Print",Integer.toString( myDrawerList.getChildCount()));


    }



    private void openSideDrawer() {
        myDrawer.openDrawer(Gravity.LEFT);

        // Getting drawer elements
        drawerCloseButton = (ImageButton) findViewById(R.id.imageButton_DrawerOpened);
        emergencyButton = (LinearLayout) findViewById(R.id.block_emergency);
        graphButton = (LinearLayout) findViewById(R.id.block_graph);
        settingsButton = (LinearLayout) findViewById(R.id.block_settings);


        // Actions on drawer elements
        drawerCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSideDrawer();
            }
        });

        emergencyButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmergencyContactsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        graphButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                startActivity(intent);
                finish();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void closeSideDrawer(){
        myDrawer.closeDrawer(Gravity.LEFT);
        drawerCloseButton = null;
        emergencyButton = null;
        graphButton = null;
        settingsButton = null;
    }
}
