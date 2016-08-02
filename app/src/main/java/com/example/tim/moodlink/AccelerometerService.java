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


public class AccelerometerService extends Service {

    public static String TAG = "ACCELEROMETER SERVICE";

    protected int[] lastValues = new int[]{0,0,0};
    private SQLLiteDBHelper db;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: Service created!");
        db = new SQLLiteDBHelper(getApplicationContext());
        AccelerometerSensor aSensor = new AccelerometerSensor();
        aSensor.trackMovements();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: Service stopped!");
        db.close();
    }


    private class AccelerometerSensor implements SensorEventListener {

        public Handler handler = null;
        public Runnable unregisterRunnable = null;

        private SensorManager mSensor;

        public void trackMovements() {
            mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
            Sensor accelerometerSensor = mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            handler = new Handler();

            unregisterRunnable = new Runnable() {
                @Override
                public void run() {
                    mSensor.unregisterListener(AccelerometerService.AccelerometerSensor.this);


                    AccelerometerService.this.stopSelf();
                }
            };

            mSensor.registerListener(AccelerometerSensor.this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);


            handler.postDelayed(unregisterRunnable, 10000);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            int[] values = new int[]{(int)event.values[0],(int)event.values[1],(int)event.values[2]};
            if ((lastValues[0] - 2 > values[0] || lastValues[0] + 2 < values[0]) ||
                    (lastValues[1] - 2 > values[1] || lastValues[1] + 2 < values[1]) ||
                    (lastValues[2] - 2 > values[2] || lastValues[2] + 2 < values[2]) ) {

                AccelerometerData accelerometerDataItem = new AccelerometerData(
                        values[0],
                        values[1],
                        values[2],
                        new TimeAndDay(Calendar.getInstance())
                );
                db.addAccelerometerTuple(accelerometerDataItem);

                Toast.makeText(AccelerometerService.this,
                        "Values Accelerometer :" +
                                // Acceleration on x axis
                                Integer.toString(values[0]) + "," +
                                // Acceleration on y axis
                                Integer.toString(values[1]) + "," +
                                // Acceleration on z axis
                                Integer.toString(values[2]),
                        Toast.LENGTH_SHORT)
                        .show();

                lastValues = values;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }


    public static void launchAccelerometerService(Context context) {
        Intent accelerometerServiceIntent = new Intent(context, AccelerometerService.class);
        Log.d(TAG, "launchAccelerometerService: " + context.startService(accelerometerServiceIntent));
    }
}