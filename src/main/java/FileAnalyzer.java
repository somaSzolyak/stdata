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
    }

    public void keyFrequencyInFile() {
        Set<String> keys = keyContainer.getData().keySet();
        keyFrequency = new HashMap<String, Double>();
        for (String key : keys) {
            keyFrequency.put(key, (double) keyContainer.getData().get(key)/lineCount);
        }
        discardRedundantKeys();
    }

    private void discardRedundantKeys() {
        discardRedundantKeyByFrequency();
        discardRedundantKeyByUselessValue();
    }

    private void discardRedundantKeyByUselessValue() {
        /// TODO: 2018.05.20. the body of this function is tricky as hell cause it has to be determined either at the 1st go through or at the second time
    }

    private void discardRedundantKeyByFrequency() {
        Map<String, Integer> tmpContainer = new HashMap<String, Integer>();
        Map<String, Double> tmpFrequency = new HashMap<String, Double>();
        for (String key : keyContainer.getData().keySet()) {
            if (keyFrequency.get(key) > 0.009 & keyFrequency.get(key) < 0.9) {
                tmpContainer.put(key, keyContainer.getData().get(key));
                tmpFrequency.put(key, keyFrequency.get(key));
            }
        }
        keyContainer.getData().clear();
        keyContainer.getData().putAll(tmpContainer);
        keyFrequency.clear();
        keyFrequency.putAll(tmpFrequency);
    }

    public void report() {
        System.out.println(keyContainer.getData());
        System.out.println(keyFrequency);
        System.out.println(keyRegex.getKeySearcherRegex());
        System.out.println(lineCount);
    }

    //todo decide if a key holds no information aka key is redundant

    //todo get specific key value from JSON

    //todo choose relevant keys from not redundant keys
}
