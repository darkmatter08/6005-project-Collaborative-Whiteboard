package canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public class Whiteboard {
    private Image drawingBuffer;
    
    public Whiteboard(Image drawingBuffer) {
        this.drawingBuffer = drawingBuffer;
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
}
