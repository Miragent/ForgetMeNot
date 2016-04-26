package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zerostar.forgetmenotnext.R;

public class ShowElimina extends Activity {

    private Button btn_si;
    private Button btn_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_elimina);

        btn_si = (Button) findViewById(R.id.btn_si_elimina);
        btn_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i_result = new Intent();
                setResult(Activity.RESULT_OK, i_result);
                finish();
            }
        });
        btn_no = (Button) findViewById(R.id.btn_no_elimina);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i_result = new Intent();
                setResult(Activity.RESULT_CANCELED, i_result);
                finish();
            }
        });
    }
}
