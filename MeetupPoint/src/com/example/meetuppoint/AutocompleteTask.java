package com.example.meetuppoint;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AutocompleteTask extends AsyncTask<String, Void, String> {
	
	@Override
    protected String doInBackground(String... urls) {
        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            //return "Unable to retrieve web page. URL may be invalid.";
        	return null;
        }
    }
	
	 // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String resultStr) {
    	//parse result and pass it to location activity
    	//locationActivity.showListOfAutoComplete(parseFile(resultStr));
   }
	
	 //Given a URL, establishes an HttpUrlConnection and retrieves
	 // the web page content as a InputStream, which it returns as
	 // a string.
	 private String downloadUrl(String myurl) throws IOException {
	     InputStream is = null;
	     // Only display the first 500 characters of the retrieved
	     // web page content.
	     int len = 500000;
	         
	     try {
	         URL url = new URL(myurl);
	         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	         conn.setReadTimeout(10000 /* milliseconds */);
	         conn.setConnectTimeout(15000 /* milliseconds */);
	         conn.setRequestMethod("GET");
	         conn.setDoInput(true);
	         // Starts the query
	         conn.connect();
	         int response = conn.getResponseCode();
	         //Log.d(DEBUG_TAG, "The response is: " + response);
	         is = conn.getInputStream();
	
//	         // Convert the InputStream into a string
	         String contentAsString = new Util().readIt(is);
	         
	         return contentAsString;
	         
	     // Makes sure that the InputStream is closed after the app is
	     // finished using it.
	     } finally {
	         if (is != null) {
	             is.close();
	         } 
	     }
	 }
	 
	 public ArrayList<String> parseFile(String jsonStr){
		 
			ArrayList<String> resultList = null;
			
		    //TODO check status first for invalid request
		    
		    if (jsonStr != null) {
	            try {
	            	// Create a JSON object hierarchy from the results
	                JSONObject jsonObj = new JSONObject(jsonStr);
	                JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

	                // Extract the Place descriptions from the results
	                resultList = new ArrayList<String>(predsJsonArray.length());
	                for (int i = 0; i < predsJsonArray.length(); i++) {
	                    resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
	                }
	                return resultList;
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
		    }
			return null;
		}

	

}
