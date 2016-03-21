/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nio.server.client;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author nbento
 */
public class StartServer {
   
    
    public static void main(String[] args) throws InterruptedException, IOException {
        
        Controller controller = new Controller();

        Server server = controller.createServer(8400);

        controller.startServer(server);
    }
    
}
