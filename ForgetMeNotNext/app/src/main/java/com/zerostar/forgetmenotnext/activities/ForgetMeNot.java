package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Bundle;
import android.widget.Toast;

import com.zerostar.forgetmenotnext.R;

public class ForgetMeNot extends Activity {

    private int delay = 2000;
    private Handler h = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget_me_not);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isConnected()) {
                    Intent goto_login_screen = new Intent(ForgetMeNot.this, LoginSignup.class);
                    startActivity(goto_login_screen);
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Connessione a internet assente",
                            Toast.LENGTH_LONG).show();
                    h.postDelayed(this, delay);
                }
            }
        }, delay);


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }
}

