package com.example.meetuppoint.activities;

import java.util.Arrays;

import com.example.meetuppoint.R;
import com.example.meetuppoint.ResetPasswordDialog;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

/**
 * Login screen where the user can enter his details
 * to login to the application. 
 * @author irena
 *
 */
public class LoginActivity extends FragmentActivity {

	//The default email to populate the email field with.
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";
	public static final int ACCOUNT_ALREADY_LINKED = 208;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	//SharedPreferences values
	public static final String PREFS_NAME = "MyPrefsFile";
	static final String PREF_USER_ID = "user_logged_in";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// Initialize Parse APIs
		Parse.initialize(this, "Zmczusi1zjt33akDOeglJXwJ25gUooC0lZ3x2oqY", "AFqNOq7g3M2IZqhQKIMirtCZkGUwQpAI8cEFN4XP");        
		ParseFacebookUtils.initialize(Integer.toString(R.string.app_id));
		
		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		
		findViewById(R.id.login_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});	
		
		// Open forgot password dialog 
		final TextView txtView = (TextView) this.findViewById(R.id.textForgotPassword);
		txtView.setOnClickListener(new OnClickListener() {

				 @Override
				 public void onClick(View v) {
				    forgotPassword();
				 }
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
    protected void onStart() {
        super.onStart();
        mLoginFormView.setVisibility(View.VISIBLE);
    }
	
	@Override
    protected void onResume() {
        super.onResume();
        mLoginFormView.setVisibility(View.VISIBLE);
    }
	
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// Simply show
		// and hide the relevant UI components.
		mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_forgot_password:
	            forgotPassword();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 *  User login and all necessary checks needed for email and password fields.
	 */
	public void attemptLogin() {

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			mLoginFormView.setVisibility(View.GONE);
			showProgress(true);
			login();
			
		}
	}
	
	/**
	 * Perform log in with Parse APIs
	 */
	private void login() {
		final Intent intent = new Intent(this, CreateEventActivity.class);
		
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();
		
		ParseUser.logInInBackground(email, password, new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			    	// User is logged in.
			    	System.out.println("Success, user logged in!");
			    	
			    	saveUserLoggedInState();
			    	
			    	linkUsers(user);
			    	
			    	startActivity(intent);
			    	
			    	showProgress(false);
			    } else { //Log in failed
			    	//Hide the progress spinner
			    	showProgress(false);
			    	mLoginFormView.setVisibility(View.VISIBLE);
			    
			    	//Show the error message in a Toast
			    	CharSequence text;
			    	int duration = Toast.LENGTH_LONG;
			    	
			    	if(e.getCode() == e.OBJECT_NOT_FOUND) {
			    		text = getString(R.string.error_invalid_credentials);
			    	}else {
			    		 text = e.getMessage() + ", code=" + e.getCode();
				    }
			    	
			    	Toast toast = Toast.makeText(getApplicationContext(), text, duration);
			    	toast.show();
			    }
			  }
			});
	}
	
	/**
	 * Shows a dialog for reseting password
	 */
	private void forgotPassword(){
	 DialogFragment alert = new ResetPasswordDialog();
	 alert.show(getSupportFragmentManager(), "fragment_reset_password");
	}
	
	/**
	 * Save user logged in state. If user logged in, state is "true", otherwise "false"
	 */
	private void saveUserLoggedInState(){
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean("PREF_USER_ID", true);
	    editor.commit();
	}
	
	private void linkUsers(final ParseUser user){
		if (!ParseFacebookUtils.isLinked(user)) {
			  ParseFacebookUtils.link(user, this, new SaveCallback() {
			    @Override
			    public void done(ParseException ex) {
			      if (ParseFacebookUtils.isLinked(user)) {
			        Log.d("MyApp", "Woohoo, user logged in with Facebook!");
			      }
			    }
			  });
			}
	}
	/**
	 * Log in with Facebook with the help of ParseFacebook APIs.
	 */
	public void facebookLogin(View view){
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
			      Intent intent = new Intent(LoginActivity.this, CreateEventActivity.class);
			      startActivity(intent);
			      
			    } else {
			      //Log.d("MyApp", "User logged in through Facebook!");
			      Log.d("MyApp", "email" + user.getEmail());
			      
			      // Save user login state
			      saveUserLoggedInState();
			      
			      // Start next activity
			      Intent intent = new Intent(LoginActivity.this, CreateEventActivity.class);
			      startActivity(intent);
			    }
			  }
			});
	}
}
