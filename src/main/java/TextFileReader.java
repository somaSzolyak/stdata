import java.io.*;
import java.util.Scanner;

public class TextFileReader {
    private BufferedReader bufferedReader;

    public TextFileReader(File file) throws FileNotFoundException {
        bufferedReader = new BufferedReader(new FileReader(file));
    }

    public String ReadNextLine () throws IOException {
        return bufferedReader.readLine();
    }
}
