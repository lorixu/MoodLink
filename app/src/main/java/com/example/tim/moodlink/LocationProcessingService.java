package com.example.tim.moodlink;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;


public class LocationProcessingService extends Service {

    public static String TAG = "LOC PROC SERVICE";

    private SQLLiteDBHelper db;


    private final IBinder binder = new LocalBinder();

    public int getMoodValue() {
        int mood_value = processLocationData();
        return mood_value;
    }

    public class LocalBinder extends Binder {
        LocationProcessingService getService() {
            return LocationProcessingService.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    private int processLocationData() {


        db = new SQLLiteDBHelper(getApplicationContext());

        UserData user = db.getUserData();
        int mood_value;

        Location userHome = new Location("");
        userHome.setLatitude(user.getHome().latitude);
        userHome.setLongitude(user.getHome().longitude);


        Location userWork = null;
        if (user.getWork() != null) {
            userWork = new Location("");
            userWork.setLatitude(user.getWork().latitude);
            userWork.setLongitude(user.getWork().longitude);
        }

        ArrayList<LocationData> locationDataList = (ArrayList<LocationData>) db.getAllLocationDatas();

        int close_places = 0;
        int near_places = 0;
        int far_places = 0;
        int vacation_places = 0;

        for (LocationData locationData : locationDataList) {

            Location loc = new Location("");
            loc.setLatitude(locationData.getLatitude());
            loc.setLongitude(locationData.getLongitude());

            float distance;

            if (userWork != null) {
                if (userHome.distanceTo(loc) < userWork.distanceTo(loc)) {
                    distance = userHome.distanceTo(loc);
                } else {
                    distance = userWork.distanceTo(loc);
                }
            } else {
                distance = userHome.distanceTo(loc);
            }
            if (distance < 1000) {
                close_places++;
            } else if (distance < 5000) {
                near_places++;
            } else if (distance < 50000) {
                far_places++;
            } else {
                vacation_places++;
            }
        }

        if (far_places >= 1) {
            mood_value = 10;
        } else {
            if (near_places >= 5) {
                mood_value = 10;
            } else {
                if (near_places == 0) {
                    mood_value = -1;
                } else {
                    mood_value = near_places * 2;
                }
            }
        }

        return mood_value;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: ");
        if (db != null) {
            db.close();
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
