package com.longcheer.reboot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootReceiver extends BroadcastReceiver{
	static final String boot_broadcast = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context,Intent intent){
		Intent boot_intent = new Intent(context,MainActivity.class);
		boot_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(boot_intent);
	}
	

}