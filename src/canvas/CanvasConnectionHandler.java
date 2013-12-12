package canvas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import shared.ConnectionDetails;
import shared.Messages;
import shared.WhiteboardAction;

/**
 * Object representing the connection between a canvas (local) and a wihteboard on the server.
 * 
 * Contains references to the network socket, input and output streams for communication,
 * as well as the history of actions received from all clients for this particular board,
 * and a pointer to the Canvas and ClientWhiteboardGUI objects related to the client's GUI
 * representation of this board.
 */
public class CanvasConnectionHandler {
	private Socket socket;
	private int boardId;
	private PrintWriter out;
	private BufferedReader in;
	private Canvas parentCanvas;
	private ClientWhiteboardGUI gui;
	private ArrayList<String> historyReceived = new ArrayList<String>();
	Whiteboard board;

	public CanvasConnectionHandler(int boardId, ClientWhiteboardGUI gui, Canvas canvas) {
		this.boardId = boardId;
		this.gui = gui;
		this.parentCanvas = canvas;
	}

	/**
	 * Initialize the connection between client and server. 
	 * Constructs a new socket as well as input and output streams to communicate with client.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void init() throws UnknownHostException, IOException {
		socket = new Socket(ConnectionDetails.SERVER_ADDRESS, ConnectionDetails.WHITEBOARD_GUI_PORT);
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	/**
	 * Continuously receive messages from the client and send to the server.
	 * 
	 * Messages processed include: ADD_ACTION (ask server to draw an action to the master copy of the whiteboard,
	 * and to send it to all connected clients), CONNECTED_USERS (ask for a list of the usernames to be sent back
	 * to the client, of all users who are currently connected to the server), YOUR_USERNAME_IS (
	 * tell server to tell user their current username, as it may have changed from what was sent when
	 * they connected due to that username already existing as a connected user).
	 */
	public void listenForServerMessages() {
		System.out.println("listening"); //TODO Remove me.
		new Thread() {
			public void run() {
				try {
					for (String message = in.readLine(); message != null; message = in
							.readLine()) {
					    String commandType = message.split(" ")[0];
					    String args = message.substring(commandType.length() + 1);
					    if (commandType.equals(Messages.ADD_ACTION)) {
    						historyReceived.add(args);
    						// Once the frame closes, whiteboard will be null, and we should break out of this loop.
    						if (parentCanvas == null || parentCanvas.getWhiteboard() == null) {
    							System.out.println("reached break condition");
    							System.out.println("parentCanvas: " + parentCanvas);
    						    break;
    						}
    						parentCanvas.getWhiteboard().applyAction(
    								WhiteboardAction.parse(args));
    						parentCanvas.repaint();
					    }
					    else if (commandType.equals(Messages.CONNECTED_USERS)) {
					    	JLabel users = gui.getConnectedUsersLabel();
					    	users.setText("Connected users: " + args);
					    }
					    else if (commandType.equals(Messages.YOUR_USERNAME_IS)) {
					    	gui.setUserName(args);
					    	System.out.println("My username is " + gui.getUsername());
					    }
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}.start();
	}

	/**
	 * Request the complete history of actions the server has received.
	 */
	public void askForHistory() {
		new Thread() {
			public void run() {
				out.println(getMessageHeader(shared.Messages.NEW_WHITEBOARD_CONNECTION));
			}
		}.start();
	}

	/**
	 * Send an individual drawn action to the server.
	 * @param action The action that was drawn
	 */
	public void sendAction(WhiteboardAction action) {
		final String messageToSend = getMessageHeader(shared.Messages.ADD_ACTION) + " " +  action.toString();
		new Thread() {
			public void run() {
				out.println(messageToSend);
			}
		}.start();
	}
	
	/**
	 * @return A pointer to the history list that has been received from the server so far.s
	 */
	public ArrayList<String> getHistoryReceived() {
		return historyReceived;
	}

	/**
	 * Close this client canvas's connection to the server.
	 */
	public void closeConnection() {
		new Thread() {
			public void run() {
				out.println(getMessageHeader(shared.Messages.DISCONNECT_ME));
			}
		}.start();
	}
	
	/**
	 * Convert the type of a message into its actual message protocal string
	 * @param messageType The type of message being sent. One of NEW_WHITEBOARD_CONNECTION,
	 * ADD_ACTION, NEW_WHITEBOARD_CONNECTION).
	 * @return
	 */
	public String getMessageHeader(String messageType) {
		return messageType + " " + boardId + " " + gui.getUsername();
	}

}
