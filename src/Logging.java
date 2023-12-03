import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logging {
    PrintWriter output = null;
    private Peer peer;
    private int peerID;

    public Logging(String filename, Peer peer) {
        try {
            output = new PrintWriter(new BufferedWriter(new FileWriter(filename)), true);

            this.peer = peer;
            peerID = peer.getID();
        } catch (Exception e) {

        }

    }

    public synchronized void connenctionMade(int peerID1, int peerID2) {

        output.write(getTime() + ": Peer " + peerID1 + " established connection to Peer " + peerID2 + ".");

    }

    public synchronized void unchoke(int peerID1, int peerID2) {

        output.write(getTime() + ": Peer " + peerID1 + " has optimistically unchoked Peer " + peerID2 + ".");

    }

    public synchronized void neighborUnchoke(int peerID1, int peerID2) {

        output.write(getTime() + ": Peer " + peerID1 +  " is unchoked by Peer " + peerID2 + ".");

    }

    public synchronized void neighborChoke(int peerID1, int peerID2 ) {

        output.write(getTime() + ": Peer " + peerID1 +  " is choked Peer " + peerID2 + ".");

    }

    public synchronized void have(int peerID1, int peerID2 ) {

        output.write(getTime() + ": Peer " + peerID1 + " received 'have' message from Peer " + peerID2 + ".");

    }

    public synchronized void receivedInterested(int peerID1, int peerID2) {

        output.write(getTime() + ": Peer " + peerID1 +  " has received the 'interested' message from Peer " + peerID2 + ".");

    }

    public synchronized void receivedNotInterested(int peerID1, int peerID2) {

        output.write(getTime() + ": Peer " + peerID1 + " has received the 'not interested' message from Peer " + peerID2 + ".");

    }

    public synchronized void receiveRequest(int peerID1, int peerID2) {

        output.write(getTime() + ": Peer " + peerID1 + " has received the 'request' message from Peer " + peerID2 + ".");

    }

    public synchronized void finishedDownload(int peerID1) {

        output.write(getTime() + ": Peer " + peerID1 + " has finished downloading the file.");

    }
    public String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:s z");
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);

    }
}