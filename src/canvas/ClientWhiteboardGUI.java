package canvas;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import shared.Ports;

public class ClientWhiteboardGUI extends JPanel {
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	private GroupLayout layout;
	private JButton colorChooser = new JButton();
	private JButton eraseButton = new JButton();
	private Canvas canvas;
	private int boardId;
	
	public ClientWhiteboardGUI(int boardId) {
		super();
		this.boardId = boardId;
	}
	
	public void init() {
		 try {
             Socket socket = new Socket(Ports.ADDRESS, shared.Ports.MASTER_PORT);
             System.out.println("client slave connecting on port " + shared.Ports.MASTER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             canvas = new Canvas(boardId, out);
             Receiver receiver = new Receiver(canvas, in);
             receiver.start();
         } catch (Exception e) {
             // TODO Auto-generated catch block
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
		layout.setHorizontalGroup(layout.createParallelGroup().addGroup(sequentialButtonGroup).addComponent(canvas));
		layout.setVerticalGroup(layout.createSequentialGroup().addGroup(parallelButtonGroup).addComponent(canvas));
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
		Color newColor = JColorChooser.showDialog(this, "Choose Pen Color", canvas.getPenColor());
		canvas.setPenColor(newColor);
		colorChooser.setBackground(newColor);
		canvas.setPenThickness(1);	
	}
	
	public void setEraserColor() {
		canvas.setPenColor(Color.WHITE);
		colorChooser.setBackground(Color.WHITE);
		canvas.setPenThickness(15);
	}
	
	public static void openEditor(int boardId) {
		final int boardToOpen = boardId;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame window = new JFrame(
						"Freehand Canvas");
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				ClientWhiteboardGUI clientGUI = new ClientWhiteboardGUI(boardToOpen);
				window.add(clientGUI);
				clientGUI.init();
				window.pack();
				window.setVisible(true);
			}
		});
	}

	public static void main(String[] args) {
		openEditor(0);
	}
	
	
}
