package com.example.smartmonitoring;

import com.qinxiaoyu.lib.android.SdCard;
import com.qinxiaoyu.lib.util.file.FileProperties;
import com.qinxiaoyu.lib.util.format.string.ip.Ip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity {


	
	EditText ipEditText;
	EditText portEditText;
	Button okButton;
    
	private void debug(String str)
	{
		Log.i("LoginActivity",str);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		ipEditText = (EditText)findViewById(R.id.setting_editText1);
		portEditText = (EditText)findViewById(R.id.setting_EditText01);
		okButton = (Button)findViewById(R.id.setting_button1);
		
		okButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				//debug("用户名不能为空"+user_EditText.getText()+"|");
				if(ipEditText.getText().toString() == null || ipEditText.getText().toString().equals(""))								
				{
					debug("IP地址不能为空");
					Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(portEditText.getText().toString() == null || portEditText.getText().toString().equals(""))
				{
					debug("端口号不能为空");
					Toast.makeText(getApplicationContext(), "端口号不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(!Ip.verifyIp(ipEditText.getText().toString()))
				{
					Toast.makeText(getApplicationContext(), "IP地址输入有误", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(!Ip.verifyPort(portEditText.getText().toString()))
				{
					Toast.makeText(getApplicationContext(), "端口输入有误", Toast.LENGTH_SHORT).show();
					return;
				}
				
				try {
		    		int portInt = Integer.parseInt(portEditText.getText().toString());
					Data.setPort(portInt);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Data.setIP(ipEditText.getText().toString());
				String file_path = SdCard.getSdcardPath()+"/smartMonitoring.config";
				FileProperties fileProperties  = new FileProperties();
				fileProperties.writeProperties(file_path, "ip", ipEditText.getText().toString());
				fileProperties.writeProperties(file_path, "port", portEditText.getText().toString());	
				
				
				Toast.makeText(getApplicationContext(), "设置网络参数成功", Toast.LENGTH_SHORT).show();
				
			}
		});
	    
	}


}
