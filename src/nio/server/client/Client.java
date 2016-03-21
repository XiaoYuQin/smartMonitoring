/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nio.server.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Client {


    private SocketChannel socketChannel;
    private int port;
    private InetSocketAddress iNetSocketAddress;
    private Selector selector;
    
    public Client(int port, String address) {
        iNetSocketAddress = new InetSocketAddress(address, port);
    }

    public int getPort(){
        return port;
    }
    	
    public Selector getSelector(){
        return selector;
    }
    public void close(){
    	try {
    		selector.close();
			socketChannel.close();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    //http://kramasundar.blogspot.pt/2009/06/java-supports-nonblocking-io-since-java.html
    public void talkToServer() throws InterruptedException, IOException {


        socketChannel = SocketChannel.open();

        // non blocking
        socketChannel.configureBlocking(false);

        // connect to a running server
        //socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8400));

        socketChannel.connect(iNetSocketAddress);
        
        // get a selector
        selector = Selector.open();

        // register the client socket with "connect operation" to the selector
        socketChannel.register(selector, SelectionKey.OP_CONNECT);


    }
}
