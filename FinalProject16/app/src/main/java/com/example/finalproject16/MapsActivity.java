package com.example.finalproject16;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final int REQUEST_LOCATION_PERMISSIONS = 0;

    private GoogleMap mMap;
    private FusedLocationProviderClient mClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Create location request
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Create location callback
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                        updateMap();
                }
            }
        };

        mClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void updateMap() {
        LatLng StartingPoint = new LatLng(34.683546, -82.837632);
        // Move and zoom to current location at the street level
        CameraUpdate update = CameraUpdateFactory.
                newLatLngZoom(StartingPoint, 20);
        mMap.animateCamera(update);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_local_dining_24);

        // Get current location
        LatLng TigerTownTavern = new LatLng(34.683375,-82.83735);
        LatLng ThreeFiveSix = new LatLng(34.683192, -82.837352);
        LatLng StudyHall = new LatLng(34.683375, -82.83774);
        LatLng itsurweiner = new LatLng(34.684303, -82.836281);
        LatLng StartingPoint = new LatLng(34.683546, -82.837632);

        // Place a marker at the current location
        MarkerOptions markerTTT = new MarkerOptions()
                .title("Tiger Town Tavern")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beer))
                //.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_local_dining_24)))
                .position(TigerTownTavern);

        MarkerOptions marker356 = new MarkerOptions()
                .title("356 Sushi")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beer))
                //.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_local_dining_24)))
                .position(ThreeFiveSix);

        MarkerOptions markerStudy = new MarkerOptions()
                .title("Study Hall")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beer))
                //.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_local_dining_24)))
                .position(StudyHall);

        MarkerOptions markerWein = new MarkerOptions()
                .title("ITSURWEINER")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beer))
                //.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_local_dining_24)))
                .position(itsurweiner);

        MarkerOptions markerStart = new MarkerOptions()
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                //.icon(icon)
                .position(StartingPoint);

                // Remove previous marker
                        mMap.clear();

        // Add new marker
        mMap.addMarker(markerTTT);
        mMap.addMarker(marker356);
        mMap.addMarker(markerStudy);
        mMap.addMarker(markerWein);
        mMap.addMarker(markerStart).showInfoWindow();

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

        // Save zoom level
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = mMap.getCameraPosition();
            }
        });

        // Handle marker click
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapsActivity.this, marker.getTitle(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(intent);

                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mClient.removeLocationUpdates(mLocationCallback);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();

        if (hasLocationPermission()) {
            mClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    private boolean hasLocationPermission() {

        // Request fine location permission if not already granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this ,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_LOCATION_PERMISSIONS);

            return false;
        }

        return true;
    }
}