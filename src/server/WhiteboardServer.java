package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
		return shared.ConnectionDetails.WHITEBOARD_GUI_PORT;
	}
	
	/**
	 * Handles a client on the socket socket.
	 * @param client socket
	 */
	@Override
	public void handleCurrentConnection(Socket socket) throws IOException {
		final int REQUEST_INDEX = 0;
		final int WHITEBOARD_ID_INDEX = 1;
		BufferedReader in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		for (String msg = in.readLine(); msg != null; msg = in.readLine()) {
			System.out.println("whiteboard server got message: " + msg);
		    /*
			 * Protocol token ordering:
			 * 0 - Request type 
			 * 1 - Whiteboard ID
			 * 2 - username (String) (only on shared.Messages.NEW_WHITEBOARD_CONNECTION)
			 * 2-7 WhiteboardAction (only on shared.Messages.ADD_ACTION)
			 *   2 - x1
			 *   3 - y1
			 *   4 - x2 
			 *   5 - y2
			 *   6 - colorRGB (int)
			 *   7 - strokeWidth (int)
			 *  
			 */
		    String[] tokens = msg.split(" ");
			String request = tokens[REQUEST_INDEX];
			int whiteBoardId = Integer.parseInt(tokens[WHITEBOARD_ID_INDEX]);
			WhiteboardServerInfo whiteboard = getWhiteBoards().get(whiteBoardId);
			if (request.equals(shared.Messages.NEW_WHITEBOARD_CONNECTION)) {
			    String username = tokens[2];
				getWhiteBoards().get(whiteBoardId).getClients()
						.add(new ClientConnection(
						        new PrintWriter(socket.getOutputStream(), true),
						        username));
				sendConnectedUsernames(whiteboard);
				sendEntireHistory(new PrintWriter(socket.getOutputStream(),
						true), whiteboard);
			} else if (request.equals(shared.Messages.ADD_ACTION)) {
				WhiteboardAction action = new WhiteboardAction(tokens[2],
						tokens[3], tokens[4], tokens[5], tokens[6], tokens[7]);
				sendSingleAction(action, getWhiteBoards().get(whiteBoardId));
				
			}
		}
	}
	
	/**
	 * @param action WhiteboardAction to transmit to all clients connected to
	 *     WhiteboardServerInfo
	 * @param info the WhiteboardServerInfo clients to send the update to
	 */
	public void sendSingleAction(WhiteboardAction action, WhiteboardServerInfo info) {
		info.getHistory().add(action.toString());
		final String actionInThread = action.toString();
		final WhiteboardServerInfo infoInThread = info;
		new Thread() {
			public void run() {
				for (ClientConnection client : infoInThread.getClients()) {
					client.getPrintWriter().println(Messages.ADD_ACTION + " " + actionInThread);
				}
			}
		}.start();
	}
	
	/**
     * @param action WhiteboardAction to transmit to all clients connected to
     *     WhiteboardServerInfo
     * @param info the WhiteboardServerInfo clients to send the update to
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
	 * @param out the client to send the history to
	 * @param info the WhiteboardServerInfo board to get the history from
	 */
	public void sendEntireHistory(PrintWriter out, WhiteboardServerInfo info) {
		final PrintWriter outInThread = out;
		final WhiteboardServerInfo infoInThread = info;
		new Thread() {
			public void run() {
				for (String actionStr : infoInThread.getHistory()) {
				        WhiteboardAction action = WhiteboardAction.parse(actionStr);
						outInThread.println(Messages.ADD_ACTION + " " + action);
						System.out.println(Messages.ADD_ACTION + " " + action);
				}
			}
		}.start();
	}

}
