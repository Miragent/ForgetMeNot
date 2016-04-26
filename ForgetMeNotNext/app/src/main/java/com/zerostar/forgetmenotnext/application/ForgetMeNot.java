package com.zerostar.forgetmenotnext.application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import android.app.Application;

public class ForgetMeNot extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.initialize(this, "3XIaJoDTJfPMjQNWsxapTUV7N9ZGEGFgsj6P7AoK", "Z3znDA6zNWdFVgZLzgJKlRYHcLEaVKoib66yzV7F");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // Remove this line if you would like all objects to be private by default
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}