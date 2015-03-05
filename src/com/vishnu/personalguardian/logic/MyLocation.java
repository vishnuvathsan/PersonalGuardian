package com.vishnu.personalguardian.logic;

public class MyLocation {
	private String address;
	private double lattitude;
	private double longitude;
	private double distanceToDest;	//in kilometer
	private long time;

	public MyLocation() {
		
	}
	
	public MyLocation(String address, double lattitude, double longitude) {
		super();
		this.address = address;
		this.lattitude = lattitude;
		this.longitude = longitude;
	}

	public MyLocation(double lattitude, double longitude,
			double distanceToDest, long time) {
		super();
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.distanceToDest = distanceToDest;
		this.time = time;
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

	public double getDistanceToDest() {
		return distanceToDest;
	}

	public void setDistanceToDest(double distanceToDest) {
		this.distanceToDest = distanceToDest;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
