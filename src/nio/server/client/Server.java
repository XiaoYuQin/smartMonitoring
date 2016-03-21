/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nio.server.client;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nbento
 */
public class Server implements Serializable{
    
    private List clients;
    private ServerSocketChannel serverSocket;
    private Selector selector;
    private int port;
    private InetSocketAddress address;
    
    
    public Server(int port){
        this.port = port;
        this.address = new InetSocketAddress(port);
        this.clients = new ArrayList();
    }
    
    public Selector getSelector(){
        return selector;
    }
    
    public void addClient(Client client){
        clients.add(client);
    }
    
    public List getClient(){
        return clients;
    }
    
    public int getPort(){
        return port;
    }
    
    public InetSocketAddress getInetSocketAddress(){
        return address;
    }
    
    public void startServer() throws IOException{
        
        // setup the socket we're listening for connections on.
        serverSocket = ServerSocketChannel.open();
        
        serverSocket.configureBlocking(false);
        
        serverSocket.socket().bind(address);
        
        // setup our selector and register the main socket on it
        selector = Selector.open();
        
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    }
    
}
