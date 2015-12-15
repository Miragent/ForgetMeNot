package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.zerostar.forgetmenotnext.R;

public class MainScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        final Intent i_giardino = new Intent(this, Giardino.class);
        final Intent i_DB = new Intent(this, DataBase.class);

        ImageButton btn_giardino = (ImageButton) findViewById(R.id.btn_giardino);
        ImageButton btn_DB = (ImageButton) findViewById(R.id.btn_DB);

        btn_giardino.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(i_giardino);
            }
        });

        btn_DB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(i_DB);
            }
        });
    }

}
