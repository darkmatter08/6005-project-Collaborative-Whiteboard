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
import javax.swing.SwingUtilities;



public class StartFrame extends JFrame {
	private final WhiteBoardTableModel whiteBoardTableModel = new WhiteBoardTableModel();
	private final JTable whiteBoardTable = new JTable(whiteBoardTableModel);
	private final JButton newWhiteBoard = new JButton();
	private final JLabel headerText = new JLabel();
	private ServerHandler server;
	private final static int MIN_WIDTH = 400;
	private final static int MIN_HEIGHT = 400;
	
	public void init() {
		server = new ServerHandler(this, whiteBoardTableModel);
		System.out.println("hey");
		server.init();
		System.out.println("there");
		this.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		initHeader();
		initWhiteBoardTable();
		initNewWhiteBoardButton();
		this.add(headerText, BorderLayout.NORTH);
		this.add(whiteBoardTable, BorderLayout.CENTER);
		this.add(newWhiteBoard, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
		System.out.println("sup");
	}
	
	public JTable getWhiteboardTable() {
		return whiteBoardTable;
	}

	public void initMainFrame() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
	}

	public void initHeader() {
		headerText.setText("Click a whiteboard ID to begin");
	}

	public void initWhiteBoardTable() {
		whiteBoardTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		whiteBoardTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					openEditor((Integer) (target.getValueAt(row, column)));
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
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				server.createNewWhiteBoard();
			}
		});
	}

	public void openEditor(int boardId) {
		System.out.println("TODO: Open editor " + boardId);
		Canvas.startCanvas();
	}
	
	public ServerHandler getServerHandler() {
		return server;
	}

	public static void main(String[] args) {
		StartFrame myFrame = new StartFrame();
		myFrame.init();
	}

}
