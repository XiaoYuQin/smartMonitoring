package com.example.smartmonitoring;

import java.io.IOException;

import nio.server.client.Client;
import nio.server.client.Controller;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView TextViewTempeture;
	TextView TextViewHumidity;
	TextView TextViewVoltage;
	TextView TextViewCurrent;
	EditText EditViewIP1;
	EditText EditViewIP2;
	EditText EditViewIP3;
	EditText EditViewIP4;
	EditText EditViewPort;
	
	TextView LimitTempeture;
	TextView LimitHumidity;
	TextView LimitVoltage;
	TextView LimitCurrent;
	
    Button button;
    Button Button01;
	EditText EditTextTemp;
	EditText EditTextHua;
	EditText EditTextVol;
	EditText EditTextCur;
	
	TextView TextView14;
	TextView TextView15;
	TextView TextView16;
	TextView TextView17;


    public Handler handler;  
    boolean tcpFlag = false;
    
    float maxTemp;
    float maxShiDu;
    float maxVoltage;
    float maxCurrent;
    
	private static String tmp = ""; 
	
	private static void debug(String str){
		Log.i("MainActivity",str);
	}
	
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		debug("onCreate");
		
		TextViewTempeture = (TextView)findViewById(R.id.textView2);
		TextViewHumidity = (TextView)findViewById(R.id.TextView04);
		TextViewVoltage = (TextView)findViewById(R.id.TextView05);
		TextViewCurrent = (TextView)findViewById(R.id.TextView06);
		button = (Button)findViewById(R.id.button1);
		Button01 = (Button)findViewById(R.id.Button01);

	 
		EditViewIP1 = (EditText)findViewById(R.id.EditText01);
		EditViewIP2 = (EditText)findViewById(R.id.editText1);
		EditViewIP3 = (EditText)findViewById(R.id.EditText02);
		EditViewIP4 = (EditText)findViewById(R.id.EditText03);
		EditViewPort = (EditText)findViewById(R.id.EditText04);

		
		EditTextTemp = (EditText)findViewById(R.id.EditText05);
		EditTextHua = (EditText)findViewById(R.id.EditText06);
		EditTextVol = (EditText)findViewById(R.id.EditText07);
		EditTextCur = (EditText)findViewById(R.id.EditText08);
		
		TextView14 = (TextView)findViewById(R.id.TextView14);
		TextView15 = (TextView)findViewById(R.id.TextView15);
		TextView16 = (TextView)findViewById(R.id.TextView16);
		TextView17 = (TextView)findViewById(R.id.TextView17);
		

		
		debug("22222");
		button.setText("连接");
		
		TextViewTempeture.setText("---℃");
		TextViewHumidity.setText("---%");
		
		TextViewVoltage.setText("---V");
		TextViewCurrent.setText("---A");
		debug("33333");
//		EditViewIP1.setText("192");
//		EditViewIP2.setText("168");
//		EditViewIP3.setText("3");
//		EditViewIP4.setText("104");
//		EditViewPort.setText("8040");
		
		maxTemp = 100;
		maxShiDu = 80;
		maxVoltage = 220;
		maxCurrent = 50;
		
		TextView14.setText(""+maxTemp+"℃");
		TextView15.setText(""+maxShiDu+"%");
		TextView16.setText(""+maxVoltage+"V");
		TextView17.setText(""+maxCurrent+"A");
		
//		 Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent   
//	     Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
//	     maxTemp=bundle.getFloat("temp");   
//	     maxShiDu=bundle.getFloat("ham");   
//	     maxVoltage=bundle.getFloat("vol");   
//	     maxCurrent=bundle.getFloat("cur");   	    

		debug("11111");

		Button01.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				try{			
					
				    maxTemp = Float.parseFloat(EditTextTemp.getText().toString());
					TextView14.setText(""+maxTemp+"℃");
				    debug("maxTemp = "+maxTemp);
				    maxShiDu = Float.parseFloat(EditTextHua.getText().toString());
				    debug("maxShiDu = "+maxShiDu);
				    TextView15.setText(""+maxShiDu+"%");
				    maxVoltage = Float.parseFloat(EditTextVol.getText().toString());
				    debug("maxVoltage = "+maxVoltage);
				    TextView16.setText(""+maxVoltage+"V");
				    maxCurrent = Float.parseFloat(EditTextCur.getText().toString());	
				    TextView17.setText(""+maxCurrent+"A");
				    debug("maxCurrent = "+maxCurrent);
				}
				catch(java.lang.NumberFormatException e)
				{
//					Toast.makeText(getApplicationContext(), "输入数据有误", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});
		
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				 NIOSocketClient tcpClient = null;
				NIOThread nioThread;
				nioThread = new NIOThread();
				if(tcpFlag == false){
					String ip1 = EditViewIP1.getText().toString();
					String ip2 = EditViewIP2.getText().toString();
					String ip3 = EditViewIP3.getText().toString();
					String ip4 = EditViewIP4.getText().toString();
					String port = EditViewPort.getText().toString();
					
					try{					
						if(Integer.parseInt(ip1)>255||Integer.parseInt(ip1)<0){
							Toast.makeText(getApplicationContext(), "IP地址输入有误", Toast.LENGTH_SHORT).show();
							return;
						}
						if(Integer.parseInt(ip2)>255||Integer.parseInt(ip2)<0){
							Toast.makeText(getApplicationContext(), "IP地址输入有误", Toast.LENGTH_SHORT).show();
							return;
						}
						if(Integer.parseInt(ip3)>255||Integer.parseInt(ip3)<0){
							Toast.makeText(getApplicationContext(), "IP地址输入有误", Toast.LENGTH_SHORT).show();
							return;
						}
						if(Integer.parseInt(ip4)>255||Integer.parseInt(ip4)<0){
							Toast.makeText(getApplicationContext(), "IP地址输入有误", Toast.LENGTH_SHORT).show();
							return;
						}
						if(Integer.parseInt(port)>65535||Integer.parseInt(port)<=0){
							Toast.makeText(getApplicationContext(), "端口输入有误", Toast.LENGTH_SHORT).show();
							return;
						}
						String ip = ip1+"."+ip2+"."+ip3+"."+ip4;
						debug("ip = "+ip);
						debug("port = "+port);
						nioThread.init(ip, Integer.parseInt(port),handler);
						nioThread.start();
						button.setClickable(false);
						button.setText("连接中");
					}
					catch(java.lang.NumberFormatException e)
					{
						Toast.makeText(getApplicationContext(), "IP地址端口输入有误", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
				else{
					//已经连接成功了,再按下就断开连接
					debug("close");
//					tcpClient.close();
					try{
            			Message msg2 = new Message();    
        				msg2.what = 3;    
        				handler.sendMessage(msg2);
						nioThread.close();			
					}
					catch(Exception e){
						e.printStackTrace();
					}
					
				}
			}
		});
		
		handler = new Handler(){
	    	public void handleMessage(Message msg){    
	    		switch(msg.what){
	     			case 1:
	     				//连接成功
	     				//按下按键能够关闭tcp连接
	     				Toast.makeText(getApplicationContext(), "连接服务器成功", Toast.LENGTH_SHORT).show();
	     				tcpFlag = true;
	     				button.setClickable(true);
	     				button.setText("断开");
        			break;
	     			case 2:
	     				//连接失败
	     				Toast.makeText(getApplicationContext(), "连接服务器失败", Toast.LENGTH_SHORT).show();
	     				tcpFlag = false;
	     				button.setText("连接");
	     				button.setClickable(true);
        			break;
	     			case 3:
	     				//连接失败
	     				Toast.makeText(getApplicationContext(), "断开服务器成功", Toast.LENGTH_SHORT).show();
	     				tcpFlag = false;
	     				button.setText("连接");
	     				button.setClickable(true);
        			break;
	     			case 4:
	     			{
        				Bundle bundle = new Bundle();
        				bundle = msg.getData();
        				String voltage = bundle.getString("voltage");
        				String current = bundle.getString("current");
        				String humidity = bundle.getString("humidity");        		
        				String tempeture = bundle.getString("tempeture");
        				
        				if(voltage != null && voltage != ""){
        					TextViewVoltage.setText(voltage+"V");
        					try{			
        					    float value = Float.parseFloat(voltage);
        					    if(value > maxVoltage){

          					    	 final AlertDialog adRef = new AlertDialog.Builder(MainActivity.this).create();  
        					         adRef.setTitle("警告！");  
        					         adRef.setMessage("电压超限");  
        					         adRef.show();  
        					    	
        					    	Handler handler = new Handler();  
        					        handler.postDelayed(new Runnable() {  
        					 
        					            public void run() {  
        					                adRef.dismiss();  
        					            }  
        					        }, 3000);  
        					    }
        					}
        					catch(java.lang.NumberFormatException e)
        					{
        						Toast.makeText(getApplicationContext(), "电压数据错误", Toast.LENGTH_SHORT).show();
        						e.printStackTrace();
        					}
        				}
						if(current != null && current != ""){
							TextViewCurrent.setText(current+"A");
        					try{			
        					    float value = Float.parseFloat(current);
        					    if(value > maxCurrent){
	        					   	final AlertDialog adRef = new AlertDialog.Builder(MainActivity.this).create();  
	        					   	adRef.setTitle("警告！");  
	        					   	adRef.setMessage("电流超限"); 
	    					        adRef.show();  
	    					    	
	    					    	Handler handler = new Handler();  
	    					        handler.postDelayed(new Runnable() {  
	    					 
	    					            public void run() {  
	    					                adRef.dismiss();  
	    					            }  
	    					        }, 3000);  
        					    }
        					}
        					catch(java.lang.NumberFormatException e)
        					{
        						Toast.makeText(getApplicationContext(), "电流数据错误", Toast.LENGTH_SHORT).show();
        						e.printStackTrace();
        					}
						}
						if(humidity != null && humidity != ""){
							TextViewHumidity.setText(humidity+"%");
        					try{			
        					    float value = Float.parseFloat(humidity);
        					    if(value > maxShiDu){

          					    	 final AlertDialog adRef = new AlertDialog.Builder(MainActivity.this).create();  
          					    	adRef.setTitle("警告！");  
       					         adRef.setMessage("湿度超限");
        					         adRef.show();  
        					    	
        					    	Handler handler = new Handler();  
        					        handler.postDelayed(new Runnable() {  
        					 
        					            public void run() {  
        					                adRef.dismiss();  
        					            }  
        					        }, 3000);  
        					    }
        					}
        					catch(java.lang.NumberFormatException e)
        					{
        						Toast.makeText(getApplicationContext(), "湿度数据错误", Toast.LENGTH_SHORT).show();
        						e.printStackTrace();
        					}
						}
						if(tempeture != null && tempeture != ""){
							TextViewTempeture.setText(tempeture+"℃");
        					try{			
           					    float value = Float.parseFloat(tempeture);
        					    if(value > maxTemp){
	        					   	final AlertDialog adRef = new AlertDialog.Builder(MainActivity.this).create();  
	        					   	adRef.setTitle("警告！");  
	        					   	adRef.setMessage("温度超限");  
	    					        adRef.show();  
	    					    	
	    					    	Handler handler = new Handler();  
	    					        handler.postDelayed(new Runnable() {  
	    					 
	    					            public void run() {  
	    					                adRef.dismiss();  
	    					            }  
	    					        }, 3000);  
        					    }
        					}
        					catch(java.lang.NumberFormatException e)
        					{
        						Toast.makeText(getApplicationContext(), "温度数据错误", Toast.LENGTH_SHORT).show();
        						e.printStackTrace();
        					}
						}
	     			}
	     			break;
	     			case 5:
	     			{
	     				
        				Bundle bundle = new Bundle();
        				bundle = msg.getData();
        				maxTemp = bundle.getFloat("temp");
        				maxShiDu = bundle.getFloat("ham");
        				maxVoltage = bundle.getFloat("vol");
        				maxCurrent = bundle.getFloat("cur");
        				debug(""+maxTemp);
        				debug(""+maxShiDu);
        				debug(""+maxVoltage);
        				debug(""+maxCurrent);
        				
	     			}
	     			break;
        		}
	    	}
		};
			
	}

}
