package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.zerostar.forgetmenotnext.R;

public class Giardino extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giardino);

        final Intent i_nuova_pianta = new Intent(this, NuovaPianta.class);

        ImageButton btn_nuova_pianta = (ImageButton) findViewById(R.id.btn_nuova_pianta);

        btn_nuova_pianta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(i_nuova_pianta);
            }
        });
    }
}
