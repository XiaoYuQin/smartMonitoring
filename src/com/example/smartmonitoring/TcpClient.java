package com.example.smartmonitoring;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;


public class TcpClient extends Thread{
	
	private String ip;
	private int port;
	private Socket s;
	private BufferedWriter bw;
	private BufferedReader br;
	Handler handler;
	boolean result;
	
	public TcpClient(){

	}
	public void init(String ip,int port,Handler handler){
		this.ip = ip;
		this.port = port;		
		this.handler = handler;
	}
	public void run(){
		Message msg2 = new Message();   
		result = open(ip,port);
		if(result == true){
			msg2.what = 1;    
			handler.sendMessage(msg2);
		}else{
			msg2.what = 2;    
			handler.sendMessage(msg2);
		}
		
		while(result){
			try {
				String data = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		msg2.what = 2;    
		handler.sendMessage(msg2);
	}
	public void closeTcp(){
		result = false;
	}
	private Boolean open(String ip, int port){
	    try{
    		this.ip = ip;
    		this.port = port;
			s = new Socket(ip, port);  
			bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
	      	br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	      	System.out.println("TcpClientReciver thread statr");  
	      	return true;
		}
	    catch (IOException e){
			e.printStackTrace();
			return false;
		}  		
	}
}

//Message msg2 = new Message();    
//msg2.what = 200;    
//handler.sendMessage(msg2);
