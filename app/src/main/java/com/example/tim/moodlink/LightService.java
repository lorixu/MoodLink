package com.example.tim.moodlink;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class LightService extends Service {

    public static String TAG = "LIGHT SERVICE";

    private SQLLiteDBHelper db;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        db = new SQLLiteDBHelper(getApplicationContext());

        LightSensor lSensor = new LightSensor();
        lSensor.getAverageLuminosity();

        Log.d(TAG, "onCreate: Service created!");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: Service stopped!");
        db.close();
    }



    private class LightSensor implements SensorEventListener {

        public Handler handler = null;
        public Runnable unregisterRunnable = null;

        private SensorManager mSensor;

        private ArrayList<Float> luxValues;

        public void getAverageLuminosity() {
            mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
            Sensor lightSensor = mSensor.getDefaultSensor(Sensor.TYPE_LIGHT);
            luxValues = new ArrayList<>();

            handler = new Handler();

            unregisterRunnable = new Runnable() {
                @Override
                public void run() {
                    mSensor.unregisterListener(LightSensor.this);
                    saveData();
                    Toast.makeText(LightService.this, "Average Luminosity : " + average(luxValues), Toast.LENGTH_SHORT).show();
                    LightService.this.stopSelf();
                }
            };

            mSensor.registerListener(LightSensor.this, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);


            handler.postDelayed(unregisterRunnable, 10000);
        }

        public int average(ArrayList<Float> list)
        {
            double average = 0.0;
            for (int i = 0; i < list.size(); i++)  {
                average += list.get(i) ;
            }
            return (int) average/list.size();
        }

        private void saveData(){
            LightData lightDataItem = new LightData(
                    average(luxValues),
                    new TimeAndDay(Calendar.getInstance())
            );
            db.addLightTuple(lightDataItem);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            luxValues.add(event.values[0]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }


    public static void launchLightService(Context context){
        Intent luminosityServiceIntent = new Intent(context, LightService.class);
        context.startService(luminosityServiceIntent);
    }
}