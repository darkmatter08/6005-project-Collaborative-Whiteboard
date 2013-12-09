package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The MasterServerStarter listens for client requests to connect 
 *  to the server. It spawns a new thread with a MasterServer for each 
 *  client that requests a connection.
 * @author jains
 *
 */
public class MasterServerStarter implements Runnable {
    
    private final List<MasterWhiteboard> whiteboards;
    private final ServerSocket serverSocket;
    private final List<MasterServer> clients;
    private int whiteboardIdIncrementer = 0;
    //private final Map<MasterWhiteboard, List<SlaveServer>> whiteboardToUsers;
    
    public static void main(String[] args) throws IOException {
        MasterServerStarter mss = new MasterServerStarter(shared.Ports.CONNECTION_PORT);
        mss.serve();
    }
    
    public MasterServerStarter(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        whiteboards = new ArrayList<MasterWhiteboard>();
        clients = new ArrayList<MasterServer>();
    }
    
    public void serve() throws IOException {
        System.out.println("started server");
        while(true) {
            final Socket socket = serverSocket.accept();
            MasterServer wch = 
                    new MasterServer(whiteboards, socket, this);
            clients.add(wch);
            new Thread(wch).start();
        }
    }
    
    /**
     * Create a new whiteboard and notify all the clients of the new whiteboard
     * @throws IOException 
     */
    synchronized void createNewWhiteboard() throws IOException {
        MasterWhiteboard w = new MasterWhiteboard(++whiteboardIdIncrementer);
        whiteboards.add(w);
        
        for (MasterServer client: clients) {
            client.announceNewWhiteboard();
        }
    }
    
    public void run() {
        try {
            serve();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
