package com.longcheer.reboot;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

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
				EditText seconds = (EditText) findViewById(R.id.seconds);
				int waittime = Integer.parseInt(seconds.getText().toString());
				EditText counts = (EditText) findViewById(R.id.counts);
				int count = Integer.parseInt(counts.getText().toString());
				int i = 0;
				while (count > 0 || i < count || !flag) {
					try {
						Thread.sleep(waittime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;
					Toast.makeText(MainActivity.this, "第"+i+"次", Toast.LENGTH_LONG).show();
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
}