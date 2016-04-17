package com.example.smartmonitoring;

import com.qinxiaoyu.lib.android.SdCard;
import com.qinxiaoyu.lib.util.file.File;
import com.qinxiaoyu.lib.util.file.FileProperties;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	
	String file_path;
	String userName = "root";
	String password = "123456";
	
	Button okButton;
	EditText user_EditText;
	EditText password_EditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		okButton = (Button)findViewById(R.id.login_button1);
		user_EditText = (EditText)findViewById(R.id.login_editText1);
		password_EditText = (EditText)findViewById(R.id.login_EditText01);
		
		
		//判断sd卡是否插入，若插入则执行以下内容。
		if(SdCard.checkSDcardStatus() == 0)
		{
			Toast.makeText(getApplicationContext(), "检测到SD卡", Toast.LENGTH_SHORT).show();
			//判断是否已经存在配置文件
			String file_path = SdCard.getSdcardPath()+"/smartMonitoring.config";			
			File file = new File(file_path);
			//配置文件不存在，初始化一个配置参数进去
			if(!file.exists())
			{
				FileProperties fileProperties  = new FileProperties();
				fileProperties.writeProperties(file_path, "userName", "root");
				fileProperties.writeProperties(file_path, "password", "123456");				
				Toast.makeText(getApplicationContext(), "初始化用户成功", Toast.LENGTH_SHORT).show();
			}
			//配置文件存在，把设定好的用户名和密码读出，存在全局变量里
			else
			{
				FileProperties fileProperties  = new FileProperties();
				userName = fileProperties.readProperties(file_path, "userName");
				password = fileProperties.readProperties(file_path, "password");
			}
		}
		else if(SdCard.checkSDcardStatus() == -1)
		{
			Toast.makeText(getApplicationContext(), "SD卡未插入", Toast.LENGTH_SHORT).show();
		}
		else 
		{
			Toast.makeText(getApplicationContext(), "无权读写SD卡", Toast.LENGTH_SHORT).show();
		}



		okButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(user_EditText.getText() == null || user_EditText.getText().equals(""))
				{
					Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(password_EditText.getText() == null || password_EditText.getText().equals(""))
				{
					Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(user_EditText.getText().equals(userName)&&password_EditText.getText().equals(password))
				{
					Toast.makeText(getApplicationContext(), "用户名密码验证成功", Toast.LENGTH_SHORT).show();
				}
				else 
				{
					Toast.makeText(getApplicationContext(), "用户名密码验证失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		

	}

	

}
