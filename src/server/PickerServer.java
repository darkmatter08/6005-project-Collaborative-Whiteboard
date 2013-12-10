package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PickerServer extends Server {
	Integer numWhiteboards = 0;
	
	@Override
	public int getPort() {
		return shared.Ports.CONNECTION_PORT;
	}

	@Override
	public void handleCurrentConnection(Socket socket) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		for (String msg = in.readLine(); msg != null; msg = in.readLine()) {
			if (msg.equals(shared.Messages.ASK_FOR_WHITEBOARDS)) {
				
			}
			else if (msg.equals(shared.Messages.CREATE_NEW_WHITEBOARD)) {
				synchronized(numWhiteboards) {
					numWhiteboards++;
				}
			}
			writeToAllClients(numWhiteboards + "");
		}
	}
	
}
