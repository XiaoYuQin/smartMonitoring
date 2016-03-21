/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nio.server.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 *
 * @author nbento
 */
public class ListenerThreadClient extends Thread {

    private Selector selector;
    private Client client;
    private ByteBuffer buffer;
    Handler handler;
    boolean index;

    String data;
    
    public ListenerThreadClient(Client client,Handler handler) {
        this.selector = client.getSelector();
        this.client = client;
        this.handler = handler;
    }
    public void close(){
    	index = false;
    }
    @Override
    public void run() {
        try {
        	index = true;
            // select() blocks until something happens on the underlying socket
            while (selector.select() > 0 && index == true) {

                Set keys = selector.selectedKeys();
                Iterator it = keys.iterator();

                while (it.hasNext()&& index == true) {

                    SelectionKey key = (SelectionKey) it.next();

                    SocketChannel channel = (SocketChannel) key.channel();

                    it.remove();

                    if (key.isConnectable() || key.isValid()) {
                        if (channel.isConnectionPending()) {
                            channel.finishConnect();
                            System.out.println("Connection was pending but now is finiehed connecting.");
                            //连接成功
                			Message msg2 = new Message();    
            				msg2.what = 1;    
            				handler.sendMessage(msg2);
                        }

                        if (key.isConnectable() || key.isWritable()) {

//                            String frase = "I am Client : " + client.getPort();
                        	String frase = "";
                            buffer = ByteBuffer.wrap(frase.getBytes());
                            channel.write(buffer);
                            buffer.clear();

                            //Register channel receive information for server
                            channel.register(selector, SelectionKey.OP_READ);
                            synchronized (this) {
                                wait(50);
                            }
                        }

                        if (key.isReadable()) {
                            ByteBuffer clientBuf = ByteBuffer.allocateDirect(1024);
                            clientBuf.clear();
                            channel.read(clientBuf);
                            clientBuf.flip();
//                            Charset charset1 = Charset.forName("UTF-8");
//                            CharsetDecoder decoder1 = charset1.newDecoder();
//                            CharBuffer charBuffer = decoder1.decode(clientBuf);
                            
                            Charset gb2312 = Charset.forName("GB2312");  
                            CharsetDecoder decoder = gb2312.newDecoder();  
                            CharBuffer charBuffer = decoder.decode(clientBuf);

                            System.out.println("From Server " + charBuffer.toString());
                            
                            /*
								当前电压值：250V
								当前电流值：48A
								当前湿度比：50%
								是否满足：是
                             * */
                            data += charBuffer.toString();
                           
                            
                            try{

	                            if(data.indexOf("当前电压值：") != -1){
	                            	if(data.indexOf("V")!=-1){
	                            		String jieguo = data.substring(data.indexOf("当前电压值：")+"当前电压值：".length(),data.indexOf("V"));       
	                            		Log.i("ListenerThreadClient","jieguo 1 = "+jieguo);
	                            		data = "";
										Message msg = new Message();    
										msg.what = 4;    
										Bundle bundle = new Bundle();	            						
										bundle.putString("voltage"	, jieguo);
										msg.setData(bundle);
										handler.sendMessage(msg);  
	                            	}
	                            }else if(data.indexOf("当前电流值：") != -1){
	                            	if(data.indexOf("A")!=-1){
	                            		String jieguo = data.substring(data.indexOf("当前电流值：")+"当前电流值：".length(),data.indexOf("A"));       
	                            		Log.i("ListenerThreadClient","jieguo 2 = "+jieguo);
	                            		data = "";
										Message msg = new Message();    
										msg.what = 4;    
										Bundle bundle = new Bundle();	            						
										bundle.putString("current"	, jieguo);
										msg.setData(bundle);
										handler.sendMessage(msg);  
	                            	}
	                            }else if(data.indexOf("当前湿度比：") != -1){
	                            	if(data.indexOf("%")!=-1){
	                            		String jieguo = data.substring(data.indexOf("当前湿度比：")+"当前湿度比：".length(),data.indexOf("%"));       
	                            		Log.i("ListenerThreadClient","jieguo 3 = "+jieguo);
	                            		data = "";
										Message msg = new Message();    
										msg.what = 4;    
										Bundle bundle = new Bundle();	            						
										bundle.putString("humidity"	, jieguo);
										msg.setData(bundle);
										handler.sendMessage(msg); 
	                            	}
	                            }else if(data.indexOf("当前温度：") != -1){
	                            	if(data.indexOf("℃")!=-1){
	                            		String jieguo = data.substring(data.indexOf("当前温度：")+"当前温度：".length(),data.indexOf("℃"));       
	                            		Log.i("ListenerThreadClient","jieguo 3 = "+jieguo);
	                            		data = "";
										Message msg = new Message();    
										msg.what = 4;    
										Bundle bundle = new Bundle();	            						
										bundle.putString("tempeture", jieguo);
										msg.setData(bundle);
										handler.sendMessage(msg); 
	                            	}
	                            }else if(data.indexOf("是否满足：是") != -1){
	                            	data = "";
									Message msg = new Message();    
									msg.what = 4;    
									Bundle bundle = new Bundle();	            						
									bundle.putString("meet"	, "yes");
									msg.setData(bundle);
									handler.sendMessage(msg); 
	                            }else if(data.indexOf("是否满足：否") != -1){
	                            	data = "";
									Message msg = new Message();    
									msg.what = 4;    
									Bundle bundle = new Bundle();	            						
									bundle.putString("meet"	, "no");
									msg.setData(bundle);
									handler.sendMessage(msg); 
	                            }
                            }
                            catch(StringIndexOutOfBoundsException e)
                            {					
                            	e.printStackTrace();
                            }
        					catch(java.lang.NumberFormatException e)
        					{        					
        						e.printStackTrace();
        					}
                            
                            
                            clientBuf.clear();
                            channel.register(selector, SelectionKey.OP_WRITE);
                        }

                    }
                }
            }
        } catch (IOException ex) {
            //TODO
            Logger.getLogger(ListenerThreadClient.class.getName()).log(Level.SEVERE, null, ex);
            //连接失败
			Message msg2 = new Message();    
			msg2.what = 2;    
			handler.sendMessage(msg2);
        } catch (InterruptedException ex) {
            //TODO
            Logger.getLogger(ListenerThreadClient.class.getName()).log(Level.SEVERE, null, ex);
            //连接失败
			Message msg2 = new Message();    
			msg2.what = 3;    
			handler.sendMessage(msg2);
        }
    }
}
