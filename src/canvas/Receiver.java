package canvas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.List;

import shared.ActionReceiver;
import shared.WhiteboardAction;

class Receiver extends ActionReceiver {
    private Canvas canvas;
    private BufferedReader in;
    
    public Receiver(Canvas canvas, BufferedReader in) {
        super(in);
        this.canvas = canvas;
    }

    @Override
    public void receiveAction(WhiteboardAction action) {
        canvas.getWhiteboard().applyAction(action);
    }
}