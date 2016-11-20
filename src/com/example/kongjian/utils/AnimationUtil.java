package com.example.kongjian.utils;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AnimationUtil{
	public static int animationCount = 0;
	
	public static void rotateOutAnimation(RelativeLayout relativeLayout, long delay) {
		RotateAnimation rotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
		rotateAnimation.setDuration(500);
		rotateAnimation.setFillAfter(true);
		rotateAnimation.setStartOffset(delay);
		relativeLayout.startAnimation(rotateAnimation);
		rotateAnimation.setAnimationListener(new MyAnimationListener());
	}
	
	public static void rotateInAnimation(RelativeLayout relativeLayout, long delay) {
		RotateAnimation rotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
		rotateAnimation.setDuration(500);
		rotateAnimation.setFillAfter(true);
		rotateAnimation.setStartOffset(delay);
		relativeLayout.startAnimation(rotateAnimation);
		rotateAnimation.setAnimationListener(new MyAnimationListener());
	}

	static class MyAnimationListener implements AnimationListener{

		@Override
		public void onAnimationStart(Animation animation) {
			animationCount ++;
			
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			animationCount --;
			
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
}
