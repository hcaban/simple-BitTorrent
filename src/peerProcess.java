import java.io.*;
import java.net.*;
import java.util.*;

public class peerProcess {
    static int id;

    static int numberOfPreferredNeighbors;
    static int unchokingInterval;
    static int optimisticUnchokingInterval;
    static String fileName;
    static int fileSize;
    static int pieceSize;

    public static void main(String[] args) {
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.err.println("Invalid peerID");
            System.exit(1);
        }

        readCommon();
        Peer.numberOfPieces = (fileSize / pieceSize) + (fileSize % pieceSize > 0 ? 1 : 0);
        ArrayList<Peer> peers = readPeerInfo();
        Logging logger = new Logging("log_peer_" + id + ".log");
        
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(peers.get(0).getPort());
        } catch (IOException e) {
            System.err.println("Could not create server socket");
            System.exit(0);
        }
        peers.remove(0);

        
    }

    public static void readCommon() {
        Scanner input = null;
        try {
            input = new Scanner(new File("Common.cfg"));
        } catch (Exception e) {
            System.err.println("Common.cfg Not Found");
            System.exit(1);
        }

        try {
            while (input.hasNextLine()) {
                switch (input.next()) {
                    case "NumberOfPreferredNeighbors":
                        numberOfPreferredNeighbors = Integer.parseInt(input.next());
                        break;
                    case "UnchokingInterval":
                        unchokingInterval = Integer.parseInt(input.next());
                        break;
                    case "OptimisticUnchokingInterval":
                        optimisticUnchokingInterval = Integer.parseInt(input.next());
                        break;
                    case "FileName":
                        fileName = input.next();
                        break;
                    case "FileSize":
                        fileSize = Integer.parseInt(input.next());
                        break;
                    case "PieceSize":
                        pieceSize = Integer.parseInt(input.next());
                        break;
                    default:
                        throw new Exception();
                }
            }

        } catch (Exception e) {
            System.err.println("Common.cfg Not Properly Formatted");
            System.exit(1);
        }

        input.close();
    }

    public static ArrayList<Peer> readPeerInfo() {
        Scanner input = null;
        try {
            input = new Scanner(new File("PeerInfo.cfg"));
        } catch (Exception e) {
            System.err.println("PeerInfo.cfg Not Found");
            System.exit(1);
        }

        ArrayList<Peer> peers = new ArrayList<>();

        try {
            while (input.hasNextLine()) {
                int peerID = Integer.parseInt(input.next());
                if (peerID > id) {
                    input.nextLine();
                    continue;
                }

                String hostname = input.next();
                int port = Integer.parseInt(input.next());
                Boolean hasFile = false;

                if (input.next().equals("1"))
                    hasFile = true;

                peers.add(new Peer(peerID, hostname, port, hasFile));
            }
        } catch (Exception e) {
            System.err.println("PeerInfo.cfg Not Properly Formatted");
            System.exit(1);
        }

        return peers;
    }
}