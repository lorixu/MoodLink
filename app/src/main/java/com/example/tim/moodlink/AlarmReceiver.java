package com.example.tim.moodlink;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    public static final int NO_SERVICES = -1;
    public static final int ALL_SERVICES = 0;
    public static final int LIGHT_SERVICE = 1;
    public static final int CAMERA_SERVICE = 2;

    Context context = null;

    @Override
    public void onReceive(Context contextReceived, Intent intent) {

        context = contextReceived;

        switch (intent.getIntExtra("TO_LAUNCH",AlarmReceiver.NO_SERVICES)) {
            case AlarmReceiver.NO_SERVICES :

                break;
            case AlarmReceiver.ALL_SERVICES :
                launchLightService();
                launchCameraService();
                break;
            case AlarmReceiver.LIGHT_SERVICE :
                launchLightService();
                break;
            case AlarmReceiver.CAMERA_SERVICE :
                launchCameraService();
                break;
        }


    }

    void launchLightService(){
        Intent luminosityServiceIntent = new Intent(context, LightService.class);

        context.startService(luminosityServiceIntent);
    }

    void launchCameraService(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {return;
        }else{

            Intent cameraServiceIntent = new Intent(context, CameraService.class);

            context.startService(cameraServiceIntent);
        }
    }

}
