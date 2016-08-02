package com.example.tim.moodlink;

import android.*;
import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.Calendar;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    protected static final String TAG = "LOCATION SERVICE";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locRequest;

    private SQLLiteDBHelper db;


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: Service created!");

        db = new SQLLiteDBHelper(getApplicationContext());


        locRequest = createLocationRequest();


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create an instance of GoogleAPIClient.


        mGoogleApiClient.connect();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
        db.close();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onConnected: Permission granted");

            Intent intent = new Intent(this, LocationService.class);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    locRequest, LocationService.this);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected LocationRequest createLocationRequest() {
        return new LocationRequest().
                setInterval(60000).
                setFastestInterval(10000).
                setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    public static void launchLocationService(Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            Intent locationServiceIntent = new Intent(context, LocationService.class);
            context.startService(locationServiceIntent);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(LocationService.this,
                "Location :" +
                        String.valueOf(location.getLatitude()) + " lat, " +
                        String.valueOf(location.getLongitude()) + " long",
                Toast.LENGTH_SHORT).show();

        LocationData locationDataItem = new LocationData(
                location.getLatitude(),
                location.getLongitude(),
                new TimeAndDay(Calendar.getInstance())
        );
        db.addLocationTuple(locationDataItem);
    }
}
