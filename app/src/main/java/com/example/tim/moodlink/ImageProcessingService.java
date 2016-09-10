package com.example.tim.moodlink;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ImageProcessingService extends Service {

    public static String TAG = "IMAGE PROC SERVICE";

    public static final int _ANGER = 1;
    public static final int _DISGUST = 2;
    public static final int _HAPPINESS = 3;
    public static final int _NEUTRALITY = 4;
    public static final int _SADNESS = 5;
    public static final int _SURPRISE = 6;

    private SQLLiteDBHelper db;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");

        db = new SQLLiteDBHelper(getApplicationContext());

        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //boolean recognizerTrained = prefs.getBoolean(getString(R.string.PREF_RECOGNIZER_TRAINED), false);
        //Log.d(TAG, "onCreate: Recognizer trained " + recognizerTrained);
        //if (!recognizerTrained) {
        //    trainRecognizer();
        //}

        ArrayList<CameraData> cameraDatas = (ArrayList<CameraData>) db.getAllCameraDatas();
        int label = 0;
        for (CameraData data : cameraDatas) {
            if (!data.isProcessed()) {
                String val;
                URL url = null;
                HttpURLConnection connection = null;
                try {
                    url = new URL("35.16.67.135/moodlink/test.php?value=android");
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setDoOutput(true);
                    connection.setChunkedStreamingMode(0);

                    InputStream in = connection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int nw_data = isw.read();
                    while (nw_data != -1) {
                        char current = (char) nw_data;
                        nw_data = isw.read();
                        System.out.print(current);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }

        this.stopSelf();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        db.close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}



