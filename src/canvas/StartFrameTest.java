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
				serverSocket = new ServerSocket(shared.Ports.MASTER_PORT);
				final Socket mySocket = serverSocket.accept();
				System.out.println("accepted a connection");
				handleMessages(mySocket);
				}
				catch (Exception e) {
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
			System.out.println("entered for loop");
			for(String msg = in.readLine(); msg != null; msg = in
					.readLine()) {
				System.out.println("Got a message!");
				if (msg == getIdMessage) {
					ArrayList<Integer> mockIds = new ArrayList<Integer>();
					mockIds.add(0);
					mockIds.add(1);
					mockIds.add(2);
					objOut.writeObject(mockIds);
				} else if (msg == createNewWhiteboardMessage) {
					objOut.writeObject(4);
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
		System.out.println("got here");
		JTable table = myFrame.getWhiteboardTable();
		for (int i = 0; i < 3; i++) {
			assertEquals(table.getValueAt(i, 0), i);
		}
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
