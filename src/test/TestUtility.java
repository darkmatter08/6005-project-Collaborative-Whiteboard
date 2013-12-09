package test;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import server.MasterServerStarter;

public class TestUtility {
    
    final private static int port = shared.Ports.CONNECTION_PORT;
    
    public static void startServer() throws IOException {
        new MasterServerStarter(port).serve();
    }
    
    public static Socket connect() throws IOException {
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
}
