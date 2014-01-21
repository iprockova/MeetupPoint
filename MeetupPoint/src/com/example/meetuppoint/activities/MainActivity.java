package com.example.meetuppoint.activities;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

import com.example.meetuppoint.R;
import com.google.android.gms.common.*;

/**
 * The first screen (welcome screen) shown to the user the first 
 * time the application is started. Allows users to pick 'Login' 
 * or 'Signup', for entering to the application.
 */
public class MainActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
//    	int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
//    	
//    	if (resultCode == ConnectionResult.SUCCESS) {
//    	   // activity.selectMap();
//    	} else if (resultCode == ConnectionResult.SERVICE_MISSING ||
//    	           resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED ||
//    	           resultCode == ConnectionResult.SERVICE_DISABLED) {
//    	    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1);
//    	    dialog.show();
//    	}
    }
    
    /**
     * Login button pressed, open Login activity
     */
    public void login(View view) {
    	Intent intent = new Intent(this, LoginActivity.class);
    	startActivity(intent);
    }
    
    /**
     * Sign up button pressed, open SignupIntro activity
     */
    public void signUp(View view) {
    	
    	Intent intent = new Intent(this, SignUpIntroActivity.class);
    	startActivity(intent);
    }
    
    
}
