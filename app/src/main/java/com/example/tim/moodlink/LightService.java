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




public class LightService extends Service {

    public static String TAG = "LIGHT SERVICE";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        LightSensor lSensor = new LightSensor();
        lSensor.getAverageLuminosity();

        Log.d(TAG, "onCreate: Service created!");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: Service stopped!");
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
                    writeToFile();
                    Toast.makeText(LightService.this, "Average Luminosity : " + average(luxValues), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Avg luminosity = "+average(luxValues)+" lux");
                    LightService.this.stopSelf();
                }
            };

            mSensor.registerListener(LightSensor.this, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);


            handler.postDelayed(unregisterRunnable, 10000);
        }

        public float average(ArrayList<Float> list)
        {
            double average = 0.0;
            for (int i = 0; i < list.size(); i++)  {
                average += list.get(i) ;
            }
            return (float) average/list.size();
        }

        private void writeToFile(){

            FileOutputStream output;

            try {
                output = openFileOutput(getString(R.string.light_values_file), MODE_APPEND);
                output.write((Float.toString(average(luxValues))+',').getBytes());
                output.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
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