package com.longcheer.reboot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	static final String REBOOT_TEST_COUNT_FILE = "/data/tmp/StressRebootCount";
	private FileInputStream fis;
	private FileOutputStream fos;
	boolean flag = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button start = (Button) findViewById(R.id.start);
		Button stop = (Button) findViewById(R.id.stop);
		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PowerManager pms = (PowerManager) getSystemService(POWER_SERVICE);
//				EditText seconds = (EditText) findViewById(R.id.seconds);
//				int waittime = Integer.parseInt(seconds.getText().toString());
				EditText counts = (EditText) findViewById(R.id.counts);
				int count = Integer.parseInt(counts.getText().toString());
//				int i = 0;
//				while (count > 0 || i < count || !flag) {
//					try {
//						Thread.sleep(waittime);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					i++;
//
//					Toast.makeText(MainActivity.this, "---"+FileRead()+"---", Toast.LENGTH_LONG).show();
//
//	
//					pms.reboot(null);
//					
//				}
				FileWrite(count);
				if(FileRead()>0){
					int c = FileRead()-1;
					FileWrite(c);
					Toast.makeText(MainActivity.this, "---"+FileRead()+"---", Toast.LENGTH_LONG).show();
					pms.reboot(null);
				}
			}
		});
		
		stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = false;
			}
		});
	}
	
	protected void FileWrite(int count) {
		try {
			File f = new File(REBOOT_TEST_COUNT_FILE);
			if(!f.exists()){
				f.createNewFile();
			}
			fos = new FileOutputStream(REBOOT_TEST_COUNT_FILE);
			fos.write(count);
			fos.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected int FileRead() {
		int count = 0;
		try {
			File f = new File(REBOOT_TEST_COUNT_FILE);
			if(!f.exists()){
				return 0;
			}
			fis = new FileInputStream(REBOOT_TEST_COUNT_FILE);
			count = fis.read();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
}