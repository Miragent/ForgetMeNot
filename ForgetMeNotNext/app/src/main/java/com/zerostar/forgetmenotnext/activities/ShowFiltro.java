package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zerostar.forgetmenotnext.R;

public class ShowFiltro extends Activity {

    private String res = "";

    private RadioGroup rg;
    private RadioButton p_aro, p_orna, p_grassa, p_carn, p_pkmn, p_none;
    private ImageButton btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_filtro);

        rg = (RadioGroup) findViewById(R.id.radio_filtri);

        p_aro = (RadioButton) findViewById(R.id.rb_p_aro);
        p_carn = (RadioButton) findViewById(R.id.rb_p_carn);
        p_orna = (RadioButton) findViewById(R.id.rb_p_orna);
        p_grassa = (RadioButton) findViewById(R.id.rb_p_grassa);
        p_pkmn = (RadioButton) findViewById(R.id.rb_p_pkmn);
        p_none = (RadioButton) findViewById(R.id.rb_none);

        btn_ok = (ImageButton)findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rg.getCheckedRadioButtonId();


                if(selectedId == p_aro.getId()) {
                    res = p_aro.getText().toString();
                } else if(selectedId == p_carn.getId()) {
                    res = p_carn.getText().toString();
                } else  if(selectedId == p_orna.getId()){
                    res = p_orna.getText().toString();
                } else  if(selectedId == p_grassa.getId()){
                    res = p_grassa.getText().toString();
                } else  if(selectedId == p_pkmn.getId()) {
                    res = p_pkmn.getText().toString();
                } else if(selectedId == p_none.getId()){
                    res = "";
                }

                Intent i_result = new Intent();
                i_result.putExtra("result", res);
                setResult(Activity.RESULT_OK, i_result);
                finish();
            }
        });
    }
}
