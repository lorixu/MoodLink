package com.example.tim.moodlink;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

// TODO : Set right elapse time between two data collect

public class SetAlarmManagerService extends Service {

    public static String TAG = "ALARM MANAGER SERVICE";

    public static final String START_SERVICE = "START";
    public static final String STOP_SERVICE = "STOP";
    public static final String SERVICE_ID = "SERVICE_ID";

    AlarmManager alarmMgr = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);


            Intent broadcastLightIntent = new Intent(this, AlarmReceiver.class);
            broadcastLightIntent.putExtra(SERVICE_ID, AlarmReceiver.LIGHT_SERVICE);
            PendingIntent alarmLightIntent;
            alarmLightIntent = PendingIntent.getBroadcast(SetAlarmManagerService.this, AlarmReceiver.LIGHT_SERVICE, broadcastLightIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent broadcastCameraIntent = new Intent(this, AlarmReceiver.class);
            broadcastCameraIntent.putExtra(SERVICE_ID, AlarmReceiver.CAMERA_SERVICE);
            PendingIntent alarmCameraIntent;
            alarmCameraIntent = PendingIntent.getBroadcast(SetAlarmManagerService.this, AlarmReceiver.CAMERA_SERVICE, broadcastCameraIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SetAlarmManagerService.this);
            SharedPreferences.Editor edit = prefs.edit();


            switch (intent.getAction()) {
                case START_SERVICE:
                    switch (intent.getIntExtra(SERVICE_ID, AlarmReceiver.NO_SERVICES)) {
                        case AlarmReceiver.NO_SERVICES:
                            this.stopSelf();

                            break;

                        case AlarmReceiver.ALL_SERVICES:
                            Log.d(TAG, "onStartCommand: START All Services");
                            // Starting Light Service
                            setAlarmRepeating(alarmLightIntent);

                            //Starting Camera Service
                            setAlarmRepeating(alarmCameraIntent);

                            edit.putBoolean(getString(R.string.PREF_LIGHT_SERVICE_STARTED), Boolean.TRUE);
                            edit.putBoolean(getString(R.string.PREF_CAMERA_SERVICE_STARTED), Boolean.TRUE);

                            break;

                        case AlarmReceiver.LIGHT_SERVICE:
                            setAlarmRepeating(alarmLightIntent);
                            edit.putBoolean(getString(R.string.PREF_LIGHT_SERVICE_STARTED), Boolean.TRUE);


                            break;

                        case AlarmReceiver.CAMERA_SERVICE:
                            setAlarmRepeating(alarmCameraIntent);
                            edit.putBoolean(getString(R.string.PREF_CAMERA_SERVICE_STARTED), Boolean.TRUE);

                            break;
                    }
                    break;
                case STOP_SERVICE:
                    switch (intent.getIntExtra(SERVICE_ID, AlarmReceiver.NO_SERVICES)) {
                        case AlarmReceiver.NO_SERVICES:
                            this.stopSelf();

                            break;

                        case AlarmReceiver.ALL_SERVICES:
                            Log.d(TAG, "onStartCommand: STOP All Services");
                            // Stopping Light Service
                            alarmMgr.cancel(alarmLightIntent);

                            //Stopping Camera Service
                            alarmMgr.cancel(alarmCameraIntent);


                            edit.putBoolean(getString(R.string.PREF_LIGHT_SERVICE_STARTED), Boolean.FALSE);
                            edit.putBoolean(getString(R.string.PREF_CAMERA_SERVICE_STARTED), Boolean.FALSE);

                            break;

                        case AlarmReceiver.LIGHT_SERVICE:
                            alarmMgr.cancel(alarmLightIntent);
                            Log.d(TAG, "onStartCommand: STOP Light Service");

                            edit.putBoolean(getString(R.string.PREF_LIGHT_SERVICE_STARTED), Boolean.FALSE);

                            break;

                        case AlarmReceiver.CAMERA_SERVICE:
                            alarmMgr.cancel(alarmCameraIntent);
                            Log.d(TAG, "onStartCommand: STOP Camera Service");

                            edit.putBoolean(getString(R.string.PREF_CAMERA_SERVICE_STARTED), Boolean.FALSE);

                            break;
                    }
                    break;
            }

            edit.apply();

        }
        return super.onStartCommand(intent, flags, startId);

    }

    public void setAlarmRepeating(PendingIntent intentToSend) {
        alarmMgr.setRepeating(
                AlarmManager.RTC,
                System.currentTimeMillis(),
                60000,
                intentToSend
        );
    }
}
