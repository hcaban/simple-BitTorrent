import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Listener implements Runnable {
    private ConcurrentLinkedQueue<MessageType> managerChannel;
    private ConcurrentLinkedQueue<MessageType> senderChannel;
    private Logging logger;
    private Peer peer;
    private DataInputStream input;
    private PieceManager pieceManager;
    private int ID;
    private Boolean choked = false;

    private Queue<Integer> interestedQueue = new LinkedList<>();

    public Listener(int ID, Peer peer, ConcurrentLinkedQueue<MessageType> channel, PieceManager pieceManager, Logging logger) {
        this.ID = ID;
        this.peer = peer;
        this.managerChannel = channel;
        input = peer.getInputStream();
        this.pieceManager = pieceManager;
        this.logger = logger;
    }

    @Override
    public void run() {
        senderChannel = new ConcurrentLinkedQueue<>();

        // Start sender
        Sender sender = new Sender(ID, peer, senderChannel, pieceManager, logger);
        Thread senderThread = new Thread(sender);
        senderThread.start();

        MessageType managerMessage = null;

        try {
            // Handshake
            byte[] handshakeHeader = "P2PFILESHARINGPROJ".getBytes();
            byte[] zeroBits = "\0\0\0\0\0\0\0\0\0\0".getBytes();

            byte[] inputHeader = input.readNBytes(18);
            byte[] inputZero = input.readNBytes(10);
            int peerID = input.readInt();

            if (!inputHeader.equals(handshakeHeader) || !inputZero.equals(zeroBits))
                throw new Exception("err");

            peer.setID(peerID);

            while ((managerMessage = managerChannel.poll()) != MessageType.KILL) {
                if (managerMessage != null) {
                    // Do command
                    // Note that this will not be a KILL message
                    // Probably choke or unchoke
                }
                
                int length = input.readInt();
                MessageType messageType = MessageType.getMessageType(input.readByte());

                switch (messageType) {
                    case CHOKE: {
                        setChoked(true);
                        // do something
                        continue;
                    }
                    case UNCHOKE:
                        emptyInterestedQueue();
                        break;

                    case INTERESTED: {

                    }
                    case NOTINTERESTED: {
                        
                    }
                    case HAVE: 
                    case REQUEST: {

                    }
                    case BITFIELD: {
                        length--;
                        peer.setBitfield(input.readNBytes(length));
                        pieceManager.compareBitFields(peer.getBitField(), interestedQueue);
                        if (interestedQueue.size() > 0)
                            senderChannel.add(MessageType.INTERESTED);
                        else
                            senderChannel.add(MessageType.NOTINTERESTED);
                        break;
                    }

                    case PIECE: {
                        length -= 5;
                        pieceManager.addPiece(input.readInt(), input.readNBytes(length));
                        break;
                    }

                    default:
                }

            }
        } catch (Exception e) {
            managerChannel.clear();
            managerChannel.add(MessageType.ERROR);
            senderChannel.add(MessageType.ERROR);
            return;
        }   
    }

    private void emptyInterestedQueue() {
        while (interestedQueue.size() > 0) {
            if (!pieceManager.hasPiece(interestedQueue.peek())) {
                MessageType msg = MessageType.REQUEST;
                msg.setPayload(interestedQueue.peek());
                senderChannel.add(msg);
            }
            interestedQueue.remove();
        }
    }

    public synchronized void setChoked(Boolean status) {
        this.choked = status;
    }

    public synchronized Boolean isChoked() {
        return choked;
    }
}