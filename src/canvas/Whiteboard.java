package canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.Serializable;

import shared.WhiteboardAction;

/**
 * Represents the image data on a whiteboard.
 */
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
    public synchronized void applyAction(WhiteboardAction action) {
        Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
        g.setColor(action.color);
        g.setStroke(action.stroke);
        g.drawLine(action.x1, action.y1, action.x2, action.y2);
    }
    
    /**
     * Make the entire image white.
     */
    public synchronized void fillWithWhite() {
        final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
    
        g.setColor(Color.WHITE);
        g.fillRect(0,  0,  getWidth(), getHeight());
    }
    
    /**
     * @return The width of this image, in pixels
     */
    public synchronized int getWidth() {
        return width;
    }
    
    /**
     * @return The height of this image, in pixels
     */
    public synchronized int getHeight() {
        return height;
    }
}
