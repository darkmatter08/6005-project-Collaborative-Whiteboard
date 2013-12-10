package server;

import java.io.PrintWriter;

public class ClientConnection {
    private PrintWriter out;
    private String username;
    
    public String getUsername() {
        return username;
    }
    
    public PrintWriter getPrintWriter() {
        return out;
    }
}
