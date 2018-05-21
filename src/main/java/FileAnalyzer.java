import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileAnalyzer {
    private KeyContainer keyContainer;
    private KeyRegex keyRegex;
    private Map<String, Double> keyFrequency;
    private List<UniqueValueListForKey> uniqueValueListForKeys;
    private TextFileReader textFileReader;
    private int lineCount = 0;

    public FileAnalyzer(KeyContainer keyContainer, KeyRegex keyRegex, TextFileReader textFileReader, List<UniqueValueListForKey> uniqueValueListForKeys) {
        this.keyContainer = keyContainer;
        this.keyRegex = keyRegex;
        this.textFileReader = textFileReader;
        this.uniqueValueListForKeys = uniqueValueListForKeys;
    }

    private void getKeysInLine(String line){
        keyContainer.add(keyRegex.getMatchesInString(line));
    }

    public void getKeysInFile() throws IOException {
        String line;
        while ((line = textFileReader.ReadNextLine()) != null) {
            getKeysInLine(line);
            lineCount++;
        }
        textFileReader.close();
    }

    public void getUniqueValuesForKeysInFile() throws IOException {
        textFileReader.reset();
        String line;
        while ((line = textFileReader.ReadNextLine()) != null) {
            getUniqueValuesForKeysInLine(line);
        }
    }

    private void getUniqueValuesForKeysInLine(String line) {
        ValueRegex valueRegex = keyRegex.getValueRegex();
        UniqueValueListForKey uniqueValueListForKey;
        for (String key : keyContainer.getData().keySet()) {
            valueRegex.setKey(key);
            if (isNewKeyValue(key) != null) {
                uniqueValueListForKey = isNewKeyValue(key);
            } else {
                uniqueValueListForKey = new UniqueValueListForKey(key);
                uniqueValueListForKeys.add(uniqueValueListForKey);
            }
            uniqueValueListForKey.add(key, valueRegex.getMatchesInString(line));
        }
    }

    private UniqueValueListForKey isNewKeyValue(String key) {
        for (UniqueValueListForKey uniqueValueListForKey : uniqueValueListForKeys) {
            if (uniqueValueListForKey.getKeyName().equals(key)) {
                return uniqueValueListForKey;
            }
        }
        return null;
    }

    public void keyFrequencyInFile() {
        Set<String> keys = keyContainer.getData().keySet();
        keyFrequency = new HashMap<String, Double>();
        for (String key : keys) {
            keyFrequency.put(key, (double) keyContainer.getData().get(key)/lineCount);
        }
    }

    public void discardRedundantKeys() {
        discardRedundantKeyByFrequency();
        discardRedundantKeyByUselessValue();
        discardRedundantKeyByPredeterminedList();
    }

    private void discardRedundantKeyByPredeterminedList() {
        List<String> redundantKeys = keyContainer.getRedundantKeyList();
        for (String key : redundantKeys) {
            keyContainer.remove(key);
        }
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
        System.out.println("\n");
    }

    public void valueReport() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("report"));
        writer.write("\nValueReport");
        writer.write(uniqueValueListForKeys.size());
        for (UniqueValueListForKey uniqueValueListForKey :
                uniqueValueListForKeys) {
            writer.write(uniqueValueListForKey.getKeyName());
            writer.write(uniqueValueListForKey.getValueMap().getUniqueValueOccurrence().toString());
            writer.write("\n");
        }
    }


    //todo decide if a key holds no information aka key is redundant

    //todo get specific key value from JSON

    //todo choose relevant keys from not redundant keys
}
