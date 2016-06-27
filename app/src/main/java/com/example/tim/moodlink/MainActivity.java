package com.example.tim.moodlink;

import android.app.Activity;
import android.media.Image;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends Activity {

    private DrawerLayout myDrawer;
    private String[] drawerItemsList;
    private ListView myDrawerList;
    private ImageButton drawerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Drawer launch
        drawerItemsList = getResources().getStringArray(R.array.items);
        myDrawerList = (ListView) findViewById(R.id.my_drawer);
        myDrawerList.setAdapter(new ArrayAdapter<String>(this,
               R.layout.drawer_item, drawerItemsList));

        // Drawer Button

        drawerButton = (ImageButton) findViewById(R.id.imageButtonDrawer);
        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSideDrawer();
            }
        });
    }

    private void openSideDrawer(){
        myDrawer.openDrawer(Gravity.LEFT);
    }
}
