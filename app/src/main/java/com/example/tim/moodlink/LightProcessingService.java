package com.example.tim.moodlink;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class LightProcessingService extends Service {

    public static String TAG = "LIGHT PROC SERVICE";

    private static final int LIGHTING_DARK = 0;
    private static final int LIGHTING_DIM = 1;
    private static final int LIGHTING_BRIGHT = 2;

    private final IBinder binder = new LocalBinder();

    private SQLLiteDBHelper db;

    public class LocalBinder extends Binder {
        LightProcessingService getService() {
            return LightProcessingService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    public int getMoodValue(){
        db = new SQLLiteDBHelper(getApplicationContext());
        int[] values = processLightData();

        int res;

        if (values[0] != -1 && values[1] != -1 && values[2] != -1){
            res = ((0*values[0]) + (5*values[1]) + (10*values[2])) / 100;
        }else{
            res = -1;
        }

        return res;
    }

    private int[] processLightData() {
        ArrayList<LightData> lightDatas = (ArrayList<LightData>) db.getAllLightDatas();

        final int[] values = new int[3];

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
        }else{

            values[0] = -1;
            values[1] = -1;
            values[2] = -1;
        }

        return values;
    }

    private int lightLevel(LightData data) {
        if (data.getLuxValue() < 15) return LIGHTING_DARK;
        else if (data.getLuxValue() < 100) return LIGHTING_DIM;
        else return LIGHTING_BRIGHT;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
        Log.d(TAG, "onDestroy: ");
    }
}
