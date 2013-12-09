package server;

import java.io.IOException;
import java.net.ServerSocket;

public class PickerServer {
	ServerSocket serverSocket;
	
	public void host() throws IOException {
		serverSocket = new ServerSocket(shared.Ports.CONNECTION_PORT);
		while (true) {
			handleNewConnections();
		}
	}
	
	public void handleNewConnections() {
		
	}
	
}
