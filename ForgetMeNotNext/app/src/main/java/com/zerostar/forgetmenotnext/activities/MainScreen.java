package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.zerostar.forgetmenotnext.R;

import java.util.HashMap;

public class MainScreen extends Activity {

    private ParseUser me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        final Intent i_giardino = new Intent(this, Giardino.class);
        final Intent i_DB = new Intent(this, DataBase.class);
        final Intent i_lo = new Intent(this, LoginSignup.class);

        ImageButton btn_giardino = (ImageButton) findViewById(R.id.btn_giardino);
        ImageButton btn_DB = (ImageButton) findViewById(R.id.btn_DB);
        ImageButton btn_lo = (ImageButton) findViewById((R.id.btn_logout));

        me = ParseUser.getCurrentUser();
        ParseInstallation pi = ParseInstallation.getCurrentInstallation();
        pi.put("userId", me.getObjectId());
        pi.saveInBackground();


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

        btn_lo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                me.logOutInBackground();
                startActivity(i_lo);
            }
        });
        //CHECK PARSE CLOUD
        // Android SDK
        ParseCloud.callFunctionInBackground("hello", new HashMap<String, Object>(), new FunctionCallback<String>() {
            public void done(String result, ParseException e) {
                if (e == null) {
                    Log.d("Ciao sono il cloud:",result);
                }
            }
        });


    }

}
