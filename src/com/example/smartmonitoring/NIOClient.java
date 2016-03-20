package com.example.smartmonitoring;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Timer;

import android.util.Log;

public class NIOClient {
	// 信道选择器
    private Selector selector;
 
    // 与服务器通信的信道
    SocketChannel socketChannel;
 
    // 要连接的服务器IP地址
    private String hostIp;
 
    // 要连接的远程服务器在监听的端口
    private int hostListenningPort ;
 
    private static boolean timeTocken=false;
    private static Timer timer = new Timer();
    private static boolean timerSet=false;
 
	private static void debug(String str){
		Log.i("NIOClient",str);
	}
    
    /**
     * 构造函数
     *
     * @param HostIp
     * @param HostListenningPort
     * @throws IOException
     */
    public NIOClient(String HostIp, int HostListenningPort) throws IOException {
        this.hostIp = HostIp;
        this.hostListenningPort = HostListenningPort;
        initialize();
    }
 
    private void initialize() throws IOException {
        // 打开监听信道并设置为非阻塞模式
        try{
        	debug("initialize");
        	socketChannel = SocketChannel.open(new InetSocketAddress(hostIp,hostListenningPort));
        	debug("opend");
        }
        catch(ConnectException e){
            System.out.println("Error happened when establishing connection, try again 5s later");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            if(!timerSet){
                timer.schedule(new SetTocken(), 15000);
                timerSet=true;
            }
            if(!timeTocken){
                initialize();
            }
            return;
        }
        socketChannel.configureBlocking(false);
        // 打开并注册选择器到信道
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);
    }
 
    /**
     * 发送字符串到服务器
     *
     * @param message
     * @throws IOException
     */
    public void sendMsg(String message) throws IOException {
        ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("UTF-8"));
        socketChannel.write(writeBuffer);
    }
 
//    public static void main(String[] args) throws IOException {
//        NIOClient client = new NIOClient("127.0.0.1", 12000);
//        client.sendMsg("This is a NIOClient, testing");
//        client.end();
//        timer.cancel();
//    }
 
    public void end() {
        // TODO Auto-generated method stub
        try {
            socketChannel.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
 
    static class SetTocken extends java.util.TimerTask {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            timeTocken=true;
        }
    }
}
