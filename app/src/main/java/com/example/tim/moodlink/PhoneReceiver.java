package com.example.tim.moodlink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.util.Calendar;

public class PhoneReceiver extends BroadcastReceiver {

    private static final String TAG = "PHONE RECEIVER";
    static MediaRecorder recorder = null;
    static TelephonyManager telManager = null;
    Boolean recordStarted = false;

    private SQLLiteDBHelper db;


    @Override
    public void onReceive(Context context, Intent intent) {
        recorder = new MediaRecorder();
        telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        db = new SQLLiteDBHelper(context.getApplicationContext());

        Log.d(TAG, "onReceive: Phone call");
        // Phone call recording
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            try {
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

                Log.d(TAG, "onReceive: Recording");
                File file = new File(context.getString(R.string.phone_recorder_storage));
                //noinspection ResultOfMethodCallIgnored
                file.mkdirs();

                Calendar c = Calendar.getInstance();
                String month = Integer.toString(c.get(Calendar.MONTH));
                if (month.length() == 1) month = "0" + month;
                String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
                if (day.length() == 1) day = "0" + day;
                String year = Integer.toString(c.get(Calendar.YEAR));
                String hour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
                if (hour.length() == 1) hour = "0" + hour;
                String minute = Integer.toString(c.get(Calendar.MINUTE));
                if (minute.length() == 1) minute = "0" + minute;

                String fileName = month + day + year + "_" + hour + minute + ".mp3";

                file = new File(context.getString(R.string.phone_recorder_storage), fileName);

                PhoneData phoneDataItem = new PhoneData(
                        context.getString(R.string.phone_recorder_storage)+"/"+fileName,
                        new TimeAndDay(Calendar.getInstance())
                );
                db.addPhoneTuple(phoneDataItem);

                recorder.setOutputFile(file.getPath());
                recorder.prepare();
                recorder.start();
                recordStarted = true;

                telManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
            } catch (Exception ignored) {

            }
        }
    }



    private final PhoneStateListener phoneListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING: {
                        //
                        break;
                    }
                    case TelephonyManager.CALL_STATE_OFFHOOK: {
                        //
                        break;
                    }
                    case TelephonyManager.CALL_STATE_IDLE: {
                        if (recordStarted) {
                            recorder.stop();
                            recordStarted = false;
                            db.close();
                        }
                        break;
                    }
                    default: {
                    }
                }
            } catch (Exception ignored) {
            }
        }
    };
}
