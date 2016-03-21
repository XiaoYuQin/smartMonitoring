package com.example.smartmonitoring;

import java.io.IOException;

import android.os.Handler;
import nio.server.client.Client;
import nio.server.client.Controller;

public class NIOThread extends Thread{

	String ip;
	int port;
	Handler handler;
	Controller controller;
	Client c1;
	
	public NIOThread(){}
	public void init(String ip,int port,Handler handler){
		this.ip = ip;
		this.port = port;
		this.handler = handler;
	}
	public void close(){
		try {
			controller.closeClient(c1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c1.close();
	}
	public void run(){
		controller = new Controller();
	    
		try {
			c1 = controller.createClient(1, this.ip,port);
			controller.startClient(c1,this.handler);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			 	        

		
	}
	
}
