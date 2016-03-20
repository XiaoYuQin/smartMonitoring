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
							    //��ȡ����ͨ��ѡ�����¼������׽���ͨ��  
							    sc = (SocketChannel)key.channel();  
							    //��ǰͨ��ѡ�������������Ѿ�׼�������¼������ҿͻ����׽���  
							    //ͨ����δ���ӵ�������׽���ͨ��  
								if(key.isConnectable() && !sc.isConnected()){  
									InetAddress addr = InetAddress.getByName(this.ip);  
									//�ͻ����׽���ͨ���������׽���ͨ���������������  
									boolean success;
									success = sc.connect(new InetSocketAddress(addr, this.port));  
									//����ͻ���û���������ӵ�����ˣ���ͻ�����ɷ��������Ӳ���  
									while (!sc.finishConnect()) {  
										success = sc.connect(new InetSocketAddress(addr, this.port));  
							        }  
									if(!success){
										//����ʧ��
										sc.finishConnect();  
										Message msg = new Message();   
										msg.what = 2;    
										handler.sendMessage(msg);
										debug(" false ");
										this.flag = false;
									} 
									else{
										//���ӳɹ�
										Message msg = new Message();   
										msg.what = 1;    
										handler.sendMessage(msg);
										debug(" sucess ");
										this.flag = true;
									}
								}  
								while(this.flag)
								{									
									//���ͨ��ѡ����������ȡ������׼�����¼������Ѿ���ͨ��д������  
									if(key.isReadable() && written){  
										    if(sc.read((ByteBuffer)buf.clear()) > 0){  
										    written = false;  
										    //���׽���ͨ���ж�ȡ����  
										    String response = cs.decode((ByteBuffer)buf.flip()).toString();  
										    System.out.println(response);  
										    //if(response.indexOf("END") != -1) done = true;  
										}  
									}  
									//���ͨ��ѡ��������д�������׼�����¼���������δ��ͨ��д������  
									if(key.isWritable() && !written){  
										//���׽���ͨ����д������  
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
