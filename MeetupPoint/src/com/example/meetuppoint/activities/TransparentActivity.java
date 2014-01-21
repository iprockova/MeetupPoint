package com.example.meetuppoint.activities;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.facebook.*;

/**
 * The FIRST ACTIVITY that is launched when the application is started.
 * The activity does not show any UI. It is used for checking user login 
 * in state, by looking at the SharedPreference file.
 */
public class TransparentActivity extends Activity {

	public static final String PREFS_NAME = "MyPrefsFile";
	static final String PREF_USER_ID = "user_logged_in";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Check if the user has already logged in and start the next activity
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean userLoggedIn = settings.getBoolean("PREF_USER_ID", false);
        
        Intent intent ;
        if(userLoggedIn){
        	// User previously logged in, start NewEvent activity 
        	intent = new Intent(this, CreateEventActivity.class);   
        }
        else {
        	// User not logged in, open Main activity
        	intent = new Intent(this, MainActivity.class);
        }
   	    startActivity(intent);
       
    	// Close this activity 
    	finish();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

}
