package com.example.smartmonitoring;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NIOSocketClient extends Thread{
	String ip;
	int port;
	Handler handler;
	boolean flag;
	
	private static final int CLIENT_PORT = 10201; 
	private static void debug(String str){
		Log.i("NIOSocketClient",str);
	}
	public NIOSocketClient(){}
	public void init(String ip,int port){
		this.ip = ip;
		this.port = port;
	}
	public void setHandler(Handler handler){
		this.handler = handler;
	}

	public void close(){
		this.flag = false;
	}
	public void run(){  
		SocketChannel sc = null;
		Selector sel = null;
		this.flag = true;
		try{
			try{  		        
				sc = SocketChannel.open();	
				debug("222");
		        sel = Selector.open();  
		    	debug("333");
			    sc.configureBlocking(false);  
			    sc.socket().bind(new InetSocketAddress(CLIENT_PORT));  
			    sc.register(sel, SelectionKey.OP_READ | SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT);  
				debug("444");
		        int i = 0;  
		        boolean written = false;  
		        //boolean done = false;  
		        String encoding = System.getProperty("file.encoding");  
		        Charset cs = Charset.forName(encoding);  
		        ByteBuffer buf = ByteBuffer.allocate(50);  
//		        while(this.flag)
//		        {
		        debug(" while ");
			        while(/*!done && */this.flag){  
			        	debug("555");
					    sel.select();  
						
					    Iterator it = sel.selectedKeys().iterator();  
						    while(it.hasNext()){  
						    	debug("111");
							    SelectionKey key = (SelectionKey)it.next();  
							    it.remove();  
							    //获取创建通道选择器事件键的套接字通道  
							    sc = (SocketChannel)key.channel();  
							    //当前通道选择器产生连接已经准备就绪事件，并且客户端套接字  
							    //通道尚未连接到服务端套接字通道  
								if(key.isConnectable() && !sc.isConnected()){  
									InetAddress addr = InetAddress.getByName(this.ip);  
									//客户端套接字通道向服务端套接字通道发起非阻塞连接  
									boolean success;
									success = sc.connect(new InetSocketAddress(addr, this.port));  
									//如果客户端没有立即连接到服务端，则客户端完成非立即连接操作  
									while (!sc.finishConnect()) {  
										success = sc.connect(new InetSocketAddress(addr, this.port));  
							        }  
									if(!success){
										//连接失败
										sc.finishConnect();  
										Message msg = new Message();   
										msg.what = 2;    
										handler.sendMessage(msg);
										debug(" false ");
										this.flag = false;
									} 
									else{
										//连接成功
										Message msg = new Message();   
										msg.what = 1;    
										handler.sendMessage(msg);
										debug(" sucess ");
										this.flag = true;
									}
								}  
								while(this.flag)
								{									
									//如果通道选择器产生读取操作已准备好事件，且已经向通道写入数据  
									if(key.isReadable() && written){  
										    if(sc.read((ByteBuffer)buf.clear()) > 0){  
										    written = false;  
										    //从套接字通道中读取数据  
										    String response = cs.decode((ByteBuffer)buf.flip()).toString();  
										    System.out.println(response);  
										    //if(response.indexOf("END") != -1) done = true;  
										}  
									}  
									//如果通道选择器产生写入操作已准备好事件，并且尚未想通道写入数据  
									if(key.isWritable() && !written){  
										//向套接字通道中写入数据  
										if(i < 10) sc.write(ByteBuffer.wrap(new String("howdy " + i + '\n').getBytes()));  
										else if(i == 10)sc.write(ByteBuffer.wrap(new String("END").getBytes()));  
										written = true;  
										i++;  
									} 									
								}
						    }  
			       	}  		
			        this.flag = false;
//				}
			}
			finally{  
				debug(" close sc sel ");
			    sc.close();  
			    sel.close();  
				Message msg = new Message();   
				msg.what = 2;    
				handler.sendMessage(msg);
			} 
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			debug("IOException");
			e.printStackTrace();
		}  
		catch (NotYetConnectedException e){
			debug("NotYetConnectedException");
			e.printStackTrace();
			
		}
		debug("run out");
	}
}
