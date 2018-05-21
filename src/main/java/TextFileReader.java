import java.io.*;

public class TextFileReader {
    private BufferedReader bufferedReader;
    private File file;

    public TextFileReader(File file) throws FileNotFoundException {
        this.bufferedReader = new BufferedReader(new FileReader(file));
        this.file = file;
    }

    public String ReadNextLine () throws IOException {
        return bufferedReader.ready() ? bufferedReader.readLine() : null;
    }

    public void close() throws IOException {
        bufferedReader.close();
    }

    public void reset() throws FileNotFoundException {
        bufferedReader = new BufferedReader(new FileReader(file));
    }
}
