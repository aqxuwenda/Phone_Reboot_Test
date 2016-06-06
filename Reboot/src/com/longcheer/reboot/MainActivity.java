/*Author: longcheer zhengbaojiang
/ Version: v3.0
*/

package com.longcheer.reboot;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	static final String REBOOT_TEST_COUNT_FILE = "/sdcard/StressRebootCount.txt";
	static final String TAG = "REBOOT";
	private Intent intent = new Intent("Acitivity.already.started");
	static final String CONFIG = "CountsAndValue";
	private int count = 0,waitTime = 0,currentCount=0;
	TimerTask mTimerTask = null;
	Timer mTimer = null;
	Timer mCountDownTime = null;
	private int miCountDown = 0;
	SharedPreferences mSp = null;
	SharedPreferences.Editor mEditor = null;
	private static final int AEEEXPCOUNT = 3;
	TextView mCountDown ;
	boolean isCountDownStop = false;

//	ArrayList<String> filenameData[] = new ArrayList<String>();
//	for(int i = 0; i <= filenameDate.le)
//Find Exception Log
	static final String AEE_EXP_EXCEPTION = "/data/aee_exp/";
	static final String AEE_EXP_EXTERNAL_EXCEPTION = "/sdcard/mtklog/aee_exp/";
	static final String AEE_EXP_EXTERNAL_EXCEPTION_BACKUP = "/sdcard/mtklog/aee_exp_backup/";

	private ArrayList<String> AeeExp[] = new ArrayList[AEEEXPCOUNT];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		for(int i = 0; i< AEEEXPCOUNT; i++){
			AeeExp[i] = new ArrayList<String>();
		}
		// LinearLayout linearLayout = (LinearLayout)findViewById(R.id.LinearLayout);

		Button start = (Button) findViewById(R.id.start);
		Button stop = (Button) findViewById(R.id.stop);
		EditText seconds = (EditText) findViewById(R.id.seconds);
		EditText counts = (EditText) findViewById(R.id.counts);
		mCountDown = (TextView)findViewById(R.id.countdown);

		mSp =MainActivity.this.getSharedPreferences(CONFIG, MODE_PRIVATE);
		String BeginCounts = mSp.getString("BeginCounts", "0").toString();
		String BeginWaitTimes = mSp.getString("BeginWaitTimes", "0").toString();
		String currentCount = mSp.getString("Counts", "0").toString();

		if(BeginCounts.equals("")) {
			count = 0;
		}else {
			count = Integer.parseInt(BeginCounts);
		}
		if(BeginWaitTimes.equals("")) {
			waitTime = 0;
		}else{
			waitTime = Integer.parseInt(BeginWaitTimes);
		}
		miCountDown = waitTime;

		Log.d(TAG, "oncreate count="+count+"  waitTime="+waitTime +" BeginCounts="+BeginCounts+"  BeginWaitTimes="+BeginWaitTimes);

		counts.setText(BeginCounts);
		seconds.setText(BeginWaitTimes);

		TextView mRebootContent =(TextView)findViewById(R.id.reboot_content);
		mRebootContent.append("\r\n"+getString(R.string.text_waittime)+waitTime+"  "+getString(R.string.text_counts)+count
				+"  "+getString(R.string.text_current)+currentCount+"\r\n");

		FindFatalException();

		for(int i = 0; i < AEEEXPCOUNT;i++){
			mRebootContent.append("------------------------------------------\r\n");
			for(int j = 0; j < AeeExp[i].size();j++){
				mRebootContent.append( AeeExp[i].get(j)+"\r\n");
				int offset=mRebootContent.getLineCount()*mRebootContent.getLineHeight();
				if(offset>mRebootContent.getHeight())
				{
					mRebootContent.scrollTo(0,offset-mRebootContent.getHeight());
				}
			}
		}

		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PowerManager pms = (PowerManager) getSystemService(POWER_SERVICE);

				EditText counts = (EditText) findViewById(R.id.counts);
				EditText seconds = (EditText) findViewById(R.id.seconds);
				if (counts.getText().toString().equals("0") || seconds.getText().toString().equals("0")
						||counts.getText().toString().equals("") || seconds.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(),getString(R.string.tip),Toast.LENGTH_LONG).show();
					return;
				}
				//FileWrite(count);
				//Counts
				mSp =MainActivity.this.getSharedPreferences(CONFIG, MODE_PRIVATE);
				mEditor =mSp.edit();
				mEditor.putString("Counts",counts.getText().toString());
				mEditor.putString("BeginCounts",counts.getText().toString());

				//WaitTimes
				mEditor.putString("WaitTimes",seconds.getText().toString());
				mEditor.putString("BeginWaitTimes",seconds.getText().toString());

				//Status
				mEditor.putString("Status","start");
				mEditor.commit();


				count = Integer.parseInt(mSp.getString("Counts", "0").toString());
				waitTime = Integer.parseInt(mSp.getString("WaitTimes", "0").toString());
				mCountDown.setText(getString(R.string.text_countdowning) + waitTime+getString(R.string.second));
				Log.d(TAG, "The Phone will Reboot! count="+count+"  waitTime="+waitTime);
				if(count > 0 && waitTime > 0){
					mEditor = mSp.edit();
					mEditor.putString("Counts",(count-1)+"");
					mEditor.commit();
					//pms.reboot(null);//Reboot start
					Intent reboot = new Intent(Intent.ACTION_REBOOT);
					 reboot.putExtra("nowait", 1);
					 reboot.putExtra("interval", 1);
					 reboot.putExtra("window", 0);
					 sendBroadcast(reboot);
				}
			}
		});
		
		stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Status
				mSp =MainActivity.this.getSharedPreferences(CONFIG, MODE_PRIVATE);
				mEditor=mSp.edit();
				mEditor.putString("Status","stop");
				mEditor.commit();
				if(null != mTimer){
					mTimer.cancel();
				}
				if(null != mCountDownTime){
					mCountDownTime.cancel();
				}
				isCountDownStop = true;
				mCountDown.setText(getString(R.string.text_countdown));
				Toast.makeText(MainActivity.this,getString(R.string.Stoptip),Toast.LENGTH_LONG).show();
			}
		});

		mTimerTask = new TimerTask(){
			@Override
			public void run(){
				//sendBroadcast(intent);
				Log.i(TAG,"mTimerTask run = "+mTimer.toString());
				rebootAgain();
			}
		};
		mTimer = new Timer();
		mTimer.schedule(mTimerTask,waitTime*1000);

		mCountDownTime = new Timer();
		mCountDownTime.schedule(new TimerTask() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = miCountDown--;
				handler.sendMessage(msg);
				isCountDownStop = false;
				Log.i(TAG,"mCountDownTime mTimer sendMessage msg.what= "+msg.what);
			}
		}, 1000);

		Log.i(TAG,"begin mTimer = "+mTimer.toString());

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Log.d(TAG, "handleMessage方法所在的线程："+ Thread.currentThread().getName()+" msg.what="+msg.what);
			
			if (msg.what > 0) {
				
				Message msg2 = new Message();
				msg2.what = --miCountDown;
				mSp = MainActivity.this.getSharedPreferences(CONFIG, MODE_PRIVATE);
				String rebootStatus = mSp.getString("Status", "stop");
				if(!isCountDownStop && rebootStatus.equals("start")) {
					handler.sendMessageDelayed(msg2, 1000);
					mCountDown.setText(getString(R.string.text_countdowning) + msg.what +getString(R.string.second));
				}else{
					mCountDown.setText(getString(R.string.text_countdown) );
				}
				Log.i(TAG,"mCountDownTime mTimer sendMessage msg.what= "+msg.what);
			} else {
				mCountDown.setText(getString(R.string.text_countdown));
				//mCountDownTime.cancel();
			}
		}
	};

	private void rebootAgain() {
		mSp =MainActivity.this.getSharedPreferences(CONFIG, MODE_PRIVATE);
		String defaultStatus = mSp.getString("Status", "stop");
		count = Integer.parseInt(mSp.getString("Counts", "0").toString());
		Log.i(TAG,"defaultStatus = "+defaultStatus + "  count="+count+"  mTimer="+mTimer.toString());
		if(defaultStatus.equals("stop") ){
			if(mTimer != null)
				mTimer.cancel();
			isCountDownStop = true;
		}else if(FindFatalException()  ){
			if(mTimer != null)
				mTimer.cancel();
			mEditor=mSp.edit();
			mEditor.putString("Status","stop");
			mEditor.commit();
			isCountDownStop= true;
		}else if(count <= 0 ){
			if(mTimer != null)
				mTimer.cancel();
			mEditor=mSp.edit();
			mEditor.putString("Status","stop");
			mEditor.commit();
			isCountDownStop = true;
		}else {
			mEditor = mSp.edit();
			mEditor.putString("Counts",(count-1)+"");
			Log.i(TAG, "rebootAgain count =" +(count-1));
			mEditor.commit();
			PowerManager pm = (PowerManager) MainActivity.this.getSystemService(Context.POWER_SERVICE);
			//pm.reboot("");
			Intent reboot = new Intent(Intent.ACTION_REBOOT);
			reboot.putExtra("nowait", 1);
			reboot.putExtra("interval", 1);
			reboot.putExtra("window", 0);
			sendBroadcast(reboot);
		}
	}

	public boolean FindFatalException() {
		boolean result = false;
		File data_exp = new File(AEE_EXP_EXCEPTION);
		File sdcard_exp_bak = new File(AEE_EXP_EXTERNAL_EXCEPTION_BACKUP);
		File sdcard_exp = new File(AEE_EXP_EXTERNAL_EXCEPTION);
		if(data_exp.exists() && getDirName(AEE_EXP_EXCEPTION)){ //Find Exception Dir
			result = true;
			mEditor=mSp.edit();
			mEditor.putString("Status","stop");
			mEditor.commit();
		}
		if(sdcard_exp.exists() && getDirName(AEE_EXP_EXTERNAL_EXCEPTION)){
			result = true;
			mEditor=mSp.edit();
			mEditor.putString("Status","stop");
			mEditor.commit();
		}
		if(sdcard_exp_bak.exists() && getDirName(AEE_EXP_EXTERNAL_EXCEPTION_BACKUP)){
			result = true;
			mEditor=mSp.edit();
			mEditor.putString("Status","stop");
			mEditor.commit();
		}
		return result;

	}

	public boolean getDirName(String path) {
		ArrayList<String> filename = new ArrayList<String>();
		if (path.equals(AEE_EXP_EXCEPTION)) {
			filename = AeeExp[0];
		} else if (path.equals(AEE_EXP_EXTERNAL_EXCEPTION)){
			filename = AeeExp[1];
		}else if(path.equals(AEE_EXP_EXTERNAL_EXCEPTION_BACKUP)){
			filename = AeeExp[2];
		}
		File dir = new File(path);
		File[] filelist = dir.listFiles();
		boolean findFatal = false;
		for(int i = 0;i < filelist.length;i++){
			if(0 == i){
				filename.add(filelist[0].getParent());
			}
			filename.add(filelist[i].getName());
			if(filelist[i].getName().contains(".fatal.")){
				findFatal = true;
			}else {
				findFatal = false;
			}
		}
		return findFatal;
	}
	

}
