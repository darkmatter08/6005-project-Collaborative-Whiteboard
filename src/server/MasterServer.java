package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import shared.*;

/**
 * The Server class accepts requests from clients to connect
 *  The handling of each connection is abstracted into 
 *  SlaveServer.java. This is a shell for all
 *  the connections the server has, and contains all whiteboards.
 * @author jains
 *
 */
public class MasterServer {
    private final List<MasterWhiteboard> whiteboards;
    private final ServerSocket serverSocket;
    private final List<SlaveServer> clients;
    private final Map<MasterWhiteboard, List<SlaveServer>> whiteboardToUsers;
    private int whiteboardIdIncrementer = 0;
    
    /**
     * Start server
     * @param port int port to start server.
     * @throws IOException
     */
    public MasterServer(int port) throws IOException{
        whiteboards = new ArrayList<MasterWhiteboard>();
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<SlaveServer>();
        whiteboardToUsers = new HashMap<MasterWhiteboard, List<SlaveServer>>();
    }
    
    /**
     * Start server, default port 8888
     */
    public MasterServer() throws IOException{
        this(Ports.MASTER_PORT);
    }
    
    public List<Integer> getWhiteboardIds() {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (int i = 0; i < whiteboards.size(); ++i) {
            ids.add(i);
        }
        return ids;
    }
    
    /**
     * Retrieve a canvas with a particular ID number
     * @param id The index of the desired canvas in the server's list
     * @return A Canvas object
     */
    private MasterWhiteboard getWhiteboardById(int id) {
        for (MasterWhiteboard b : whiteboards) {
            if (b.hasSameId(id))
                return b;
        }
        throw new RuntimeException("no whiteboard match");
    }
    
    /**
     * Create a new canvas with dimensions 800 (width) by 600 (height) and return its ID number.
     * @param width The width of the canvas to create, in pixels
     * @param height The height of the canvas to create, in pixels
     * @return The ID number of the newly created canvas
     */
    public synchronized int createNewWhiteBoard() {
        MasterWhiteboard w = new MasterWhiteboard(++whiteboardIdIncrementer);
        whiteboards.add(w);
        return w.getId();
    }
    
    public static void main(String[] args) throws IOException {
        new MasterServer().serve();
    }
    
    void announceNewWhiteboard(int newWhiteboardId) {
        for (SlaveServer client : clients) {
            client.announceNewWhiteboard(newWhiteboardId);
        }
    }
    
    /**
     * TODO Will cause problems because it'll block forever. Need a new connection
     *  handler 
     * @throws IOException
     */
    public void serve() throws IOException {
        while(true) {
            final Socket socket = serverSocket.accept();
            SlaveServer wch = 
                    new SlaveServer(whiteboards, socket, this);
            clients.add(wch);
            new Thread(wch).run();
        }
    }
}
