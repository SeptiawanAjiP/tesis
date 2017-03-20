package com.example.septiawanajipradan.thesis;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.septiawanajipradan.thesis.aractivity.ARActivity;
import com.example.septiawanajipradan.thesis.aractivity.CobaActivity;
import com.example.septiawanajipradan.thesis.formisian.MapsActivity;
import com.example.septiawanajipradan.thesis.formisian.TrackingActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button form,scan,lihat;
    private final static int REQUEST_CAMERA_PERMISSIONS_CODE = 11;
    public static final int REQUEST_LOCATION_PERMISSIONS_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        form = (Button)findViewById(R.id.bt_form);
        scan = (Button)findViewById(R.id.bt_scan);
        lihat = (Button)findViewById(R.id.bt_track);
        form.setOnClickListener(this);
        scan.setOnClickListener(this);
        lihat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==form){
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        }else if (view==scan){
            Intent intent = new Intent(getApplicationContext(), ARActivity.class);
            startActivity(intent);
        }else if(view==lihat){
            Intent intent = new Intent(getApplicationContext(), TrackingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestLocationPermission();
        requestCameraPermission();
    }

    public void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSIONS_CODE);
        } else {

        }
    }

    public void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSIONS_CODE);
        } else {

        }
    }


}
