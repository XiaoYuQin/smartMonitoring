package com.example.smartmonitoring;

import java.io.IOException;

import com.qinxiaoyu.lib.android.SdCard;
import com.qinxiaoyu.lib.util.file.File;
import com.qinxiaoyu.lib.util.file.FileProperties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	
	String file_path;
	public String getFile_path() {
		return file_path;
	}

	String userName = "root";
	String password = "123456";
	
	String otherName = "";
	String otherPassword = "";
	
	String isSave = "no";
	
	Button okButton;
	EditText user_EditText;
	EditText password_EditText;
	
	CheckBox saveCheckBox; 
	
	private void debug(String str)
	{
		Log.i("LoginActivity",str);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		okButton = (Button)findViewById(R.id.login_button1);
		user_EditText = (EditText)findViewById(R.id.login_editText1);
		password_EditText = (EditText)findViewById(R.id.login_EditText01);
		saveCheckBox = (CheckBox)findViewById(R.id.checkBox1);
		
		//判断sd卡是否插入，若插入则执行以下内容。
		if(SdCard.checkSDcardStatus() == 0)
		{
			Toast.makeText(getApplicationContext(), "检测到SD卡", Toast.LENGTH_SHORT).show();
			//判断是否已经存在配置文件
			file_path = SdCard.getSdcardPath()+"/smartMonitoring.config";
			debug(file_path);
			File file = new File(file_path);
			//配置文件不存在，初始化一个配置参数进去
			if(!file.exists())
			{
				try 
				{
					debug("初始化配置文件");					
					file.createNewFile();
					FileProperties fileProperties  = new FileProperties();
					fileProperties.writeProperties(file_path, "userName", "root");
					fileProperties.writeProperties(file_path, "password", "123456");	
					fileProperties.writeProperties(file_path, "isSave", "yes");
					Toast.makeText(getApplicationContext(), "初始化用户成功", Toast.LENGTH_SHORT).show();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//配置文件存在，把设定好的用户名和密码读出，存在全局变量里
			else
			{
				debug("读取配置文件");
				try
				{
					FileProperties fileProperties  = new FileProperties();
					userName = fileProperties.readProperties(file_path, "userName");
					password = fileProperties.readProperties(file_path, "password");
					otherName = fileProperties.readProperties(file_path, "otherName");
					otherPassword = fileProperties.readProperties(file_path, "otherPassword");
					isSave = fileProperties.readProperties(file_path, "isSave");
				}catch(Exception e) 
				{
					e.printStackTrace();
					FileProperties fileProperties  = new FileProperties();
					fileProperties.writeProperties(file_path, "userName", "root");
					fileProperties.writeProperties(file_path, "password", "123456");	
					fileProperties.writeProperties(file_path, "isSave", "yes");
				}
				debug("读参数完成");
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

		if(isSave.equals("yes"))
		{			
			user_EditText.setText(otherName);
			password_EditText.setText(otherPassword);
			saveCheckBox.setChecked(true);
		}
		else {
			saveCheckBox.setChecked(false);
		}

		saveCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
	            @Override
	            public void onCheckedChanged(CompoundButton buttonView, 
	                    boolean isChecked) { 
	                // TODO Auto-generated method stub 
	                if(isChecked){ 
	                	debug("isChecked true");
	                	FileProperties fileProperties  = new FileProperties();
						fileProperties.writeProperties(file_path, "isSave", "yes");
//						fileProperties.writeProperties(file_path, "otherName", user_EditText.getText().toString());
//						fileProperties.writeProperties(file_path, "otherPassword", password_EditText.getText().toString());
	                }else{
	                	debug("isChecked false");
	                	FileProperties fileProperties  = new FileProperties();
						fileProperties.writeProperties(file_path, "isSave", "false");						
						fileProperties.writeProperties(file_path, "otherName", "");
						fileProperties.writeProperties(file_path, "otherPassword", "");
	                } 
	            } 
	        }); 
		

		okButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				//debug("用户名不能为空"+user_EditText.getText()+"|");
				if(user_EditText.getText().toString() == null || user_EditText.getText().toString().equals(""))								
				{
					debug("用户名不能为空");
					Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(password_EditText.getText().toString() == null || password_EditText.getText().toString().equals(""))
				{
					debug("密码不能为空");
					Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(user_EditText.getText().toString().equals(userName)&&password_EditText.getText().toString().equals(password))
				{
					if(isSave.equals("yes"))
					{			
						FileProperties fileProperties  = new FileProperties();
						fileProperties.writeProperties(file_path, "otherName", user_EditText.getText().toString());
						fileProperties.writeProperties(file_path, "otherPassword", password_EditText.getText().toString());
					}
					
					debug("用户名密码验证成功");
					Toast.makeText(getApplicationContext(), "用户名密码验证成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LoginActivity.this,SelectActivity.class);
	            	startActivity(intent);
	            	//finish();
				}
				else 
				{
					if(isSave.equals("yes"))
					{			
						FileProperties fileProperties  = new FileProperties();
						fileProperties.writeProperties(file_path, "otherName", user_EditText.getText().toString());
						fileProperties.writeProperties(file_path, "otherPassword", password_EditText.getText().toString());
					}

					debug("用户名密码验证失败");
					Toast.makeText(getApplicationContext(), "用户名密码验证失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		

	}

	

}
