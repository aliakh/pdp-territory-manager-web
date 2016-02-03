import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Text file splitter.
 */
public class TextFileSplitter {

    static public final int MAX_FILE_SIZE = 5 * 1024 * 1024;

    static public void main(String[] args) throws IOException {
        TextFileSplitter splitter = new TextFileSplitter();
        if (args.length != 1) {
            throw new RuntimeException();
        }
        splitter.split(args[0]);
    }

    private void split(String path) throws IOException {
        int i = path.indexOf('.');
        if (i < 0) {
            throw new RuntimeException();
        }

        String name = path.substring(0, i + 1);
        String extension = path.substring(i);

        int j = 0;
        BufferedReader br = new BufferedReader(new FileReader(path));
        BufferedWriter bw = new BufferedWriter(new FileWriter(name + j + extension));
        int size = 0;

        String line;
        while ((line = br.readLine()) != null) {
            bw.write(line);
            bw.newLine();

            size += line.length();

            if (size > MAX_FILE_SIZE) {
                bw.close();
                j++;
                bw = new BufferedWriter(new FileWriter(name + j + extension));
                size = 0;
            }
        }

        br.close();
        bw.close();
    }
}
