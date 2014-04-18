package com.baidu.fex.share;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.fex.here.R;
import com.baidu.fex.share.ShareUtils.Wechat;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Plugin extends CordovaPlugin{
	
	private Context context;
	
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		// TODO Auto-generated method stub
		super.initialize(cordova, webView);
		this.context = webView.getContext();
	}
	
	@Override
	public boolean execute(String action, final CordovaArgs args,
			CallbackContext callbackContext) throws JSONException {
		
		if("wechat".equals(action)){
			cordova.getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					
					
					JSONObject object;
					try {
						object = args.getJSONObject(0);
						String data = object.getString("data");
						int scene = object.getInt("scene");
						byte[] bytes = null;
						if("file".equals(object.getString("type"))){
							bytes = ShareUtils.bmpToByteArray(BitmapFactory.decodeFile(data), true);
//							bytes = ShareUtils.bmpToByteArray(BitmapFactory.decodeResource(context.getResources(),R.drawable.icon), true);
						}else if("datauri".equals(object.getString("type"))){
							bytes = Base64.decode(data, Base64.DEFAULT);
						}
						Wechat wechat = new Wechat(cordova.getActivity());
						
						wechat.sharePhoto(bytes, scene);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			
			
		}
		
		return super.execute(action, args, callbackContext);
		
	}
}
