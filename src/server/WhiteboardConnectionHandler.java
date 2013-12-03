package server;

import canvas.Whiteboard;
import canvas.WhiteboardAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WhiteboardConnectionHandler implements Runnable {
    // Must synchronize on whiteboard, as it is shared across multiple threads
    // Every whiteboard instance is unique.
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
    private Whiteboard getWhiteboardById(int id) {
        return whiteboards.get(id);
    }
    
    private int getWhiteboardByInstance(Whiteboard w) {
        return whiteboards.indexOf(w);
    }
    
    private void createNewWhiteboard(Whiteboard newWhiteboard) {
        whiteboards.add(newWhiteboard);
        server.announceNewWhiteboard(getWhiteboardByInstance(newWhiteboard));
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
                handleRequest(line);
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
    
    private void handleRequest(String input) throws IOException, ClassNotFoundException{
        final String getWhiteboardIds = "getWhiteboardIds";
        final String getWhiteboardById = "getWhiteboardById";
        final String drawLine = "drawLine";
        final String createNewWhiteboard = "createNewWhiteboard";
        
        String[] tokens = input.split(" ");
        
        if (tokens[0].equals(getWhiteboardIds)){
            objOut.writeObject(whiteboards);
        } else if (tokens[0].equals(getWhiteboardById)) {
            objOut.writeObject(getWhiteboardById(Integer.parseInt(tokens[1])));
        } else if (tokens[0].equals(createNewWhiteboard)) {
            Whiteboard newWhiteboard = (Whiteboard) objIn.readObject();
            createNewWhiteboard(newWhiteboard);
        } else if (tokens[0].equals(drawLine)) {
            int boardId = Integer.parseInt(tokens[1]);
            ArrayList<WhiteboardAction> actions = (ArrayList<WhiteboardAction>) objIn.readObject();
            Whiteboard board = getWhiteboardById(boardId);
            for (WhiteboardAction action : actions) {
                board.applyAction(action);
            }
        } else
            throw new IOException("Invalid input from user");
    }

    void announceNewWhiteboard(int newWhiteboardId) {
        final String newWhiteboardAvailiable = "newWhiteboardAvailiable";
        out.write(newWhiteboardAvailiable + " " + Integer.toString(newWhiteboardId));
    }
    
}
