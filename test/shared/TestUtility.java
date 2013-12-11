package shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import server.ServerRunner;
import shared.*;

public class TestUtility {
    
    final private static int port = ConnectionDetails.CLIENT_PICKER_GUI_PORT;
    final private static int masterPort = ConnectionDetails.WHITEBOARD_GUI_PORT;
    
    /**
     * Starts the server on a new thread
     * @throws IOException
     */
    public static void startServer() throws IOException {
        new Thread(new ServerRunner()).start();
    }
    
    /**
     * @return Socket connected to the WhiteboardServer
     * @throws IOException
     */
    public static Socket connectToWhiteboardServer() throws IOException {
        return connectHelper(masterPort);
    }
    
    /**
     * @return Socket connected to the PickerServer
     * @throws IOException
     */
    public static Socket connect() throws IOException {
        return connectHelper(port);
    }
    
    /**
     * General form connection - attempts to connect on ConnectionDetails.SERVER_ADDRESS:port
     * and returns the socket. 
     * @param port int port to connect to. 
     * @return Socket with connection
     * @throws IOException
     */
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
    
  public static void main(String[] args) throws UnknownHostException, IOException {
      startServer();
      System.out.println("after start server");
      Socket socket = connect();//new Socket(ConnectionDetails.SERVER_ADDRESS, port);
      System.out.println("after connect call");
      BufferedReader in, inBoard; 
      PrintWriter out, outBoard;
      
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);
      
      out.println("createNewWhiteboard");
      System.out.println("wrote createnewwhiteboard");
      System.out.println(in.readLine());
      
      Socket whiteboardSock = connectToWhiteboardServer();
      inBoard = new BufferedReader(new InputStreamReader(whiteboardSock.getInputStream()));
      outBoard = new PrintWriter(whiteboardSock.getOutputStream(), true);
      System.out.println("finished connection to the whiteboard server");
      
      /*
       * Protocol token ordering:
       * 0 - Request type 
       * 1 - Whiteboard ID
       * 2 - username (String) (only on shared.Messages.NEW_WHITEBOARD_CONNECTION)
       * 2-7 WhiteboardAction (only on shared.Messages.ADD_ACTION)
       *   2 - x1
       *   3 - y1
       *   4 - x2 
       *   5 - y2
       *   6 - colorRGB (int)
       *   7 - strokeWidth (int)
       */
      
      final String sp = " ";
      final String name = "ShawnJ";
      
      outBoard.println(Messages.NEW_WHITEBOARD_CONNECTION + sp + "0" + sp + name);
      // read lines of history
      try {
          for (String action = inBoard.readLine(); action != null; action = inBoard
                  .readLine()) {
              System.out.println(action);
          }
      } catch (SocketTimeoutException e) {
          System.out.println("Done getting history");
      }
      outBoard.println("Garbage");
  }
}
