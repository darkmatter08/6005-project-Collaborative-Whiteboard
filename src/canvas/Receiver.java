package canvas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.List;

import shared.WhiteboardAction;

class Receiver extends Thread {
    private Canvas canvas;
    private BufferedReader in;
    
    public Receiver(Canvas canvas, BufferedReader in) {
        this.canvas = canvas;
        this.in = in;
    }
    
    public void run() {
        try {
            System.out.println("receive server actions");
            receiveServerActions();
        } catch (IOException e) {
            System.out.println("caught ioexception in receiver.run");
            e.printStackTrace(); // but don't terminate serve()
        }
    }
    
    private void receiveServerActions() throws IOException {
        System.out.println("entered receiver.receiveServerActions()");
        
        try {
            System.out.println("entered receiver.receiveServerActions() try block");
            for (String actionStr = in.readLine(); actionStr != null; actionStr = in.readLine()) {
                WhiteboardAction action = WhiteboardAction.parse(actionStr);
                canvas.getWhiteboard().applyAction(action);
            }
        } catch (Exception e){
            System.out.println("caught exception in receiver.receiveserveractions()");
            e.printStackTrace();
        } finally {
            System.out.println("socket close attempt in receiver.receiveServerAction()");
            //client.socket.close();
        }
    }
}