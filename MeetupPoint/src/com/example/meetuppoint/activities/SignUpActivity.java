package com.example.meetuppoint.activities;

import com.example.meetuppoint.R;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The actual Signup screen where the user creates an account.
 * @author irena
 */
public class SignUpActivity extends FragmentActivity  {
	// The default email to populate the email field with.
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private String mPasswordAgain;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mPasswordAgainView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	//SharedPreferences values
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String PREF_USER_ID = "user_logged_in";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		// Initialize Parse APIs
		Parse.initialize(this, "Zmczusi1zjt33akDOeglJXwJ25gUooC0lZ3x2oqY", "AFqNOq7g3M2IZqhQKIMirtCZkGUwQpAI8cEFN4XP");        
		ParseFacebookUtils.initialize("623048194426303");

		// Set up the login form.
		//mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		//mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mPasswordAgainView = (EditText) findViewById(R.id.passwordAgain);
		mPasswordAgainView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});
		
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_up_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		//
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.sign_up, menu);
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
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mPasswordAgainView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mPasswordAgain = mPasswordAgainView.getText().toString();

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
		
		// Check for a valid second password.
		if (TextUtils.isEmpty(mPasswordAgain)) {
			mPasswordAgainView.setError(getString(R.string.error_field_required));
			focusView = mPasswordAgainView;
			cancel = true;
		} else if (mPasswordAgain.length() < 4) {
			mPasswordAgainView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordAgainView;
			cancel = true;
		}
		
		//Check if both passwords are equal
		if(!mPassword.equals(mPasswordAgain)){
			mPasswordView.setError(getString(R.string.error_different_passwords));
			focusView = mPasswordView;
			mPasswordAgainView.setError(getString(R.string.error_different_passwords));
			focusView = mPasswordAgainView;
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
//			mAuthTask = new UserLoginTask();
//			mAuthTask.execute((Void) null);
			signUp();
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	/**
	 * Perform sign up with Parse APIs. If there are any errors, show them 
	 * to the user with a Toast
	 */
	public void signUp() {
		final Intent intent = new Intent(this, CreateEventActivity.class);
		
		ParseUser user = new ParseUser();
		user.setUsername(mEmailView.getText().toString());
		user.setPassword(mPasswordView.getText().toString());
		user.setEmail(mEmailView.getText().toString());
		
		user.signUpInBackground(new SignUpCallback() {
			  public void done(ParseException e) {
			    if (e == null) {
			    	// User is logged in.
			    	showProgress(false);
			    	saveUserLoggedInState();
			    	
			    	startActivity(intent);
			    } else {
			    	//Sign up failed
			    	
			    	// Hide progress spinner
			    	showProgress(false);
			    	mLoginFormView.setVisibility(View.VISIBLE);
			    	
			    	//Show the error message in a Toast
			    	CharSequence text;
			    	int duration = Toast.LENGTH_LONG;
			    	
			    	if (e.getCode() == e.EMAIL_TAKEN || e.getCode() == e.USERNAME_TAKEN){
			    		text = getString(R.string.error_email_taken);
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
	 * Save user logged in state. If user logged in, state is "true", otherwise "false"
	 */
	private void saveUserLoggedInState(){
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean("PREF_USER_ID", false);
	    editor.commit();
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
		ParseFacebookUtils.logIn(this, new LogInCallback() {
			  @Override
			  public void done(ParseUser user, ParseException err) {
			    if (user == null) {
			      Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
			      Log.d("MyApp", err.getMessage());
			    } else if (user.isNew()) {
			      Log.d("MyApp", "User signed up and logged in through Facebook!");
			    } else {
			      Log.d("MyApp", "User logged in through Facebook!");
			    }
			  }
			});
	}
	
}
