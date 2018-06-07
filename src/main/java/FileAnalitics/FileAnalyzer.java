package FileAnalitics;

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

    private void getKeysInLine(String line){
        getKeyContainer().add(getKeyRegex().getMatchesInString(line));
    }

    public void getKeysInFile() throws IOException {
        String line;
        while ((line = getTextFileReader().readNextLine()) != null) {
            getKeysInLine(line);
            incrementLineCount();
        }
        getTextFileReader().close();
    }

    public void getKeysInFile(int lineNumber) throws IOException {
        String line;
        while ((line = getTextFileReader().readNextLine()) != null & lineNumber != 0) {
            getKeysInLine(line);
            incrementLineCount();
            lineNumber--;
        }
        getTextFileReader().close();
    }

    public void getUniqueValuesForKeysInFile() throws IOException {
        getTextFileReader().reset();
        String line;
        while ((line = getTextFileReader().readNextLine()) != null) {
            getUniqueValuesForKeysInLine(line);
        }
    }

    private void getUniqueValuesForKeysInLine(String line) {
        ValueRegex valueRegex = getKeyRegex().getValueRegex();
        KeyHolder keyHolder;
        for (KeyHolder key : getKeyContainer().getData()) {
            valueRegex.setKey(key.getKeyName());
            if (isNewKeyValue(key.getKeyName()) == null) {
                keyHolder = new KeyHolder(key.getKeyName(), getStringOnlyKeys().contains(key.getKeyName()));
                getKeyHolderList().add(keyHolder);
            } else keyHolder = isNewKeyValue(key.getKeyName());

            keyHolder.add(key.getKeyName(), valueRegex.getMatchesInString(line));
        }
    }

    private KeyHolder isNewKeyValue(String key) {
        for (KeyHolder keyHolder : getKeyHolderList()) {
            if (keyHolder.getKeyName().equals(key)) {
                return keyHolder;
            }
        }
        return null;
    }

    public void getUniqueValuesForKeysInFile(int lineNumber) throws IOException {
        getTextFileReader().reset();
        String line;
        while ((line = getTextFileReader().readNextLine()) != null & lineNumber != 0) {
            getUniqueValuesForKeysInLine(line);
            lineNumber--;
        }
    }

    public void keyFrequencyInFile() {
        List<KeyHolder> keys = getKeyContainer().getData();
        keys.forEach(k -> getKeyFrequency().put(k.getKeyName(), (double) k.getKeyOccurrenceCounter()/getLineCount()));
    }

    public void discardRedundantKeys() {
        discardRedundantKeyByFrequency();
        discardRedundantKeyByPredeterminedList();
    }

    private void discardRedundantKeyByPredeterminedList() {
        List<String> redundantKeys = getKeyContainer().getRedundantKeyList();
        for (String key : redundantKeys) {
            getKeyContainer().remove(key);
        }
    }

    private void discardRedundantKeyByFrequency() {
        List<KeyHolder> tmpContainer = new ArrayList<>();
        Map<String, Double> tmpFrequency = new HashMap<>();
        getKeyContainer().getData().stream()
                .filter(keyHolder ->
                        getKeyFrequency().get(keyHolder.getKeyName()) > 0.009
                                & getKeyFrequency().get(keyHolder.getKeyName()) < 0.9)
                .forEach(keyHolder -> {
                    tmpContainer.add(keyHolder);
                    tmpFrequency.put(keyHolder.getKeyName(), getKeyFrequency().get(keyHolder.getKeyName()));
                });
        getKeyContainer().addReducedKeyChain(tmpContainer);
        getKeyFrequency().clear();
        getKeyFrequency().putAll(tmpFrequency);
    }

    public void valueReport() {
        System.out.println("\nValueReport\n");
        System.out.println("Number of keys: " + getKeyHolderList().size());
        System.out.println(Arrays.toString(getKeyContainer().getData().stream().map(KeyHolder::getKeyName).toArray()));
        System.out.println(getKeyFrequency());
        System.out.println(" ");
        for (KeyHolder keyHolder : getKeyHolderList()) {
            System.out.println(keyHolder.getKeyName());
            System.out.println(keyHolder.getValueMap().getUniqueValueOccurrence().toString());
            System.out.println("\n");
        }
        System.out.println("lineCount: " + getLineCount());
    }

    public KeyContainer getKeyContainer() {
        return keyContainer;
    }

    public KeyRegex getKeyRegex() {
        return keyRegex;
    }

    public Map<String, Double> getKeyFrequency() {
        return keyFrequency;
    }

    public List<KeyHolder> getKeyHolderList() {
        return keyHolderList;
    }

    public List<String> getStringOnlyKeys() {
        return stringOnlyKeys;
    }

    public TextFileReader getTextFileReader() {
        return textFileReader;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void incrementLineCount() {
        lineCount++;
    }
}
