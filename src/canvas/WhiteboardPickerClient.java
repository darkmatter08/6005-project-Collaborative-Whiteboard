package canvas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * GUI for selecting a whiteboard to connect to.
 * @author blake
 *
 */
public class WhiteboardPickerClient extends JFrame {
	private String userName;
	private final WhiteBoardTableModel whiteBoardTableModel = new WhiteBoardTableModel();
	private final JTable whiteBoardTable = new JTable(whiteBoardTableModel);
	private final JButton newWhiteBoard = new JButton();
	private final JLabel infoText = new JLabel();
	private PickerClientConnectionHandler connectionHandler;
	private final static int MIN_WIDTH = 400;
	private final static int MIN_HEIGHT = 400;
	
	public WhiteboardPickerClient(String userName) {
		super();
		this.userName = userName;
	}
	
	/**
	 * Initialize connection between WhiteboardPickerClient and Server.
	 */
	public void init() {
		connectionHandler = new PickerClientConnectionHandler(this, whiteBoardTableModel);
		whiteBoardTableModel.addColumn("boardId");
		connectionHandler.init();
		this.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		initHeader();
		initWhiteBoardTable();
		initNewWhiteBoardButton();
		this.add(infoText, BorderLayout.NORTH);
		this.add(whiteBoardTable, BorderLayout.CENTER);
		this.add(newWhiteBoard, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}
	
	/**
	 * @return The JTable element showing the available whiteboards.
	 */
	public JTable getWhiteboardTable() {
		return whiteBoardTable;
	}

	/**
	 * Open main window.
	 */
	public void initMainFrame() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
	}

	/**
	 * Display welcome message.
	 */
	public void initHeader() {
		infoText.setText("Hi " + userName + ", pick a whiteboard to begin");
	}

	/**
	 * Set the table of whiteboards available.
	 */
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

	/**
	 * Display a buton for creating a new whiteboard on the server.
	 */
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
				connectionHandler.createNewWhiteBoard();
			}
		});
	}

	public void openEditor(int boardId) {
		connectionHandler.openWhiteboard(boardId, getUsername());
	}
	
	public PickerClientConnectionHandler getServerHandler() {
		return connectionHandler;
	}
	
	public String getUsername() {
		return userName;
	}

	public static void main(String[] args) {
		JTextField userName = new JTextField();
		final JComponent[] inputs = new JComponent[] {
				new JLabel("User Name:"),
				userName,
		};
		JOptionPane.showMessageDialog(null, inputs, "Enter your username!", JOptionPane.PLAIN_MESSAGE);
		String acquiredUserName = userName.getText().replace(' ', '_');
		if (acquiredUserName.equals("")) {
		    acquiredUserName = "defaultUser";
		}
		WhiteboardPickerClient myFrame = new WhiteboardPickerClient(acquiredUserName);
		myFrame.init();
	}

}
