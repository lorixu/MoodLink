package com.example.tim.moodlink;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


            // TODO : Take Picture every hour
            // TODO : record phone calls
            // TODO : FIX EVENT LISTENER FOR LUX

public class MyIntentService extends Service implements SensorEventListener {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    private SensorManager mSensorManager;
    private Sensor mLight;




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service created!", Toast.LENGTH_SHORT).show();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                Log.d("SERVICE", "Service is still running");
                handler.postDelayed(runnable, 10000);
            }
        };

        handler.postDelayed(runnable, 10000);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
        mSensorManager.unregisterListener(this);
    }


    public void onStartCommand(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
        Log.d("SENSOR", mLight.toString());
        mSensorManager.registerListener(MyIntentService.this, mLight, SensorManager.SENSOR_DELAY_FASTEST);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("SENSOR", "Sensor changed");
        Toast.makeText(this, "SENSOR", Toast.LENGTH_SHORT).show();
        if( event.sensor.getType() == Sensor.TYPE_LIGHT)
        {
            Toast.makeText(this, "Luminosité : " + event.values[0], Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}