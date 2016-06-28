package com.example.tim.moodlink;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity {

    private DrawerLayout myDrawer;
    private String[] drawerItemsList;
    private ListView myDrawerList;
    private ImageButton drawerOpenButton;
    private ImageButton drawerCloseButton;
    private LinearLayout emergencyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Drawer launch
        drawerItemsList = getResources().getStringArray(R.array.items);
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



    private void openSideDrawer(){
        myDrawer.openDrawer(Gravity.LEFT);


        drawerCloseButton = (ImageButton) findViewById(R.id.imageButton_DrawerOpened);
        drawerCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSideDrawer();
            }
        });

        emergencyButton = (LinearLayout) findViewById(R.id.block_emergency);

        emergencyButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmergencyContactsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void closeSideDrawer(){
        myDrawer.closeDrawer(Gravity.LEFT);
        drawerCloseButton = null;
        emergencyButton = null;
    }
}
