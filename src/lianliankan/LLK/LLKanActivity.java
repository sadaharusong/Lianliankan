package lianliankan.LLK;

import java.util.Date;
import java.util.Random;

import lianliankan.LLK.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.*;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.*;
import android.widget.*;

public class LLKanActivity extends Activity {
	private ProgressBar pb;
	private CtrlView cv;
	private TextView tv;
	private TextView msg;
	private ImageButton menuIBtn;
	private EditText text;
	private boolean isRun = true;  //��Ϸ�Ƿ�����
	private boolean isBlueTooth = false;
	private Intent mediaServiceIntent;
	private RefreshHandler mRedrawHandler;
	Animation animation;
	private UserData data = new UserData();
	private Configuration config = new Configuration();
	public static final int MAX_VALUE = 1000; // ���ý��������ֵ
	public static final int START_MENU = Menu.FIRST; // ���忪ʼ��ťλ��
	public static final int REFRESH_MENU = Menu.FIRST + 1; // �������Ű�ťλ��
	public static final int EXIT_MENU = Menu.FIRST + 4; // �����˳���ťλ��
	public static final int HIT_MENU = Menu.FIRST + 2;
	public static final int SEND_MENU = Menu.FIRST + 3;
	
    public  int getflag()
    {
    	int flag = 0;
    	return flag;
    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);   
		super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);   
		super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		mRedrawHandler = new RefreshHandler();
		mediaServiceIntent= new Intent();
		mediaServiceIntent.setClass(this, MediaService.class);	
		setViews();		
		mRedrawHandler.sleep(200);
		data = config.load();
		
	}

	// ��ʼ���ؼ�
	private void setViews() {
		cv = (CtrlView) findViewById(R.id.cv);
		cv.initGame();
		cv.invalidate();
		pb = (ProgressBar) findViewById(R.id.pb);
		tv = (TextView) findViewById(R.id.tv);
		msg = (TextView) findViewById(R.id.msg);
		menuIBtn = (ImageButton)findViewById(R.id.menu);
		// cv.randomIcons();
		pb.setMax(MAX_VALUE- ((cv.count - 1) * 125)<=0?100:MAX_VALUE- ((cv.count - 1) * 125));
		pb.setProgress(pb.getMax());
		cv.musicOn = DataSet.music;
		if (cv.musicOn) {
			this.startService(mediaServiceIntent);
		}
		
		/**
		 * ����˵������ѡ��˵�
		 */
		menuIBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				//��menu
				openOptionsMenu();
			}
		});
	}
	
	// ��Ϸ��ʱ
	class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				run();
				break;
	            }
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);// �Ƴ���Ϣ�������������Ϣ���Ӷ���ȡ����Ϣ��
			sendMessageDelayed(obtainMessage(0), delayMillis);// ��ö�����Ϣ����ʱ����
		}
	};

	private void run() {
		if (isRun) {
			if (pb.getProgress() > 0) {
				cv.process_value -= 1;
				pb.setProgress(cv.process_value);
				msg.setText("" + cv.score);
				tv.setText(goLight(tv.getText().toString()));
				//�����ת��Ϊdouble�ͣ��ô�����һֱΪ��int��0
				if ((double)pb.getProgress()/(double)pb.getMax() < 0.25) {
					cv.noticeCount++;
					if(cv.noticeCount==1){
						animation = AnimationUtils.loadAnimation(this,R.anim.progress);
						animation.setRepeatCount(3);
						if(DataSet.vibrator){
							new Thread(){
								public void run() {
									Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);   
									long[] pattern = {300, 400, 300, 400,300,400}; // OFF/ON/OFF/ON...   
									vibrator.vibrate(pattern, 3);
									try {
										Thread.sleep(2100);
									} catch (InterruptedException e) {}
									vibrator.cancel();
								}
							}.start();
						}
						pb.setAnimation(animation);
						Random r = new Random();
						int i = r.nextInt(10);
						if (i > 6) {
							String notime_msg = getText(R.string.notime_msg).toString()
									+ "\n" + "��˵����Ʒ�ã������" + i * 10 + "��ʱ�䣡";
							cv.process_value += i * 11;
							MyControl.showDialog(notime_msg, 1000, 1,getApplicationContext());
						}
					}
				}
			}

			if (pb.getProgress() <= 0) {
				if(DataSet.vibrator){
					new Thread(){
						public void run() {
							Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);   
							long[] pattern = {0, 2000}; // OFF/ON/OFF/ON...   
							vibrator.vibrate(pattern, 1);
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {}
							vibrator.cancel();
						}
					}.start();
				}
				text = new EditText(LLKanActivity.this);
				//������а�Ϊ�ջ��ߵ÷ֳ�����¼��߷֣�����
				if (data.getHiScore(0) == null|| data.getHiScore(4) == null || Integer.parseInt(msg.getText().toString().trim()) > Integer.parseInt(data.getHiScore(0).trim())) {
					isRun = false;
					Builder builder = MyControl.showAlert(R.string.victory_title, R.string.victory_msg, R.drawable.p10, LLKanActivity.this,text);
					AlertDialog alert = builder.setPositiveButton(
							R.string.ok_btn, new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									data.setName(text.getText().toString(),4);
									data.setDate(new Date().toLocaleString(),4);
									data.setHiScore(msg.getText().toString(),4);
									config.save(data,4);
				
									for(int i = 4;i > 0;i--)
									{    
										if(data.getHiScore(i - 1) == null)
										{
											String a = new String();
											a = data.getHiScore(i);
											data.setHiScore(data.getHiScore(i - 1),i);
											data.setHiScore(a,i - 1);
											a = data.getDate(i);
											data.setDate(data.getDate(i - 1), i);
											data.setDate(a,i - 1);
											a = data.getName(i);
											data.setName(data.getName(i - 1), i);
											data.setName(a,i -1);
											config.save(data,i);
											config.save(data,i -1 );
										}
										else if(Integer.parseInt(data.getHiScore(i).trim()) > Integer.parseInt(data.getHiScore(i-1).trim()))
										{
											String a = new String();
											a = data.getHiScore(i);
											data.setHiScore(data.getHiScore(i - 1),i);
											data.setHiScore(a,i - 1);
											a = data.getDate(i);
											data.setDate(data.getDate(i - 1), i);
											data.setDate(a,i - 1);
											a = data.getName(i);
											data.setName(data.getName(i - 1), i);
											data.setName(a,i -1);
											config.save(data,i);
											config.save(data,i -1 );
										}
									}
									
									isRun = true;
									finish();
								}
							}).create();
					alert.show();
				} else {
					String message = getText(R.string.overnotice).toString()+msg.getText().toString()+"�֣�";
					MyControl.showDialog(message, 3000, 1,getApplicationContext());
					finish();
				}
			} else if (cv.process_value != 0 && cv.isWin) {
				if(DataSet.vibrator){
					new Thread(){
						public void run() {
							Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);   
							long[] pattern = {0, 800, 400, 500}; // OFF/ON/OFF/ON...   
							vibrator.vibrate(pattern, 2);
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {}
							vibrator.cancel();
						}
					}.start();
				}
				cv.score += (cv.process_value * cv.count / 5);
				cv.count++;
				if(cv.count >= 9) pb.setMax(100);
				else pb.setMax(MAX_VALUE - ((cv.count - 1) * 125));
				pb.setProgress(pb.getMax());
				cv.initGame();
				String message = getText(R.string.next).toString()+cv.count+"�أ�";
				MyControl.showDialog(message, 1000, 1,LLKanActivity.this);
				cv.process_value += 10;
			}
		}
		mRedrawHandler.sleep(200);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, START_MENU, 0, R.string.startgame);
		menu.add(0, REFRESH_MENU, 0, R.string.refresh);
		if(isBlueTooth){
			menu.add(0,HIT_MENU, 0, "����");
			menu.add(0,SEND_MENU, 0, "����");
		}
		menu.add(0, EXIT_MENU, 0, R.string.exit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// ��ѡ���ؿ�,��ʼ����Ϸ
		case START_MENU:
			cv.initGame();
			pb.setMax(MAX_VALUE- ((cv.count - 1) * 125)<=0?100:MAX_VALUE- ((cv.count - 1) * 125));
			break;
		// ��ѡ��ż��ܣ���Ϸʣ��ʱ���ȥ5*��4+����/2����
		case REFRESH_MENU:
			cv.process_value -= 5 * (4 + cv.count/2);
			cv.rearrange();
			//�ż��ܺ���Ϊ���֣����������
			while(cv.isDead()){
				cv.rearrange();
				MyControl.showDialog(R.string.isDeadNotice, 500, 1,getApplicationContext());
				cv.process_value += 10;
			}
			break;
		// ��ѡ���˳��������Ƿ��˳��Ի���
		case EXIT_MENU:
			//��ͣ��Ϸ
			isRun = false;
			Builder builder = MyControl.showAlert(R.string.exit_title, R.string.exit_msg, R.drawable.p10, LLKanActivity.this);
			AlertDialog alert = builder.setNegativeButton(R.string.cancel_btn, new OnClickListener() {
						public void onClick(DialogInterface arg0,int arg1) {
							isRun = true;
						}
					}).setPositiveButton(R.string.ok_btn,new OnClickListener() {
						public void onClick(DialogInterface arg0,int arg1) {
							finish();
						}
					}).create();
			alert.show();
			break;
		case SEND_MENU:
		case HIT_MENU:
	
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//System.out.println(keyCode);
			isRun = false;
			Builder builder = MyControl.showAlert(R.string.exit_title, R.string.exit_msg, R.drawable.p10, LLKanActivity.this);
			AlertDialog alert = builder.setNegativeButton(R.string.cancel_btn, new OnClickListener() {
						public void onClick(DialogInterface arg0,int arg1) {
							isRun = true;
						}
					}).setPositiveButton(R.string.ok_btn,new OnClickListener() {
						public void onClick(DialogInterface arg0,int arg1) {
							finish();
						}
					}).create();
			alert.show();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	// �������������Ƶ�Ч��
	private String goLight(String str) {
		String s1 = str.substring(0, str.length() - 1);
		String s2 = str.substring(str.length() - 1, str.length());
		return s2 + s1;
	}
	

	@Override
	protected void onPause() {
		isRun = false;
		super.onPause();
	}
	
	@Override
	protected void onStart() {
		isRun = true;
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		isRun = true;
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		this.stopService(mediaServiceIntent);
		super.onDestroy();
	}
	
	@Override
	public void finish() {
		this.stopService(mediaServiceIntent);
		onStop();
		super.finish();
	}
	
	//��ͣ��Ϸ
	@Override
	protected void onStop() {
		isRun = false;
		super.onStop();
	}
	
}