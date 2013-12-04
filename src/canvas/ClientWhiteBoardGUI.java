package canvas;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ClientWhiteBoardGUI extends JPanel {
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	private GroupLayout layout;
	private JButton blackButton = new JButton();
	private JButton redButton = new JButton();
	private JButton blueButton = new JButton();
	private JButton greenButton = new JButton();
	private JButton eraseButton = new JButton();
	private Canvas canvas = new Canvas(800, 800);

	
	public void init() {
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
		blackButton.setText("Black Pen");
		redButton.setText("Red Pen");
		blueButton.setText("Blue Pen");
		greenButton.setText("Green Pen");
		eraseButton.setText("Eraser");
		blackButton.setBackground(Color.BLACK);
		redButton.setBackground(Color.RED);
		blueButton.setBackground(Color.BLUE);
		greenButton.setBackground(Color.GREEN);
		eraseButton.setBackground(Color.WHITE);
		buttons.add(blackButton);
		buttons.add(redButton);
		buttons.add(blueButton);
		buttons.add(greenButton);
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
