package shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.List;

public abstract class ActionReceiver extends Thread {
    private BufferedReader in;
    
    public ActionReceiver(BufferedReader in) {
        this.in = in;
    }
    
    public void run() {
        try {
            System.out.println("receive server actions");
            receiveActions();
        } catch (IOException e) {
            System.out.println("caught ioexception in receiver.run");
            e.printStackTrace(); // but don't terminate serve()
        }
    }
    
    private void receiveActions() throws IOException {
        System.out.println("entered receiver.receiveServerActions()");
        
        try {
            System.out.println("entered receiver.receiveServerActions() try block");
            for (String actionStr = in.readLine(); actionStr != null; actionStr = in.readLine()) {
                System.out.println("received actionStr: " + actionStr);
                WhiteboardAction action = WhiteboardAction.parse(actionStr);
                receiveAction(action);
            }
        } catch (Exception e){
            System.out.println("caught exception in receiver.receiveserveractions()");
            e.printStackTrace();
        } finally {
            System.out.println("socket close attempt in receiver.receiveServerAction()");
            //client.socket.close();
        }
    }
    
    public abstract void receiveAction(WhiteboardAction action);
}