package com.example.meetuppoint;

import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.view.Menu;

public class ErrorDialogFragment extends DialogFragment {
	
	// Instance of the interface to deliver action events
    NoticeDialogListener mListener;
    
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_location_title)
        	   .setMessage(R.string.dialog_text)
               .setPositiveButton(R.string.dialog_settings, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	// Send the positive button event back to the host activity
                       mListener.onDialogPositiveClick(ErrorDialogFragment.this);
                   }
               })
               .setNegativeButton(R.string.dialog_skip, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	  // Send the negative button event back to the host activity
                       mListener.onDialogNegativeClick(ErrorDialogFragment.this);
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
	
	// Override Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

}
