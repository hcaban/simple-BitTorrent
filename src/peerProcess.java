import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

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
        Logging logger = new Logging("log_peer_" + id + ".log", peers.get(peers.size() - 1));
        PieceManager pieceManager = new PieceManager("peer_" + id, fileName, fileSize, pieceSize);
        
        int temp = -1;
        for (int i = 0; i < peers.size(); i++) {
            if (peers.get(i).getID() == id) {
                temp = peers.get(i).getPort();
                peers.remove(i);
                break;
            }
        }
        final int listeningPort = temp;

        ConcurrentLinkedQueue<Socket> socketQueue = new ConcurrentLinkedQueue<>();

        Thread server = new Thread() {
            public void run() {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(listeningPort);
                    while (true) {
                        socketQueue.add(serverSocket.accept());
                    }
                } catch (Exception e) {
                    return;
                }
            }
        };

        server.start();

        Thread manager = new Thread(new Manager(id, peers, socketQueue, pieceManager, logger));
        manager.start();

        try {
            // TEMPORARY
            manager.join();
        } catch(Exception e) {
            System.exit(1);
        }

        server.interrupt();
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