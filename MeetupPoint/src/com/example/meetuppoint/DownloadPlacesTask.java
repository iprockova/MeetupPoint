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

import com.example.meetuppoint.activities.LocationActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.ListView;

	public class DownloadPlacesTask extends AsyncTask<String, Void, String> {
		private LocationActivity locationActivity;
		
		public DownloadPlacesTask(LocationActivity locationActivity){
			this.locationActivity = locationActivity;
		}
		
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
	    	locationActivity.showListOfPlaces(parseFile(resultStr));
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
		
//		         // Convert the InputStream into a string
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
		 
		 public ArrayList parseFile(String jsonStr){
				// Hashmap for ListView
			    //ArrayList<HashMap<String, String>> namesList = null;
			    //HashMap<String, String> names = null;
				List<Place> names = new ArrayList<Place>();
				
			    //TODO check status first for invalid request
			    
			    if (jsonStr != null) {
		            try {
		                JSONObject jsonObj = new JSONObject(jsonStr);
		                 
		                // Getting JSON Array node
		                JSONArray results = jsonObj.getJSONArray("results");

		                // looping through All Contacts
		                for (int i = 0; i < results.length(); i++) {
		                    JSONObject c = results.getJSONObject(i);
		                    
		                    String name = c.getString("name");
		                    String logoUrl = c.getString("icon");
		                    
		                    // adding each name to list
		                    names.add(i, new Place(name, logoUrl));

		                }
		                return (ArrayList) names;
		            } catch (JSONException e) {
		                e.printStackTrace();
		            }
			    }
				return null;
			}
	
	}
