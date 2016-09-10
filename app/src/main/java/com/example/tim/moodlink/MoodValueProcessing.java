package com.example.tim.moodlink;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class MoodValueProcessing extends Service {

    public static final String TAG = "Mood Processing";

    private SQLLiteDBHelper db;

    LightProcessingService lightProcess;
    boolean lightBound = false;
    int lightMoodValue;
    private ServiceConnection lightConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Log.d(TAG, "onServiceConnected: BINDING");
            LightProcessingService.LocalBinder binder = (LightProcessingService.LocalBinder) service;
            lightProcess = binder.getService();
            lightBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            lightBound = false;
        }
    };

    LocationProcessingService locationProcess;
    boolean locationBound = false;
    private ServiceConnection locationConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocationProcessingService.LocalBinder binder = (LocationProcessingService.LocalBinder) service;
            locationProcess = binder.getService();
            locationBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            lightBound = false;
        }
    };

     private Runnable WorkTask = new Runnable(){

        public void run() {
            while (lightBound == false && locationBound == false) {
                try {
                    Thread.sleep(2000);
                    Log.d(TAG, "run:  waiting binding");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            db = new SQLLiteDBHelper(getApplicationContext());


            TimeAndDay today = new TimeAndDay(Calendar.getInstance());


            int lightValue = lightProcess.getMoodValue();
            int locationValue = locationProcess.getMoodValue();
            int moodValue;

            if (lightValue != -1 && locationValue != -1)
                moodValue = (lightValue + locationValue) / 2;
            else if (lightValue != -1)
                moodValue = lightValue;
            else if (locationValue != -1)
                moodValue = locationValue;
            else
                moodValue = -1;

            MoodData moodData  = db.getMoodDataByDay(today);
            Log.d(TAG, "run: moodData = "+ moodValue);
            if (moodData == null) {
                MoodData moodDataItem = new MoodData(
                        moodValue,
                        today
                );
                db.addMoodTuple(moodDataItem);

            }else{
                MoodData moodDataItem = new MoodData(
                        moodValue,
                        new TimeAndDay(Calendar.getInstance())
                );
                db.updateMoodData(moodData.getId(),moodDataItem);
            }
        }
    };

        @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");


        Intent lightIntent = new Intent(MoodValueProcessing.this, LightProcessingService.class);
        bindService(lightIntent, lightConnection, Context.BIND_AUTO_CREATE);

        Intent locationIntent = new Intent(MoodValueProcessing.this, LocationProcessingService.class);
        bindService(locationIntent, locationConnection, Context.BIND_AUTO_CREATE);

        Thread work = new Thread(WorkTask);
        work.start();

        this.stopSelf();
    }

    public static void launchMoodValueProcessingService(Context context) {
        Intent moodValueProcessingServiceIntent = new Intent(context, MoodValueProcessing.class);
        context.startService(moodValueProcessingServiceIntent);

    }
}
