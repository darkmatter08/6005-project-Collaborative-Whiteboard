package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Server {
ServerSocket serverSocket;
	
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
				handleCurrentConnection(newConnection);
			}
		}.start();
	}
	
	public abstract int getPort();
	public abstract void handleCurrentConnection(Socket socket);
}
