package com.example.septiawanajipradan.thesis.aractivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.location.Location;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.septiawanajipradan.thesis.R;
import com.example.septiawanajipradan.thesis.aractivity.helper.LocationHelper;
import com.example.septiawanajipradan.thesis.aractivity.model.ARPoint;
import com.example.septiawanajipradan.thesis.database.DatabaseHandler;
import com.example.septiawanajipradan.thesis.entitas.BangunanSensus;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ntdat on 1/13/17.
 */

public class AROverlayView extends View implements View.OnClickListener {

    Context context;
    private float[] rotatedProjectionMatrix = new float[16];
    private Location currentLocation;
    private ArrayList<BangunanSensus> bsArray;
    Bitmap bitmap;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private OnClickListener listener;
    private ArrayList<Bitmap> arrayBitmap;

    private DatabaseHandler db;
    float x,y;
    public AROverlayView(Context context) {
        super(context);

        this.context = context;
        db = new DatabaseHandler(context);
        arrayBitmap = new ArrayList<>();

        bsArray = db.getAll();
//        setOnClickListener(this);

        //Demo points
//        arPoints = new ArrayList<ARPoint>() {{
//            add(new ARPoint("STIS",-6.231472, 106.866830, 0));
//        }};
    }

    @Override
    public void onClick(View view) {

    }

    public double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public void updateRotatedProjectionMatrix(float[] rotatedProjectionMatrix) {
        this.rotatedProjectionMatrix = rotatedProjectionMatrix;
        this.invalidate();
    }

    public void updateCurrentLocation(Location currentLocation){
        this.currentLocation = currentLocation;
        this.invalidate();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currentLocation == null) {
            return;
        }

        final int radius = 30;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(60);

        if(bsArray!=null){
            for (int i = 0; i < bsArray.size(); i ++) {
                Location loc = new Location(bsArray.get(i).getNamaKRT());
                loc.setLatitude(bsArray.get(i).getLat());
                loc.setLongitude(bsArray.get(i).getLon());
                float[] currentLocationInECEF = LocationHelper.WSG84toECEF(currentLocation);
                float[] pointInECEF = LocationHelper.WSG84toECEF(loc);
                float[] pointInENU = LocationHelper.ECEFtoENU(currentLocation, currentLocationInECEF, pointInECEF);

//                double jarak = distance(currentLocation.getLatitude(),currentLocation.getLongitude(),currentLocation.getLatitude(),currentLocation.getLongitude(),"K");
                float[] cameraCoordinateVector = new float[4];
                Matrix.multiplyMV(cameraCoordinateVector, 0, rotatedProjectionMatrix, 0, pointInENU, 0);

                // cameraCoordinateVector[2] is z, that always less than 0 to display on right position
                // if z > 0, the point will display on the opposite
                if (cameraCoordinateVector[2] < 0) {
                    x  = (0.5f + cameraCoordinateVector[0]/cameraCoordinateVector[3]) * canvas.getWidth();
                    y = (0.5f - cameraCoordinateVector[1]/cameraCoordinateVector[3]) * canvas.getHeight();
//                canvas.drawCircle(x, y, radius, paint);
                    canvas.drawText(bsArray.get(i).getNamaKRT(), x - (30 * bsArray.get(i).getNamaKRT().length() / 2), y - 80, paint);

                    Resources res = getResources();
                    bitmap = BitmapFactory.decodeResource(res, R.drawable.ic_poin);
                    canvas.drawBitmap(bitmap, x, y, paint);
                    Log.d("jos_x",Integer.toString(bitmap.getWidth()));
                    Log.d("jos_y", Integer.toString(bitmap.getHeight()));
                    arrayBitmap.add(bitmap);
                }
            }
        }


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        Log.d("jos_six",Integer.toString(arrayBitmap.size()));
//        for(Bitmap bit : arrayBitmap){
//            if(bit.contains(touchX,touchY)){
//                System.out.println("Touched Rectangle, start activity.");
//                Intent i = new Intent(<your activity info>);
//                startActivity(i);
//            }
//        }
//        int touchX = (int) event.getX();
//        int touchY = (int) event.getY();
//        switch(event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                System.out.println("Touching down!");
//                    if(touchX < bitmap.getWidth() && touchY <bitmap.getHeight()){
//                        Toast.makeText(context, "Kena", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                System.out.println("Touching up!");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                System.out.println("Sliding your finger around on the screen.");
//                break;
//        }
        return true;
    }

//    public void onClick(View arg0) {
//        Toast.makeText(context, "View clicked ", Toast.LENGTH_SHORT).show();
//    }



    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            invalidate();
            return true;
        }
    }
}
