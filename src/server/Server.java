package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public abstract class Server {
	List<WhiteboardServerInfo> whiteBoards;
	ServerSocket serverSocket;
	ArrayList<PrintWriter> clientOutputs = new ArrayList<PrintWriter>();
	
	public Server(List<WhiteboardServerInfo> whiteBoards) {
		this.whiteBoards = whiteBoards;
	}
	
	public void host() throws IOException {
		serverSocket = new ServerSocket(getPort());
		while (true) {
			handleNewConnections();
		}
	}
	
	public void handleNewConnections() throws IOException {
		final Socket newConnection = serverSocket.accept();
		new Thread() {
			public void run() {
				try {
				clientOutputs.add(new PrintWriter(newConnection.getOutputStream(), true));
				handleCurrentConnection(newConnection);
				} catch(Exception e) {}
			}
		}.start();
	}
	
	public ArrayList<PrintWriter> getClientOutputs() {
		return clientOutputs;
	}
	
	public List<WhiteboardServerInfo> getWhiteBoards() {
		return whiteBoards;
	}
	
	public void writeToAllClients(String message) {
		for (PrintWriter out : clientOutputs) {
			out.println(message);
		}
	}
	
	public abstract int getPort();
	public abstract void handleCurrentConnection(Socket socket) throws IOException;
}
