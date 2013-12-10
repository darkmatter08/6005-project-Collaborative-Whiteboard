package server;

import java.io.PrintWriter;
import java.util.ArrayList;

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
	private ArrayList<PrintWriter> clients;
	private ArrayList<String> history;
	
	public WhiteboardServerInfo() {
		clients = new ArrayList<PrintWriter>();
		history = new ArrayList<String>();
	}
	
	/**
	 * @return List<PrintWriter> connected to all clients of this 
	 *     whiteboard
	 */
	public ArrayList<PrintWriter> getClients() {
		return clients;
	}
	
	/**
	 * @return ArrayList<String> representing the board's entire history
	 */
	public ArrayList<String> getHistory() {
		return history;
	}
	

}
