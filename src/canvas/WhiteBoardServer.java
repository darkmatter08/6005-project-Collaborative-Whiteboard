package canvas;

import java.util.List;

public interface WhiteBoardServer {
	
	/**
	 * Creates a new white board that is hosted by the server.
	 * @return The boardID of the newly created white board.
	 */
	public int createNewWhiteboard();
	
	
	/**
	 *  @return A list of all the current white board IDs.
	 */
	public List<Integer> getWhiteBoardIds();
	
}
