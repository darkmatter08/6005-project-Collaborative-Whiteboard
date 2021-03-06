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

import shared.Messages;
import shared.WhiteboardAction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import canvas.ClientWhiteboardGUI;

/**
 * Automatically test the functionality of the canvas GUI window.
 */
public class ClientWhiteBoardGUITest {
	ServerSocket serverSocket;
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	ObjectOutputStream objOut;
	ObjectInputStream objIn;
	String actionString = new WhiteboardAction(10, 10, 10, 10, 10, 10).toString();
	
	/**
	 * Start a server and connect to it.
	 */
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
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	/**
	 * Disconnect from the server.
	 */
	@After
	public void tearDown() {
		try {
			socket.close();
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("Couldn't close socket");
			e.printStackTrace();
		}
	}

	/**
	 * Confirm switching of erase button from "erase" button.
	 */
	@Test
	public void testEraseButton() {
		ClientWhiteboardGUI clientGUI = new ClientWhiteboardGUI(0, "testUser");
		clientGUI.init();
		clientGUI.setEraserColor();
		assertEquals(clientGUI.getCanvas().getPenColor(), Color.WHITE);

	}

	/**
	 * Confirm that client's added actions get received by server and sent back to eventually
	 * arrive in client's historyReceived queue.
	 * @throws IOException
	 */
	@Test
	public void testAppliesAction() throws IOException {
		ClientWhiteboardGUI clientGUI = new ClientWhiteboardGUI(0, "testUser");
		clientGUI.init();
		clientGUI.getCanvas().getConnectionHandler().listenForServerMessages();
		out.println(Messages.ADD_ACTION + " " + actionString);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(clientGUI.getCanvas().getConnectionHandler().getHistoryReceived().get(0),
		actionString);
	}
	
}
