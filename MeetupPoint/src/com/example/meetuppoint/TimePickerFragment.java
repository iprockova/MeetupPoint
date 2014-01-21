package com.example.meetuppoint;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

	private EditText txtTime;
	
	private Calendar c; 
	
	public TimePickerFragment(){
		
	}
	public TimePickerFragment(EditText txtTime){
			this.txtTime = txtTime;
		}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		// Use the current time as the default values for the picker
        c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
        		DateFormat.is24HourFormat(getActivity()));
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		
		java.text.DateFormat df = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT);
		txtTime.setText(df.format(c.getTime()));
		
	}
}
