package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zerostar.forgetmenotnext.R;


public class MenuSensore extends Activity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mLight;
    private int esposizione;
    private float light_value;
    private ImageButton btn_finito_luce;
    private TextView intensità_luce;
    private TextView luce_ok;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_sensore);

        //retrieve esposizione pianta
        esposizione = getIntent().getExtras().getInt("Esposizione");
        Log.d("Esposizione:", "" + esposizione);

        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        intensità_luce = (TextView) findViewById(R.id.txt_intensita_luce);
        luce_ok = (TextView) findViewById(R.id.txt_luce_ok);

        btn_finito_luce = (ImageButton) findViewById( (R.id.imageButton));
        btn_finito_luce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /*
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sensor__menu, container, false);

    }
    */

    @Override
    public void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        intensità_luce.setText("Intensità luce = " + light_value);
    }

    @Override
    public void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        light_value = lux;
        intensità_luce.setText("Intensità luce = "+ light_value);
        if (light_value < esposizione){
            luce_ok.setText("Poca luce!");
            setResult(0);
        }
        if (light_value > esposizione + 150 ){
            luce_ok.setText("Troppa luce!");
            setResult(0);
        }
        if (light_value > esposizione && light_value < esposizione + 150){
            luce_ok.setText("Perfetto!");
            setResult(RESULT_OK);
        }
    }




}
