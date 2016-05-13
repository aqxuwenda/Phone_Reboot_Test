
package com.longcheer.reboot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;


@SuppressLint("NewApi")
public class CheckReceiver extends BroadcastReceiver{
	static final String REBOOT_TEST_COUNT_FILE = "/sdcard/StressRebootCount.txt";
	static final String TAG = "XUWENDA";
	int count = 0;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		count = FileRead();
		if(action.equals("Acitivity.already.started")){
			
			if(count > 0){
				if(FindException()){
					Intent failIntent = new Intent(context,TestFailActivity.class);
					failIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					FileWrite(0);
					context.startActivity(failIntent);
				} else {
					FileWrite(count-1);
					PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);	
					pm.reboot(null);
				}
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

	//Find Exception Log
	static final String AEE_EXP_EXCEPTION = "/data/aee_exp/";
	static final String AEE_EXP_EXCEPTION_BACKUP = "/sdcard/aee_exp_backup/";


	public boolean FindException() {

		File data_exp = new File(AEE_EXP_EXCEPTION);
		File sdcard_exp = new File(AEE_EXP_EXCEPTION_BACKUP);
		if(data_exp.exists()){ //Find Exception Dir			
			if(getDirName(AEE_EXP_EXCEPTION).toString().contains("fatal")){
				return true;
			}
		}else if(sdcard_exp.exists()){
			if(getDirName(AEE_EXP_EXCEPTION).toString().contains("fatal")){
				return true;
			}
		}else{
			return false;
		}
		return false;
	}

	public ArrayList<String> getDirName(String path){
		File dir = new File(path);
		File[] filelist = dir.listFiles();
		ArrayList<String> filename = new ArrayList<String>();
		for(int i = 0;i < filelist.length;i++){
			filename.add(filelist[i].getName());
		}
		Log.d(TAG,"filename:"+filename);
		return filename;
	}

	// public boolean isException(String exp){
	// 	String EXCEPTION = "db.fatal."+"\\d{2}"+"."+"\\D+";
	// 	Pattern p = Pattern.compile(EXCEPTION);
	// 	Matcher m = p.matcher(exp);
	// 	boolean b = m.matches();
	// 	return b;
	// }

}
