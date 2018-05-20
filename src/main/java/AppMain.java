import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AppMain {
    public static void main(String[] args) throws IOException {

        String path = "telekom_anonym";
        File file = new File(path);
        String keySearcherRegex = "\"[a-zA-Z0-9]+\":";
        String keyValueRegex = "\"[a-zA-Z0-9]+\":\"";
        String keyObjectRegex = "\"[a-zA-Z0-9]+\":\\{";
        TextFileReader textFileReader = new TextFileReader(file);
        KeyContainer keyContainer = new KeyContainer(new HashMap<String, Integer>());
        KeyRegex keyRegex = new KeyRegex(keySearcherRegex, 1, 2);
        KeyRegex keyRegexForValue = new KeyRegex(keyValueRegex, 1, 3);
        KeyRegex keyRegexForObject = new KeyRegex(keyObjectRegex, 1, 3);
        FileAnalyzer fileAnalyzer = new FileAnalyzer(keyContainer, keyRegex);
        FileAnalyzer fileAnalyzerForValues = new FileAnalyzer(keyContainer, keyRegexForValue);
        FileAnalyzer fileAnalyzerForObjects = new FileAnalyzer(keyContainer, keyRegexForObject);

        fileAnalyzerForValues.getKeysInFile(textFileReader);
        fileAnalyzerForValues.keyFrequencyInFile();
        fileAnalyzerForValues.report();
    }
}
