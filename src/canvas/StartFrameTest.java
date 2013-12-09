package canvas;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JTable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StartFrameTest {
	private ServerSocket serverSocket;
	final String getIdMessage = "getWhiteboardIds";
	final String createNewWhiteboardMessage = "createNewWhiteboard";

	@Before
	public void setUp() {
		new Thread() {
			public void run() {
				try {
					serverSocket = new ServerSocket(shared.Ports.CONNECTION_PORT);
					final Socket mySocket = serverSocket.accept();
					handleMessages(mySocket);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void handleMessages(Socket socket) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			ObjectOutputStream objOut = new ObjectOutputStream(
					socket.getOutputStream());
			for (String msg = in.readLine(); msg != null; msg = in.readLine()) {
				if (msg.equals(getIdMessage)) {
					ArrayList<Integer> mockIds = new ArrayList<Integer>();
					mockIds.add(0);
					mockIds.add(1);
					mockIds.add(2);
					objOut.writeObject(mockIds);
					objOut.flush();
				} else if (msg.equals(createNewWhiteboardMessage)) {
					ArrayList<Integer> mockIds = new ArrayList<Integer>();
					mockIds.add(0);
					mockIds.add(1);
					mockIds.add(2);
					mockIds.add(3);
					objOut.writeObject(mockIds);
					objOut.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testConstructor() {
		StartFrame myFrame = new StartFrame();
		myFrame.init();
		JTable table = myFrame.getWhiteboardTable();
		for (int i = 0; i < 3; i++) {
			assertEquals(table.getValueAt(i, 0), i);
		}
	}

	@Test
	public void testAddWhiteboard() {
		StartFrame myFrame = new StartFrame();
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
