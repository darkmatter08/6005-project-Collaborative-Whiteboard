package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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
public class MasterServer implements Runnable{
    
    private final List<MasterWhiteboard> whiteboards;
    //private final ServerSocket serverSocket;
    private final Socket socket;
    private final ServerSocket serverSocket;
    private final List<SlaveServer> open_client_boards;
    private int whiteboardIdIncrementer = 0;
    private final MasterServerStarter god;
    
    // IO
    private ObjectOutputStream objOut = null;
    private ObjectInputStream objIn = null;
    private BufferedReader in = null; 
    private PrintWriter out = null; 
    
    // Server Strings
    private final String getWhiteboardIds = "getWhiteboardIds";
    private final String createNewWhiteboard = "createNewWhiteboard";
    private final String getWhiteboardById = "getWhiteboardById";
    
//    /**
//     * Start server
//     * @param port int port to start server.
//     * @throws IOException
//     */
//    public MasterServer(int port) throws IOException{
//        whiteboards = new ArrayList<MasterWhiteboard>();
//        //serverSocket = new ServerSocket(port);
//        open_client_boards = new ArrayList<SlaveServer>();
//        whiteboardToUsers = new HashMap<MasterWhiteboard, List<SlaveServer>>();
//    }
//    
//    /**
//     * Start server, default port 8888
//     */
//    public MasterServer() throws IOException{
//        this(Ports.MASTER_PORT);
//    }
//    
    /**
     * clientSocket's request comes from the main method in the client package(pre gui) 
     * clientSocket <=> MasterClient (Whiteboard Selector Frame)
     * serverSocket's request comes from MasterClient 
     * serverSocket <=>SlaveClient (CanvasFrame)
     * @throws IOException 
     */
    public MasterServer(List<MasterWhiteboard> masterWhiteboards, Socket clientSocket, MasterServerStarter shawn) throws IOException {
        whiteboards = masterWhiteboards;
        socket = clientSocket; // implicitly on Ports.CONNECTION_PORT
        god = shawn;
        open_client_boards = new ArrayList<SlaveServer>();
        serverSocket = new ServerSocket(Ports.MASTER_PORT);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        try {
            serve();
        } catch (IOException e) {
            throw new RuntimeException();
        }
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
    
    void announceNewWhiteboard(int newWhiteboardId) {
        for (SlaveServer client : open_client_boards) {
            //client.announceNewWhiteboard(newWhiteboardId);
            //TODO write out of a new whiteboard
        }
    }
    
    /**
     * TODO Will cause problems because it'll block forever. Need a new connection
     *  handler 
     * @throws IOException
     */
    public void serve() throws IOException {
        // request a connection to a specific board
        // wait on the serverSocket for a new connection 
        // accept on the serverSocket, and spawn a new thread
        // and pass the accepted socket to SlaveServer
        while (true){
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                String[] tokens = line.split(" ");
                if (tokens[0].equals(getWhiteboardById)){
                    Socket whiteboard_client_socket = serverSocket.accept();
                    SlaveServer ss = new SlaveServer(
                            getWhiteboardById(Integer.parseInt(tokens[1])), whiteboard_client_socket);
                    open_client_boards.add(ss);
                    ss.run();
                }
            }
        }
//        while(true) {
//            final Socket socket = serverSocket.accept();
//            SlaveServer wch = 
//                    new SlaveServer(whiteboards, socket, this); 
//            open_client_boards.add(wch);
//            new Thread(wch).run();
//        }
    }

    public void run() {
        try {
            serve();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException();
        }
        
    }
}
