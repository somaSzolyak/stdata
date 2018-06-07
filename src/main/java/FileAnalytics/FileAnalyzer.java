package FileAnalytics;

import Model.*;

import java.io.IOException;
import java.util.*;

public class FileAnalyzer {
    private KeyContainer keyContainer;
    private KeyRegex keyRegex;
    private Map<String, Double> keyFrequency;
    private List<KeyHolder> keyHolderList;
    private List<String> stringOnlyKeys;
    private TextFileReader textFileReader;
    private int lineCount = 0;

    public FileAnalyzer(KeyContainer keyContainer, KeyRegex keyRegex, TextFileReader textFileReader,
                        List<KeyHolder> keyHolderList, List<String> stringOnlyKeys, Map<String, Double> keyFrequency) {
        this.keyContainer = keyContainer;
        this.keyRegex = keyRegex;
        this.keyFrequency = keyFrequency;
        this.textFileReader = textFileReader;
        this.keyHolderList = keyHolderList;
        this.stringOnlyKeys = stringOnlyKeys;
    }

    public List<KeyHolder> getKeyHolderList() {
        return keyHolderList;
    }

    private void getKeysInLine(String line){
        keyContainer.add(keyRegex.getMatchesInString(line));
    }

    public void getKeysInFile() throws IOException {
        String line;
        while ((line = textFileReader.readNextLine()) != null) {
            getKeysInLine(line);
            lineCount++;
        }
        textFileReader.close();
    }

    public void getKeysInFile(int lineNumber) throws IOException {
        String line;
        while ((line = textFileReader.readNextLine()) != null & lineNumber != 0) {
            getKeysInLine(line);
            lineCount++;
            lineNumber--;
        }
        textFileReader.close();
    }

    public void getUniqueValuesForKeysInFile() throws IOException {
        textFileReader.reset();
        String line;
        while ((line = textFileReader.readNextLine()) != null) {
            getUniqueValuesForKeysInLine(line);
        }
    }

    private void getUniqueValuesForKeysInLine(String line) {
        ValueRegex valueRegex = keyRegex.getValueRegex();
        KeyHolder keyHolder;
        for (KeyHolder key : keyContainer.getData()) {
            valueRegex.setKey(key.getKeyName());
            if (isNewKeyValue(key.getKeyName()) == null) {
                keyHolder = new KeyHolder(key.getKeyName(), stringOnlyKeys.contains(key.getKeyName()));
                keyHolderList.add(keyHolder);
            } else keyHolder = isNewKeyValue(key.getKeyName());

            keyHolder.add(key.getKeyName(), valueRegex.getMatchesInString(line));
        }
    }

    private KeyHolder isNewKeyValue(String key) {
        for (KeyHolder keyHolder : keyHolderList) {
            if (keyHolder.getKeyName().equals(key)) {
                return keyHolder;
            }
        }
        return null;
    }

    public void getUniqueValuesForKeysInFile(int lineNumber) throws IOException {
        textFileReader.reset();
        String line;
        while ((line =textFileReader.readNextLine()) != null & lineNumber != 0) {
            getUniqueValuesForKeysInLine(line);
            lineNumber--;
        }
    }

    public void keyFrequencyInFile() {
        List<KeyHolder> keys = keyContainer.getData();
        keys.forEach(k -> keyFrequency.put(k.getKeyName(), (double) k.getKeyOccurrenceCounter()/lineCount));
    }

    public void discardRedundantKeys() {
        discardRedundantKeyByFrequency();
        discardRedundantKeyByPredeterminedList();
    }

    private void discardRedundantKeyByPredeterminedList() {
        List<String> redundantKeys = keyContainer.getRedundantKeyList();
        for (String key : redundantKeys) {
            keyContainer.remove(key);
        }
    }

    private void discardRedundantKeyByFrequency() {
        List<KeyHolder> tmpContainer = new ArrayList<>();
        Map<String, Double> tmpFrequency = new HashMap<>();
        keyContainer.getData().stream()
                .filter(keyHolder ->
                        keyFrequency.get(keyHolder.getKeyName()) > 0.009
                                & keyFrequency.get(keyHolder.getKeyName()) < 0.9)
                .forEach(keyHolder -> {
                    tmpContainer.add(keyHolder);
                    tmpFrequency.put(keyHolder.getKeyName(), keyFrequency.get(keyHolder.getKeyName()));
                });
        keyContainer.addReducedKeyChain(tmpContainer);
        keyFrequency.clear();
        keyFrequency.putAll(tmpFrequency);
    }

    public void valueReport() {
        System.out.println("\nValueReport\n");
        System.out.println("Number of keys: " + keyHolderList.size());
        System.out.println(Arrays.toString(keyContainer.getData().stream().map(KeyHolder::getKeyName).toArray()));
        System.out.println(keyFrequency);
        System.out.println(" ");
        for (KeyHolder keyHolder : keyHolderList) {
            System.out.println(keyHolder.getKeyName());
            System.out.println(keyHolder.getValueMap().getUniqueValueOccurrence().toString());
            System.out.println("\n");
        }
        System.out.println("lineCount: " + lineCount);
    }
}
