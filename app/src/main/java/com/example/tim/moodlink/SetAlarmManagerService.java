package com.example.tim.moodlink;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class SetAlarmManagerService extends Service {



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

        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);


        Intent broadcastIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent alarmIntent = null;


        switch (intent.getIntExtra("TO_LAUNCH",AlarmReceiver.NO_SERVICES)) {
            case AlarmReceiver.NO_SERVICES :
                this.stopSelf();
                break;
            case AlarmReceiver.ALL_SERVICES :
                alarmIntent = PendingIntent.getBroadcast(SetAlarmManagerService.this, 0, broadcastIntent, 0);
                broadcastIntent.putExtra("TO_LAUNCH",AlarmReceiver.ALL_SERVICES);
                break;
            case AlarmReceiver.LIGHT_SERVICE :
                alarmIntent = PendingIntent.getBroadcast(SetAlarmManagerService.this, 1, broadcastIntent, 0);
                broadcastIntent.putExtra("TO_LAUNCH",AlarmReceiver.LIGHT_SERVICE);
                break;
            case AlarmReceiver.CAMERA_SERVICE :
                alarmIntent = PendingIntent.getBroadcast(SetAlarmManagerService.this, 2, broadcastIntent, 0);
                broadcastIntent.putExtra("TO_LAUNCH",AlarmReceiver.CAMERA_SERVICE);
                break;
        }

        alarmMgr.setRepeating(
                AlarmManager.RTC,
                System.currentTimeMillis(),
                60000,
                alarmIntent
        );

        return super.onStartCommand(intent, flags, startId);
    }
}
