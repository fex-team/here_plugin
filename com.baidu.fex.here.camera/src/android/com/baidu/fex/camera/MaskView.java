package com.baidu.fex.camera;


import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.graphics.PorterDuff;
public class MaskView extends ImageView {

	public MaskView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MaskView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MaskView(Context context) {
		super(context);
		init();
	}

	private Paint clearPaint = new Paint();

	private void init() {
		clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		clearPaint.setColor(getContext().getResources().getColor(
				R.color.transparent));
		clearPaint.setFilterBitmap(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		int w = getWidth(),h = getHeight();
		
		canvas.drawCircle(w/2, h/2, h/3, clearPaint);

	}

}
