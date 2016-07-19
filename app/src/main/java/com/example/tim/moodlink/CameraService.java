package com.example.tim.moodlink;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

/*
Reference: http://stackoverflow.com/questions/28003186/capture-picture-without-preview-using-camera2-api

 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CameraService extends Service {
    protected static final String TAG = "CAMERA SERVICE";
    protected static final int CAMERACHOICE = CameraCharacteristics.LENS_FACING_FRONT;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession session;
    protected ImageReader imageReader;
    private Surface mDummySurface;
    private SurfaceTexture mDummyPreview;

    public void onCreate() {
        SurfaceTexture mDummyPreview = new SurfaceTexture(1);
        mDummySurface = new Surface(mDummyPreview);
        Log.d(TAG, "onCreate: Service created!");
    }


    protected CameraDevice.StateCallback cameraStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            actOnReadyCameraDevice();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
        }
    };

    protected CameraCaptureSession.StateCallback sessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            CameraService.this.session = session;

            try {
                for (int i = 0; i < 30; i++) {
                    session.capture(createPreviewRequest(),null,null);
                }
                session.capture(createCaptureRequest(), null, null);
            } catch (CameraAccessException e) {
                Log.e(TAG, "onConfigured: " + e.getMessage(), null);
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
        }
    };

    protected ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image img = reader.acquireLatestImage();
            if (img != null) {
                processImage(img);
                img.close();
            }
            CameraService.this.stopSelf();
        }
    };

    public void readyCamera() {
        CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            String pickedCamera = getCamera(manager);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.openCamera(pickedCamera, cameraStateCallback, null);
            imageReader = ImageReader.newInstance(320, 240, ImageFormat.JPEG, 2 /* images buffered */);
            imageReader.setOnImageAvailableListener(onImageAvailableListener, null);
        } catch (CameraAccessException e) {
            Log.e(TAG, "onConfigured: " + e.getMessage(), null);

        }
    }


    /**
     * Return the Camera Id which matches the field CAMERACHOICE.
     */
    public String getCamera(CameraManager manager) {
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                // TODO : check if device has a front camera
                int cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cOrientation == CAMERACHOICE) {
                    return cameraId;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        readyCamera();

        return super.onStartCommand(intent, flags, startId);
    }

    public void actOnReadyCameraDevice() {
        try {
            cameraDevice.createCaptureSession(Arrays.asList(imageReader.getSurface(), mDummySurface), sessionStateCallback, null);
        } catch (CameraAccessException e) {
            Log.e(TAG, "onConfigured: " + e.getMessage(), null);
        }
    }

    @Override
    public void onDestroy() {
        try {
            session.abortCaptures();
        } catch (CameraAccessException e) {
            Log.e(TAG, "onConfigured: " + e.getMessage(), null);
        }


        imageReader.close();
        session.close();
        cameraDevice.close();

        Log.d(TAG, "onDestroy: Service Stopped!");
    }

    /**
     * Process image data as desired.
     */
    private void processImage(Image image) {
        int orientation = CameraService.this.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            File file = new File(getString(R.string.camera_storage));

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

            String fileName = month + day + year + "_" + hour + minute;

            file = new File(getString(R.string.camera_storage), fileName + ".jpg");


            try {


                ByteBuffer imageBuffer = image.getPlanes()[0].getBuffer();

                FileOutputStream output = new FileOutputStream(file);
                byte[] imageBytes = new byte[imageBuffer.capacity()];
                imageBuffer.get(imageBytes);

                output.write(imageBytes);

                Toast.makeText(CameraService.this, "Pictures Saved", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Picture saved! ");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected CaptureRequest createCaptureRequest() {
        try {
            CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            builder.addTarget(imageReader.getSurface());
            builder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
            builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
            builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
            builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);
            return builder.build();
        } catch (CameraAccessException e) {
            return null;
        }
    }

    protected CaptureRequest createPreviewRequest() {
        try {
            CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            builder.addTarget(mDummySurface);
            builder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
            builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
            builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
            builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);
            return builder.build();
        } catch (CameraAccessException e) {
            return null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void launchCameraService(Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent cameraServiceIntent = new Intent(context, CameraService.class);
            context.startService(cameraServiceIntent);
        }
    }
}