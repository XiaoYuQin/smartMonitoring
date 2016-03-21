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
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

/**
* User: mihasya
* Date: Jul 25, 2010
* Time: 9:09:03 AM
*/

/*
* the thread is completely unnecessary, it could all just happen
* in main()
*/
class ListenerThreadServer extends Thread {
    
    private ByteBuffer bufferWrite;
    private Server server;
    private ServerSocketChannel serverSocketChannel;
    private SocketChannel socketChannel;
    private Selector selector;
    
    public ListenerThreadServer(Server server) {
        this.selector = server.getSelector();
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            
            String frase = "Got it";
            
            // our canned response for now
            ByteBuffer bufferRead = ByteBuffer.wrap(frase.getBytes());
            
            try {
                // loop over all the sockets that are ready for some activity
                while (selector.select() > 0) {
                    
                    Set keys = selector.selectedKeys();
                    Iterator i = keys.iterator();
                    
                    while (i.hasNext()) {
                        
                        SelectionKey key = (SelectionKey)i.next();
                        
                        if (key.isAcceptable()) {
                            // this means that a new client has hit the port our main
                            // socket is listening on, so we need to accept the connection
                            // and add the new client socket to our select pool for reading
                            // a command later
                            System.out.println("Accepting connection!");
                            // this will be the ServerSocketChannel we initially registered
                            // with the selector in main()
                            serverSocketChannel = (ServerSocketChannel)key.channel();
                            socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            
                            server.addClient(new Client(socketChannel.socket().getPort(), socketChannel.socket().getLocalAddress().getHostName()));
                            
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            // one of our client sockets has received a command and
                            // we're now ready to read it in
                            System.out.println("Accepting command!");
                            socketChannel = (SocketChannel)key.channel();
                            
                            bufferWrite = ByteBuffer.allocate(200);
                            socketChannel.read(bufferWrite);
                            bufferWrite.flip();
                            Charset charset = Charset.forName("UTF-8");
                            CharsetDecoder decoder = charset.newDecoder();
                            CharBuffer cbuf = decoder.decode(bufferWrite);
                            System.out.print(cbuf.toString());
                            // re-register this socket with the selector, this time
                            // for writing since we'll want to write something to it
                            // on the next go-around
                            socketChannel.register(selector, SelectionKey.OP_WRITE);
                        } else if (key.isWritable()) {
                            // we are ready to send a response to one of the client sockets
                            // we had read a command from previously
                            System.out.println("Sending response!");
                            socketChannel = (SocketChannel)key.channel();
                            socketChannel.write(bufferRead);
                            bufferRead.rewind();
                            // we may get another command from this guy, so prepare
                            // to read again. We could also close the channel, but
                            // that sort of defeats the whole purpose of doing async
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        }
                        i.remove();
                    }
                }
            } catch (IOException e) {
                System.out.println("Error in poll loop");
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }
    }
}
