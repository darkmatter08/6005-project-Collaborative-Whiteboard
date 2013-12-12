package canvas;

import java.awt.Color;
import java.awt.event.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * The graphical user interface containing one canvas.
 */
public class ClientWhiteboardGUI extends JPanel {
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	private JLabel connectedUsers = new JLabel("Connected Users: ");
	private GroupLayout layout;
	private JButton colorChooser = new JButton();
	private JButton eraseButton = new JButton();
	private Canvas canvas;
	private int boardId;
	private String username;

	public ClientWhiteboardGUI(int boardId, String username) {
		super();
		this.boardId = boardId;
		this.username = username;
	}

	/**
	 * Initialize this graphical user interface and the canvas data structure that goes along with it.
	 */
	public void init() {
		try {
			canvas = new Canvas(boardId, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		layout = new GroupLayout(this);
		this.setLayout(layout);
		initButtons();
		setupLayout();
	}

	/**
	 * Set up the window layout of this canvas.
	 */
	public void setupLayout() {
		Group sequentialButtonGroup = layout.createSequentialGroup();
		Group parallelButtonGroup = layout.createParallelGroup();
		for (JButton button : buttons) {
			sequentialButtonGroup = sequentialButtonGroup.addComponent(button);
			parallelButtonGroup = parallelButtonGroup.addComponent(button);
		}
		// TODO Maybe rename sequentialButtonGroup and parallelButtonGroup
		sequentialButtonGroup.addComponent(connectedUsers);
		parallelButtonGroup.addComponent(connectedUsers);
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(sequentialButtonGroup).addComponent(canvas));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(parallelButtonGroup).addComponent(canvas));
	}

	/**
	 * Initialize all the buttons that will be displayed in this window.
	 */
	public void initButtons() {
		colorChooser.setText("Choose color");
		eraseButton.setText("Eraser");
		colorChooser.setBackground(Color.BLACK);
		eraseButton.setBackground(Color.WHITE);
		colorChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newDialogColor();
			}
		});
		eraseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setEraserColor();
			}
		});
		buttons.add(colorChooser);
		buttons.add(eraseButton);
		for (JButton button : buttons) {
			button.setOpaque(true);
		}
	}

	/**
	 * Launch dialag window for picking new pen color.
	 * 
	 * If the "pen" was previously an eraser, the pen will be set back to normal pen properties
	 * with a new user-chosen color and normal stroke thickness.
	 */
	public void newDialogColor() {
		Color newColor = JColorChooser.showDialog(this, "Choose Pen Color",
				canvas.getPenColor());
		canvas.setPenColor(newColor);
		colorChooser.setBackground(newColor);
		canvas.setPenThickness(Canvas.DEFAULT_STROKE_LENGTH);
	}

	/**
	 * Set the current "pen" to be in erase-mode, meaning the color is the same as the background
	 * color of the canvas (white), and set the pen thickness to be the default erase thickness.
	 */
	public void setEraserColor() {
		canvas.setPenColor(Color.WHITE);
		colorChooser.setBackground(Color.WHITE);
		canvas.setPenThickness(Canvas.DEFAULT_ERASE_LENGTH);
	}

	/**
	 * Open a new ClientWhiteboardGUI on the screen which connects a canavs to the board with ID
	 * boardId and alerts the server to this user connecting with name {@code username}.
	 * @param boardId The board number to conect to on the server, for both sending actions that
	 *     are made locally and receiving actions from other users connected to the server. 
	 * @param username The username that this client will attempt to connect as on the server.
	 */
	public static void openEditor(final int boardId, final String username) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame window = new JFrame("Freehand Canvas");
				window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				final ClientWhiteboardGUI clientGUI = new ClientWhiteboardGUI(
						boardId, username);
				window.add(clientGUI);
				clientGUI.init();
				window.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent we) {
						clientGUI.getCanvas().getConnectionHandler()
								.closeConnection();
					}
				});
				window.pack();
				window.setVisible(true);
			}
		});
	}

	/**
	 * Set the username of the user in this gui window.
	 * @param username The username with which to try to connect to the server.
	 */
	public void setUserName(String username) {
		this.username = username;
	}

	/**
	 * A reference to the canvas that this GUI is displaying.
	 * @return The canvas that this GUI is displaying too and receiving actions from.
	 */
	public Canvas getCanvas() {
		return canvas;
	}

	/**
	 * Run an independent GUI with a test username in order to see this GUI properly function
	 * independent of the server.
	 * @param args
	 */
	public static void main(String[] args) {
		openEditor(0, "testUser");
	}

	/**
	 * @return The JLabel object showing the list of all connected users on this whiteboard.
	 */
	public JLabel getConnectedUsersLabel() {
		return connectedUsers;
	}

	/**
	 * @return the username of this user
	 */
	public String getUsername() {
		return username;
	}
}
