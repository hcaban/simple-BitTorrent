import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Manager implements Runnable {
    int ID;
    ArrayList<Peer> peers;
    ArrayList<ConcurrentLinkedQueue<MessageType>> peerChannels;
    ConcurrentLinkedQueue<Socket> socketQueue;
    ArrayList<Thread> threads;
    PieceManager pieceManager;
    Logging logger;

    public Manager(int ID, ArrayList<Peer> peers, ConcurrentLinkedQueue<Socket> socketQueue, PieceManager pieceManager, Logging logger) {
        this.peers = peers;
        this.socketQueue = socketQueue;
    }
    
    @Override
    public void run() {
        // Begin connections with peers
        for (int i = 0; i < peers.size(); i++) {
            peers.get(i).startConnection();
            peerChannels.add(new ConcurrentLinkedQueue<>());
            Thread thread = new Thread(new Listener(ID, peers.get(i), peerChannels.get(i), pieceManager, logger));
            thread.start();
        }

        while (true) {
            
        }
        
    }
}