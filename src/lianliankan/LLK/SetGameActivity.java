package lianliankan.LLK;

import lianliankan.LLK.R;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SetGameActivity extends ListActivity{
	private String[] items;
	private boolean music = DataSet.music;
	private int count = DataSet.count;
	private int style = DataSet.style;
	private boolean vibrator = DataSet.vibrator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		setList();
	}
	
	private void setList(){
		items = getResources().getStringArray(R.array.menu);
		ArrayAdapter<String> itemList = 
			new ArrayAdapter<String>(SetGameActivity.this,R.layout.menu_row, items);
		setListAdapter(itemList);
		AnimationSet animation = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.list);
		getListView().startAnimation(animation);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		switch (position) {
		//打开音乐选项
		case 0:
			int i = music == true ? 0 : 1;
			new AlertDialog.Builder(SetGameActivity.this).setIcon(R.drawable.p2).setTitle(R.string.music).setSingleChoiceItems(
					R.array.musicItem, i,
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(which==0){
								music = true;
							}else music=false;
							System.out.println(music);
							dialog.dismiss();
						}
					}).setNegativeButton(R.string.cancel_btn, null).show();
			break;
		case 1:
			int s = vibrator == true ? 0 : 1;
			new AlertDialog.Builder(SetGameActivity.this).setIcon(R.drawable.p2).setTitle(R.string.vibrator).setSingleChoiceItems(
					R.array.vibratorItem, s,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(which==0){
								vibrator = true;
							}else vibrator=false;
							dialog.dismiss();
						}
					}).setNegativeButton(R.string.cancel_btn, null).show();
			break;
		//打开关卡选项
		case 2:
			new AlertDialog.Builder(SetGameActivity.this).setIcon(R.drawable.p2).setTitle(R.string.count).setSingleChoiceItems(
					R.array.countItem, count-1,
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							count = which + 1;
							dialog.dismiss();
						}
					}).setNegativeButton(R.string.cancel_btn, null).show();
			break;
		//打开风格选项
		case 3:
			new AlertDialog.Builder(SetGameActivity.this).setIcon(R.drawable.p2).setTitle(R.string.style).setSingleChoiceItems(
					R.array.styleItem, style - 1,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							style = which + 1;
							dialog.dismiss();
						}
					}).setNegativeButton(R.string.cancel_btn, null).show();
			break;
		//恢复默认选项
		case 4:
			Builder builder = MyControl.showAlert(R.string.recovery_title, R.string.recovery_msg, R.drawable.p15, SetGameActivity.this);
			AlertDialog alert = builder.setNegativeButton(R.string.cancel_btn, new OnClickListener() {
						public void onClick(DialogInterface arg0,int arg1) {
						}
					}).setPositiveButton(R.string.ok_btn,new OnClickListener() {
						public void onClick(DialogInterface arg0,int arg1) {
							count = 1;
							music = false;
							style = 1;
							save();
							MyControl.showDialog(R.string.recovery_ok, 2000, 0, SetGameActivity.this);
						}
					}).create();
			alert.show();
			break;
		//退出选项
		case 5:
			save();
			finish();
			break;
		default:
			break;
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Builder builder = MyControl.showAlert(R.string.save_title, R.string.save_msg, R.drawable.p10, SetGameActivity.this);
			AlertDialog alert = builder.setNegativeButton(R.string.cancel_btn, new OnClickListener() {
						public void onClick(DialogInterface arg0,int arg1) {
							finish();
						}
					}).setPositiveButton(R.string.ok_btn,new OnClickListener() {
						public void onClick(DialogInterface arg0,int arg1) {
							save();
							finish();
							MyControl.showDialog(R.string.save_ok, 1000, 0, SetGameActivity.this);
						}
					}).create();
			alert.show();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 保存游戏设置
	 */
	private void save(){
		DataSet.count = count;
		DataSet.music = music;
		DataSet.style = style;
		DataSet.vibrator = vibrator;
	}

}
