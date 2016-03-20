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
	// �ŵ�ѡ����
    private Selector selector;
 
    // �������ͨ�ŵ��ŵ�
    SocketChannel socketChannel;
 
    // Ҫ���ӵķ�����IP��ַ
    private String hostIp;
 
    // Ҫ���ӵ�Զ�̷������ڼ����Ķ˿�
    private int hostListenningPort ;
 
    private static boolean timeTocken=false;
    private static Timer timer = new Timer();
    private static boolean timerSet=false;
 
	private static void debug(String str){
		Log.i("NIOClient",str);
	}
    
    /**
     * ���캯��
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
        // �򿪼����ŵ�������Ϊ������ģʽ
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
        // �򿪲�ע��ѡ�������ŵ�
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);
    }
 
    /**
     * �����ַ�����������
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
