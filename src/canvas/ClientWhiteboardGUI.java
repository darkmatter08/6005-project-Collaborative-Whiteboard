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

	public void newDialogColor() {
		Color newColor = JColorChooser.showDialog(this, "Choose Pen Color",
				canvas.getPenColor());
		canvas.setPenColor(newColor);
		colorChooser.setBackground(newColor);
		canvas.setPenThickness(Canvas.DEFAULT_STROKE_LENGTH);
	}

	public void setEraserColor() {
		canvas.setPenColor(Color.WHITE);
		colorChooser.setBackground(Color.WHITE);
		canvas.setPenThickness(Canvas.DEFAULT_ERASE_LENGTH);
	}

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

	public void setUserName(String username) {
		this.username = username;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public static void main(String[] args) {
		openEditor(0, "testUser");
	}

	public JLabel getConnectedUsersLabel() {
		return connectedUsers;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
}
