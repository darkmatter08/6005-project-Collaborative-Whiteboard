package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

public class PickerServer extends Server {
	
	public PickerServer(List<WhiteboardServerInfo> whiteBoards) {
		super(whiteBoards);
	}
	
	@Override
	public int getPort() {
		return shared.Ports.CLIENT_PICKER_GUI_PORT;
	}

	@Override
	public void handleCurrentConnection(Socket socket) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		for (String msg = in.readLine(); msg != null; msg = in.readLine()) {
			if (msg.equals(shared.Messages.ASK_FOR_WHITEBOARDS)) {
				
			}
			else if (msg.equals(shared.Messages.CREATE_NEW_WHITEBOARD)) {
					this.getWhiteBoards().add(new WhiteboardServerInfo());
			}
			writeToAllClients(this.getWhiteBoards().size() + "");
		}
	}
	
}
