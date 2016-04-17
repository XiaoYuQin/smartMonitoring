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
		
		
		//�ж�sd���Ƿ���룬��������ִ���������ݡ�
		if(SdCard.checkSDcardStatus() == 0)
		{
			Toast.makeText(getApplicationContext(), "��⵽SD��", Toast.LENGTH_SHORT).show();
			//�ж��Ƿ��Ѿ����������ļ�
			String file_path = SdCard.getSdcardPath()+"/smartMonitoring.config";			
			File file = new File(file_path);
			//�����ļ������ڣ���ʼ��һ�����ò�����ȥ
			if(!file.exists())
			{
				FileProperties fileProperties  = new FileProperties();
				fileProperties.writeProperties(file_path, "userName", "root");
				fileProperties.writeProperties(file_path, "password", "123456");				
				Toast.makeText(getApplicationContext(), "��ʼ���û��ɹ�", Toast.LENGTH_SHORT).show();
			}
			//�����ļ����ڣ����趨�õ��û������������������ȫ�ֱ�����
			else
			{
				FileProperties fileProperties  = new FileProperties();
				userName = fileProperties.readProperties(file_path, "userName");
				password = fileProperties.readProperties(file_path, "password");
			}
		}
		else if(SdCard.checkSDcardStatus() == -1)
		{
			Toast.makeText(getApplicationContext(), "SD��δ����", Toast.LENGTH_SHORT).show();
		}
		else 
		{
			Toast.makeText(getApplicationContext(), "��Ȩ��дSD��", Toast.LENGTH_SHORT).show();
		}



		okButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(user_EditText.getText() == null || user_EditText.getText().equals(""))
				{
					Toast.makeText(getApplicationContext(), "�û�������Ϊ��", Toast.LENGTH_SHORT).show();
					return;
				}
				if(password_EditText.getText() == null || password_EditText.getText().equals(""))
				{
					Toast.makeText(getApplicationContext(), "���벻��Ϊ��", Toast.LENGTH_SHORT).show();
					return;
				}
				if(user_EditText.getText().equals(userName)&&password_EditText.getText().equals(password))
				{
					Toast.makeText(getApplicationContext(), "�û���������֤�ɹ�", Toast.LENGTH_SHORT).show();
				}
				else 
				{
					Toast.makeText(getApplicationContext(), "�û���������֤ʧ��", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		

	}

	

}
