import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FileAnalyzer {
    private KeyContainer keyContainer;
    private KeyRegex keyRegex;
    private Map<String, Double> keyFrequency;
    private int lineCount = 0;

    public FileAnalyzer(KeyContainer keyContainer, KeyRegex keyRegex) {
        this.keyContainer = keyContainer;
        this.keyRegex = keyRegex;
    }

    private void getKeysInLine(String line){
        keyContainer.add(keyRegex.getMatchesInString(line));
    }

    public void getKeysInFile(TextFileReader textFileReader) throws IOException {
        String line;
        while ((line= textFileReader.ReadNextLine()) != null) {
            getKeysInLine(line);
            lineCount++;
        }
        System.out.println(keyContainer.getData());
        // System.out.println(keyContainer.toString());
        System.out.println(lineCount);
    }

    public void keyFrequencyInFile() {
        Set<String> keys = keyContainer.getData().keySet();
        keyFrequency = new HashMap<String, Double>();
        for (String key : keys) {
            keyFrequency.put(key, (double) keyContainer.getData().get(key)/lineCount);
        }
        System.out.println(keyFrequency);
    }



}
