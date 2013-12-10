package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerRunner {
	PickerServer pickerServer;
	WhiteboardServer whiteboardServer;
	
	public ServerRunner() {
		List<WhiteboardServerInfo> whiteBoards = Collections.synchronizedList(new ArrayList<WhiteboardServerInfo>());
		pickerServer = new PickerServer(whiteBoards);
		whiteboardServer = new WhiteboardServer(whiteBoards);
	}
	
	public void hostServers() throws IOException {
		//We need a thread for each server to host in, so pickerServer will be done in a new thread.
		new Thread() {
			@Override
			public void run() {
				try {
					pickerServer.host();
				}
				catch(IOException e) {
				}
			}
		}.start();
		whiteboardServer.host();
	}
	
	
	public static void main(String[] args) throws IOException {
		ServerRunner serverRunner = new ServerRunner();
		serverRunner.hostServers();
	}
}
