package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.zerostar.forgetmenotnext.R;
import com.zerostar.forgetmenotnext.utils.Util;

public class Signup extends Activity {

    Button signup;
    String usernametxt;
    String passwordtxt;
    String mailtxt;
    EditText password;
    EditText username;
    EditText mail;

    private Util util;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        setContentView(R.layout.activity_signup);
        // Locate EditTexts in main.xml
        username = (EditText) findViewById(R.id.txt_username);
        password = (EditText) findViewById(R.id.txt_password);
        mail = (EditText) findViewById(R.id.txt_mail);

        // Locate Buttons in main.xml

        signup = (Button) findViewById(R.id.btn_signup);

        // Sign up Button Click Listener
        signup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                util = new Util(Signup.this);
                if(!util.isConnected()){
                    Toast.makeText(
                            getApplicationContext(),
                            "Connessione a internet assente",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                // Retrieve the text entered from the EditText
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();
                mailtxt = mail.getText().toString();

                // Force user to fill up the form
                if (usernametxt.equals("") && passwordtxt.equals("") && mailtxt.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "E' obbligatorio non lasciare campi vuoti",
                            Toast.LENGTH_LONG).show();

                } else {
                    // Save new user data into Parse.com Data Storage
                    ParseUser user = new ParseUser();
                    user.setUsername(usernametxt);
                    user.setPassword(passwordtxt);
                    user.setEmail(mailtxt);
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Show a simple Toast message upon successful registration
                                Toast.makeText(getApplicationContext(),
                                        "Registrazione effettuata con successo",
                                        Toast.LENGTH_LONG).show();
                                        finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Errore durante la registrazione", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
                }

            }
        });

    }
}
