import java.io.*;

public class Logging {
    PrintWriter output = null;

    public Logging(String filename) {
        try {
            output = new PrintWriter(new BufferedWriter(new FileWriter(filename)), true);
        } catch (Exception e) {

        }

    }
}