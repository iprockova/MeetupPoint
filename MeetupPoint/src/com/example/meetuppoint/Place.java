package com.example.meetuppoint;

public class Place {

	private String name;
	private String logoURL;
	
	public Place(String name, String logoURL){
		this.name = name;
		this.logoURL = logoURL;
	}
	
	public String getName(){
		return name;
	}
	
	public String getLogoURL(){
		return logoURL;
	}
}
