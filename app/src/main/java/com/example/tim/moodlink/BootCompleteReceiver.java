package com.example.tim.moodlink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

    public static String TAG = "BOOT RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context,"Boot event received",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onReceive: Boot Received!");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(context.getString(R.string.PREF_PHONE_RECEIVER_ACTIVATED),Boolean.TRUE);
        edit.apply();

        Intent setAlarmManagerIntent = new Intent(context, SetAlarmManagerService.class);
        setAlarmManagerIntent.setAction(SetAlarmManagerService.START_SERVICE);
        setAlarmManagerIntent.putExtra(SetAlarmManagerService.SERVICE_ID,AlarmReceiver.ALL_SERVICES);
        context.startService(setAlarmManagerIntent);
    }
}
