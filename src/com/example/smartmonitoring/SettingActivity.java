package com.example.smartmonitoring;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity {

	EditText LimitTempeture;
	EditText LimitHumidity;
	EditText LimitVoltage;
	EditText LimitCurrent;
	
    Button buttonOK;
    Button buttonCancel;
    Handler handler;

	public void setHandle(Handler handler){		
		this.handler = handler;
	}
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);



		LimitTempeture = (EditText)findViewById(R.id.EditText01);
		LimitHumidity = (EditText)findViewById(R.id.EditText02);
		LimitVoltage = (EditText)findViewById(R.id.EditText03);
		LimitCurrent = (EditText)findViewById(R.id.EditText04);

	    buttonOK = (Button)findViewById(R.id.button1);
	    buttonCancel = (Button)findViewById(R.id.button2);

	    
	    buttonOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				float temp;
				float ham;
				float vol;
				float cur;
				
				try{
					temp = Float.parseFloat(LimitTempeture.getText().toString());
					ham = Float.parseFloat(LimitHumidity.getText().toString());
					vol = Float.parseFloat(LimitVoltage.getText().toString());
					cur = Float.parseFloat(LimitCurrent.getText().toString());
					
//					Message msg = new Message();    
//					msg.what = 5;    
//					Bundle bundle = new Bundle();	            						
//					bundle.putFloat("temp"	, temp);
//					bundle.putFloat("ham"	, ham);
//					bundle.putFloat("vol"	, vol);
//					bundle.putFloat("cur"	, cur);
//					msg.setData(bundle);
//					handler.sendMessage(msg); 
					Intent intent = new Intent(SettingActivity.this, MainActivity.class);
					intent.putExtra("temp", temp);
					intent.putExtra("ham", ham);
					intent.putExtra("vol", vol);
					intent.putExtra("cur", cur);
					startActivity(intent);//打开新的activity
					
					
//					SettingActivity.this.finish();
				}
				catch(java.lang.NumberFormatException e)
				{
					Toast.makeText(getApplicationContext(), "输入的值错误", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});
	    buttonCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				SettingActivity.this.finish();
			}
		});
	    
	}


}
