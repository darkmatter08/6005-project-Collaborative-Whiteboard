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

public class WhiteboardServer extends Server {
    
    /**
     * Constructs the server with all WhiteboardServerInfo
     * @param whiteBoards WhiteboardServerInfos to initialize the server with.
     */
	public WhiteboardServer(List<WhiteboardServerInfo> whiteBoards) {
		super(whiteBoards);
	}
	
	/**
	 * @return int port to host the server on
	 */
	@Override
	public int getPort() {
		return shared.Ports.WHITEBOARD_GUI_PORT;
	}

	@Override
	public void handleCurrentConnection(Socket socket) throws IOException {
		final int REQUEST_INDEX = 0;
		final int WHITEBOARD_ID_INDEX = 1;
		BufferedReader in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		for (String msg = in.readLine(); msg != null; msg = in.readLine()) {
			String[] tokens = msg.split(" ");
			String request = tokens[REQUEST_INDEX];
			int whiteBoardId = Integer.parseInt(tokens[WHITEBOARD_ID_INDEX]);
			if (request.equals(shared.Messages.GET_ALL_HISTORY)) {
			    String username = tokens[2];
				getWhiteBoards().get(whiteBoardId).getClients()
						.add(new ClientConnection(
						        new PrintWriter(socket.getOutputStream(), true),
						        username));
				sendEntireHistory(new PrintWriter(socket.getOutputStream(),
						true), getWhiteBoards().get(whiteBoardId));
			} else if (request.equals(shared.Messages.ADD_ACTION)) {
				WhiteboardAction action = new WhiteboardAction(tokens[2],
						tokens[3], tokens[4], tokens[5], tokens[6], tokens[7]);
				sendSingleAction(action, getWhiteBoards().get(whiteBoardId));
				
			}
		}
	}

	public void sendSingleAction(WhiteboardAction action, WhiteboardServerInfo info) {
		info.getHistory().add(action.toString());
		final String actionInThread = action.toString();
		final WhiteboardServerInfo infoInThread = info;
		new Thread() {
			public void run() {
				for (ClientConnection client : infoInThread.getClients()) {
					client.getPrintWriter().println(actionInThread);
				}
			}
		}.start();
	}
	
	public void sendEntireHistory(PrintWriter out, WhiteboardServerInfo info) {
		final PrintWriter outInThread = out;
		final WhiteboardServerInfo infoInThread = info;
		new Thread() {
			public void run() {
				for (String action : infoInThread.getHistory()) {
						outInThread.println(action);
					}
			}
		}.start();
	}

}
