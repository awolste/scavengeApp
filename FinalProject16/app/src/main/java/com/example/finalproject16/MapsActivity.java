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
import android.util.Log;
import android.view.View;
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

import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final int REQUEST_LOCATION_PERMISSIONS = 0;

    private GoogleMap mMap;
    private FusedLocationProviderClient mClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location lastLoc;

    private double startingLat = 34.683546;
    private double startingLong = -82.837632;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        lastLoc = new Location("");
        lastLoc.setLatitude(0);
        lastLoc.setLongitude(0);

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
        //only trigger camera reset when moving a mile
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && (
                        Math.abs(locationResult.getLastLocation().getLatitude() - lastLoc.getLatitude()) > .01 ||
                        Math.abs(locationResult.getLastLocation().getLongitude() - lastLoc.getLongitude()) > .01) ) {
                    Log.i("LOCATION", locationResult.getLastLocation().getLatitude() + " " + locationResult.getLocations().get(0).getLatitude());
                    lastLoc = locationResult.getLastLocation();
                    updateMap();
                }
            }
        };

        mClient = LocationServices.getFusedLocationProviderClient(this);
        //mClient.removeLocationUpdates(mLocationRequest);
    }

    private void updateMap() {
        LatLng StartingPoint = new LatLng(startingLat, startingLong);
        // Move and zoom to current location at the street level
        CameraUpdate update = CameraUpdateFactory.
                newLatLngZoom(StartingPoint, 20);
        mMap.animateCamera(update);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Random rand = new Random();
        String[] tasks = getResources().getStringArray(R.array.tasks);

        mMap = googleMap;
        //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_local_dining_24);

        // Get current location
        LatLng StartingPoint = new LatLng(34.683546, -82.837632);
        LatLng TigerTownTavern = new LatLng(34.683375,-82.83735);
        LatLng ThreeFiveSix = new LatLng(34.683192, -82.837352);
        LatLng StudyHall = new LatLng(34.683375, -82.83774);
        LatLng itsurweiner = new LatLng(34.684303, -82.836281);
        LatLng Backstreets = new LatLng(34.683597,-82.836729);
        LatLng Tds = new LatLng(34.682356, -82.837336);
        LatLng Nicks = new LatLng(34.683567, -82.837857);
        LatLng Loose = new LatLng(34.682469, -82.837405);
        LatLng Csp = new LatLng(34.682743, -82.837509);

        Log.i("NUMBER", rand.nextInt(tasks.length) + "");

        // Place a marker at the current location
        MarkerOptions markerTTT = new MarkerOptions()
                .title(getString(R.string.ttt))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beer))
                //.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_local_dining_24)))
                .position(TigerTownTavern)
                .snippet(tasks[rand.nextInt(tasks.length)]);

        MarkerOptions marker356 = new MarkerOptions()
                .title(getString(R.string.sushi))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beer))
                //.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_local_dining_24)))
                .position(ThreeFiveSix)
                .snippet(tasks[rand.nextInt(tasks.length)]);

        MarkerOptions markerStudy = new MarkerOptions()
                .title(getString(R.string.study))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beer))
                //.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_local_dining_24)))
                .position(StudyHall)
                .snippet(tasks[rand.nextInt(tasks.length)]);

        MarkerOptions markerWein = new MarkerOptions()
                .title(getString(R.string.wein))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beer))
                //.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_local_dining_24)))
                .position(itsurweiner)
                .snippet(tasks[rand.nextInt(tasks.length)]);

        MarkerOptions markerBack = new MarkerOptions()
                .title(getString(R.string.back))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beer))
                //.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_local_dining_24)))
                .position(Backstreets)
                .snippet(tasks[rand.nextInt(tasks.length)]);

        MarkerOptions markerTd = new MarkerOptions()
                .title(getString(R.string.td))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beer))
                //.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_local_dining_24)))
                .position(Tds)
                .snippet(tasks[rand.nextInt(tasks.length)]);

        MarkerOptions markerNick = new MarkerOptions()
                .title(getString(R.string.nicks))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beer))
                //.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_local_dining_24)))
                .position(Nicks)
                .snippet(tasks[rand.nextInt(tasks.length)]);

        MarkerOptions markerLoose = new MarkerOptions()
                .title(getString(R.string.loose))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beer))
                //.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_local_dining_24)))
                .position(Loose)
                .snippet(tasks[rand.nextInt(tasks.length)]);

        MarkerOptions markerCsp = new MarkerOptions()
                .title(getString(R.string.csp))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beer))
                //.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_local_dining_24)))
                .position(Csp)
                .snippet(tasks[rand.nextInt(tasks.length)]);

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
        mMap.addMarker(markerLoose);
        mMap.addMarker(markerBack);
        mMap.addMarker(markerTd);
        mMap.addMarker(markerNick);
        mMap.addMarker(markerLoose);
        mMap.addMarker(markerCsp);
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
                Log.i("CLICK", "clicked" + marker.getTitle());
                //Toast.makeText(MapsActivity.this, marker.getTitle(), Toast.LENGTH_LONG).show();
                marker.showInfoWindow();

                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MapsActivity.this, CameraActivity.class);

                intent.putExtra("BARNAME", marker.getTitle());
                intent.putExtra("TASK", marker.getSnippet());
                intent.putExtra("TASKLAT", marker.getPosition().latitude);
                intent.putExtra("TASKLONG", marker.getPosition().longitude);

                //for now we hard code a starting location for demoing
                intent.putExtra("USERLAT", startingLat);
                intent.putExtra("USERLONG", startingLong);

                startActivity(intent);

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

    public void goHome(View view) {
        startActivity(new Intent(MapsActivity.this, MainActivity.class));
    }
}