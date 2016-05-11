package com.longcheer.reboot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

@SuppressLint("NewApi")
public class CheckReceiver extends BroadcastReceiver{
	static final String REBOOT_TEST_COUNT_FILE = "/sdcard/StressRebootCount.txt";
	//static final String boot_broadcast = "android.intent.action.BOOT_COMPLETED";
	//static final String TAG = "XUWENDA";
	int count = 0;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		count = FileRead();
		if(action.equals("Acitivity.already.started")){
			if(count > 0){
				FileWrite(count-1);
				PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);	
				pm.reboot(null);
			}
			return;
		}
	}

	private void FileWrite(int count) {
		try {
			File f = new File(REBOOT_TEST_COUNT_FILE);
			if (!f.exists()) {
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

	private int FileRead() {
		File f = new File(REBOOT_TEST_COUNT_FILE);
		if (!f.exists()) {
			return 0;
		}
		try {
			FileReader fr = new FileReader(REBOOT_TEST_COUNT_FILE);
			count = fr.read();
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
