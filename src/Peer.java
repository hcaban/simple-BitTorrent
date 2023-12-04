import java.io.*;
import java.net.*;
import java.util.BitSet;

public class Peer {
    // class variables
    public static int numberOfPieces = -1;

    // Peer attributes
    private int id;
    private String hostname;
    private int port;
    private Boolean hasFile;

    BitSet bitField;

    // Socket
    private Socket socket = null;

    // Streams
    private DataOutputStream output = null;
    private DataInputStream input = null;

    Peer(int id, String hostname, int port, Boolean hasFile) {
        if (numberOfPieces == -1) {
            System.err.println("Configuration not complete, class variable `numberOfPieces` not instantiated");
            System.exit(1);
        }

        this.id = id;
        this.hostname = hostname;
        this.port = port;
        this.hasFile = hasFile;
        this.bitField = new BitSet(numberOfPieces);

        if (hasFile) {
            bitField.set(0, bitField.size(), true);
        } else {
            bitField.set(0, bitField.size(), false);
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

    public int getID() {
        return id;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public DataInputStream getInputStream() {
        return input;
    }

    public DataOutputStream getOutputStream() {
        return output;
    }

    public BitSet getBitField() {
        return bitField;
    }

    public void setID(int id) {
        this.id = id;
    }

    public synchronized void setPiece(int index) {
        bitField.set(index);
    }

    public synchronized void setBitfield(byte[] bitField) {
        this.bitField = BitSet.valueOf(bitField);
    }
}
