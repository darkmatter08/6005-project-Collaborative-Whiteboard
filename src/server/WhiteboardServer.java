package server;

import java.net.Socket;

public class WhiteboardServer extends Server {
	
	@Override
	public int getPort() {
		return shared.Ports.MASTER_PORT;
	}

	@Override
	public void handleCurrentConnection(Socket socket) {
		
	}
}
