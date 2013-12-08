package canvas;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.JButton;
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
    private Color currentColor = Color.BLACK;
    private final String DRAW_MODE = "Draw";
    private final String ERASE_MODE = "Erase";
    private final BasicStroke DRAW_MODE_STROKE = new BasicStroke(1);
    private final BasicStroke ERASE_MODE_STROKE = new BasicStroke(15);
    private Stroke currentStroke = DRAW_MODE_STROKE;
    private String mode = DRAW_MODE;
    
    private ArrayList<WhiteboardAction> currentActions;
    
    // IO
    private BufferedReader in = null;
    private PrintWriter out = null;
    private ObjectOutputStream objOut = null;
    private ObjectInputStream objIn = null;
    
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
        // note: we can't call makeDrawingBuffer here, because it only
        // works *after* this canvas has been added to a window.  Have to
        // wait until paintComponent() is first called.
        currentActions = new ArrayList<WhiteboardAction>();
        Socket socket = new Socket("127.0.0.1", 8888);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.objOut = new ObjectOutputStream(socket.getOutputStream());
        this.objIn = new ObjectInputStream(socket.getInputStream());
    }
    
    public Canvas(int width, int height, Whiteboard board) throws UnknownHostException, IOException {
        this(width, height);
        this.board = board;
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
    private void drawLineSegment(int x1, int y1, int x2, int y2) {
        WhiteboardAction action = new WhiteboardAction(x1, y1, x2, y2, currentColor, currentStroke);
        board.applyAction(action);
        // IMPORTANT!  every time we draw on the internal drawing buffer, we
        // have to notify Swing to repaint this component on the screen.
        this.repaint();
        
        synchronized(currentActions) {
            currentActions.add(action);
        }
        
    }
    
    private void sendCurrentActionsAndUpdate() throws ClassNotFoundException, IOException {
        synchronized (currentActions) {
            ArrayList<WhiteboardAction> copyCurrentActions = (ArrayList)currentActions.clone();
            currentActions.clear();
        }
        int boardId = 0;
        out.println("drawLine" + " " + boardId); //TODO add in board id
        Whiteboard newBoard = (Whiteboard) objIn.readObject();
        //objOut.writeObject(object);
        // Whiteboard newBoard = "send copyCurrentActions to server"()
        for (WhiteboardAction action : currentActions) {
            newBoard.applyAction(action);
        }
        
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
    
    /**
     * 
     * @param args
     */
    private JButton createEraseToggleButton() {
    	final JButton toggle = new JButton();
    	toggle.setText(ERASE_MODE);
    	toggle.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    				toggleErase(toggle);
    		}
    	});
    	return toggle;
    }
    
    private void toggleErase(JButton button) {
    	// we're currently in draw mode so set to erase mode.
    	if (mode == DRAW_MODE) {
    		currentColor = Color.WHITE;
    		currentStroke = ERASE_MODE_STROKE;
    		mode = ERASE_MODE;
    		button.setText(DRAW_MODE);
    	}
    	// we're currently in erase mode so set to draw mode.
    	else if (mode == ERASE_MODE) {
    		currentColor = Color.BLACK;
    		currentStroke = DRAW_MODE_STROKE;
    		mode = DRAW_MODE;
    		button.setText(ERASE_MODE);
    	}
    }
 
    
    /*
     * Main program. Make a window containing a Canvas.
     */
    public static void main(String[] args) {
        // set up the UI (on the event-handling thread)
    	startCanvas();
    }
    
    /*
     * 
     */
    public static void startCanvas() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame window = new JFrame("Freehand Canvas");
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setLayout(new BorderLayout());
                Canvas canvas = null;
                try {
                    canvas = new Canvas(800, 600);
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                JButton toggleButton = canvas.createEraseToggleButton();
                window.add(toggleButton, BorderLayout.NORTH);
                window.pack();
                window.add(canvas, BorderLayout.CENTER);
                window.pack();
                window.setVisible(true);
            }
        });
    }
}
