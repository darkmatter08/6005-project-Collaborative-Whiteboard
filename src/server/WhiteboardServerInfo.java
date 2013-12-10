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
	 * @param clients resets the clients of this board to clients
	 */
	public void setClients(ArrayList<PrintWriter> clients) {
		this.clients = clients;
	}
	
	/**
	 * @return ArrayList<String> representing the state of this whiteboard
	 */
	public ArrayList<String> getHistory() {
		return history;
	}
	
	/**
	 * @param history resets the history of this board to history.
	 */
	public void setHistory(ArrayList<String> history) {
		this.history = history;
	}
	
}
