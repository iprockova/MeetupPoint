package com.example.meetuppoint.activities;

import java.util.ArrayList;
import java.util.List;

import com.example.meetuppoint.DownloadPlacesTask;
import com.example.meetuppoint.ErrorDialogFragment;
import com.example.meetuppoint.LocationListArrayAdapter;
import com.example.meetuppoint.Place;
import com.example.meetuppoint.R;
import com.example.meetuppoint.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class LocationActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener, ErrorDialogFragment.NoticeDialogListener{
		
	 // Define a map variable 
	 private GoogleMap mMap;
	 
	 // Google Maps API URL
	 private static String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyDWFLPak4uRRCLAoefCUi956GQLbKe45pI&";
	
	 // Define a location client 
	 private LocationClient mLocationClient;
	 
	 // Define an object that holds accuracy and frequency parameters
	 private LocationRequest mLocationRequest;
	 
	 // Define a current location object
	 private Location location;
	 
	 // Define a request code to send to Google Play services
	 private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	 
	 // Define intervals for sending update requests
	 private static final long UPDATE_INTERVAL = 500;
	 private static final long FASTEST_INTERVAL = 300;
	 
	 // Name of Preferences file
	 public static final String PREFS_NAME = "MyPrefsFile";
	 
	 // Define a listview object
	 private ListView listview;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		
		location = new Location("Current Location Provider");
		mLocationClient = new LocationClient(this, this, this);
		
		mLocationRequest = LocationRequest.create();	
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setNumUpdates(1);
        
        // Read previous location from Shared file and set it as current location
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        double locationLat =  Double.parseDouble(settings.getString("currentLocationLat", "60.1708"));
        double locationLon = Double.parseDouble(settings.getString("currentLocationLon", "24.9375"));
        location.setLatitude(locationLat);
        location.setLongitude(locationLon);
		
        // Image Loader, Initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);
        
        listview = (ListView) findViewById(R.id.listview);
        
        /*
         * Respond to click events on each list item click
         */
        final OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				// Get the text of the list item that was clicked
				TextView textView = (TextView) v.findViewById(R.id.txtTitle);
				String item = textView.getText().toString();
				
				// Close this activity and return the result to the previous activity
				Intent i = new Intent(LocationActivity.this, CreateEventActivity.class);
				i.putExtra("location", item);
				setResult(RESULT_OK, i);
				finish();
			}
        };
        listview.setOnItemClickListener(mMessageClickedHandler); 
	}
	
	/*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        if(new Util().googlePlayServiceConnected(this))
        	mLocationClient.connect();
    }
	@Override
	protected void onResume() {
		super.onResume();
	    setUpMap();
	    //getNearbyPlaces();
	}
	
	@Override
    public void onPause() {
        super.onPause();
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
    }
	
	/*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
    	// If the client is connected
        if (mLocationClient.isConnected()) {
             //Remove location updates.
        	mLocationClient.removeLocationUpdates(this);
        }
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        
        super.onStop();
    }
    /*
     * Initialize and show Google Map with current user's location
     */
    private void setUpMap() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                // Move the camera to Helsinki - a default location at start
                LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                	moveCameraToCurrentLocation();
                }
            }
        } 
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
		return true;
	}
	
	/*
     * Called by Location Services when the request to connect the
     * client finishes successfully.
     */
	@Override
	public void onConnected(Bundle arg0) {
		// Display the connection status
       // Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
      
        //GPS or WiFi return a location
        if (mLocationClient != null &&  mLocationClient.isConnected()) {
        	location = mLocationClient.getLastLocation();
        	
        	if(location != null) {
        		moveCameraToCurrentLocation();
        		getNearbyPlaces();
        		
        		// save current location to Shared Preff
            	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        	    SharedPreferences.Editor editor = settings.edit();
        	    editor.putString("currentLocationLat", String.valueOf(location.getLatitude()));
        	    editor.putString("currentLocationLon", String.valueOf(location.getLongitude()));
        	    editor.commit();
        	} 
        }// GPS or WiFi not available
        else {
        	//moveCameraToCurrentLocation();
    		//getNearbyPlaces();
        	
        	// Show error dialog to enable GPS
        	showErrorDialog();
        }
	}

	/*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 1).show();
        }
	}
	
	/*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
	@Override
	public void onDisconnected() {
		// Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();		
	}
	
	/*
	 * Position the camera of the Map to point to that location
	 */
	private void moveCameraToCurrentLocation(){
		// Set the camera towards current location 
    	CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(new LatLng(location.getLatitude(), location.getLongitude()))    // Sets the center of the map to current loc
        .zoom(13)                   // Sets the zoom
        .build();                   // Creates a CameraPosition from the builder
    	mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	
	/*
	 * Download nearby places with Google Places API
	 */
	private void getNearbyPlaces() {
		if(networkConnectionAvailable()){
			if(location!=null){
				String latitude = String.valueOf(location.getLatitude());
				String longitude =  String.valueOf(location.getLongitude());
				 
				 String stringUrl = url + "location=" + latitude + "," + longitude + "&radius=1000&sensor=false";
				 new DownloadPlacesTask(this).execute(stringUrl);
			}
		}else{
			 Toast.makeText(this, "Not Connected to the network!", Toast.LENGTH_LONG).show();
		}
	}
	
	/*
	 * Verify network connectivity on phone
	 */
	private boolean networkConnectionAvailable(){
    	ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    	if (networkInfo != null && networkInfo.isConnected()) {
    	        return true;
    	} else {
    	        return false;
    	}
    }
	
	
	public void showErrorDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new ErrorDialogFragment();
        dialog.show(getSupportFragmentManager(), "ErrorDialogFragment");
    }
	
	/* The dialog fragment receives a reference to this Activity through the
    * Fragment.onAttach() callback, which it uses to call the following methods
    * defined by the ErrorDialogFragment.ErrorDialogListener interface
    */
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
 	   	Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS ); 
 	   	startActivity(myIntent);
	}
	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
    	//moveCameraToDefaultTarget();
		
	}
	
	/*
	 * Callback method that get called when a location is updated. When new location is found,
	 *  move the camera of the map to that location and show nearby places. Also, save the new
	 *  location to the Pref file.
	 */
	@Override
	public void onLocationChanged(Location location) {
		this.location.setLatitude(location.getLatitude());
		this.location.setLongitude(location.getLongitude());
		
		moveCameraToCurrentLocation();
		getNearbyPlaces();
		
		// save current location to Shared Preff
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString("currentLocationLat", String.valueOf(location.getLatitude()));
	    editor.putString("currentLocationLon", String.valueOf(location.getLongitude()));
	    editor.commit();
	}
	
	public void showListOfPlaces(List<Place> places) {
		 if(places != null){
			 ArrayList<String> names = new ArrayList<String>();
			 for (int i = 0; i < places.size(); i++) {
				 names.add(places.get(i).getName());
			 }
			 ArrayList<String> urls = new ArrayList<String>();
			 for (int i = 0; i < places.size(); i++) {
				 urls.add(places.get(i).getLogoURL());
			 }
			 LocationListArrayAdapter adapter = new LocationListArrayAdapter(this, names, urls);
			 listview.setAdapter(adapter);
		 }
	}
}











