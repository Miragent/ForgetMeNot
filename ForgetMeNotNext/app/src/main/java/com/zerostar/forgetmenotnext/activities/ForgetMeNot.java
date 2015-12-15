package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.zerostar.forgetmenotnext.R;

public class ForgetMeNot extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget_me_not);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                //ATTENZIONE CAMBIARE MAIN SCREEN.CLASS CON LOGINSIGNUP.CLASS!!!!!!
                Intent goto_login_screen = new Intent(ForgetMeNot.this, MainScreen.class);
                startActivity(goto_login_screen);

            }
        }, 2000);


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

