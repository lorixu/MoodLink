package com.example.tim.moodlink;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class LightProcessingService extends Service {

    public static String TAG = "LIGHT PROC SERVICE";

    private static final int LIGHTING_DARK = 0;
    private static final int LIGHTING_DIM = 1;
    private static final int LIGHTING_BRIGHT = 2;

    private SQLLiteDBHelper db;
    final int[] values = new int[3];

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");

        //final CountDownLatch latch = new CountDownLatch(1);


        Thread t = new Thread("LightProcessingService") {
            @Override
            public void run() {
                db = new SQLLiteDBHelper(getApplicationContext());
                processLightData();
                db.close();
                stopSelf();
            }
        };
        t.start();
        try {
            t.join();
            Toast.makeText(LightProcessingService.this, "Dark = " + values[0] + "% Dim = " + values[1] + "% Bright = " + values[2] + "%", Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.stopSelf();

    }

    public void processLightData() {
        ArrayList<LightData> lightDatas = (ArrayList<LightData>) db.getAllLightDatas();

        int dark_inSecond = 0;
        int dim_inSecond = 0;
        int bright_inSecond = 0;

        int lastLevel = -1;
        TimeAndDay lastTime = null;

        for (LightData data : lightDatas) {

            switch (lastLevel) {
                case -1:
                    lastLevel = lightLevel(data);
                    lastTime = data.getTimeStamp();

                case LIGHTING_DARK:
                    if (data.getTimeStamp().elapsedSince(lastTime) < 120)
                        dark_inSecond += data.getTimeStamp().elapsedSince(lastTime);

                    lastLevel = lightLevel(data);
                    lastTime = data.getTimeStamp();
                    break;
                case LIGHTING_DIM:
                    if (data.getTimeStamp().elapsedSince(lastTime) < 120)
                        dim_inSecond += data.getTimeStamp().elapsedSince(lastTime);

                    lastLevel = lightLevel(data);
                    lastTime = data.getTimeStamp();
                    break;
                case LIGHTING_BRIGHT:
                    if (data.getTimeStamp().elapsedSince(lastTime) < 120)
                        bright_inSecond += data.getTimeStamp().elapsedSince(lastTime);

                    lastLevel = lightLevel(data);
                    lastTime = data.getTimeStamp();
                    break;
            }
        }

        int totalTime = dark_inSecond + dim_inSecond + bright_inSecond;
        if (totalTime != 0) {
            values[0] = (dark_inSecond * 100) / totalTime;
            values[1] = (dim_inSecond * 100) / totalTime;
            values[2] = (bright_inSecond * 100) / totalTime;
        }
    }

    private int lightLevel(LightData data) {
        if (data.getLuxValue() < 15) return LIGHTING_DARK;
        else if (data.getLuxValue() < 100) return LIGHTING_DIM;
        else return LIGHTING_BRIGHT;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
