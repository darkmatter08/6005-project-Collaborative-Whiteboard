package canvas;

import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import shared.WhiteboardAction;

import org.junit.Before;
import org.junit.Test;

public class ClientWhiteBoardGUITest {
	ServerSocket serverSocket;
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	ObjectOutputStream objOut;
	ObjectInputStream objIn;

	@Before
	public void setUp() {
		new Thread() {
			public void run() {
				try {
					serverSocket = new ServerSocket(shared.ConnectionDetails.WHITEBOARD_GUI_PORT);
					socket = serverSocket.accept();
					in = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
					out = new PrintWriter(socket.getOutputStream(), true);
					objOut = new ObjectOutputStream(socket.getOutputStream());
					objIn = new ObjectInputStream(socket.getInputStream());
				} catch (Exception e) {

				}
			}
		}.start();
	}

	@Test
	public void testEraseButton() {
		ClientWhiteboardGUI clientGUI = new ClientWhiteboardGUI(0);
		clientGUI.init();
		clientGUI.setEraserColor();
		assertEquals(clientGUI.getCanvas().getPenColor(), Color.WHITE);
	}

	@Test
	public void testAppliesAction() throws IOException {
		ClientWhiteboardGUI clientGUI = new ClientWhiteboardGUI(0);
		clientGUI.init();
		ArrayList<WhiteboardAction> actions = new ArrayList<WhiteboardAction>();
		//actions.add(new WhiteboardAction(0, 0, 1, 1, Color.RED, new BasicStroke(1)));
		objOut.writeObject(actions);
		objOut.flush();
		//assertEquals(clientGUI.getCanvas().getWhiteboard());
	}
	
}
