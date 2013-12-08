package server;

import shared.*;

import java.util.concurrent.BlockingQueue;

/**
 * The entire history of a whiteboard object, stored internally
 * as a queue of WhiteboardActions.
 * @author eliasb
 *
 */
public class MasterWhiteboard {
    BlockingQueue<WhiteboardAction> history;
}
