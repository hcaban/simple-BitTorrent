import java.io.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class Logging {
    PrintWriter output = null;

    public Logging(String filename) {
        try {
            output = new PrintWriter(new BufferedWriter(new FileWriter(filename)), true);
        } catch (Exception e) {

        }

    }

    public void connenctionMade(int peerID1, int peerID2) {

        output.write(getTime() + ": Peer " + peerID1 + " established connection to Peer " + peerID2 + ".");

    }

    public void connection(int peerID1, int peerID2) {

        output.write(getTime() + ": Peer " + peerID1 + " is connectedFrom " + peerID2 + ".");

    }

    public void neighbors(int peerID1, int peerID2) {



    }

    public void unchoke(int peerID1, int peerID2) {



    }

    public void neighborUnchoke(int peerID1, int peerID2) {



    }

    public void neighborChoke(int peerID1, int peerID2 ) {



    }

    public void has(int peerID1, int peerID2 ) {



    }

    public void receivedInterested(int peerID1, int peerID2) {



    }

    public void receivedUninterested(int peerID1, int peerID2) {



    }

    public void receiveRequest(int peerID1, int peerID2) {



    }

    public void finishedDownload(int peerID1, int peerID2) {



    }
    public String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:s z");
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);

    }
}