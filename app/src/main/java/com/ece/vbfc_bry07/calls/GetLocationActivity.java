package com.ece.vbfc_bry07.calls;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ece.vbfc_bry07.calls.psr_activity.SignatureFormActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class GetLocationActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {

    static Activity context;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    // Google client to interact with Google API
    private static GoogleApiClient mGoogleApiClient;

    public GetLocationActivity(Activity context) {
        GetLocationActivity.context = context;

        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();

            if (mGoogleApiClient != null)
                mGoogleApiClient.connect();

        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    public String displayLocation() {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        double latitude = 0, longitude = 0;

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }

        SignatureFormActivity.insertCall(latitude, longitude);

        return latitude + "";
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("details", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, context, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else
                Log.d("details_check", "this device is not supported");

            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        int UPDATE_INTERVAL = 10000;
        int FATEST_INTERVAL = 5000;
        int DISPLACEMENT = 10;

        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }
}
