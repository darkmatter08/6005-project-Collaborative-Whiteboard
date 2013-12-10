package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class WhiteboardServer extends Server {
	
	@Override
	public int getPort() {
		return shared.Ports.WHITEBOARD_GUI_PORT;
	}

	@Override
	public void handleCurrentConnection(Socket socket) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		for (String msg = in.readLine(); msg != null; msg = in.readLine()) {
			
		}
	}
}
