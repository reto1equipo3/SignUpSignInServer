/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.ServerSocket;


/**
 * This class connects the server side with the Client socket
 * @author Igor
 */
public class SocketServer {
    
    /**
     * @param PORT int: The port that ServerSocket use to connect with the ClientSocket
     * 
     */
    private static final Logger LOGGER = Logger.getLogger("signupsigninserver.ILogicImplementation");
    private static int PORT = 6000;

   
    /**
    * The method that start the server side of the socket and create the 
    * thread for the client
    * 
    */
    public void start(){
        ServerSocket server = null;
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try{
            server = new ServerSocket(PORT);
            while(true){
                LOGGER.info("Waiting for connection from client...");
                socket = server.accept();
                LOGGER.info("Client connected successfully!");
                
                //Creating the thread for this client
                ConnectionThread thread = new ConnectionThread();
                thread.setSocket(socket);
                thread.start();
                
            }
        } catch (IOException e) {
		LOGGER.info("Error: "+ e.getMessage());

	} catch (Exception e) {
                LOGGER.info("Error: "+ e.getMessage());
        } finally{
            try{
                if(oos!=null){
                    oos.close();
                }
                if(ois!=null){
                    ois.close();
                }
                if(socket!=null){
                    socket.close();
                }
                if(server!=null){
                    server.close();
                }
            } catch(IOException ex){
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * Main method to start the Server side
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SocketServer server = new SocketServer();
        server.start();
    }
    
}