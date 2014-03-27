package com.baidu.fex.camera;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;

public class Plugin extends CordovaPlugin{
	
	
	
	
	@Override
	public boolean execute(String action, CordovaArgs args,
			CallbackContext callbackContext) throws JSONException {
		if("camera".equals(action)){
			this.start(args, callbackContext);
			return true;
		}
		
		
		return false;
	}
	
	private void start(CordovaArgs args,final CallbackContext callbackContext){
		
		
		try {
			 JSONObject jsonObject = args.getJSONObject(0);
			 final String mask = jsonObject.getString("maskUrl");
			 cordova.getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					 CameraActivity.open(webView.getContext(), new CameraActivity.CameraLisenter() {
							
							private static final long serialVersionUID = 311320768459729904L;

							@Override
							public void onCapture(Result result) {
								callbackContext.success(new Gson().toJson(result));
								
							}

							@Override
							public void onError(String msg) {
								callbackContext.error(msg);
								
							}
						}, mask);
					
				}
			});
			
		} catch (JSONException e) {
			e.printStackTrace();
			callbackContext.error(e.getMessage());
		}
		
	}
	
}

