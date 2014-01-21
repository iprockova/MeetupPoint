package com.example.meetuppoint;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.*;

public final class Util {
	// Reads an InputStream and converts it to a String.
		 public String readIt(InputStream inputStream) throws IOException, UnsupportedEncodingException {

		     
		     StringWriter writer = new StringWriter();
		     IOUtils.copy(inputStream, writer, "UTF-8");
		     String theString = writer.toString();
		     return theString;
		     
//		     Reader reader = null;
//		     reader = new InputStreamReader(stream, "UTF-8");        
//		     char[] buffer = new char[len];
//		     reader.read(buffer);
//		     return new String(buffer);
		 }
		 
		 public boolean googlePlayServiceConnected(Activity activity) {
			    int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity.getApplicationContext());
		    	
			    // If Google Play services is available
		    	if (errorCode == ConnectionResult.SUCCESS) {
		    	   return true;
		    	} // Google Play services was not available for some reason
		    	else if (errorCode == ConnectionResult.SERVICE_MISSING ||
		    			 errorCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED ||
		    			 errorCode == ConnectionResult.SERVICE_DISABLED) {
		    		GooglePlayServicesUtil.getErrorDialog(errorCode, activity, 0).show();
		    	}
		    	return false;
		    }

}
