import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class AppMain {
    public static void main(String[] args) throws FileNotFoundException {

        //todo should initialize TextFileReader and FileAnalyzer and call FileAnalyzer to analyze
        String path = "telekom_anonym";
        File file = new File(path);
        String keySearcherRegex = "\"[a-zA-Z]+\":";
        TextFileReader textFileReader = new TextFileReader(file);
        KeyContainer keyContainer = new KeyContainer(new HashMap<String, Integer>());
        KeyRegex keyRegex = new KeyRegex(keySearcherRegex, 1, 2);
        FileAnalyzer fileAnalyzer = new FileAnalyzer(keyContainer, keyRegex);

        fileAnalyzer.getKeysInLine(textFileReader.ReadNextLine());
        System.out.println(keyContainer.getKeysInJSON());
    }
}
