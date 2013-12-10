package canvas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import shared.ConnectionDetails;
import shared.WhiteboardAction;

public class CanvasConnectionHandler {
	private Socket socket;
	private int boardId;
	private PrintWriter out;
	private BufferedReader in;
	private Canvas parentCanvas;
	private ClientWhiteboardGUI gui;
	private ArrayList<String> historyReceived = new ArrayList<String>();
	Whiteboard board;

	public CanvasConnectionHandler(int boardId, ClientWhiteboardGUI gui) {
		this.boardId = boardId;
		this.gui = gui;
		this.parentCanvas = gui.getCanvas();
	}

	public void init() throws UnknownHostException, IOException {
		socket = new Socket(ConnectionDetails.SERVER_ADDRESS, ConnectionDetails.WHITEBOARD_GUI_PORT);
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		listenForActions();
		askForHistory();
	}

	public void listenForActions() {
		new Thread() {
			public void run() {
				try {
					for (String action = in.readLine(); action != null; action = in
							.readLine()) {
						historyReceived.add(action);
						// Once the frame closes, whiteboard will be null, and we should break out of this loop.
						if (parentCanvas == null || parentCanvas.getWhiteboard() == null) {
							break;
						}
						parentCanvas.getWhiteboard().applyAction(
								WhiteboardAction.parse(action));
						parentCanvas.repaint();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}.start();
	}

	public void askForHistory() {
		new Thread() {
			public void run() {
				out.println(shared.Messages.NEW_WHITEBOARD_CONNECTION + " " + boardId + " " + gui.getUsername());
			}
		}.start();
	}

	public void sendAction(WhiteboardAction action) {
		final String messageToSend = shared.Messages.ADD_ACTION + " " + boardId
				+ " " + action.toString();
		new Thread() {
			public void run() {
				out.println(messageToSend.toString());
			}
		}.start();
	}
	
	public ArrayList<String> getHistoryReceived() {
		return historyReceived;
	}

}
