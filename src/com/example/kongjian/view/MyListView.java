package com.example.kongjian.view;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.kongjian.R;

import android.content.Context;
import android.graphics.Canvas;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class MyListView extends ListView implements OnScrollListener{
	
	private static final int DRAW_DOWN = 0;
	private static final int RELEASE_REFLUSH = 1;
	private static final int REFLUSH = 2;
	private View view;
	private RotateAnimation downRotateAnimation;
	float downY = 0;
	private int measuredHeight;
	private TextView tv_state;
	private TextView tv_update_time;
	private int state = 0;
	private int eventState;
	private ImageView iv_draw_arrow;
	private RotateAnimation upRotateAnimation;
	private ProgressBar pb_reflush;
	private OnReflushListener onReflushListener;
	private View footerView;
	private boolean isLoading;

	public MyListView(Context context) {
		super(context);
		init();
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		addHeader();
		initAnimation();
		initFooter();
		setOnScrollListener(this);
	}

	private void initFooter() {
		footerView = View.inflate(getContext(), R.layout.list_view_footer, null);
		footerView.measure(0, 0);
		footerView.setPadding(0, -footerView.getMeasuredHeight(), 0, 0);
		addFooterView(footerView);       
	}

	private void initAnimation() {
		upRotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		upRotateAnimation.setDuration(500);
		upRotateAnimation.setFillAfter(true);
		
		downRotateAnimation = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		downRotateAnimation.setDuration(500);
		downRotateAnimation.setFillAfter(true);
	}

	private void addHeader() {
		view = View.inflate(getContext(), R.layout.list_view_header, null);
		view.measure(0, 0);
		measuredHeight = view.getMeasuredHeight();
		view.setPadding(0, -measuredHeight, 0, 0);
		addHeaderView(view);
		
		tv_state = (TextView) view.findViewById(R.id.tv_state);
		tv_update_time = (TextView) view.findViewById(R.id.tv_update_time);
		iv_draw_arrow = (ImageView) findViewById(R.id.iv_draw_arrow);
		pb_reflush = (ProgressBar) findViewById(R.id.pb_reflush);
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(getFirstVisiblePosition()!=0){
			return super.onTouchEvent(ev);
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = ev.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			float moveY = ev.getY();
			int offset = (int)(moveY - downY);
			if(offset >0){
				int paddingTop = -measuredHeight + offset;
				view.setPadding(0, paddingTop, 0, 0);

				if(paddingTop >= 0 && state != RELEASE_REFLUSH){
					state = RELEASE_REFLUSH;
					updateState();
				} else if(paddingTop < 0 && state != DRAW_DOWN){
					state = DRAW_DOWN;	
					updateState();
				}
				
				return true;
			}
			break;
			
		case MotionEvent.ACTION_UP:
			if(state == DRAW_DOWN){
				view.setPadding(0, -measuredHeight, 0, 0);
				
			} else {
				view.setPadding(0, 0, 0, 0);
				state = REFLUSH;
				updateState();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	private void updateState() {
		switch (state) {
		case DRAW_DOWN:
			tv_state.setText("下拉刷新");
			iv_draw_arrow.startAnimation(downRotateAnimation);
			break;
		case RELEASE_REFLUSH:
			tv_state.setText("释放刷新");
			iv_draw_arrow.startAnimation(upRotateAnimation);
			break;
		case REFLUSH:
			iv_draw_arrow.clearAnimation();
			tv_state.setText("正在刷新中...");
			iv_draw_arrow.setVisibility(View.INVISIBLE);
			pb_reflush.setVisibility(View.VISIBLE);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date curDate = new Date(System.currentTimeMillis());
			tv_update_time.setText("最后更新时间：" + simpleDateFormat.format(curDate));
			onReflushListener.onReflush();
			break;
		}
	}
	
	

	
	public interface OnReflushListener{
		void onReflush();
		void onLoad();
	}
	
	public void setOnReflushListener(OnReflushListener onReflushListener){
		this.onReflushListener = onReflushListener;
	}

	public void reflushOver() {
		view.setPadding(0, -measuredHeight, 0, 0);
		tv_state.setText("下拉刷新");
		state = DRAW_DOWN;
		iv_draw_arrow.setVisibility(View.VISIBLE);
		pb_reflush.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(isLoading){
			return;
		}
		if(scrollState == SCROLL_STATE_IDLE){
			
			if(getLastVisiblePosition() >= (getCount()-1)){
				isLoading = true;
				footerView.setPadding(0, 0, 0, 0);
				setSelection(getCount());
				onReflushListener.onLoad();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	public void loadOver() {
		isLoading = false;
		footerView.setPadding(0, -footerView.getMeasuredHeight(), 0, 0);
	}



}
