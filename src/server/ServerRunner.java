package server;

import java.io.IOException;

public class ServerRunner {
	PickerServer pickerServer;
	WhiteboardServer whiteboardServer;
	
	public ServerRunner() {
		pickerServer = new PickerServer();
		whiteboardServer = new WhiteboardServer();
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
