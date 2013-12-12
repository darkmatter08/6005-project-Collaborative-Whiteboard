package canvas;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JTable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import canvas.WhiteboardPickerClient;

/**
 * Automatically test functionality of the whiteboard picker window.
 */
public class WhiteboardPickerClientTest {
	private ServerSocket serverSocket;
	final String getIdMessage = "getWhiteboardIds";
	final String createNewWhiteboardMessage = "createNewWhiteboard";
	private final String user = "Shawn";

	/**
	 * Initialize a server and a connection to it.
	 */
	@Before
	public void setUp() {
		new Thread() {
			public void run() {
				try {
					serverSocket = new ServerSocket(shared.ConnectionDetails.CLIENT_PICKER_GUI_PORT);
					final Socket mySocket = serverSocket.accept();
					handleMessages(mySocket);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * Read messages on socket to process messages from server.
	 * @param socket Socket on connection between client and server.
	 */
	public void handleMessages(Socket socket) {
		try {
			PrintWriter out = new PrintWriter(
					socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			for (String msg = in.readLine(); msg != null; msg = in.readLine()) {
				if (msg.equals(shared.Messages.ASK_FOR_WHITEBOARDS)) {
					out.println(3);
				} else if (msg.equals(shared.Messages.CREATE_NEW_WHITEBOARD)) {
					out.println(4);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test WhiteboardPickerClient is constructed properly.
	 */
	@Test
	public void testConstructor() {
		WhiteboardPickerClient myFrame = new WhiteboardPickerClient(user);
		myFrame.init();
		// TODO Make it so we get rid of Thread Sleeping
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JTable table = myFrame.getWhiteboardTable();
		for (int i = 0; i < 3; i++) {
		}
	}

	/**
	 * Test the behavior of asking to add a whiteboard.
	 */
	@Test
	public void testAddWhiteboard() {
		WhiteboardPickerClient myFrame = new WhiteboardPickerClient(user);
		myFrame.init();
		myFrame.addWhiteBoard();
		// TODO Make it so we get rid of Thread Sleeping
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(myFrame.getWhiteboardTable().getValueAt(3, 0), 3);
	}

	/**
	 * Close the socket to the server that was started.
	 */
	@After
	public void tearDown() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
