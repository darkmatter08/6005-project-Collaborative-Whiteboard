package test;

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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import canvas.ClientWhiteboardGUI;

public class ClientWhiteBoardGUITest {
	ServerSocket serverSocket;
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	ObjectOutputStream objOut;
	ObjectInputStream objIn;
	String actionString = new WhiteboardAction(10, 10, 10, 10, 10, 10).toString();
	
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
	
	@After
	public void tearDown() {
		try {
			socket.close();
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testEraseButton() {
		ClientWhiteboardGUI clientGUI = new ClientWhiteboardGUI(0, "testUser");
		clientGUI.init();
		clientGUI.setEraserColor();
		assertEquals(clientGUI.getCanvas().getPenColor(), Color.WHITE);

	}

	@Test
	public void testAppliesAction() throws IOException {
		ClientWhiteboardGUI clientGUI = new ClientWhiteboardGUI(0, "testUser");
		clientGUI.init();
		clientGUI.setEraserColor();
		out.println(actionString);
		// TODO: Remove sleep call.
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(clientGUI.getCanvas().getConnectionHandler().getHistoryReceived().get(0),
		actionString);
	}
	
}
