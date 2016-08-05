package com.example.tim.moodlink;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class ImageProcessingService extends Service {

    public static String TAG = "IMAGE PROC SERVICE";

    private SQLLiteDBHelper db;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
            Log.e(TAG, "static initializer: Error", null);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");

        db = new SQLLiteDBHelper(getApplicationContext());

        ArrayList<CameraData> cameraDatas = (ArrayList<CameraData>) db.getAllCameraDatas();

        for (CameraData data : cameraDatas) {
            if (!data.isProcessed()) {
                if(detectFace(data.getPath())) {
                    //detectEmotion(data.getPath());
                    db.updateCameraData(data.getId(), new CameraData(data.getPath(), data.getTimeStamp(), true));
                }else{
                    db.deleteCameraData(data.getId());
                }
            }
        }
    }



    private boolean detectFace(String picturePath) {
        InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
        File cascadeDir = getDir("cascade", ImageProcessingService.MODE_PRIVATE);
        File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");

        try {
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        CascadeClassifier faceDetector = new CascadeClassifier(mCascadeFile.getPath());
        Mat image = Imgcodecs
                .imread(picturePath);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        Mat croppedImage = new Mat();

        if (faceDetections.toArray().length == 1) {
            Mat croppedRef = new Mat(image, faceDetections.toArray()[0]);
            croppedRef.copyTo(croppedImage);
            new File(picturePath).delete();
            Imgcodecs.imwrite(picturePath, croppedImage);
            return true;
        } else {
            new File(picturePath).delete();
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
