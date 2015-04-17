package com.ajkayfishgmail.discount;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private static final String TAG = "GooglePlayActivity";

    private static final String KEY_IN_RESOLUTION = "is_in_resolution";

    /**
     * Request code for auto Google Play Services error resolution.
     */
    protected static final int REQUEST_CODE_RESOLUTION = 1;

    /**
     * Google API client.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Determines if the client is in a resolution state, and
     * waiting for resolution intent to return.
     */
    private boolean mIsInResolution;

    private Marker myMarker;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    public double[] lats;
    public double[] longs;

    ArrayList<Double> doubleLongArray;
    ArrayList<Double> doubleLatArray;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        doubleLatArray = new ArrayList<Double>();
        doubleLongArray = new ArrayList<Double>();


        setContentView(R.layout.activity_results_map);
        if (savedInstanceState != null) {
            mIsInResolution = savedInstanceState.getBoolean(KEY_IN_RESOLUTION, false);
        }
        setUpMapIfNeeded();
        Intent intent = getIntent();
        double[] longArray;
        double[] latArray;


        longArray = intent.getDoubleArrayExtra("LongData");
        latArray =intent.getDoubleArrayExtra("LatData");

        for(double x : longArray )
    {
        doubleLongArray.add(x);

    }
        for(double x : latArray )
        {
            doubleLatArray.add(x);

        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    // Optionally, add additional APIs and scopes if required.
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        setUpMap();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IN_RESOLUTION, mIsInResolution);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_RESOLUTION:
                retryConnecting();
                break;
        }
    }

    private void retryConnecting() {
        mIsInResolution = false;
        if (!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }



    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            FragmentManager mapManager = getFragmentManager();
            MapFragment mapFragment = (MapFragment) mapManager.findFragmentById(R.id.mapFragment);
            mMap = mapFragment.getMap();
           // Try to obtain the map from the SupportMapFragment.


            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {
        mMap.clear();
        char c = 64;
        for (int i = 0; i < doubleLatArray.size(); i++) {
            c++;
            double lati=doubleLatArray.get(i);
            double longLat=doubleLongArray.get(i);
            mMap.addMarker(new MarkerOptions().position(new LatLng(lati, longLat)).title(c + ""));
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "GoogleApiClient connected");
        // TODO: Start making API requests.
        LocationRequest req = new LocationRequest();
        req.setInterval (3000); /* every 3 seconds */
        req.setFastestInterval (1000); /* how fast our app can handle the notifications */
        req.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates (
                mGoogleApiClient,   /* fill in with the name of your GoogleMap object */
                req,
                this);  /* this class is the LocationListener */
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
        retryConnecting();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // Show a localized error dialog.
            GooglePlayServicesUtil.getErrorDialog(
                    result.getErrorCode(), this, 0, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            retryConnecting();
                        }
                    }).show();
            return;
        }
        // If there is an existing resolution error being displayed or a resolution
        // activity has started before, do nothing and wait for resolution
        // progress to be completed.
        if (mIsInResolution) {
            return;
        }
        mIsInResolution = true;
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
            retryConnecting();
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        LatLng geoPos = new LatLng(location.getLatitude(), location.getLongitude());
        CameraPosition campos = CameraPosition.builder()
                .target(geoPos)
                .zoom(18)
                .build();
/* zoom level 18: buildings. Smaller number zoom-out, bigger: zoom-in */

/* fill in the blanks with the name of your GoogleMap object */
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(campos));
        if (myMarker == null)
            mMap.addMarker(new MarkerOptions().position(geoPos));
        else
            myMarker.setPosition (geoPos);
    }
}
