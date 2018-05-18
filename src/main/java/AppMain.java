import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class AppMain {
    public static void main(String[] args) {

        //todo should initialize TextFileReader and FileAnalyzer and call FileAnalyzer to analyze
        String path = "telekom_anonym";
        File file = new File(path);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }
        if (scanner.hasNext()){
            String line = scanner.nextLine();
            System.out.println(line);
        }
    }
}
