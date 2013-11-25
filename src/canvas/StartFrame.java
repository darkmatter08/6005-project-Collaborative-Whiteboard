package canvas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class StartFrame extends JFrame {
	private final DefaultTableModel whiteBoardTableModel = new DefaultTableModel();
	private final JTable whiteBoardTable = new JTable(whiteBoardTableModel);
	private final JButton newWhiteBoard = new JButton();
	private final JLabel headerText = new JLabel();
	private final WhiteBoardServer server;
	private final static int MIN_WIDTH = 400;
	private final static int MIN_HEIGHT = 400;
	
	public StartFrame(WhiteBoardServer server) {
		super();
		this.server = server;
	}
	
	public void init() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		headerText.setText("Click a whiteboard ID to begin");
		this.add(headerText, BorderLayout.NORTH);
		this.add(whiteBoardTable, BorderLayout.CENTER);
		newWhiteBoard.setText("Create new Whiteboard");
		this.add(newWhiteBoard, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}
	
	public void initWhiteboardTable() {
		List<Integer> boardIds = server.getWhiteBoardIds();
		whiteBoardTableModel.addColumn("boardId");
		whiteBoardTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int boardId : boardIds) {
			whiteBoardTableModel.addRow(new Object[] {boardId});
		}
		
	}
	
	public static void main(String[] args) {
		StartFrame myFrame = new StartFrame();
		myFrame.init();
	}

}
