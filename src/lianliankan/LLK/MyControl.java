package lianliankan.LLK;

import lianliankan.LLK.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

public class MyControl {
	/**
	 * 显示提示信息Toast
	 * @param messageID
	 * @param milliseconds
	 * @param selectPic
	 * @param context
	 */
	public static void showDialog(int messageID, int milliseconds, int selectPic,Context context) {
		// show message
		Toast dialog = Toast.makeText(context, R.string.no_msg,milliseconds);
		dialog.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout dialogView = (LinearLayout) dialog.getView();
		dialogView.setOrientation(LinearLayout.VERTICAL);
		dialogView.setBackgroundResource(R.drawable.dialog);
		ImageView coolImage = new ImageView(context);
		TextView text = new TextView(context);
	//	Typeface lcdFont = Typeface.createFromAsset(context.getAssets(),"fonts/MINYN.TTF");
	//	text.setTypeface(lcdFont);
		text.setTextSize(20);
		text.setTextColor(Color.RED);
		text.setText(messageID);
		if (selectPic == 0) {
			coolImage.setImageResource(R.drawable.p6);
		}
		if (selectPic == 1) {
			coolImage.setImageResource(R.drawable.p7);
		}
		if (selectPic == 2) {
			coolImage.setImageResource(R.drawable.p8);
		}
		dialogView.addView(coolImage, 0);
		dialogView.addView(text, 1);
		dialog.show();
	}
	
	/**
	 * 重载showDialog
	 * @param message
	 * @param milliseconds
	 * @param selectPic
	 * @param context
	 */
	public static void showDialog(String message, int milliseconds, int selectPic,Context context) {
		// show message
		Toast dialog = Toast.makeText(context, R.string.no_msg,milliseconds);
		//dialog.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout dialogView = (LinearLayout) dialog.getView();
		dialogView.setOrientation(LinearLayout.VERTICAL);
		dialogView.setBackgroundResource(R.drawable.dialog);
		ImageView coolImage = new ImageView(context);
		TextView text = new TextView(context);
		//Typeface lcdFont = Typeface.createFromAsset(context.getAssets(),"fonts/MINYN.TTF");
		//text.setTypeface(lcdFont);
		text.setTextSize(15);
		text.setTextColor(Color.RED);
		text.setText(message);
		if (selectPic == 0)   text.setHeight(680);
		if (selectPic == 1)   text.setHeight(580);
		if (selectPic == 2)   text.setHeight(480);
		if (selectPic == 3)   text.setHeight(380);
		if (selectPic == 4)   text.setHeight(280);
		if (selectPic == 0) {
			coolImage.setImageResource(R.drawable.p6);
		}
		if (selectPic == 1) {
			coolImage.setImageResource(R.drawable.p7);
		}
		if (selectPic == 2) {
			coolImage.setImageResource(R.drawable.p8);
		}
		if (selectPic == 3) {
			coolImage.setImageResource(R.drawable.p9);
		}
		if (selectPic == 4) {
			coolImage.setImageResource(R.drawable.p10);
		}
		dialogView.addView(coolImage, 0);
		dialogView.addView(text, 1);
		//dialog.setDuration(milliseconds);
		dialog.show();
	}
	
	/**
	 * 输出一个只带文本的弹出窗口
	 * @param title
	 * @param message
	 * @param pic
	 * @param context
	 * @return Builder
	 */
	public static Builder showAlert(int title,int message,int pic,Context context){
		ScrollView scroll = new ScrollView(context);
		scroll.setScrollContainer(true);
		TextView view = new TextView(context);
		Builder builder = new AlertDialog.Builder(context).setIcon(pic).setTitle(title);
		//Typeface lcdFont = Typeface.createFromAsset(context.getAssets(),"fonts/MINYN.TTF");
		//view.setTypeface(lcdFont);
		view.setBackgroundResource(R.drawable.alert);
		view.setTextSize(20);
		view.setTextColor(Color.RED);
		view.setScroller(new Scroller(view.getContext()));
		view.setText(message);
		scroll.addView(view);
		builder.setView(scroll);
		return builder;
	}
	
	/**
	 * 输出一个只带文本的弹出窗口
	 * @param title
	 * @param message
	 * @param pic
	 * @param context
	 * @return Builder
	 */
	public static Builder showAlert(int title,String message,int pic,Context context){
		ScrollView scroll = new ScrollView(context);
		scroll.setScrollContainer(true);
		TextView view = new TextView(context);
		Builder builder = new AlertDialog.Builder(context).setIcon(pic).setTitle(title);
		//Typeface lcdFont = Typeface.createFromAsset(context.getAssets(),"fonts/MINYN.TTF");
		//view.setTypeface(lcdFont);
		view.setBackgroundResource(R.drawable.alert);
		view.setTextSize(20);
		view.setTextColor(Color.RED);
		view.setScroller(new Scroller(view.getContext()));
		view.setText(message);
		scroll.addView(view);
		builder.setView(scroll);
		return builder;
	}
	
	public static Builder showAlert(int title,int message,int pic,Context context,View text){
		LinearLayout dialogView = new LinearLayout(context);
		dialogView.setOrientation(LinearLayout.VERTICAL);
		TextView view = new TextView(context);
		Builder builder = new AlertDialog.Builder(context).setIcon(pic).setTitle(title);
		dialogView.setBackgroundResource(R.drawable.alert);
		view.setTextSize(21);
		view.setTextColor(Color.RED);
		view.setText(message);
		dialogView.addView(view, 0);
		dialogView.addView(text, 1);
		builder.setView(dialogView);
		return builder;
	}
	
}
