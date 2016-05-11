package com.longcheer.reboot;



import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.util.Log;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	static final String REBOOT_TEST_COUNT_FILE = "/sdcard/StressRebootCount.txt";
	static final String TAG = "XUWENDA";
	private Intent intent = new Intent("Acitivity.already.started");
	private int count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// LinearLayout linearLayout = (LinearLayout)findViewById(R.id.LinearLayout);
		
		Button start = (Button) findViewById(R.id.start);
		Button stop = (Button) findViewById(R.id.stop);
		EditText seconds = (EditText) findViewById(R.id.seconds);
		int waittime = Integer.parseInt(seconds.getText().toString());
		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PowerManager pms = (PowerManager) getSystemService(POWER_SERVICE);
				EditText counts = (EditText) findViewById(R.id.counts);
				count = Integer.parseInt(counts.getText().toString());
				
				FileWrite(count);
				Toast.makeText(MainActivity.this, "---------"+count+"-------", Toast.LENGTH_LONG).show();
				
				
				Log.d(TAG, "The Phone will Reboot!");
				pms.reboot(null);//Reboot start
				
			}
		});
		
		stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FileWrite(0);
				Toast.makeText(MainActivity.this,"Stress test Stop!",Toast.LENGTH_LONG).show();
			}
		});
		
		TimerTask task = new TimerTask(){

			@Override
			public void run(){
				sendBroadcast(intent);
			}
		};
		Timer timer = new Timer();
		timer.schedule(task,waittime*1000);
		//@test by xuwenda
		// Button forward = (Button)findViewById(R.id.forward);
		// forward.setOnClickListener(new OnClickListener(){
		// 	@Override
		// 	public void onClick(View v){
		// 		Intent intent = new Intent(MainActivity.this,TestPassActivity.class);
		// 		startActivity(intent);
		// 	}
		// });
	}
	
	protected void FileWrite(int count) {
		try {
			File f = new File(REBOOT_TEST_COUNT_FILE);
			if(!f.exists()){
				f.createNewFile();
			}
			FileWriter fw = new FileWriter(REBOOT_TEST_COUNT_FILE);
			fw.write(count);
			fw.flush();
			fw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected int FileRead() {
		File f = new File(REBOOT_TEST_COUNT_FILE);
		if(!f.exists()){
			return 0;
		}
		try {
			FileReader fr = new FileReader(REBOOT_TEST_COUNT_FILE);
			count= fr.read();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
}
