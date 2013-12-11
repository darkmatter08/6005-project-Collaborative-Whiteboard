package shared;

/**
 * ConnectionDetails contains network connection specific constants. 
 * @author jains
 *
 */
public class ConnectionDetails {
    // Port to be used by the Picker (Server and client)
    public static int CLIENT_PICKER_GUI_PORT = 8887;
    // Port to be used by the Whiteboard (Server and client)
    public static int WHITEBOARD_GUI_PORT = 8888;
    // Address that the client attempts a connection to the server. 
    public static String SERVER_ADDRESS = "127.0.0.1";
}
