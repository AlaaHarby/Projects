package com.example.alaa.parkingapp;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Callable;

import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private int PERMISSION_LOCATION;

    private Location mLocation;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private FloatingActionButton mMarker;
    private DBAdapter mDBadapter;
    private SharedPreferences mSharedPref;
    private String mEmail;
    private HashMap<String, String> mParkings;
    private int mZoomLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDBadapter = new DBAdapter();
        mZoomLevel = 13;
        mSharedPref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mEmail = mSharedPref.getString("email_session", "");
        setContentView(R.layout.activity_maps);

        mSharedPref = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mParkings = new HashMap<>();
        mMarker = (FloatingActionButton) findViewById(R.id.add_marker);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // get new location of ur marker
                mDBadapter.addParking(new Parking(mParkings.get(marker.getId()), marker.getPosition().latitude,
                        marker.getPosition().longitude, mEmail, ""));
            }
        });

        mMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLocation == null) {
                    mLocation = new Location("handle null!");
                    mLocation.setLatitude(-37.813);
                    mLocation.setLongitude(144.962);
                }
                LatLng loc = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                Marker m = mMap.addMarker(new MarkerOptions().position(loc).draggable(true));

                Parking p = new Parking(UUID.randomUUID().toString(), loc.latitude,
                        loc.longitude, mEmail, "");
                mDBadapter.addParking(p);
                mParkings.put(m.getId(), p.getParkingID());

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                getAddress();
            }
        });

        (mDBadapter.getMarkers()).subscribe(new Observer<Parking>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Parking parking) {
                LatLng loc = new LatLng(parking.getLatitude(), parking.getLongitude());
                MarkerOptions opts;
                if (parking.getEmail().equals(mEmail))
                    opts = new MarkerOptions().position(loc).draggable(true);
                else
                    opts = new MarkerOptions().position(loc)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                Marker m = mMap.addMarker(opts);
                mParkings.put(m.getId(), parking.getParkingID());
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_LOCATION &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMarker.setVisibility(View.VISIBLE);
            updateLocation();
        }
        else {
            Toast.makeText(this, R.string.permission_loc_denied, Toast.LENGTH_LONG).show();
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            updateLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSION_LOCATION);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation();
    }

    private void updateLocation() {
        try {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mMap.animateCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), mZoomLevel));
            mMap.setMyLocationEnabled(true);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void getAddress() {
        Single<List<Address>> addressesObservable = Single.fromCallable(new Callable<List<Address>>() {
            @Override
            public List<Address> call() throws Exception {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                return geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
            }
        });

        addressesObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleSubscriber<List<Address>>() {
            @Override
            public void onSuccess(List<Address> value) {
                Toast.makeText(getApplicationContext(), value.get(0).getAddressLine(0), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }
}

