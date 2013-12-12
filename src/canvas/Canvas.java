package canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JPanel;

import shared.WhiteboardAction;

/**
 * Canvas represents a drawing surface that allows the user to draw
 * on it freehand, with the mouse.
 */
public class Canvas extends JPanel {
    // image where the user's drawing is stored
    private Whiteboard board;
    private int boardId;
    public boolean readyToPaint = false;
    private Color currentColor = Color.BLACK;
    public final static int DEFAULT_STROKE_LENGTH = 25;
    public final static int DEFAULT_ERASE_LENGTH = 50;
    private CanvasConnectionHandler connectionHandler;
    int thickness = DEFAULT_STROKE_LENGTH;
    Stroke currentStroke = new BasicStroke(DEFAULT_STROKE_LENGTH);
    private ClientWhiteboardGUI gui;
    
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
    
    /**
     * A reference to the whiteboard this canvas contains
     * @return A Whiteboard
     */
    public synchronized Whiteboard getWhiteboard() {
    	return board;
    }
    
    public Canvas(int boardId, ClientWhiteboardGUI gui) throws Exception {
    	this(800, 600);
    	this.boardId = boardId;
    	this.gui = gui;
    	initConnection();
    }
    
    /**
     * Initialize the connection between this canvas and the server.
     */
    public void initConnection() {
    	//Do a quick draw so board is Initialized.
    	//drawLineSegment(0, 0, 0, 0);
    	connectionHandler = new CanvasConnectionHandler(boardId, gui, this);
    	try {
    	connectionHandler.init();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    public Canvas(int width, int height, Whiteboard board) throws UnknownHostException, IOException {
        this(width, height);
        this.board = board;
    }
    
    /**
     * The color of the pen that will draw on the whiteboard if the user
     * drags their mouse at this moment.
     * @return The color being drawn with
     */
    public Color getPenColor() {
    	return currentColor;
    }
    
    /**
     * Change the color that the user is drawing with
     * @param color The color to be drawn with
     */
    public void setPenColor(Color color) {
    	currentColor = color;
    }
    
    /**
     * Set the thickness of the user's stroke from now on (until the next change,
     * called by this method).
     * @param thickness 
     */
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
        connectionHandler.listenForServerMessages();
		connectionHandler.askForHistory();
    }
    
    /**
     * Retrieve a reference to the CanvasConnectionHandler, containing the network connection
     * between this canvas and the server.
     * @return A CanvasConnectionHandler representing the connection to the server
     */
    public CanvasConnectionHandler getConnectionHandler() {
    	return connectionHandler;
    }
    
    /**
     * Method indicating whether this canvas is ready to be painted on
     * @return true if this canvas's board is ready to be painted on, false otherwise. 
     */
    public boolean readyToPaint() {
    	return readyToPaint;
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
     * Uses the current color and stroke with provided by getPenColor() and basicStroke
     */
    private synchronized void drawLineSegment(int x1, int y1, int x2, int y2) {
        WhiteboardAction action = new WhiteboardAction(x1, y1, x2, y2, currentColor.getRGB(), thickness);
        board.applyAction(action);
        connectionHandler.sendAction(action);
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
