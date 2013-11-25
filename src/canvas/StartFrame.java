package canvas;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class StartFrame extends JFrame {
	private final DefaultTableModel whiteboardTabelModel = new DefaultTableModel();
	private final JTable whiteboardTable = new JTable(whiteboardTabelModel);
	private final JButton newWhiteboard = new JButton();
	private final JLabel headerText = new JLabel();
	private final static int MIN_WIDTH = 400;
	private final static int MIN_HEIGHT = 400;
	
	public void init() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		headerText.setText("Click a whiteboard ID to begin");
		this.add(headerText, BorderLayout.NORTH);
		this.add(whiteboardTable, BorderLayout.CENTER);
		newWhiteboard.setText("Create new Whiteboard");
		this.add(newWhiteboard, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		StartFrame myFrame = new StartFrame();
		myFrame.init();
	}

}
