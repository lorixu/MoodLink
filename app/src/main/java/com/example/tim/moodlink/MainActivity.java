package com.example.tim.moodlink;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

// TODO : on boot verify services bool

public class MainActivity extends Activity {

    public static String TAG = "Main Activity";

    private DrawerLayout myDrawer;
    private ImageButton drawerCloseButton;
    private LinearLayout emergencyButton;
    private LinearLayout graphButton;
    private LinearLayout settingsButton;
    private Resources res;

    private SQLLiteDBHelper db;

    Handler handler = new Handler();
    int updateFrequency = 10000;
    Runnable updateUIRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run");
            setMoodDataUI();
            handler.postDelayed(updateUIRunnable, updateFrequency);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking if it's a first launch
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.PREF_PREVIOUSLY_STARTED), false);
        if (!previouslyStarted) {
            Intent intent = new Intent(MainActivity.this, FirstLaunchActivity.class);
            startActivity(intent);
            Log.d(TAG, "onCreate: First launched");
            MainActivity.this.finish();
        }

        setContentView(R.layout.activity_main);

        res = getApplicationContext().getResources();
        db = new SQLLiteDBHelper(getApplicationContext());

        //Permission for accessing the camera, the external storage ...

        checkPermissions();

        // Username reading

        TextView welcomeText = (TextView) findViewById(R.id.welcome_textView);
        welcomeText.setText(res.getString(R.string.welcome, db.getUserData().getUsername()));


        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Drawer launch
        String[] drawerItemsList = getResources().getStringArray(R.array.items_drawer);
        ListView myDrawerList = (ListView) findViewById(R.id.my_drawer);
        myDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_item, R.id.text1, drawerItemsList));

        // Drawer Buttons

        ImageButton drawerOpenButton = (ImageButton) findViewById(R.id.imageButton_DrawerClosed);

        drawerOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawer.openDrawer(Gravity.LEFT);
            }
        });

        myDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                setDrawerButtons();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                unsetDrawerButtons();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent moodValueProcessingServiceIntent = new Intent(this, MoodValueProcessing.class);
        this.startService(moodValueProcessingServiceIntent);
        updateUIRunnable.run();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateUIRunnable);
    }

    private void setDrawerButtons() {
        // Getting drawer elements
        drawerCloseButton = (ImageButton) findViewById(R.id.imageButton_DrawerOpened);
        emergencyButton = (LinearLayout) findViewById(R.id.block_emergency);
        graphButton = (LinearLayout) findViewById(R.id.block_graph);
        settingsButton = (LinearLayout) findViewById(R.id.block_settings);
        // Actions on drawer elements
        drawerCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawer.closeDrawer(Gravity.LEFT);
            }
        });
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmergencyContactsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        graphButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                startActivity(intent);
                finish();
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void unsetDrawerButtons() {
        drawerCloseButton = null;
        emergencyButton = null;
        graphButton = null;
        settingsButton = null;
    }

    void checkPermissions() {


        String[] permissionsArray = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.PROCESS_OUTGOING_CALLS,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};

        ArrayList<String> permissionsToAsk = new ArrayList<>();

        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(this,
                    permission) == PackageManager.PERMISSION_DENIED) {
                permissionsToAsk.add(permission);
            }
        }

        int MY_PERMISSIONS_REQUEST = 0;

        if (!permissionsToAsk.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsToAsk.toArray(new String[permissionsToAsk.size()]),
                    MY_PERMISSIONS_REQUEST);
        }
    }

    private void setMoodDataUI() {
        Log.d(TAG, "setMoodDataUI");
        TimeAndDay today = new TimeAndDay(Calendar.getInstance());

        if (db.getMoodDataByDay(today) != null) {

            int mood_value = db.getMoodDataByDay(today).getMoodValue();
            Log.d(TAG, "setMoodDataUI: mood value = " + mood_value);

            TextView legend = (TextView) findViewById(R.id.legend_textView);

            View dot;

            resetDots();
            switch (mood_value) {
                case 0:
                    legend.setText(res.getString(R.string.legend01));

                    dot = findViewById(R.id.scale1);
                    setDotSelected(dot);
                    break;

                case 1:
                    legend.setText(res.getString(R.string.legend01));

                    dot = findViewById(R.id.scale1);
                    setDotSelected(dot);
                    break;

                case 2:
                    legend.setText(res.getString(R.string.legend02));

                    dot = findViewById(R.id.scale2);
                    setDotSelected(dot);
                    break;

                case 3:
                    legend.setText(res.getString(R.string.legend03));

                    dot = findViewById(R.id.scale3);
                    setDotSelected(dot);
                    break;

                case 4:
                    legend.setText(res.getString(R.string.legend04));

                    dot = findViewById(R.id.scale4);
                    setDotSelected(dot);
                    break;

                case 5:
                    legend.setText(res.getString(R.string.legend05));
                    Log.d(TAG, "setMoodDataUI: text set");
                    dot = findViewById(R.id.scale5);
                    setDotSelected(dot);
                    break;

                case 6:
                    legend.setText(res.getString(R.string.legend06));

                    dot = findViewById(R.id.scale6);
                    setDotSelected(dot);
                    break;

                case 7:
                    legend.setText(res.getString(R.string.legend07));

                    dot = findViewById(R.id.scale7);
                    setDotSelected(dot);
                    break;

                case 8:
                    legend.setText(res.getString(R.string.legend08));

                    dot = findViewById(R.id.scale8);
                    setDotSelected(dot);
                    break;

                case 9:
                    legend.setText(res.getString(R.string.legend09));

                    dot = findViewById(R.id.scale9);
                    setDotSelected(dot);
                    break;

                case 10:
                    legend.setText(res.getString(R.string.legend10));

                    dot = findViewById(R.id.scale10);
                    setDotSelected(dot);
                    break;

                default:
                    legend.setText(res.getString(R.string.legend_empty));
                    break;

            }
            legend.postInvalidate();
        }
    }

    private void resetDots() {
        RelativeLayout dotsRelativeLayout = (RelativeLayout) findViewById(R.id.dotsRelativeLayout);

        int childCount = dotsRelativeLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View dot = dotsRelativeLayout.getChildAt(i);

            ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) dot.getLayoutParams();
            margin.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, res.getDisplayMetrics());
            margin.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, res.getDisplayMetrics());
            margin.leftMargin = margin.leftMargin - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, res.getDisplayMetrics());

            dot.setWillNotDraw(false);
            dot.postInvalidate();
        }
    }

    private void setDotSelected(View dot) {
        ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) dot.getLayoutParams();
        margin.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, res.getDisplayMetrics());
        margin.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, res.getDisplayMetrics());
        margin.leftMargin = margin.leftMargin + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, res.getDisplayMetrics());

        dot.setWillNotDraw(false);
        dot.postInvalidate();
    }
}

