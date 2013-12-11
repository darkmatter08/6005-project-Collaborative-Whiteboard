package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import shared.Messages;
import shared.WhiteboardAction;

/**
 * WhiteboardServer is a server that responds to requests to modify the whiteboard
 *  or to modify a client's username. 
 * @see Server.java
 * @author jains
 *
 */
public class WhiteboardServer extends Server {

	/**
	 * Constructs the server with all WhiteboardServerInfo
	 * 
	 * @param whiteBoards
	 *            WhiteboardServerInfos to initialize the server with.
	 */
	public WhiteboardServer(List<WhiteboardServerInfo> whiteBoards) {
		super(whiteBoards);
	}

	/**
	 * @see Server.java
	 */
	@Override
	public int getPort() {
		return shared.ConnectionDetails.WHITEBOARD_GUI_PORT;
	}

	/**
	 * @see Server.java
	 */
	@Override
	public void handleCurrentConnection(Socket socket) throws IOException {
		final int REQUEST_INDEX = 0;
		final int WHITEBOARD_ID_INDEX = 1;
		final int USERNAME_INDEX = 2;
		BufferedReader in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		for (String msg = in.readLine(); msg != null; msg = in.readLine()) {
			System.out.println("whiteboard server got message: " + msg);
			/*
			 * Protocol token ordering: 
			 * 0 - Request type 
			 * 1 - Whiteboard ID 
			 * 2 - username (String)  
			 * 3 - 7 WhiteboardAction (only on shared.Messages.ADD_ACTION) 
			 * 3 - x1 
			 * 4 - y1 
			 * 5 - x2 
			 * 6 - y2
			 * 7 - colorRGB (int) 
			 * 8 - strokeWidth (int)
			 */
			String[] tokens = msg.split(" ");
			String request = tokens[REQUEST_INDEX];
			int whiteboardId = Integer.parseInt(tokens[WHITEBOARD_ID_INDEX]);
			WhiteboardServerInfo whiteboard = getWhiteBoards()
					.get(whiteboardId);
			if (request.equals(shared.Messages.NEW_WHITEBOARD_CONNECTION)) {
				String username = updateUserName(tokens[USERNAME_INDEX],
						whiteboardId, new PrintWriter(socket.getOutputStream(), true));
				getWhiteBoards()
						.get(whiteboardId)
						.getClients()
						.add(new ClientConnection(new PrintWriter(socket
								.getOutputStream(), true), username));
				sendConnectedUsernames(whiteboard);
				sendEntireHistory(new PrintWriter(socket.getOutputStream(),
						true), whiteboard);
			} else if (request.equals(shared.Messages.ADD_ACTION)) {
				WhiteboardAction action = new WhiteboardAction(tokens[3],
						tokens[4], tokens[5], tokens[6], tokens[7], tokens[8]);
				sendSingleAction(action, getWhiteBoards().get(whiteboardId));

			}
			else if (request.equals(shared.Messages.DISCONNECT_ME)) {
				disconnectUser(socket, tokens[USERNAME_INDEX], whiteboardId);
			}
		}
	}
	
	/**
	 * Disconnects the user with the given username.
	 * @param Socket of the user
	 * @param userName String username to disconnect
	 * @param whiteboardId board to disconnect from. 
	 */
	 public void disconnectUser(Socket socket, String userName, int whiteboardId) {
		 ArrayList<ClientConnection> clients = getWhiteBoards().get(whiteboardId).getClients();
		 int indexToRemove = -1;
		 for (int i = 0; i < clients.size(); i++) {
			 if (clients.get(i).getUsername().equals(userName)) {
				 indexToRemove = i;
				 break;
			 }
		 }
		 if (indexToRemove != -1) {
			 clients.remove(indexToRemove);
		 }
		 sendConnectedUsernames(getWhiteBoards().get(whiteboardId));
	 }

	/**
	 * Updates the username to have a # added if its already a username
	 * connected. If the username is not a user currently connected to the
	 * whiteboard, it keeps the same username.This method also informs the client of its new username.
	 */
	public String updateUserName(String original, int whiteboardId, PrintWriter out) {
		String suffix = "";
		boolean uniqueUsername = false;
		while (!uniqueUsername) {
			uniqueUsername = true;
			for (String client : getWhiteBoards().get(whiteboardId)
					.getConnectedUsernamesMessage().split(" ")) {
				if ((original + suffix).equals(client)) {
					uniqueUsername = false;
					if (suffix.equals("")) {
						suffix = "0";
					} else {
						suffix = (Integer.parseInt(suffix) + 1) + "";
					}
				}
			}
		}
		tellClientItsUserName(original + suffix, out);
		return original + suffix;
	}
	
	/**
	 * Communicates to the user (represetned by out) his username. 
	 * @param username String username to send
	 * @param out client to send it to. 
	 */
	public void tellClientItsUserName(final String username, final PrintWriter out) {
		new Thread() {
			public void run() {
				out.println(shared.Messages.YOUR_USERNAME_IS + " " + username);
			}
		}.start();
	}

	/**
	 * @param action
	 *            WhiteboardAction to transmit to all clients connected to
	 *            WhiteboardServerInfo
	 * @param info
	 *            the WhiteboardServerInfo clients to send the update to
	 */
	public void sendSingleAction(WhiteboardAction action,
			WhiteboardServerInfo info) {
		info.addAction(action.toString());
		final String actionInThread = action.toString();
		final WhiteboardServerInfo infoInThread = info;
		new Thread() {
			public void run() {
				for (ClientConnection client : infoInThread.getClients()) {
					client.getPrintWriter().println(
							Messages.ADD_ACTION + " " + actionInThread);
				}
			}
		}.start();
	}

	/**
	 * @param action
	 *            WhiteboardAction to transmit to all clients connected to
	 *            WhiteboardServerInfo
	 * @param info
	 *            the WhiteboardServerInfo clients to send the update to
	 */
	public void sendConnectedUsernames(final WhiteboardServerInfo info) {
		new Thread() {
			public void run() {
				String message = info.getConnectedUsernamesMessage();
				for (ClientConnection client : info.getClients()) {
					client.getPrintWriter().println(message);
				}
			}
		}.start();
	}

	/**
	 * @param out
	 *            the client to send the history to
	 * @param info
	 *            the WhiteboardServerInfo board to get the history from
	 */
	public void sendEntireHistory(PrintWriter out, WhiteboardServerInfo info) {
		final PrintWriter outInThread = out;
		final WhiteboardServerInfo infoInThread = info;
		for (String actionStr : infoInThread.getHistory()) {
			WhiteboardAction action = WhiteboardAction.parse(actionStr);
			outInThread.println(Messages.ADD_ACTION + " " + action);
			System.out.println(Messages.ADD_ACTION + " " + action);
		}
	}
}