package server;

import java.util.*;
import canvas.*;

public class Server {
    private ArrayList<Canvas> whiteboards;
    
    public Server() {
        whiteboards = new ArrayList<Canvas>();
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
    public synchronized int newCanvasID(int width, int height) {
        Canvas c = new Canvas(width, height);
        whiteboards.add(c);
        return whiteboards.size() - 1;
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
