package lianliankan.LLK;

import java.io.File;
import java.io.IOException;

import lianliankan.LLK.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class AboutActivity extends ListActivity {
	private EditText text;
	private SmsManager smsManager;

	private String[] items;
	private String[] users;
	private UserData data = new UserData();
	private Configuration config = new Configuration();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		setList();
	}
	
	private void setList(){
		items = getResources().getStringArray(R.array.about);
		ArrayAdapter<String> itemList = 
			new ArrayAdapter<String>(AboutActivity.this,R.layout.menu_row, items);
		setListAdapter(itemList);
		AnimationSet animation = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.list);
		getListView().startAnimation(animation);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		switch (position) {
		//显示高分排行信息
		case 0:
			/*	Intent intent = new Intent();
				intent.setClass(AboutActivity.this,
						ScoreActivity.class);
				AboutActivity.this.startActivity(intent); */
			data = config.load();
			for(int i = 0;i<5;i++)
			{
				MyControl.showDialog("第"+ (i+1) +"名："+"\n姓名：" +data.getName(i)+ "\n得分：" + data.getHiScore(i)+
						"\n时间：" + data.getDate(i), 10000 , i, getApplicationContext());
				
			}
			break;
		//删除排行信息
		case 1:
			Builder builder = MyControl.showAlert(R.string.del_title, R.string.del_msg, R.drawable.p13, AboutActivity.this);
			AlertDialog alert = builder.setNegativeButton(R.string.cancel_btn, new OnClickListener() {
						public void onClick(DialogInterface arg0,int arg1) {
						}
					}).setPositiveButton(R.string.ok_btn,new OnClickListener() {
						public void onClick(DialogInterface arg0,int arg1) {
							File file = new File("/sdcard/llk.dat");
							file.delete();
							MyControl.showDialog(R.string.del_ok, 1000, 1, AboutActivity.this);
						}
					}).create();
			alert.show();
			break;
		//分享给好友
		case 2:
			text = new EditText(getApplicationContext());
			text.setInputType(InputType.TYPE_CLASS_PHONE);
			smsManager = SmsManager.getDefault();
			Configuration config = new Configuration();
			UserData data = config.load();
			//发送的消息内容
			final String message = getText(R.string.tofriend_notice).toString()+data.getHiScore(0)+"分！";
			Builder builder3 = MyControl.showAlert(R.string.tofriend_title, R.string.tofriend_msg, R.drawable.p5, AboutActivity.this,text);
			AlertDialog alert3 = builder3.setPositiveButton(R.string.ok_btn,new OnClickListener() {
						public void onClick(DialogInterface arg0,int arg1) {
							users = text.getText().toString().split(" ");
							try{
							for(int i=0;i<users.length;i++){
								System.out.println(users[i]);
								PendingIntent pintent = PendingIntent.getBroadcast(AboutActivity.this, 0, new Intent(), 0);
								smsManager.sendTextMessage(users[i], null,message, pintent, null);
							}
							MyControl.showDialog(R.string.tofriend_ok, 1000, 1, AboutActivity.this);
							}catch (Exception e) {
								MyControl.showDialog(R.string.msg_error, 1000, 1, AboutActivity.this);
							}
						}
					}).setNegativeButton(R.string.cancel_btn, new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {}
					}).create();
			alert3.show();
			break;
		//反馈给我们
		case 3:
			text = new EditText(getApplicationContext());
			text.setLines(5);
			smsManager = SmsManager.getDefault();
			Builder builder2 = MyControl.showAlert(R.string.tome_title, R.string.tome_msg, R.drawable.p2, AboutActivity.this,text);
			AlertDialog alert2 = builder2.setPositiveButton(R.string.ok_btn,new OnClickListener() {
						public void onClick(DialogInterface arg0,int arg1) {
							try{
								//那一串神秘的数字就可以填写你的手机号码
								PendingIntent pintent = PendingIntent.getBroadcast(AboutActivity.this, 0, new Intent(), 0);
								smsManager.sendTextMessage("13333333333", null, text.getText().toString(), pintent, null);
								MyControl.showDialog(R.string.tome_ok, 1000, 1, AboutActivity.this);
							}catch (Exception e) {
								MyControl.showDialog(R.string.msg_error, 1000, 1, AboutActivity.this);
							}
						}
					}).setNegativeButton(R.string.cancel_btn, new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {}
					}).create();
			alert2.show();
			break;
		case 4:
			config = new Configuration();
			String msg = "★将相同的两张牌用三根以内的直线连在一起就可以消除，规则简单容易上手。" +
					     "\n" +
					     "\n★使用技能可以重新排列，不过要以减少时间作为代价。" +
					     "\n" +
					     "\n★玩的HIGH一点哦！" +
					     "\n" +				  
					     "\n●作者：萨达哈鲁";
			Builder builder1 = MyControl.showAlert(R.string.ctrl_title, msg, R.drawable.p15, AboutActivity.this);
			AlertDialog alert1 = builder1.setPositiveButton(R.string.ok_btn,new OnClickListener() {
						public void onClick(DialogInterface arg0,int arg1) {}
					}).create();
			alert1.show(); 
			
			break;
		case 5:
			finish();
			break;
		default:
			break;
		}
	}
}
