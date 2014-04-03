package com.baidu.fex.camera;

import java.io.Serializable;
import android.graphics.BitmapFactory;

import com.baidu.location.BDLocation;

public class Result implements Serializable{

	public static final int ORIENTATION_LANDSCAPE = 1;
	
	public static final int ORIENTATION_PORTRAIT = 2;
	
	private static final long serialVersionUID = 1L;

	private float sensorX;
	
	private float sensorY;
	
	private float sensorZ;
	
	private long datetime;
	
	private double latitude;
	
	private double lontitude;
	
	private double radius;
	
	private int orientation;
	
	private int width;
	
	private int height;
	
	private String filepath;
	
	private Result(){}

	public float getSensorX() {
		return sensorX;
	}

	public float getSensorY() {
		return sensorY;
	}

	public float getSensorZ() {
		return sensorZ;
	}

	public long getDatetime() {
		return datetime;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLontitude() {
		return lontitude;
	}

	public double getRadius() {
		return radius;
	}

	public int getOrientation() {
		return orientation;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public String getFilepath() {
		return filepath;
	}
	
	public static Result createresult(String filepath,int width,int height,int orientation, BDLocation location,float[] sensor){
	
		Result result = new Result();
		result.filepath = filepath;
		if(sensor != null ){
			result.sensorX = sensor[0];
			result.sensorY = sensor[1];
			result.sensorZ = sensor[2];
		}
		
		result.datetime = System.currentTimeMillis();
		if(location != null){
			result.latitude = location.getLatitude();
			result.lontitude = location.getLongitude();
			result.radius = location.getRadius();
		}
		result.orientation = orientation;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		result.width = width;
		result.height = height;
		return result;
		
	}
	
	
}
