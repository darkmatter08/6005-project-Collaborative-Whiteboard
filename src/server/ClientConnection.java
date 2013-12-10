package server;

import java.io.PrintWriter;

public class ClientConnection {
    private PrintWriter out;
    private String username;
    
    public ClientConnection(PrintWriter out, String username) {
        this.out = out;
        this.username = username;
    }
    
    public String getUsername() {
        return username;
    }
    
    public PrintWriter getPrintWriter() {
        return out;
    }
}
