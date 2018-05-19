import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TextFileReader {
    private Scanner scanner;

    public TextFileReader(File file) throws FileNotFoundException {
        scanner = new Scanner(file);
    }

    public String ReadNextLine (){
        return scanner.hasNext() ? scanner.nextLine() : null;
    }
}
