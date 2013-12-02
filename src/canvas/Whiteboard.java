package canvas;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public class Whiteboard {
    private Image drawingBuffer;
    
    public Whiteboard(Image drawingBuffer) {
        this.drawingBuffer = drawingBuffer;
    }
}
