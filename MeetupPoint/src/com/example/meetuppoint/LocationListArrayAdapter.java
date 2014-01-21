package com.example.meetuppoint;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationListArrayAdapter extends ArrayAdapter<String>{
	private final Context context;
	private final ArrayList<String> names;
	private final ArrayList<String> urls;
	
	public LocationListArrayAdapter(Context context, ArrayList<String> names, ArrayList<String> urls) {
	    super(context, R.layout.list_row, names);
	    this.context = context;
	    this.names = names;
	    this.urls = urls;
	  }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		
		if(convertView==null){
			// inflate the layout
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			rowView = inflater.inflate(R.layout.list_row, parent, false);
		}
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txtTitle);
		ImageView image= (ImageView) rowView.findViewById(R.id.icon); // thumb image
		 
		
		txtTitle.setText(names.get(position));
		ImageLoader.getInstance().displayImage(urls.get(position), image);
		return rowView;
	}

}
