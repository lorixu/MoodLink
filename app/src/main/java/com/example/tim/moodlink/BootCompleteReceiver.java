package com.example.tim.moodlink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context,"Boot event received",Toast.LENGTH_SHORT).show();
        Log.d("BOOT RECEIVER", "Boot received");

        Intent setAlarmManagerIntent = new Intent(context, SetAlarmManagerService.class);
        setAlarmManagerIntent.putExtra("TO_LAUNCH",AlarmReceiver.ALL_SERVICES);
        context.startService(setAlarmManagerIntent);
    }
}
