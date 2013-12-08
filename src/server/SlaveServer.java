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

import shared.WhiteboardAction;

public class SlaveServer implements Runnable {
    // Must synchronize on whiteboard, as it is shared across multiple threads
    // Every whiteboard instance is unique.
    private final MasterWhiteboard whiteboard;
    private final Socket socket;
    
    // IO
    private BufferedReader in = null;
    private PrintWriter out = null;
    private ObjectOutputStream objOut = null;
    private ObjectInputStream objIn = null;
    
    /**
     * Constructor for a new ConnectionHandler. 
     * @param whiteboard
     * @param socket
     */
    public SlaveServer(MasterWhiteboard whiteboard, Socket socket) {
        this.whiteboard = whiteboard;
        this.socket = socket;
        this.server = server;
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
        this.objOut = new ObjectOutputStream(socket.getOutputStream());
        this.objIn = new ObjectInputStream(socket.getInputStream());
        
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
            objOut.writeObject(board);
        } else
            throw new IOException("Invalid input from user");
    }

    void announceNewWhiteboard(int newWhiteboardId) {
        final String newWhiteboardAvailiable = "newWhiteboardAvailiable";
        out.write(newWhiteboardAvailiable + " " + Integer.toString(newWhiteboardId));
    }
    
}
