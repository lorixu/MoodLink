package com.example.tim.moodlink;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.face.Face;
import org.opencv.face.FaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

// 2 problems
// TODO : Segfault on FaceRecognizer.train() JNI function
// TODO : Crash (memory over-consumption caused probably) on loading images to train the model

public class ImageProcessingService extends Service {

    public static String TAG = "IMAGE PROC SERVICE";

    public static final int _ANGER = 1;
    public static final int _DISGUST = 2;
    public static final int _HAPPINESS = 3;
    public static final int _NEUTRALITY = 4;
    public static final int _SADNESS = 5;
    public static final int _SURPRISE = 6;

    private SQLLiteDBHelper db;
    FaceRecognizer moodRecognizer;

    ArrayList<Mat> pictures = new ArrayList<Mat>();
    Mat labels = new Mat(2,1, CvType.CV_32SC1);

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean recognizerTrained = prefs.getBoolean(getString(R.string.PREF_RECOGNIZER_TRAINED), false);
        Log.d(TAG, "onCreate: Recognizer trained " + recognizerTrained);
        if (!recognizerTrained) {
            //trainRecognizer();
        }

        ArrayList<CameraData> cameraDatas = (ArrayList<CameraData>) db.getAllCameraDatas();
        int label = 0;
        for (CameraData data : cameraDatas) {
            if (!data.isProcessed()) {
                if (detectFace(data.getPath())) {
                     //label = moodRecognizer.predict_label(Imgcodecs.imread(data.getPath(),Imgcodecs.IMREAD_GRAYSCALE));
                    Log.d(TAG, "onCreate: picture " + data.getMinute() + " label = " + label);
                    //db.updateCameraData(data.getId(), new CameraData(data.getPath(), data.getTimeStamp(), true));
                } else {
                    db.deleteCameraData(data.getId());
                }
            }
        }

        this.stopSelf();
    }

    private void trainRecognizer() {

        // Getting the lists of different images

        String[][] picturesPaths = new String[][]{//getResources().getStringArray(R.array.anger_pictures),
                //getResources().getStringArray(R.array.disgust_pictures),
                getResources().getStringArray(R.array.happiness_pictures),
                //getResources().getStringArray(R.array.neutrality_pictures),
                getResources().getStringArray(R.array.sadness_pictures),
                //getResources().getStringArray(R.array.surprise_pictures)
        };

        moodRecognizer = Face.createFisherFaceRecognizer();



        int labelCount = 1;
        int imgCount = 0;


        for (String[] paths : picturesPaths) {
            for (String path : paths) {
                //if (detectFace(path)) {
                    Log.d(TAG, "trainRecognizer: Picture Treated");
                    pictures.add(imgCount, Imgcodecs.imread(path,Imgcodecs.IMREAD_GRAYSCALE));
                    labels.put(imgCount,0,labelCount);
                    imgCount++;
                //}
            }
            labelCount++;
        }

        Log.d(TAG, "trainRecognizer: about to start training " + pictures.size() + " " + labels.size());
        moodRecognizer.train(pictures, labels);
        Log.d(TAG, "trainRecognizer: Training done");

        pictures.clear();
        labels.release();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ImageProcessingService.this);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putBoolean(getString(R.string.PREF_RECOGNIZER_TRAINED), Boolean.TRUE);

        edit.apply();
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
        Log.d(TAG, "onDestroy: ");
        db.close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}



