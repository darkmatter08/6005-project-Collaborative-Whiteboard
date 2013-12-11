package server;

import java.io.PrintWriter;

/**
 * ClientConnection encapsulates a client's PrintWriter and his username. 
 * @author jains
 */
public class ClientConnection {
    
    private PrintWriter out;
    private String username;
    
    /**
     * ClientConnection constructor
     * @param out PrintWriter for this user
     * @param username String representing the user's username (no spaces allowed)
     */
    public ClientConnection(PrintWriter out, String username) {
        this.out = out;
        this.username = username;
    }
    
    /**
     * @return String username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * @return PrintWriter of this user
     */
    public PrintWriter getPrintWriter() {
        return out;
    }
}
