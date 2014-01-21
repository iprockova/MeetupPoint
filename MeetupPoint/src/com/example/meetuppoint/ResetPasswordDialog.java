package com.example.meetuppoint;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class ResetPasswordDialog extends DialogFragment{
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.dialog_title);
		builder.setMessage(R.string.dialog_enter_email);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		builder.setView(inflater.inflate(R.layout.dialog_reset_password, null));
		
		 View emailView= inflater.inflate(R.layout.dialog_reset_password, null);
		 builder.setView(emailView);
		  
		final EditText txtEmailView = (EditText) emailView.findViewById(R.id.dialog_email);
		
		builder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   ParseUser.requestPasswordResetInBackground(txtEmailView.getText().toString(),
                               new RequestPasswordResetCallback() {
									public void done(ParseException e) {
										if (e == null) {
										// An email was successfully sent with reset instructions.
											//System.out.println("SUccess, an email was sent with reset instric.");
										} else {
										// Something went wrong. Look at the ParseException to see what's up.
										}
										}
									});

		           }
		       });
		builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   ResetPasswordDialog.this.getDialog().cancel();		    
		        	   }
		       });
		
		// Create the AlertDialog
		AlertDialog dialog = builder.create();
        return dialog;
    }
}
