package com.example.septiawanajipradan.thesis.formisian;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.septiawanajipradan.thesis.R;
import com.example.septiawanajipradan.thesis.database.DatabaseHandler;
import com.example.septiawanajipradan.thesis.entitas.BangunanSensus;


/**
 * Created by kaddafi on 08/03/2017.
 */

public class DialogInputTempat extends Dialog implements View.OnClickListener {
    public final static String LATITUDE = "latitude";
    public final static String LONGITUDE = "longitude";

    private Activity activity;
    private EditText namaTempat;
    private TextView tutupBtn, simpanBtn;
    private BangunanSensus bs;
    private double latitude,longitude;
    private DatabaseHandler db;

    public DialogInputTempat(Activity activity,double latitude, double longitude) {
        super(activity);
        this.activity = activity;
        this.latitude= latitude;
        this.longitude = longitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modal_dialog_input_tempat);

        db = new DatabaseHandler(activity);


        namaTempat = (EditText) findViewById(R.id.et_nama_tempat);
        tutupBtn = (TextView) findViewById(R.id.btn_tutup);
        tutupBtn.setOnClickListener(this);
        simpanBtn = (TextView)findViewById(R.id.btn_simpan);
        simpanBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==simpanBtn){
            if(namaTempat.getText().length()!=0){
                bs = new BangunanSensus(namaTempat.getText().toString(),latitude,longitude," ");
                db.insertTabel(bs);
//                Intent intent = new Intent();
//                intent.putExtra(LATITUDE,latitude);
//                intent.putExtra(LONGITUDE,longitude);
//                activity.setResult(7,intent);
//                activity.finish();
                dismiss();
            } else {
                Toast.makeText(activity.getApplicationContext(),"Tempat tidak boleh kosong",Toast.LENGTH_SHORT).show();
            }
        }else if(view==tutupBtn){
            dismiss();
        }
    }
}
