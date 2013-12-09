package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import server.MasterServerStarter;

public class TestUtility {
    
    final private static int port = shared.Ports.CONNECTION_PORT;
    final private static int masterPort = shared.Ports.MASTER_PORT;
    
    public static void startServer() throws IOException {
        new Thread(new MasterServerStarter(port)).start();
    }
    
    public static Socket connectToSlaveServer() throws IOException {
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
            ret = new Socket("127.0.0.1", port);
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
      Socket socket = new Socket("127.0.0.1", shared.Ports.CONNECTION_PORT);
      Thread.sleep(1500);
      ObjectOutputStream objOut;
      ObjectInputStream objIn;
      BufferedReader in; 
      PrintWriter out; 
      
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//      objIn = new ObjectInputStream(socket.getInputStream());
//      objOut = new ObjectOutputStream(socket.getOutputStream());
      out = new PrintWriter(socket.getOutputStream(), true);
      
      out.write("createNewWhiteboard\n");
      //while(objIn.available() == 0){}
      System.out.println("wrote createnewwhiteboard");
      String s = in.readLine();
      System.out.println(s);
  }
}
