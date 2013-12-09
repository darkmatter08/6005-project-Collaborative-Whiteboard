package server;

import shared.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.List;

/**
 * Represents the state of a whiteboard with a unique id, as a complete 
 * history of actions applied to the whiteboard, as a queue of WhiteboardActions.
 * @author eliasb jains
 *
 */
public class MasterWhiteboard {
    private BlockingQueue<WhiteboardAction> history;
    final private int id;
    
    /**
     * Initializes new blank whiteboard, with an id
     */
    public MasterWhiteboard(int id) {
        this.id = id;
        history = new LinkedBlockingQueue<WhiteboardAction>();
    }
    
    public synchronized void applyAction(WhiteboardAction action) {
        history.add(action);
    }
    
    public synchronized void applyActions(List<WhiteboardAction> actions) {
        for (WhiteboardAction action : actions) {
            applyAction(action);
        }
    }
    
    public BlockingQueue<WhiteboardAction> getHistory() {
        return history;
    }
    
    public int getId() {
        return id;
    }
    
    public boolean hasSameId(int otherID) {
        return id == otherID;
    }
}
