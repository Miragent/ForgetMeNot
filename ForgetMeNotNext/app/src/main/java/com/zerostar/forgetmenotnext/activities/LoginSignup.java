package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.zerostar.forgetmenotnext.R;

public class LoginSignup extends Activity {
    // Declare Variables
    Button loginbutton;
    Button signup;
    String usernametxt;
    String passwordtxt;
    EditText password;
    EditText username;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        setContentView(R.layout.activity_login_signup);
        // Locate EditTexts in main.xml
        username = (EditText) findViewById(R.id.txt_username);
        password = (EditText) findViewById(R.id.txt_password);

        // Locate Buttons in main.xml
        loginbutton = (Button) findViewById(R.id.btn_login);
        signup = (Button) findViewById(R.id.btn_signup);

        // Login Button Click Listener
        loginbutton.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();

                // Send data to Parse.com for verification
                ParseUser.logInInBackground(usernametxt, passwordtxt,
                        new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    // If user exist and authenticated, send user to MainScreen.class
                                    Intent i_main_screen = new Intent(
                                            LoginSignup.this,
                                            MainScreen.class);
                                    startActivity(i_main_screen);
                                    Toast.makeText(getApplicationContext(),
                                            "Autenticato con successo!",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                } else {

                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Utente non registrato",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        // Sign up Button Click Listener
        signup.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                Intent i_signup = new Intent(
                        LoginSignup.this,
                        Signup.class);
                startActivity(i_signup);
            }
        });

    }
}
