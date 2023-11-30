import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

class PieceManager {

    final private String directory;
    final private String filename;
    final private String prefix = "piece_";

    final private int pieceSize;
    final private int fileSize;

    private BitSet pieceField;

    public PieceManager(String directory, String filename, int fileSize, int pieceSize) {
        pieceField = new BitSet(Math.ceilDiv(fileSize, pieceSize));

        this.directory = directory;
        this.filename = filename;
        this.pieceSize = pieceSize;
        this.fileSize = fileSize;
    }

    public void setPiece(int index) {
        pieceField.set(index);
    }

    public void clearPiece(int index) {
        pieceField.clear(index);
    }

    public void setAllPieces() {
        pieceField.set(0, pieceField.size());
    }

    public void clearAllPieces() {
        pieceField.clear(0, pieceField.size());
    }

    public int breakdownFile() {
        if (pieceField.cardinality() != pieceField.size())
            return -1;

        DataInputStream file = null;

        try {
            file = new DataInputStream(new FileInputStream(directory + "/" + filename));
        } catch (Exception e) {
            System.err.println(filename + " not found");
            System.exit(1);
        }

        byte[] buffer = new byte[pieceSize];

        try {
            for (int i = 0; i < pieceField.size(); i++) {
                DataOutputStream piece = new DataOutputStream(new FileOutputStream(directory + "/" + prefix + i));

                int bytesRead = file.read(buffer, 0, pieceSize);
                piece.write(buffer, 0, bytesRead);

                piece.close();
            }

            file.close();
        } catch (Exception e) {
            System.err.println("Error with breaking down file");
            System.exit(1);
        }

        return 0;
    }

    public int joinPieces() {
        if (pieceField.cardinality() != pieceField.size())
            return -1;

        DataOutputStream file = null;

        try {
            file = new DataOutputStream(new FileOutputStream(directory + "/" + filename));
        } catch (Exception e) {
            System.err.println("Error with creating file");
            System.exit(1);
        }

        byte[] buffer = new byte[pieceSize];

        try {
            for (int i = 0; i < pieceField.size(); i++) {
                DataInputStream piece = new DataInputStream(new FileInputStream(directory + "/" + prefix + i));

                int bytesRead = piece.read(buffer, 0, pieceSize);
                file.write(buffer, 0, bytesRead);

                piece.close();
            }

            file.close();
        } catch (Exception e) {
            System.err.println("Error with joining pieces");
            System.exit(1);
        }

        return 0;
    }
}