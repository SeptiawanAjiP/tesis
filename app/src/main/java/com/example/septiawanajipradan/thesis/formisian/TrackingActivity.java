package com.example.septiawanajipradan.thesis.formisian;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.septiawanajipradan.thesis.R;
import com.example.septiawanajipradan.thesis.aractivity.helper.GPSTracker;
import com.example.septiawanajipradan.thesis.database.DatabaseHandler;
import com.example.septiawanajipradan.thesis.entitas.BangunanSensus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Septiawan Aji Pradan on 3/11/2017.
 */

public class TrackingActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker gps;
    private double latitude, longitude;
    private LatLng lokasi;
    private ArrayList<BangunanSensus> arrayBangunanSensus;
    String message;
    private BangunanSensus tempat;


    public LinearLayout layout;

    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempat);
        arrayBangunanSensus = new ArrayList<>();
        tempat = new BangunanSensus();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        db = new DatabaseHandler(getApplicationContext());
        arrayBangunanSensus = db.getAll();

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
        mMap.setInfoWindowAdapter(new TagTempatInfoWindow(TrackingActivity.this));
        mMap.setOnInfoWindowClickListener(this);

        viewLokasi();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

//    public void monitoring(){
//        final Handler mHandler = new Handler();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
////                if (Utils.hasMarshmallow()) {
////                    new ApplozicPermissions(this, layout).checkRuntimePermissionForLocation();
////                } else {
////                    updateLocation();
////                }
//
//                // do your stuff here, called every second
//
//                updateLocation(getIntent().getStringExtra("id_user"),getIntent().getStringExtra("id_meetup"));
//
//                mHandler.postDelayed(this, 1000);
//            }
//        };
//        mHandler.post(runnable);
//    }

    private void viewLokasi() {

        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }


        final Handler handler = new Handler();
        final MarkerOptions markerStand = new MarkerOptions();
        final MarkerOptions awal = new MarkerOptions();
        LatLng pointAwal = new LatLng(arrayBangunanSensus.get(0).getLat(),arrayBangunanSensus.get(0).getLon());
        awal.position(pointAwal);
        awal.icon(BitmapDescriptorFactory.fromResource(R.drawable.person_loc));
        awal.title(arrayBangunanSensus.get(0).getNamaKRT());

        for(int i=0;i<arrayBangunanSensus.size();i++){

            LatLng point = new LatLng(arrayBangunanSensus.get(i).getLat(), arrayBangunanSensus.get(i).getLon());
            markerStand.position(point);
            markerStand.title(arrayBangunanSensus.get(i).getNamaKRT());
            markerStand.icon(BitmapDescriptorFactory.fromResource(R.drawable.person_loc));

            mMap.addMarker(markerStand);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointAwal, 15));
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

//                markerStand.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_contact));



    }

    @Override
    public void onBackPressed() {
        String message = "kosong";
        Intent intent = new Intent();
        intent.putExtra("MESSAGE",message);
        setResult(7,intent);
        finish();
    }


}

