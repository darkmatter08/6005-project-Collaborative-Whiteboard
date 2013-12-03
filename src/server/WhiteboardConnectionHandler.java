package server;

import canvas.Whiteboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WhiteboardConnectionHandler implements Runnable{
    // Must synchronize on whiteboard, as it is shared across multiple threads
    private final List<Whiteboard> whiteboards;
    private final Socket socket;
    private final Server server;
    
    // IO
    private BufferedReader in = null;
    private PrintWriter out = null;
    private ObjectOutputStream objOut = null;
    private ObjectInputStream objIn = null;
    
    /**
     * Constructor for a new ConnectionHandler. 
     * @param w
     * @param s
     */
    public WhiteboardConnectionHandler(List<Whiteboard> w, Socket s, Server god) {
        whiteboards = w;
        socket = s;
        server = god;
    }
    
    /**
     * @return a list of all whiteboard IDs
     */
    private List<Integer> getWhiteBoardIds() {
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
    private Whiteboard getWhiteboardByID(int id) {
        return whiteboards.get(id);
    }
    
    /**
     * Create a new blank whiteboard and return its ID number.
     * @param width The width of the whiteboard to create, in pixels
     * @param height The height of the whiteboard to create, in pixels
     * @return The ID number of the newly created canvas
     */
    private synchronized int createNewWhiteBoard(int width, int height) {
        Whiteboard w = new Whiteboard();//createImage(BOARD_WIDTH, BOARD_HEIGHT));
        w.fillWithWhite();
        whiteboards.add(w);
        return whiteboards.size() - 1;
    }
    
    /**
     * Create a new canvas with dimensions 800 (width) by 600 (height) and return its ID number.
     * @param width The width of the canvas to create, in pixels
     * @param height The height of the canvas to create, in pixels
     * @return The ID number of the newly created canvas
     */
    private synchronized int createNewWhiteBoard() {
        return createNewWhiteBoard(800, 600);
    }
    
    public void run() {
        // handle the client
        try {
            handleConnection();
        } catch (IOException e) {
            e.printStackTrace(); // but don't terminate serve()
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void handleConnection() throws IOException {
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.objOut = new ObjectOutputStream(socket.getOutputStream());
        this.objIn = new ObjectInputStream(socket.getInputStream());
        
        // Send list of Whiteboards
        objOut.writeObject(whiteboards);
        
        try {
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                String output = handleRequest(line);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            out.close();
            in.close();
            objOut.close();
            socket.close();
        }
    }
    
    private String handleRequest(String input) throws IOException, ClassNotFoundException{
        final String getWhiteboardIds = "getWhiteboardIds";
        final String getWhiteboardById = "getWhiteboardById";
        final String drawLine = "drawLine";
        final String createNewWhiteboard = "createNewWhiteboard";
        
        String[] tokens = input.split(" ");
        
        if (tokens[0].equals(getWhiteboardIds)){
            objOut.writeObject(whiteboards);
            return null;
        } else if (tokens[0].equals(getWhiteboardById)) {
            objOut.writeObject(getWhiteboardByID(Integer.parseInt(tokens[1])));
            return null;
        } else if (tokens[0].equals(createNewWhiteboard)) {
            Whiteboard newWhiteboard = (Whiteboard) objIn.readObject();
        }
        throw new IOException("Invalid input from user");
    }

    void announceNewWhiteboard(int newWhiteboardId) {
        final String newWhiteboardAvailiable = "newWhiteboardAvailiable";
        out.write(newWhiteboardAvailiable + " " + Integer.toString(newWhiteboardId));
    }
    
}
