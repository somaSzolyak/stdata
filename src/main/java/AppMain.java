import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppMain {
    public static void main(String[] args) {

        //todo should initialize TextFileReader and FileAnalyzer and call FileAnalyzer to analyze
        String path = "telekom_anonym";
        File file = new File(path);
        Scanner scanner = null;
        List<Integer> keyStartIndexes = new ArrayList<Integer>();
        String regex = "\"[a-zA-Z]+\":";
        try {
            scanner = new Scanner(file);
            if (scanner.hasNext()){
                String line = scanner.nextLine();
                System.out.println(line);
                printMatches(line, regex);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }


    }

    public static void printMatches(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        // Check all occurrences
        while (matcher.find()) {
            System.out.print("Start index: " + matcher.start());
            System.out.print(" End index: " + matcher.end());
            System.out.println(" Found: " + matcher.group());
        }
    }
}
