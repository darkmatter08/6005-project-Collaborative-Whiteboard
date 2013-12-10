package server;

import java.io.PrintWriter;
import java.util.ArrayList;

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
	
	public void setClients(ArrayList<ClientConnection> clients) {
		this.clients = clients;
	}
	
	public ArrayList<String> getHistory() {
		return history;
	}
	
	public void setHistory(ArrayList<String> history) {
		this.history = history;
	}
	
}
