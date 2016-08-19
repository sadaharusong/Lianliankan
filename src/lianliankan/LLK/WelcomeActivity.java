package lianliankan.LLK;

import lianliankan.LLK.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.*;
import android.view.animation.Animation.AnimationListener;
import android.widget.*;

public class WelcomeActivity extends Activity{
	private Button startBtn;
	private Button exitBtn;
	private Button aboutBtn;
	private Button setBtn;
	//private Button blueToothBtn;
	private TextView logo;
	private NotificationManager nMgr;
	
	private boolean music = false;
	private RefreshHandler mRedrawHandler;
	private int[] count = {0,0,0,0,0};
	private int[] sizes  = {10,10,10,10,10};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除程序的标题栏
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);   
		super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//去除系统消息提示栏，游戏全屏
		super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.begin);
		logo = (TextView)findViewById(R.id.logo);
		AnimationSet animation = (AnimationSet) AnimationUtils.loadAnimation(getApplicationContext(), R.anim.welcome);
		Interpolator i = new AccelerateDecelerateInterpolator();
		animation.setInterpolator(i);
		logo.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationEnd(Animation animation) {
				setContentView(R.layout.in);
				startBtn = (Button) findViewById(R.id.startBtn);
				exitBtn = (Button) findViewById(R.id.exitBtn);
				setBtn = (Button) findViewById(R.id.setBtn);
				aboutBtn = (Button) findViewById(R.id.aboutBtn);
				//blueToothBtn = (Button) findViewById(R.id.blueToothBtn);
				mRedrawHandler = new RefreshHandler();
				mRedrawHandler.sleep(50);

				/**
				 * 点击开始按钮，跳转到游戏主界面
				 */
				startBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startBtn.setBackgroundResource(R.drawable.color_bkg_3);
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putBoolean("music", music);
						intent.putExtras(bundle);
						intent.setClass(WelcomeActivity.this,LLKanActivity.class);
						WelcomeActivity.this.startActivity(intent);
					}
				});

				/**
				 * 点击设置按钮，跳转到游戏设置界面
				 */
				setBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						setBtn.setBackgroundResource(R.drawable.color_bkg_3);
						Intent intent = new Intent();
						intent.setClass(WelcomeActivity.this,
								SetGameActivity.class);
						WelcomeActivity.this.startActivity(intent);

					}
				});

				/**
				 * 点击关于按钮，跳转到游戏关于界面
				 */
				aboutBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						aboutBtn.setBackgroundResource(R.drawable.color_bkg_3);
						Intent intent = new Intent();
						intent.setClass(WelcomeActivity.this,
								AboutActivity.class);
						WelcomeActivity.this.startActivity(intent);
					}
				});
				
			
				/**
				 * 点击退出按钮，提示是否退出 是：返回欢迎界面 否：继续游戏
				 */
				exitBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						exitBtn.setBackgroundResource(R.drawable.color_bkg_3);
						Builder builder = MyControl.showAlert(R.string.exit_title, R.string.exit_msg,
								R.drawable.p10, WelcomeActivity.this);
						AlertDialog alert = builder.setNegativeButton(R.string.cancel_btn,
										new android.content.DialogInterface.OnClickListener() {
											public void onClick(DialogInterface arg0,int arg1) {}
										}).setPositiveButton(R.string.ok_btn,
										new android.content.DialogInterface.OnClickListener() {
											public void onClick(DialogInterface arg0,int arg1) {
												finish();
											}
										}).create();
						alert.show();
					}
				});
				startBtn.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							startBtn.setBackgroundResource(R.drawable.color_bkg_4);
						} else {
							startBtn.setBackgroundResource(R.drawable.color_bkg_1);
						}
					}
				});
				setBtn.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							setBtn.setBackgroundResource(R.drawable.color_bkg_4);
						} else {
							setBtn.setBackgroundResource(R.drawable.color_bkg_1);
						}
					}
				});
				aboutBtn.setOnFocusChangeListener(new OnFocusChangeListener() {
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							aboutBtn.setBackgroundResource(R.drawable.color_bkg_4);
						} else {
							aboutBtn.setBackgroundResource(R.drawable.color_bkg_1);
						}
					}
				});
				exitBtn.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							exitBtn.setBackgroundResource(R.drawable.color_bkg_4);
						} else {
							exitBtn.setBackgroundResource(R.drawable.color_bkg_1);
						}
					}
				});
			}
		});
	}
	
	class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if(!isFinishing()){
				run();
			}
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);// 移除信息队列中最顶部的信息（从顶部取出信息）
			sendMessageDelayed(obtainMessage(0), delayMillis);// 获得顶部信息并延时发送
		}
	};
	
	private void run(){
		for(int i=0;i<count.length;i++){
			count[i]++;
		}
		if(startBtn.isFocused()){
			if(count[0]<=10)startBtn.setTextSize(sizes[0]++);
			else if(count[0]<=20)startBtn.setTextSize(sizes[0]--);
			else count[0] = 0;
		}else{
			startBtn.setTextSize(15);
			sizes[0]=15;
			count[0]=0;
		}
		
		if(setBtn.isFocused()){
			if(count[1]<=10)setBtn.setTextSize(sizes[1]++);
			else if(count[1]<=20)setBtn.setTextSize(sizes[1]--);
			else count[1] = 0;
		}else{
			setBtn.setTextSize(15);
			sizes[1]=15;
			count[1]=0;
		}
		
		if(aboutBtn.isFocused()){
			if(count[2]<=10)aboutBtn.setTextSize(sizes[2]++);
			else if(count[2]<=20)aboutBtn.setTextSize(sizes[2]--);
			else count[2] = 0;
		}else{
			aboutBtn.setTextSize(15);
			sizes[2]=15;
			count[2]=0;
		}
		
		if(exitBtn.isFocused()){
			if(count[3]<=10)exitBtn.setTextSize(sizes[3]++);
			else if(count[3]<=20)exitBtn.setTextSize(sizes[3]--);
			else count[3] = 0;
		}else{
			exitBtn.setTextSize(15);
			sizes[3]=15;
			count[3]=0;
		}
		mRedrawHandler.sleep(100);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		showNotification();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//nMgr.cancel(R.id.cv);
	}
	
	//标题栏显示消息提示
	
	private void showNotification(){
	    /*Notification notifi = new Notification(R.drawable.p5, getText(R.string.app_name), System.currentTimeMillis());
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, null, 0);
	  	notifi.setLatestEventInfo(this, getText(R.string.app_name),getText(R.string.startgame), pIntent);
		if(nMgr == null){
			nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		}
		nMgr.notify(R.id.cv, notifi);*/
		/*PendingIntent pIntent = PendingIntent.getActivity(this, 0, null, 0);
		Notification notifi = new Notification.Builder(this)
		.setAutoCancel(true)
		.setContentTitle(getText(R.string.app_name))
		.setContentText(getText(R.string.startgame))
		.setContentIntent(pIntent)
		.setSmallIcon(R.drawable.p5)
		.setWhen(System.currentTimeMillis())
		.build();
		if(nMgr == null){
			nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		}*/
	
	}
}
