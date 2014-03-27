package com.baidu.fex.camera;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class Utils {
	
	
	
	public static interface LocationListener{
		public void onReceiveLocation(BDLocation location);
	}
	

	public static LocationClient getLocation(Context context){
		LocationClient mLocationClient = new LocationClient(context);
		LocationClientOption option = new LocationClientOption();
		option.setTimeOut(50000);
		option.setScanSpan(100);
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
		option.setIsNeedAddress(true);//返回的定位结果包含地�?���?
		option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		return mLocationClient;
		
			
		
	}
	
}
