/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nio.server.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.os.Handler;

/**
 *
 * @author nbento
 */
public class Controller {

	ListenerThreadClient listener;
	
    public Client createClient(int id, String address, int port) throws IOException, ClassNotFoundException {

        Client client = new Client(port, address);
        
        return client;

    }
    public Server createServer(int port) throws IOException {

        Server server = new Server(port);

        return server;
    }

    public void startClient(Client client,Handler handler) throws InterruptedException, IOException {

        client.talkToServer();

        //Start Selector
        listener = new ListenerThreadClient(client,handler);
        listener.start();
    }
    public void closeClient(Client client) throws InterruptedException, IOException {
    	client.close();
    	listener.stop();
    }

    public void startServer(Server server) throws IOException {

        server.startServer();

        ListenerThreadServer listener = new ListenerThreadServer(server);
        listener.start();

    }
}
