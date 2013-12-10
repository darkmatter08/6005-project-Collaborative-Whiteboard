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

import shared.ConnectionDetails;

public class PickerClientConnectionHandler {
	private WhiteBoardTableModel tableModel;
	private WhiteboardPickerClient parentFrame;
	private Socket mySocket;
	private PrintWriter out;
	private BufferedReader in;

	public PickerClientConnectionHandler(WhiteboardPickerClient parentFrame, WhiteBoardTableModel tableModel) {
		this.parentFrame = parentFrame;
		this.tableModel = tableModel;
	}

	public synchronized void init() {
		try {
			mySocket = new Socket(ConnectionDetails.SERVER_ADDRESS, ConnectionDetails.CLIENT_PICKER_GUI_PORT);
			out = new PrintWriter(mySocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			watchForNewWhiteboards();
			askForWhiteboardIds();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void askForWhiteboardIds() {
		out.println(shared.Messages.ASK_FOR_WHITEBOARDS);
	}

	public synchronized void createNewWhiteBoard() {
		out.println(shared.Messages.CREATE_NEW_WHITEBOARD);
	}

	public void watchForNewWhiteboards() {
		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				while (true) {
					try {
						int boardIds = Integer.parseInt(in.readLine());
						tableModel.removeAllRows();
						for (int i = 0; i < boardIds; i++) {
							tableModel.addRow(new Object[] { i });
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
				ClientWhiteboardGUI.openEditor(boardToOpen);
			}
		}.start();
	}
}
