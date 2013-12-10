package server;

import java.io.PrintWriter;
import java.util.ArrayList;

import shared.Messages;

/**
 * WhiteboardServerInfo represents a whiteboard's state, as a 
 *  list of history items. It also maintains all the clients connected 
 *  to the whiteboard, represented as a list of PrintWriters. 
 *  The class represents the relationship between a whiteboard and it's
 *  clients
 * @author jains
 *
 */
public class WhiteboardServerInfo {
	private ArrayList<ClientConnection> clients;
	private ArrayList<String> history;
	
	public WhiteboardServerInfo() {
		clients = new ArrayList<ClientConnection>();
		history = new ArrayList<String>();
	}
	
	public ArrayList<ClientConnection> getClients() {
		return clients;
	}
	
	/**
	 * @return ArrayList<String> representing the board's entire history
	 */
	public ArrayList<String> getHistory() {
		return history;
	}
	
	public String getConnectedUsernamesMessage() {
	    String s = Messages.CONNECTED_USERS;
	    for (ClientConnection client : clients) {
	        s += " " + client.getUsername();
	    }
	    return s;
	}
}
