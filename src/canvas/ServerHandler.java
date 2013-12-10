package canvas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

import shared.ProtocolUtility;

public class ServerHandler {
	private final String getIdMessage = "getWhiteboardIds";
	private final String createNewWhiteboardMessage = "createNewWhiteboard";
	private final String getWhiteboardById = "getWhiteboardById";
	private WhiteBoardTableModel tableModel;
	private WhiteboardPickerClient parentFrame;
	private Socket mySocket;
//	private PrintWriter out;
//	private ObjectInputStream in;
	private BufferedReader in = null; 
	private PrintWriter out = null;
	
	public ServerHandler(WhiteboardPickerClient parentFrame, WhiteBoardTableModel tableModel) {
		this.parentFrame = parentFrame;
		this.tableModel = tableModel;
	}

	public synchronized void init() {
		try {
			mySocket = new Socket("127.0.0.1", shared.Ports.CONNECTION_PORT);
			this.out = new PrintWriter(mySocket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			watchForNewWhiteboards();
			askForWhiteboardIds();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void askForWhiteboardIds() {
		out.println(getIdMessage);
	}

	public synchronized void createNewWhiteBoard() {
		out.println(createNewWhiteboardMessage);
	}

	public void watchForNewWhiteboards() {
		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				while (true) {
					try {
					    List<String> boardIdsStrings = ProtocolUtility.deserialize(in.readLine());
					    List<Integer> boardIds = ProtocolUtility.convertListTypeToInt(boardIdsStrings);
						tableModel.removeAllRows();
						for (Integer boardId : boardIds) {
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

	public void openWhiteboard(int boardId) {
		final int boardToOpen = boardId;
		new Thread() {
			public void run() {
				out.println(getWhiteboardById + " " + boardToOpen);
				ClientWhiteboardGUI.openEditor(boardToOpen);
			}
		}.start();
	}
}
