import java.io.*;
import java.net.*;

public class Peer {
    // class variables
    public static int numberOfPieces = -1;

    // Peer attributes
    private int id;
    private String hostname;
    private int port;
    private Boolean hasFile;

    // Socket
    private Socket socket = null;

    // Streams
    private DataOutputStream output = null;
    private DataInputStream input = null;

    Peer(int id, String hostname, int port, Boolean hasFile) {
        this.id = id;
        this.hostname = hostname;
        this.port = port;
        this.hasFile = hasFile;

        if (numberOfPieces == -1) {
            System.err.println("Configuration not complete, class variable `numberOfPieces` not instantiated");
            System.exit(1);
        }
    }

    public void startConnection() {
        try {
            socket = new Socket(hostname, port);
            output = new DataOutputStream(new BufferedOutputStream((socket.getOutputStream())));
            input = new DataInputStream(new BufferedInputStream((socket.getInputStream())));
        } catch (UnknownHostException hostErr) {
            System.out.println("Invalid hostname: " + hostname);
            System.exit(1);
        } catch (IllegalArgumentException portErr) {
            System.out.println("Invalid port number: " + port);
            System.exit(1);
        } catch (IOException err) {
            System.out.println("IO error in method startConnection");
            System.exit(1);
        }
    }
}
