package canvas;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import javax.swing.JTable;

public class ServerHandler {
	private final String getIdMessage = "getWhiteboardIds";
	private final String createNewWhiteboardMessage = "createNewWhiteboard";
	private WhiteBoardTableModel tableModel;
	private StartFrame parentFrame;
	private Socket mySocket;
	private PrintWriter out;
	private ObjectInputStream in;

	public ServerHandler(StartFrame parentFrame, WhiteBoardTableModel tableModel) {
		this.parentFrame = parentFrame;
		this.tableModel = tableModel;
	}

	public synchronized void init() {
		try {
			mySocket = new Socket("127.0.0.1", shared.Ports.CONNECTION_PORT);
			out = new PrintWriter(mySocket.getOutputStream(), true);
			System.out.println("2");
			in = new ObjectInputStream(mySocket.getInputStream());
			this.watchForNewWhiteboards();
			System.out.println("3");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized List<Integer> getWhiteBoardIds() {
		out.println(getIdMessage);
		List<Integer> boards = null;
		try {
			boards = (List<Integer>) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return boards;
	}

	public synchronized void createNewWhiteBoard() {
		out.println(createNewWhiteboardMessage);
	}

	public void watchForNewWhiteboards() {
		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				while (true) {
					List<Integer> boardIds;
					System.out.println("got here!!!");
					try {
						//Don't do anything if there are no bytes to read.
						System.out.println("before in.readObject");
						boardIds = (List<Integer>) in.readObject();
						System.out.println(boardIds);
						tableModel.removeAllRows();
						for (int boardId : boardIds) {
							tableModel.addRow(new Object[] { boardId });
						}
						parentFrame.pack();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/**
	 * Simply locks on the object. Used for testing purposes.
	 */
	public synchronized void hold() {

	}

}
