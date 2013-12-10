package server.slave;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import shared.WhiteboardAction;
import shared.ActionReceiver;

class Receiver extends ActionReceiver {
    private SlaveServer server;
    private BufferedReader in;
    
    public Receiver(SlaveServer server, BufferedReader in) throws IOException {
        super(in);
        this.server = server;
        System.out.println("constructed Receiver");
    }
    
    @Override
    public void receiveAction(WhiteboardAction action) {
        server.whiteboard.getHistory().add(action);
        System.out.println("received action " + action);
    }
}