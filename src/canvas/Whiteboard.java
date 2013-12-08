package canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.Serializable;

import shared.WhiteboardAction;

public class Whiteboard implements Serializable {
    public Image drawingBuffer;
    private int width;
    private int height;
    
    public Whiteboard(Image drawingBuffer, int width, int height) {
        this.drawingBuffer = drawingBuffer;
        this.width = width;
        this.height = height;
    }
    
    /*
     * Draw a line between two points (x1, y1) and (x2, y2), specified in
     * pixels relative to the upper-left corner of the drawing buffer.
     */
    public void applyAction(WhiteboardAction action) {
        Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
        g.setColor(action.color);
        g.setStroke(action.stroke);
        g.drawLine(action.x1, action.y1, action.x2, action.y2);
    }
    
    public void fillWithWhite() {
        final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
    
        g.setColor(Color.WHITE);
        g.fillRect(0,  0,  getWidth(), getHeight());
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
}
