package com.baidu.fex.camera;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import com.baidu.fex.camera.CameraPreview.PreviewReadyCallback;
import com.baidu.fex.here.R;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class CameraActivity extends Activity implements OnClickListener,
		PreviewCallback, BDLocationListener,SensorEventListener  {
	
	public static final String PARAM_CAMERA_LISENTER = "PARAM_CAMERA_LISENTER";
	
	public static final String PARAM_MASK_URL = "PARAM_MASK_URL";
	
	public static interface CameraLisenter extends Serializable{
		
		public void onCapture(Result result);
		
		public void onError(String msg);
		
	}
	
	private ResizableCameraPreview mPreview;
	private RelativeLayout mLayout;
	private int mCameraId = 0;
	private LocationClient locationClient;
	private BDLocation bdLocation;
	private float[] sensor;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private WebView webView;
	private static CameraLisenter _cameraLisenter;
	private String maskUrl;
	
	
	public static void open(Context context,CameraLisenter cameraLisenter,String maskUrl) {
		Intent intent = new Intent(context, CameraActivity.class);
		_cameraLisenter = cameraLisenter;
		intent.putExtra(PARAM_MASK_URL, maskUrl);
		context.startActivity(intent);
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		maskUrl = getIntent().getStringExtra(PARAM_MASK_URL);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		locationClient = Utils.getLocation(this);
		locationClient.registerLocationListener(this);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		mLayout = (RelativeLayout) findViewById(R.id.layout);
		if(maskUrl != null){
			initWebview();
		}
		
		

		findViewById(R.id.capture).setOnClickListener(this);
	}
	
	private void initWebview(){
		webView = (WebView) findViewById(R.id.mask);
		webView.setBackgroundColor(0);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onConsoleMessage(ConsoleMessage cm) {
				Log.d("MyApplication",
						cm.message() + " -- From line " + cm.lineNumber()
								+ " of " + cm.sourceId());
				return true;
			}
		});

		webView.loadUrl(maskUrl);
	}

	@Override
	protected void onResume() {
		super.onResume();
		locationClient.start();
		createCameraPreview();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mPreview.setPreviewCallback(null);
		mPreview.stop();
		mLayout.removeView(mPreview);
		mPreview = null;
		locationClient.stop();
		mSensorManager.unregisterListener(this);
	}

	private void createCameraPreview() {
		mPreview = new ResizableCameraPreview(this, mCameraId,
				CameraPreview.LayoutMode.FitToParent, false);

		mPreview.setOnPreviewReady(new PreviewReadyCallback() {
			
			public void onPreviewReady() {
				mPreview.setPreviewCallback(CameraActivity.this);
				
			}
		});
		
		LayoutParams previewLayoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mLayout.addView(mPreview, 0, previewLayoutParams);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.capture:
			capture();
			break;
		}
	}

	private void capture() {
		locationClient.requestLocation();
		Camera.Parameters parameters = camera.getParameters();
		Size size = parameters.getPreviewSize();
		
		
		try {
			File dir = new File("/sdcard/hereApp");
			dir.mkdirs();
			File outputFile = File.createTempFile("capture", ".jpg", dir);
			String filepath = outputFile.toString().replace("/sdcard/","");
			YuvImage image = new YuvImage(data, parameters.getPreviewFormat(),
					size.width, size.height, null);
			FileOutputStream filecon = new FileOutputStream(outputFile);
			image.compressToJpeg(
					new Rect(0, 0, image.getWidth(), image.getHeight()), 100,
					filecon);
			if(_cameraLisenter != null){
				_cameraLisenter.onCapture(Result.createresult(filepath, image.getWidth(), image.getHeight(), 1, bdLocation, sensor));
			}
			_cameraLisenter = null;
		} catch (IOException e) {
			e.printStackTrace();
			_cameraLisenter.onError(e.getMessage());
		}

		
		
		finish();

	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQ_CONFIRM && resultCode == RESULT_OK){
			finish();
		}
	}
	
	private static final int REQ_CONFIRM = 10;

	private byte[] data;

	private Camera camera;

	public void onPreviewFrame(byte[] data, Camera camera) {
		this.data = data;
		this.camera = camera;
		camera.getParameters().getVerticalViewAngle();

	}

	public void onReceiveLocation(BDLocation bdLocation) {
		this.bdLocation = bdLocation;
	
	}

	public void onReceivePoi(BDLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_DOWN:
		case KeyEvent.KEYCODE_VOLUME_UP:
		case KeyEvent.KEYCODE_POWER:
			capture();
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		sensor = event.values;
		
	}

}
