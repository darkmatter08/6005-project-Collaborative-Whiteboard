package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ServerRunner launches the whole server application, starting the 
 *  pickerServer and whiteboardServer in separate threads. 
 * @author jains
 *
 */
public class ServerRunner implements Runnable {
	PickerServer pickerServer;
	WhiteboardServer whiteboardServer;
	
	public ServerRunner() {
		List<WhiteboardServerInfo> whiteBoards = Collections.synchronizedList(new ArrayList<WhiteboardServerInfo>());
		pickerServer = new PickerServer(whiteBoards);
		whiteboardServer = new WhiteboardServer(whiteBoards);
	}
	
	/**
	 * Starts the pickerServer (in a new thread) and the whiteboardServer. 
	 * @throws IOException
	 */
	public void hostServers() throws IOException {
		// We need a thread for each server to host in, so pickerServer 
	    // will be done in a new thread.
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
	
	/**
	 * Main to start the whole server. 
	 */
	public static void main(String[] args) throws IOException {
		ServerRunner serverRunner = new ServerRunner();
		serverRunner.hostServers();
	}

    @Override
    public void run() {
        try {
            main(null);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't Start Server");
        }
    }
}
