package canvas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

import shared.WhiteboardAction;

public class CanvasConnectionHandler {
	private Socket socket;
	private int boardId;
	private PrintWriter out;
	private BufferedReader in;
	private Canvas parentCanvas;
	Whiteboard board;

	public CanvasConnectionHandler(int boardId, Canvas canvas) {
		this.boardId = boardId;
		this.parentCanvas = canvas;
	}

	public void init() throws UnknownHostException, IOException {
		socket = new Socket("127.0.0.1", shared.Ports.WHITEBOARD_GUI_PORT);
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
				out.println(shared.Messages.GET_ALL_HISTORY + " " + boardId);
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

}
