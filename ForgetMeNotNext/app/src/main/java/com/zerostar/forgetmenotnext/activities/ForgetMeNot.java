package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.ParseUser;
import com.zerostar.forgetmenotnext.R;
import com.zerostar.forgetmenotnext.utils.Util;

public class ForgetMeNot extends Activity {

    private int delay = 2000;
    private Handler h = new Handler();
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_me_not);

        util = new Util(this);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (util.isConnected()) {
                    if(ParseUser.getCurrentUser() == null) {
                        Intent goto_login_screen = new Intent(ForgetMeNot.this, LoginSignup.class);
                        startActivity(goto_login_screen);
                    }else {
                        Intent goto_main_screen = new Intent(ForgetMeNot.this,MainScreen.class);
                        startActivity(goto_main_screen);
                    }
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

}

