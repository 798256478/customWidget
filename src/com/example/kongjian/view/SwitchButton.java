package com.example.kongjian.view;

import com.example.kongjian.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SwitchButton extends View {

	private Bitmap slide_button;
	private Paint paint;
	private Bitmap bitmap_switch_background;
	private Bitmap bitmap_slide_button;
	private float buttonLocationX;
	private float downX;
	private Boolean switch_state;
	private Boolean is_touch = false;
	private OnSwitchStateListener onSwitchStateListener;

	public SwitchButton(Context context) {
		super(context);
		init();
	}

	public SwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		String namespace = "http://schemas.android.com/apk/res/com.example.kongjian";
		int switch_background = attrs.getAttributeResourceValue(namespace, "switch_background", -1);
		int slide_button = attrs.getAttributeResourceValue(namespace, "slide_button", -1);
		switch_state = attrs.getAttributeBooleanValue(namespace, "switch_state", false);
		
		bitmap_switch_background = BitmapFactory.decodeResource(getResources(), switch_background);
		bitmap_slide_button = BitmapFactory.decodeResource(getResources(), slide_button);
	}

	public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		paint = new Paint();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(bitmap_switch_background.getWidth(), bitmap_switch_background.getHeight());
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(bitmap_switch_background, 0, 0, paint);
		if(is_touch){
			if(buttonLocationX < 0){
				canvas.drawBitmap(bitmap_slide_button, 0, 0, paint);
			}else if(buttonLocationX > (bitmap_switch_background.getWidth()-bitmap_slide_button.getWidth())){
				canvas.drawBitmap(bitmap_slide_button, (bitmap_switch_background.getWidth()-bitmap_slide_button.getWidth()), 0, paint);
			}else {
				canvas.drawBitmap(bitmap_slide_button, buttonLocationX, 0, paint);
			}
		}else{
			if(switch_state){
				canvas.drawBitmap(bitmap_slide_button, (bitmap_switch_background.getWidth()-bitmap_slide_button.getWidth()), 0, paint);
			}else{
				canvas.drawBitmap(bitmap_slide_button, 0, 0, paint);
			}
		}
	}
	
	Boolean state;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			state = switch_state;
			is_touch = true;
			downX = event.getX();
			buttonLocationX = downX - (bitmap_slide_button.getWidth()/2);
			break;
		case MotionEvent.ACTION_MOVE:
			downX = event.getX();
			buttonLocationX = downX - (bitmap_slide_button.getWidth()/2);
			if(buttonLocationX < 0){
				switch_state = false;
			}else if(buttonLocationX > (bitmap_switch_background.getWidth()-bitmap_slide_button.getWidth())){
				switch_state = true;
			}
			break;
		case MotionEvent.ACTION_UP:
			is_touch = false;
			downX = event.getX();
			if(downX > (bitmap_switch_background.getWidth()/2)){
				switch_state = true;
			} else {
				switch_state = false;
			}
			if(state != switch_state && onSwitchStateListener != null){
				onSwitchStateListener.onSwitchState(switch_state);
			}
			break;
		}
		invalidate();
		return true;
	}
	
	public interface OnSwitchStateListener{
		void onSwitchState(boolean switch_state);
	}

	public void setOnSwitchStateListener(
			OnSwitchStateListener onSwitchStateListener) {
		this.onSwitchStateListener = onSwitchStateListener;
	}
}
