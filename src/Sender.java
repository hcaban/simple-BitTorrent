import java.io.*;
import java.util.concurrent.*;


public class Sender implements Runnable {
    private ConcurrentLinkedQueue<MessageType> channel;
    private Peer peer;
    private DataOutputStream output;
    private PieceManager pieceManager;
    private int ID;

    public Sender(int ID, Peer peer, ConcurrentLinkedQueue<MessageType> channel, PieceManager pieceManager) {
        this.ID = ID;
        this.peer = peer;
        this.channel = channel;
        output = peer.getOutputStream();
        this.pieceManager = pieceManager;
    }

    @Override
    public void run() {
        try {
            // Send handshake
            output.writeBytes("P2PFILESHARINGPROJ");
            output.writeBytes("\0\0\0\0\0\0\0\0\0\0");
            output.writeInt(ID);
            
            MessageType message = null;
            while ((message = channel.poll()) != MessageType.KILL) {
                if (message == null) {
                    Thread.yield();
                    continue;
                }

                switch (message) {
                    case CHOKE:
                    case UNCHOKE:
                    case INTERESTED:
                    case NOTINTERESTED:
                        // MessageType Length
                        output.writeInt(1);
                        // MessageType Type
                        output.writeByte(message.getValue());
                        output.flush();
                        break;

                    case HAVE:
                    case REQUEST:
                        // MessageType Length
                        output.writeInt(1 + 4);
                        // MessageType Type
                        output.writeByte(message.getValue());
                        // MessageType Payload
                        output.writeInt(channel.remove().getPayload());
                        output.flush();
                        break;

                    case BITFIELD: {
                        byte[] bitField = pieceManager.getBitField();
                        // MessageType Length
                        output.writeInt(1 + bitField.length);
                        // MessageType Type
                        output.writeByte(message.getValue());
                        // MessageType Payload
                        output.write(bitField);
                        output.flush();
                        break;
                    }
                        
                    case PIECE: {
                        int piece = channel.remove().getPayload();
                        byte[] data = pieceManager.getPiece(piece);
                        // MessageType Length
                        output.writeInt(1 + 4 + data.length);
                        // MessageType Type
                        output.writeByte(message.getValue());
                        // MessageType Payload
                        output.writeInt(piece);
                        output.write(data);
                        output.flush();
                        break;
                    }
                        
                    default:
                        channel.clear();
                        channel.add(MessageType.ERROR);
                        return;
                }
            
            }
        } catch (Exception e) {
            channel.clear();
            channel.add(MessageType.ERROR);
            return;
        }
    }
}
