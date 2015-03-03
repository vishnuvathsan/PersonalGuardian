package com.vishnu.personalguardian.logic;

public class MyLocation {
	private String address;
	private double lattitude;
	private double longitude;

	public MyLocation() {
		
	}
	
	public MyLocation(String address, double lattitude, double longitude) {
		super();
		this.address = address;
		this.lattitude = lattitude;
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLattitude() {
		return lattitude;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
