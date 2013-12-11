package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

/**
 * The PickerServer is the server that handles requests from the WhiteboardPickerClient.
 * It listens on shared.ConnectionDetails.CLIENT_PICKER_GUI_PORT for client requests. 
 * 
 * THREAD SAFETY: We are concurrently accessing and modifying the whiteBoards, as 
 *  each client has his own thread that calls handleCurrentConnection() for him. 
 *  We rely on the client to provide a threadsafe List on instantiation. Our access and 
 *  modification to the list uses only safe atomic operations. 
 * @author jains
 *
 */
public class PickerServer extends Server {
	
    /**
     * Constructor for the PickerServer. 
     * @param whiteBoards List<WhiteboardServerInfo>. If the client of this class
     *  wants more than one user to connect, he must make sure that whiteBoards is a
     *  threadsafe type. 
     */
	public PickerServer(List<WhiteboardServerInfo> whiteBoards) {
		super(whiteBoards);
	}
	
	/**
	 * @see Server.java
	 */
	@Override
	public int getPort() {
		return shared.ConnectionDetails.CLIENT_PICKER_GUI_PORT;
	}
	
	/**
     * Expects the messages shared.Messages.ASK_FOR_WHITEBOARDS and 
     *  shared.Messages.CREATE_NEW_WHITEBOARD.
     * @see Server.java
     */
	@Override
	public void handleCurrentConnection(Socket socket) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		for (String msg = in.readLine(); msg != null; msg = in.readLine()) {
			if (msg.equals(shared.Messages.ASK_FOR_WHITEBOARDS)) {
				// do nothing in addition to alerting all users of the 
			    // number of whiteboards. 
			} else if (msg.equals(shared.Messages.CREATE_NEW_WHITEBOARD)) {
				this.getWhiteBoards().add(new WhiteboardServerInfo());
			}
			writeToAllClients(this.getWhiteBoards().size() + "");
		}
	}
	
}
