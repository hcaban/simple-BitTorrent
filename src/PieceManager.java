import java.io.*;
import java.util.*;
import java.lang.Math;

public class PieceManager {

    final private String directory;
    final private String filename;
    final private String prefix = "piece_";

    final private int pieceSize;
    final private int fileSize;

    private BitSet bitField;
    private BitSet pieceUtilization;

    public PieceManager(String directory, String filename, int fileSize, int pieceSize) {
        bitField = new BitSet((int)Math.ceil((double)fileSize / (double)pieceSize));
        pieceUtilization = new BitSet(bitField.size());

        this.directory = directory;
        this.filename = filename;
        this.pieceSize = pieceSize;
        this.fileSize = fileSize;
    }

    public synchronized void setPiece(int index) {
        bitField.set(index);
    }

    public synchronized void setAllPieces() {
        bitField.set(0, bitField.size());
    }

    public Boolean hasPiece(int piece) {
        return bitField.get(piece);
    }

    public byte[] getPiece(int piece) {
        synchronized (pieceUtilization) {
            if (pieceUtilization.get(piece) == true)
                return null;
            pieceUtilization.set(piece);
        }

        DataInputStream input = null;

        try {
            input = new DataInputStream(new FileInputStream(directory + "/" + prefix + piece));
        } catch (Exception e) {

        }

        byte[] buffer = new byte[1];

        return buffer;
    }

    public byte[] getBitField() {
        return bitField.toByteArray();
    }

    public int breakdownFile() {
        if (bitField.cardinality() != bitField.size())
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
            for (int i = 0; i < bitField.size(); i++) {
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
        if (bitField.cardinality() != bitField.size())
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
            for (int i = 0; i < bitField.size(); i++) {
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