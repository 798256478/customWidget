package com.example.kongjian.view;

import android.content.Context;
import android.graphics.Canvas;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class SlideMenu extends ViewGroup {

	private static final int OPEN = 0;
	private static final int CLOSE = 1;
	private Scroller scroller;
	private float downX;
	private float moveX;
	private View leftMenu;
	private int currentState;
	private int offsetX;
	private float downY;
	private float moveY;

	public SlideMenu(Context context) {
		super(context);
		init();
	}

	public SlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlideMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		scroller = new Scroller(getContext());
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		leftMenu = getChildAt(0);
		leftMenu.measure(leftMenu.getLayoutParams().width, leftMenu.getLayoutParams().height);
		
		View mainContent = getChildAt(1);
		mainContent.measure(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		leftMenu.layout(-leftMenu.getMeasuredWidth(), 0, 0, b);
		getChildAt(1).layout(l, t, r, b);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = event.getX();
			
			break;
		case MotionEvent.ACTION_MOVE:
			moveX = event.getX();
			int newScrollX = getScrollX() - (int)(moveX - downX);
			if(newScrollX < -getChildAt(0).getMeasuredWidth()){
				scrollTo(-getChildAt(0).getMeasuredWidth(), 0);
			} else if(newScrollX > 0){
				scrollTo(0, 0);
			} else {
				scrollBy((int)(downX - moveX), 0);
			}
			break;
		case MotionEvent.ACTION_UP:
			if(getScrollX() < -getChildAt(0).getMeasuredWidth()/2){
				currentState = OPEN;
			}else{
				currentState = CLOSE;
			}
			updateScroll();
			break;		
		}
		return true;
	}

	private void updateScroll() {
		if(currentState == OPEN){
			offsetX = -getChildAt(0).getMeasuredWidth() - getScrollX();
		} else {
			offsetX = 0 - getScrollX();
			Log.i("state", offsetX +"");
		}
		
		/*平滑滚动*/
		int duration = Math.abs(offsetX * 2);
		scroller.startScroll(getScrollX(), 0, offsetX, 0, duration);
		invalidate();
	}
	
	@Override
	public void computeScroll() {
		super.computeScroll();
		/*如果还存在偏移量*/
		if(scroller.computeScrollOffset()){
			/*获取当前模拟数据，就是要滚动到的位置*/
			int currx = scroller.getCurrX();
			scrollTo(currx, 0);
			invalidate();
		}
	}
	
	public void open(){
		currentState = OPEN;
		updateScroll();
	}
	
	public void close(){
		currentState = CLOSE;
		updateScroll();
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = ev.getX();
			downY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			moveX = Math.abs(ev.getX() -  downX);
			moveY = Math.abs(ev.getY() - downY);
			if(moveX > moveY && moveX >10){
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			
			break;

		}
		return super.onInterceptTouchEvent(ev);
	}

}
