package com.example.kongjian;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.kongjian.utils.AnimationUtil;
import com.example.kongjian.view.SwitchButton;
import com.example.kongjian.view.SwitchButton.OnSwitchStateListener;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener, OnPageChangeListener{

	private RelativeLayout rl_level1;
	private RelativeLayout rl_level2;
	private RelativeLayout rl_level3;
	
	private Boolean level1IsShow = true;
	private Boolean level2IsShow = true;
	private Boolean level3IsShow = true;
	private ViewPager vp_lunbo;
	private List<ImageView> imageList;
	private String[] contentDescs;
	private TextView tv_image_des;
	private LinearLayout ll_point_group;
	private int prevPointId = 0;
	private Timer timer;
	private EditText et_search;
	private ListView popListView;
	private List<String> popListDatas;
	private PopupWindow popupWindow;
	private MyListAdapter myListAdapter;
	private ListView mlv_list_view;
	private List<String> customArrayList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		initView();
		initData();
		initAdapter();
	}

	private void initData() {
		int[] imageIds = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};
		imageList = new ArrayList<ImageView>();
		for (int i = 0; i < imageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imageIds[i]);
			TextView textView = new TextView(this);
			LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
			if(i!=0){	
				layoutParams.setMargins(20, 0, 0, 0);
			}
			textView.setLayoutParams(layoutParams);
			textView.setEnabled(false);
			textView.setBackgroundResource(R.drawable.select_lunbo_point_bg);
			ll_point_group.addView(textView);
			imageList.add(imageView);
		}
		
		contentDescs = new String[]{
				"巩俐不低俗，我就不能低俗",
				"扑树又回来啦！再唱经典老歌引万人大合唱",
				"揭秘北京电影如何升级",
				"乐视网TV版大派送",
				"热血屌丝的反杀"
		};
		
		popListDatas = new ArrayList<String>();
		for (int i = 0; i < 15; i++) {
			popListDatas.add("123456" + i);
		}
		
		customArrayList = new ArrayList<String>();
		for (int i = 0; i < 30; i++) {
			customArrayList.add("这是初始listView中的数据"+ i);
		}
	}

	private void initAdapter() {
		MyAdapter myAdapter = new MyAdapter();
		vp_lunbo.setAdapter(myAdapter);
		vp_lunbo.setCurrentItem(5000000);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						vp_lunbo.setCurrentItem(vp_lunbo.getCurrentItem() + 1);
					}
					
				});
			}
		}, 3000, 3000);
		
		vp_lunbo.setOnPageChangeListener(this);
		tv_image_des.setText(contentDescs[0]);
		ll_point_group.getChildAt(0).setEnabled(true);
		
		popListView = new ListView(this);
		popListView.setBackgroundResource(R.drawable.listview_background);
		popListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				et_search.setText(popListDatas.get(position));
				popupWindow.dismiss();
			}
		});
		myListAdapter = new MyListAdapter();
		popListView.setAdapter(myListAdapter);
		
		CustomListAdapter customListAdapter = new CustomListAdapter();
		mlv_list_view.setAdapter(customListAdapter);
	}
	
	class CustomListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return customArrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return customArrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView = new TextView(getApplicationContext());

			textView.setText(customArrayList.get(position));
			textView.setTextSize(18);
			return textView;
		}
		
	}
	
	class MyListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return popListDatas.size();
		}

		@Override
		public String getItem(int position) {
			return popListDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = View.inflate(getApplicationContext(), R.layout.item_pop_list, null);
			}
			
			TextView tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
			tv_user_name.setText(getItem(position));
			
			convertView.findViewById(R.id.ib_delete_btn).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					popListDatas.remove(position);
					myListAdapter.notifyDataSetChanged();
					if(popListDatas.size() == 0){
						popupWindow.dismiss();
					}
				}
			});
			
			return convertView;
		}
		
	}
	
	class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			int currentPosition = position % imageList.size();
			container.removeView(imageList.get(currentPosition));
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			int currentPosition = position % imageList.size();
			ImageView imageView = imageList.get(currentPosition);
			container.addView(imageView);
			
			return imageView;
		}
		
	}

	private void initView() {
		findViewById(R.id.ib_home).setOnClickListener(this);
		findViewById(R.id.ib_menu).setOnClickListener(this);
		
		rl_level1 = (RelativeLayout) findViewById(R.id.rl_level1);
		rl_level2 = (RelativeLayout) findViewById(R.id.rl_level2);
		rl_level3 = (RelativeLayout) findViewById(R.id.rl_level3);
		
		vp_lunbo = (ViewPager) findViewById(R.id.vp_lunbo);
		
		tv_image_des = (TextView) findViewById(R.id.tv_image_des);
		ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
		
		et_search = (EditText) findViewById(R.id.et_search);
		findViewById(R.id.ib_down_arrow).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showPopUpWindow();
			}
		});
		
		SwitchButton sb_switch_btn = (SwitchButton) findViewById(R.id.sb_switch_btn);
		sb_switch_btn.setOnSwitchStateListener(new OnSwitchStateListener() {
			
			@Override
			public void onSwitchState(boolean switch_state) {
				if(switch_state){
					Toast.makeText(getApplicationContext(), "开关打开了", 1000).show();
				}else {
					Toast.makeText(getApplicationContext(), "开关关闭了", 1000).show();
				}
			}
		});
		
		mlv_list_view = (ListView) findViewById(R.id.mlv_list_view);
	}

	@Override
	public void onClick(View v) {
		if(AnimationUtil.animationCount == 0){
			switch (v.getId()) {
			case R.id.ib_home:
				if(level2IsShow){
					if(level3IsShow){
						AnimationUtil.rotateOutAnimation(rl_level3, 200);
						level3IsShow = false;
					}
					AnimationUtil.rotateOutAnimation(rl_level2, 0);
				}else{
					AnimationUtil.rotateInAnimation(rl_level3, 200);
					AnimationUtil.rotateInAnimation(rl_level2, 0);
					
					level3IsShow = true;
				}
				level2IsShow = !level2IsShow;
				break;
				
			case R.id.ib_menu:
				if(level3IsShow){
					AnimationUtil.rotateOutAnimation(rl_level3, 0);
				} else{
					AnimationUtil.rotateInAnimation(rl_level3, 0);
				}
				level3IsShow = !level3IsShow;
				break;
			}
		}
		
	}
	
	
	private void showPopUpWindow() {
		popupWindow = new PopupWindow(popListView, et_search.getWidth(), 300);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.showAsDropDown(et_search, 0, 5, Gravity.CENTER);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_MENU){
			if(AnimationUtil.animationCount > 0){
				return true;
			}
			if(level1IsShow){
				if(level2IsShow){
					if(level3IsShow){
						AnimationUtil.rotateOutAnimation(rl_level3, 400);
						level3IsShow = false;
					}
					AnimationUtil.rotateOutAnimation(rl_level2, 200);
					level2IsShow = false;
				}
				AnimationUtil.rotateOutAnimation(rl_level1, 0);
				level1IsShow = false;
				
			} else {

				AnimationUtil.rotateInAnimation(rl_level3, 400);
				AnimationUtil.rotateInAnimation(rl_level2, 200);
				AnimationUtil.rotateInAnimation(rl_level1, 0);
				
				level1IsShow = true;
				level2IsShow = true;
				level3IsShow = true;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		int currentPosition = arg0 % 5;
		
		tv_image_des.setText(contentDescs[currentPosition]);
		ll_point_group.getChildAt(prevPointId).setEnabled(false);
		ll_point_group.getChildAt(currentPosition).setEnabled(true);
		prevPointId  = currentPosition;
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		timer.cancel();
	}


}
