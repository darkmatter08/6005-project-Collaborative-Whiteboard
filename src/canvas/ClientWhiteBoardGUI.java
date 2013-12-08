package canvas;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ClientWhiteBoardGUI extends JPanel {
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	private GroupLayout layout;
	private JButton colorChooser = new JButton();
	private JButton blackButton = new JButton();
	private JButton redButton = new JButton();
	private JButton blueButton = new JButton();
	private JButton greenButton = new JButton();
	private JButton eraseButton = new JButton();
	private Canvas canvas;

	
	public void init() {
		 try {
             canvas = new Canvas(800, 600);
         } catch (UnknownHostException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } catch (IOException e) {
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
		final JPanel parentGui = this;
		colorChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					canvas.setPenColor(JColorChooser.showDialog(parentGui, "Choose Pen Color", canvas.getPenColor()));
					canvas.setPenThickness(1);
				}
		});
		eraseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas.setPenColor(Color.WHITE);
				canvas.setPenThickness(15);
			}
		});
		buttons.add(colorChooser);
		buttons.add(eraseButton);
		for (JButton button : buttons) {
			button.setOpaque(true);
		}
	}
	
	

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame window = new JFrame(
						"Freehand Canvas");
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				ClientWhiteBoardGUI clientGUI = new ClientWhiteBoardGUI();
				window.add(clientGUI);
				clientGUI.init();
				window.pack();
				window.setVisible(true);
			}
		});
	}
	
	
}
