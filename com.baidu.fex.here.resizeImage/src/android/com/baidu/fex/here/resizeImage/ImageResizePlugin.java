
package com.baidu.fex.here.resizeImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

public class ImageResizePlugin extends CordovaPlugin {

	private Context context;

	private static File storageDir;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		// TODO Auto-generated method stub
		super.initialize(cordova, webView);
		this.context = webView.getContext();
		storageDir = new File(context.getExternalCacheDir(), "imageResize");
		if (!storageDir.exists()) {
			storageDir.mkdirs();
		}
	}

	@Override
	public boolean execute(String action, CordovaArgs args,
			final CallbackContext callbackContext) throws JSONException {

		if ("load".equals(action)) {

			JSONObject param = args.getJSONObject(0);

			final String filepath = param.getString("filepath");

			final int width = param.getInt("width");

			final int height = param.getInt("height");

			final File storageFile = getStorageFile(filepath, width, height);

			

			new AsyncTask<Void, Void, byte[]>() {

				@Override
				protected byte[] doInBackground(Void... params) {
					Bitmap bitmap = null;
					if(storageFile.exists()){
						bitmap = BitmapFactory.decodeFile(storageFile.toString());
					}else{
						bitmap = decodeFile(filepath, width, height);
					}
					byte[] res = bitmapToBytes(bitmap);
					bitmap.recycle();
					return res;
				}

				protected void onPostExecute(byte[] result) {
					String base64 = Base64.encodeToString(result,
							Base64.NO_WRAP);
					callbackContext.success(base64);
					if(!storageFile.exists()){
						saveByte(result,storageFile);
					}
					
				};

			}.execute();

			

			return true;

		}

		return false;
	}

	private void saveByte(final byte[] bytes, final File storage) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(storage);
					fos.write(bytes);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.flush();
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}

			}
		}).start();
	}

	public static byte[] bitmapToBytes(Bitmap bitmap) {

		byte[] result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);

				baos.flush();
				baos.close();

				result = baos.toByteArray();

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static File getStorageFile(String filepath, int width, int height) {
		return new File(storageDir, ""
				+ (filepath + "_" + width + "_" + height).hashCode());
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int ReqHeight, int ReqWidth) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSzie = 1;

		if (height > ReqHeight || width > ReqWidth) {
			if (width > height) {
				inSampleSzie = Math.round((float) height / (float) ReqHeight);
			} else {
				inSampleSzie = Math.round((float) width / (float) ReqWidth);
			}
		}
		return inSampleSzie;
	}

	public static Bitmap decodeFile(String fileName, int width, int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(fileName, options);

		options.inSampleSize = calculateInSampleSize(options, width, height);
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(fileName, options);
	}

}
