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

    public FileAnalyzer(KeyContainer keyContainer, KeyRegex keyRegex, TextFileReader textFileReader, List<KeyHolder> keyHolderList, List<String> stringOnlyKeys) {
        this.keyContainer = keyContainer;
        this.keyRegex = keyRegex;
        this.textFileReader = textFileReader;
        this.keyHolderList = keyHolderList;
        this.stringOnlyKeys = stringOnlyKeys;
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

    public void getKeysInFile(int lineNumber) throws IOException {
        String line;
        while ((line = textFileReader.ReadNextLine()) != null & lineNumber != 0) {
            getKeysInLine(line);
            lineCount++;
            lineNumber--;
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

    public void getUniqueValuesForKeysInFile(int lineNumber) throws IOException {
        textFileReader.reset();
        String line;
        while ((line = textFileReader.ReadNextLine()) != null & lineNumber != 0) {
            getUniqueValuesForKeysInLine(line);
            lineNumber--;
        }
    }

    private void getUniqueValuesForKeysInLine(String line) {
        ValueRegex valueRegex = keyRegex.getValueRegex();
        KeyHolder keyHolder;
        for (KeyHolder key : keyContainer.getData()) {
            valueRegex.setKey(key.getKeyName());
            if (isNewKeyValue(key.getKeyName()) != null) {
                keyHolder = isNewKeyValue(key.getKeyName());
            } else {
                keyHolder = new KeyHolder(key.getKeyName(), stringOnlyKeys.contains(key.getKeyName()));
                keyHolderList.add(keyHolder);
            }
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

    public void keyFrequencyInFile() {
        // TODO: 2018.05.31. correct rest of the errors
        List<KeyHolder> keys = keyContainer.getData();
        keyFrequency = new HashMap<String, Double>();
        // is a good example if the lambda does the same thing as the foreach
        keys.forEach(k -> keyFrequency.put(k.getKeyName(), (double) k.getKeyOccurrenceCounter()/lineCount));
//        for (KeyHolder key : keys) {
//            keyFrequency.put(key.getKeyName(), (double) key.getKeyOccurrenceCounter()/lineCount);
//        }
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
        KeyContainer tmpContainer = new KeyContainer(new ArrayList<KeyHolder>(), keyContainer.getRedundantKeyList(), keyContainer.getStringOnlyKeys());
        Map<String, Double> tmpFrequency = new HashMap<String, Double>();
        keyContainer.getData().stream()
                .filter(keyHolder ->
                        keyFrequency.get(keyHolder.getKeyName()) > 0.009
                                & keyFrequency.get(keyHolder.getKeyName()) < 0.9)
                .forEach(keyHolder -> {
                    tmpContainer.add(keyHolder);
                    tmpFrequency.put(keyHolder.getKeyName(), keyFrequency.get(keyHolder.getKeyName()));
                });
        keyContainer = tmpContainer;
        keyFrequency.clear();
        keyFrequency.putAll(tmpFrequency);
    }

    public void report() {
        System.out.println(Arrays.toString(keyContainer.getData().stream().map(KeyHolder::getKeyName).toArray()));
        System.out.println(keyFrequency);
        System.out.println(keyRegex.getKeySearcherRegex());
        System.out.println(lineCount);
        System.out.println("\n");
    }

    public void valueReport() throws IOException {
        System.out.println("\nValueReport\n");
        System.out.println("Number of keys: " + keyHolderList.size());
        System.out.println(keyContainer.getData());
        System.out.println(keyFrequency);
        System.out.println(" ");
        for (KeyHolder keyHolder : keyHolderList) {
            System.out.println(keyHolder.getKeyName());
            System.out.println(keyHolder.getValueMap().getUniqueValueOccurrence().toString());
            System.out.println("\n");
        }
    }
}
