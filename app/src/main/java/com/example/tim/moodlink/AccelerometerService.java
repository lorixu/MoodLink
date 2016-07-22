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


public class AccelerometerService extends Service {

    public static String TAG = "ACCELEROMETER SERVICE";

    protected double[] lastValues = new double[]{0,0,0};

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: Service created!");
        AccelerometerSensor aSensor = new AccelerometerSensor();
        aSensor.trackMovements();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: Service stopped!");
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

            double[] values = new double[]{truncateDouble(event.values[0]),truncateDouble(event.values[1]),truncateDouble(event.values[2])};
            if ((lastValues[0] - 1 > values[0] || lastValues[0] + 1 < values[0]) ||
                    (lastValues[1] - 1 > values[1] || lastValues[1] + 1 < values[1]) ||
                    (lastValues[2] - 1 > values[2] || lastValues[2] + 1 < values[2]) ) {

                Log.d(TAG, "Values Accelerometer :" +
                        // Acceleration on x axis
                        Double.toString(values[0]) + "," +
                        // Acceleration on y axis
                        Double.toString(values[1]) + "," +
                        // Acceleration on z axis
                        Double.toString(values[2]));

                Toast.makeText(AccelerometerService.this,
                        "Values Accelerometer :" +
                                // Acceleration on x axis
                                Double.toString(values[0]) + "," +
                                // Acceleration on y axis
                                Double.toString(values[1]) + "," +
                                // Acceleration on z axis
                                Double.toString(values[2]),
                        Toast.LENGTH_SHORT)
                        .show();

                lastValues = values;
            }
        }

        double truncateDouble(double nb){
            int coef = 1;
            return Math.floor(nb * coef) / coef;
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