package canvas;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ServerHandler {
	final String getIdMessage = "getWhiteboardIds";
	final String createNewWhiteboardMessage = "createNewWhiteboard";
	Socket mySocket;
	PrintWriter out;
	ObjectInputStream in;

	public ServerHandler() {
		try {
			mySocket = new Socket("localhost", 8888);
			out = new PrintWriter(mySocket.getOutputStream());
			in = new ObjectInputStream(mySocket.getInputStream());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getWhiteBoardIds() {
		out.println(getIdMessage);
		List<Integer> boards = null;
		try {
			boards = (List<Integer>)in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return boards;
	}

	public int createNewWhiteBoard() {
		out.println(createNewWhiteboardMessage);
		Integer boardId = null;
		try {
			boardId = (Integer)in.readObject();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return boardId;
	}

}
