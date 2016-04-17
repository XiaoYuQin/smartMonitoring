package com.example.smartmonitoring;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SelectActivity extends Activity {

	
	Button connectedDeviceButton;
	Button viewDeviceButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);

		connectedDeviceButton = (Button)findViewById(R.id.select_button1);		
		viewDeviceButton = (Button)findViewById(R.id.select_button2);

		
		connectedDeviceButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SelectActivity.this,SettingActivity.class);
            	startActivity(intent);    
            	//finish();
			}
		});
		viewDeviceButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SelectActivity.this,MainActivity.class);
            	startActivity(intent);
            	//finish();
			}
		});
		
		
	}


}
