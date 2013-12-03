package canvas;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.Serializable;

public class Whiteboard implements Serializable{
    private Image drawingBuffer;
    
    public Whiteboard(Image drawingBuffer) {
        this.drawingBuffer = drawingBuffer;
    }
    
    public Whiteboard() {
        
    }

    public void fillWithWhite() {
        // TODO Auto-generated method stub
        
    }
}
