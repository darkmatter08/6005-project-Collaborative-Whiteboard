package canvas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import canvas.ClientWhiteboardGUI;

public class ClientWhiteBoardGUIManualTest {
    /**
     * Run an individual canvas GUI window with a connection to a server to manually test
     * canvas and GUI functionality.
     */
	public static void main(String[] args) {
		new Thread() {
			public void run() {
				try {
					ServerSocket serverSocket = new ServerSocket(shared.ConnectionDetails.WHITEBOARD_GUI_PORT);
					Socket socket = serverSocket.accept();
			        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			        ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
			        ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
				}
				catch (Exception e) {
					
				}
			}
		}.start();
		ClientWhiteboardGUI.openEditor(0, "testUser");
	}
}
