package canvas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import server.Server;

public class StartFrame extends JFrame {
	private final WhiteBoardTableModel whiteBoardTableModel = new WhiteBoardTableModel();
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
		initHeader();
		initWhiteBoardTable();
		initNewWhiteBoardButton();
		this.add(headerText, BorderLayout.NORTH);
		this.add(whiteBoardTable, BorderLayout.CENTER);
		this.add(newWhiteBoard, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}
	
	public void initMainFrame() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
	}
	
	public void initHeader() {
		headerText.setText("Click a whiteboard ID to begin");
	}
	
	public void initWhiteBoardTable() {
		List<Integer> boardIds = server.getWhiteBoardIds();
		whiteBoardTableModel.addColumn("boardId");
		whiteBoardTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int boardId : boardIds) {
			whiteBoardTableModel.addRow(new Object[] {boardId});
		}
		// TODO Add an action listener for the table.
		
		whiteBoardTable.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent e) {
			    if (e.getClickCount() == 2) {
			      JTable target = (JTable)e.getSource();
			      int row = target.getSelectedRow();
			      int column = target.getSelectedColumn();
			      openEditor((int)(target.getValueAt(row, column)));
			    }
			  }
			});
		
	}
	
	public void initNewWhiteBoardButton() {
		newWhiteBoard.setText("Create new Whiteboard");
		newWhiteBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addWhiteBoard();
			}
		});
	}
	
	public void addWhiteBoard() {
		int id = server.createNewWhiteBoard();
		whiteBoardTableModel.addRow(new Object[] {id});
		this.pack();
	}
	
	public void openEditor(int boardId) {
		System.out.println("TODO: Open editor " + boardId);
		Canvas myCanvas = new Canvas(500 /* canvas width */, 500 /* canvas height */);
		myCanvas.startCanvas();
	}
	
	public static void main(String[] args) {
		Server myServer = new Server();
		StartFrame myFrame = new StartFrame(myServer);
		myFrame.init();
	}

}
