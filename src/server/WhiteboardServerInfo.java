package server;

import java.io.PrintWriter;
import java.util.ArrayList;

public class WhiteboardServerInfo {
	private ArrayList<PrintWriter> clients;
	private ArrayList<String> history;
	
	public WhiteboardServerInfo() {
		clients = new ArrayList<PrintWriter>();
		history = new ArrayList<String>();
	}
	
	public ArrayList<PrintWriter> getClients() {
		return clients;
	}
	
	public void setClients(ArrayList<PrintWriter> clients) {
		this.clients = clients;
	}
	
	public ArrayList<String> getHistory() {
		return history;
	}
	
	public void setHistory(ArrayList<String> history) {
		this.history = history;
	}
	
}
