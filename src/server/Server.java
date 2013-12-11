package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a general server that deals with whiteboard objects.
 *  It is abstract, since each subclass must implement the handleCurrentConnection()
 *  method. The server is always waiting for clients on the port specified by 
 *  getPort() (to be implemented). 
 *  
 * THREAD SAFTY: Not considered here, as there is no access to the data in this
 *  class. 
 * @author jains
 *
 */
public abstract class Server {
	
    List<WhiteboardServerInfo> whiteBoards;
	ServerSocket serverSocket;
	ArrayList<PrintWriter> clientOutputs = new ArrayList<PrintWriter>();
	
	/**
	 * Constructor for the Server
	 * @param whiteBoards List<WhiteboardServerInfo to be served. 
	 */
	public Server(List<WhiteboardServerInfo> whiteBoards) {
		this.whiteBoards = whiteBoards;
	}
	
	/**
	 * Runs the server, waiting forever for new clients. 
	 * @throws IOException
	 */
	public void host() throws IOException {
		serverSocket = new ServerSocket(getPort());
		while (true) {
			handleNewConnections();
		}
	}
	
	/**
	 * Waits for a new connection request to the server, and then accepts it.  
	 * @throws IOException
	 */
	public void handleNewConnections() throws IOException {
		final Socket newConnection = serverSocket.accept();
		new Thread() {
			public void run() {
				try {
    				clientOutputs.add(new PrintWriter(newConnection.getOutputStream(), true));
    				handleCurrentConnection(newConnection);
				} catch(Exception e) {
				    e.printStackTrace();
				}
			}
		}.start();
	}
	
	/**
	 * @return List<PrintWriter> of all clients' PrintWriters
	 */
	public ArrayList<PrintWriter> getClientOutputs() {
		return clientOutputs;
	}
	
	/**
	 * @return List<WhiteboardServerInfo> all board-client relationships.
	 */
	public List<WhiteboardServerInfo> getWhiteBoards() {
		return whiteBoards;
	}
	
	/**
	 * Announces to all clients via their PrintWriter streams a message
	 * @param message String to be sent to all clients. 
	 */
	public void writeToAllClients(String message) {
		for (PrintWriter out : clientOutputs) {
			out.println(message);
		}
	}
	
	/**
	 * @return int port to host the server on.
	 */
	public abstract int getPort();
	
	/**
	 * Handles one client's connection to the server. Started in a new thread for every client. 
	 * @param socket Socket that the client is connected to this server on. 
	 * @throws IOException 
	 */
	public abstract void handleCurrentConnection(Socket socket) throws IOException;
}
