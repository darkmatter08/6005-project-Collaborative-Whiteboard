package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import server.ServerRunner;
import shared.*;

public class TestUtility {
    
    final private static int port = ConnectionDetails.CLIENT_PICKER_GUI_PORT;
    final private static int masterPort = ConnectionDetails.WHITEBOARD_GUI_PORT;
    
    public static void startServer() throws IOException {
        new Thread(new ServerRunner()).start();
    }
    
    public static Socket connectToWhiteboardServer() throws IOException {
        return connectHelper(masterPort);
    }
    
    public static Socket connect() throws IOException {
        return connectHelper(port);
    }
    
    public static Socket connectHelper(int port) throws IOException {
        Socket ret = null;
        final int MAX_ATTEMPTS = 50;
        int attempts = 0;
        do {
          try {
            ret = new Socket(ConnectionDetails.SERVER_ADDRESS, port);
          } catch (ConnectException ce) {
            try {
              if (++attempts > MAX_ATTEMPTS)
                throw new IOException("Exceeded max connection attempts", ce);
              Thread.sleep(300);
            } catch (InterruptedException ie) {
              throw new IOException("Unexpected InterruptedException", ie);
            }
          }
        } while (ret == null);
        ret.setSoTimeout(3000);
        return ret;
    }
    
  public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
      startServer();
      Socket socket = connect();//new Socket(ConnectionDetails.SERVER_ADDRESS, port);
      Thread.sleep(1500);
      BufferedReader in; 
      PrintWriter out;
      
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);
      
      out.println("createNewWhiteboard");
      System.out.println("wrote createnewwhiteboard");
      System.out.println(in.readLine());
  }
}
