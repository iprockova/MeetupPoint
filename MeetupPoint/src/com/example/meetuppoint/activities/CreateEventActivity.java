package com.example.meetuppoint.activities;

import com.example.meetuppoint.DatePickerFragment;
import com.example.meetuppoint.R;
import com.example.meetuppoint.TimePickerFragment;
import com.parse.Parse;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

public class CreateEventActivity extends FragmentActivity {
	
	private EditText txtLocation;
	private EditText txtDate;
	private EditText txtTime;
	
	private static int PICK_LOCATION_REQUEST = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);
		
		Parse.initialize(this, "Zmczusi1zjt33akDOeglJXwJ25gUooC0lZ3x2oqY", "AFqNOq7g3M2IZqhQKIMirtCZkGUwQpAI8cEFN4XP");
		//ParseFacebookUtils.initialize(Integer.toString(R.string.app_id));
		
		txtLocation = (EditText) findViewById(R.id.editLocation);
		txtLocation.setOnTouchListener(new OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        if(event.getAction() == MotionEvent.ACTION_UP){

		            Intent intent = new Intent(CreateEventActivity.this, LocationActivity.class);
		            startActivityForResult(intent, PICK_LOCATION_REQUEST);
		        }
		        return false;
		    }
		});
		
		
		txtDate = (EditText) findViewById(R.id.editDate);
		txtDate.setOnTouchListener(new OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        if(event.getAction() == MotionEvent.ACTION_UP){
		        	
		        	showDatePickerDialog();
		        }
		        return false;
		    }
		});
		
		txtTime = (EditText) findViewById(R.id.editTime);
		txtTime.setOnTouchListener(new OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        if(event.getAction() == MotionEvent.ACTION_UP){
		        	
		        	showTimePickerDialog();
		        }
		        return false;
		    }
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public void showDatePickerDialog() {
	    DialogFragment newFragment = new DatePickerFragment(txtDate);
	    newFragment.show(getSupportFragmentManager(), "datePicker");
	}
	
	public void showTimePickerDialog() {
	    DialogFragment newFragment = new TimePickerFragment(txtTime);
	    newFragment.show(getSupportFragmentManager(), "timePicker");
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == PICK_LOCATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                // A location was picked.  Here we will just display it
                // in the edit text.
                txtLocation.setText(data.getStringExtra("location"));
            }
        }
    }
	
}
