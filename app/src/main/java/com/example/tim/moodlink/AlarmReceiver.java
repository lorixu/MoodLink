package com.example.tim.moodlink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    public static String TAG = "ALARM RECEIVER";

    public static final int NO_SERVICES = -1;
    public static final int ALL_SERVICES = 0;
    public static final int LIGHT_SERVICE = 1;
    public static final int CAMERA_SERVICE = 2;

    @Override
    public void onReceive(Context contextReceived, Intent intent) {

        switch (intent.getIntExtra("SERVICE_ID",NO_SERVICES)) {
            case NO_SERVICES :

                break;
            case LIGHT_SERVICE :
                Log.d(TAG, "onReceive: Light Alarm Received!");
                LightService.launchLightService(contextReceived);
                break;
            case CAMERA_SERVICE :
                Log.d(TAG, "onReceive: Camera Alarm Received!");
                CameraService.launchCameraService(contextReceived);
                break;
        }


    }
}
