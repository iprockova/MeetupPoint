package com.example.meetuppoint.activities;

import java.util.Arrays;

import com.example.meetuppoint.R;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * The SignupIntro screen where the user is offered the option
 * to sign up with Facebook or sign up with email.
 * @author irena
 */
public class SignUpIntroActivity extends Activity {
	
	//SharedPreferences values
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String PREF_USER_ID = "user_logged_in";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_intro);
		
		// Initialize Parse APIs
		Parse.initialize(this, "Zmczusi1zjt33akDOeglJXwJ25gUooC0lZ3x2oqY", "AFqNOq7g3M2IZqhQKIMirtCZkGUwQpAI8cEFN4XP");        
		ParseFacebookUtils.initialize("623048194426303");
				
		// Start SignUp activity for user sign up
		final TextView txtView = (TextView) this.findViewById(R.id.textSignUp);
		txtView.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View v) {
		    	Intent intent = new Intent(SignUpIntroActivity.this, SignUpActivity.class);
		    	startActivity(intent);
		    }
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intro_sign_up, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}
	
	/**
	 * Log in with Facebook with the help of ParseFacebook APIs.
	 */
	public void facebookLogin(View view){
		System.out.println("In FB Login");
		ParseFacebookUtils.logIn(Arrays.asList("email"),this, new LogInCallback() {
			  @Override
			  public void done(ParseUser user, ParseException err) {
			    if (user == null) {
			      Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
			    } else if (user.isNew()) {
			     //Log.d("MyApp", "User signed up and logged in through Facebook!");
			      
			      // Save user login state
			      saveUserLoggedInState();
			      
			      // Start next activity
			      Intent intent = new Intent(SignUpIntroActivity.this, CreateEventActivity.class);
			      startActivity(intent);
			      
			    } else {
			      //Log.d("MyApp", "User logged in through Facebook!");
			      Log.d("MyApp", "email" + user.getEmail());
			      
			      // Start next activity
			      Intent intent = new Intent(SignUpIntroActivity.this, CreateEventActivity.class);
			      startActivity(intent);
			    }
			  }
			});
	}
	
	/**
	 * Save user logged in state. If user logged in, state is "true", otherwise "false"
	 */
	private void saveUserLoggedInState(){
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean("PREF_USER_ID", false);
	    editor.commit();
	}

}
