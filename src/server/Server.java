package server;

import java.util.*;
import canvas.*;

public class Server implements WhiteBoardServer {
    private ArrayList<Canvas> whiteboards;
    
    public Server() {
        whiteboards = new ArrayList<Canvas>();
    }
    
    public List<Integer> getWhiteBoardIds() {
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
    public Canvas getCanvasByID(int id) {
        return whiteboards.get(id);
    }
    
    /**
     * Create a new canvas and return its ID number.
     * @param width The width of the canvas to create, in pixels
     * @param height The height of the canvas to create, in pixels
     * @return The ID number of the newly created canvas
     */
    public synchronized int createNewWhiteBoard(int width, int height) {
        Canvas c = new Canvas(width, height);
        whiteboards.add(c);
        return whiteboards.size() - 1;
    }
    
    /**
     * Create a new canvas with dimensions 800 (width) by 600 (height) and return its ID number.
     * @param width The width of the canvas to create, in pixels
     * @param height The height of the canvas to create, in pixels
     * @return The ID number of the newly created canvas
     */
    public synchronized int createNewWhiteBoard() {
        return createNewWhiteBoard(800, 600);
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
