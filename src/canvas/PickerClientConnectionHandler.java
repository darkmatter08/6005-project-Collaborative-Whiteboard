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

/**
 * Represents the connection of the picker client (program to set username and choose
 * whiteboard) and the server.
 */
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

	/*
	 * Initialize connection between client and server.
	 */
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

	/**
	 * Ask for the latest list of whiteboard IDs.
	 * 
	 * Called in order to send a message to ask for list of whiteboard IDs.
	 */
	@SuppressWarnings("unchecked")
	public void askForWhiteboardIds() {
		out.println(shared.Messages.ASK_FOR_WHITEBOARDS);
	}

	/**
	 * Ask to create a new whiteboard on the server
	 */
	public synchronized void createNewWhiteBoard() {
		out.println(shared.Messages.CREATE_NEW_WHITEBOARD);
	}

	/**
	 * Display latest list of whiteboards from server in selection list.
	 */
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

	/**
	 * Open a whiteboard in a new window.
	 * @param boardId The ID of the board to open.
	 * @param username The username with which to connect to this whiteboard.
	 */
	public void openWhiteboard(final int boardId, final String username) {
		new Thread() {
			public void run() {
				ClientWhiteboardGUI.openEditor(boardId, username);
			}
		}.start();
	}
}
