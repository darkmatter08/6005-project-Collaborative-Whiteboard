package canvas;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import shared.WhiteboardAction;

/**
 * Canvas represents a drawing surface that allows the user to draw
 * on it freehand, with the mouse.
 */
public class Canvas extends JPanel {
    // image where the user's drawing is stored
    private Whiteboard board;
    private int boardId;
    private Color currentColor = Color.BLACK;
    public final static int DEFAULT_STROKE_LENGTH = 25;
    public final static int DEFAULT_ERASE_LENGTH = 50;
    private CanvasServerHandler server;
    int thickness = DEFAULT_STROKE_LENGTH;
    Stroke currentStroke = new BasicStroke(DEFAULT_STROKE_LENGTH);
    
    /**
     * Make a canvas.
     * @param width width in pixels
     * @param height height in pixels
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public Canvas(int width, int height) throws UnknownHostException, IOException {
        this.setPreferredSize(new Dimension(width, height));
        addDrawingController();
    }
    
    public synchronized Whiteboard getWhiteboard() {
    	return board;
    }
    
    public Canvas(int boardId) throws Exception {
    	this(800, 600);
    	this.boardId = boardId;
    	initConnection();
    }
    
    public void initConnection() {
    	//Do a quick draw so board is Initialized.
    	//drawLineSegment(0, 0, 0, 0);
    	server = new CanvasServerHandler(boardId, this);
    	try {
    	server.init();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    public Canvas(int width, int height, Whiteboard board) throws UnknownHostException, IOException {
        this(width, height);
        this.board = board;
    }
    
    public Color getPenColor() {
    	return currentColor;
    }
    
    public void setPenColor(Color color) {
    	currentColor = color;
    }
    
    public void setPenThickness(int thickness) {
    	this.thickness = thickness;
    	currentStroke = new BasicStroke(thickness);
    }
    
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        // If this is the first time paintComponent() is being called,
        // make our drawing buffer.
        if (board == null) {
            makeDrawingBuffer();
        }
        
        // Copy the drawing buffer to the screen.
        g.drawImage(board.drawingBuffer, 0, 0, null);
    }
    
    /*
     * Make the drawing buffer and draw some starting content for it.
     */
    private void makeDrawingBuffer() {
        Image drawingBuffer = createImage(getWidth(), getHeight());
        board = new Whiteboard(drawingBuffer, getWidth(), getHeight());
        fillWithWhite();
    }
    
    /*
     * Make the drawing buffer entirely white.
     */
    private void fillWithWhite() {
        board.fillWithWhite();
        
        // IMPORTANT!  every time we draw on the internal drawing buffer, we
        // have to notify Swing to repaint this component on the screen.
        this.repaint();
    }
    
    /*
     * Draw a line between two points (x1, y1) and (x2, y2), specified in
     * pixels relative to the upper-left corner of the drawing buffer.
     */
    private synchronized void drawLineSegment(int x1, int y1, int x2, int y2) {
        WhiteboardAction action = new WhiteboardAction(x1, y1, x2, y2, currentColor.getRGB(), thickness);
        board.applyAction(action);
        server.sendAction(action);
        // IMPORTANT!  every time we draw on the internal drawing buffer, we
        // have to notify Swing to repaint this component on the screen.
        this.repaint();
    }
    
    /*
     * Add the mouse listener that supports the user's freehand drawing.
     */
    private void addDrawingController() {
        DrawingController controller = new DrawingController();
        addMouseListener(controller);
        addMouseMotionListener(controller);
    }
    
    /*
     * DrawingController handles the user's freehand drawing.
     */
    private class DrawingController implements MouseListener, MouseMotionListener {
        // store the coordinates of the last mouse event, so we can
        // draw a line segment from that last point to the point of the next mouse event.
        private int lastX, lastY; 

        /*
         * When mouse button is pressed down, start drawing.
         */
        public void mousePressed(MouseEvent e) {
            lastX = e.getX();
            lastY = e.getY();
        }

        /*
         * When mouse moves while a button is pressed down,
         * draw a line segment.
         */
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            drawLineSegment(lastX, lastY, x, y);
            lastX = x;
            lastY = y;
        }

        // Ignore all these other mouse events.
        public void mouseMoved(MouseEvent e) { }
        public void mouseClicked(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }
    }
}
