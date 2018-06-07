package FileAnalitics;

import Model.KeyContainer;
import Model.KeyHolder;
import Model.KeyRegex;
import Model.TextFileReader;

import java.io.IOException;
import java.util.*;

public abstract class FileAnalyzer {
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

    public abstract void getKeysInFile() throws IOException;
    public abstract void getKeysInFile(int lineNumber) throws IOException;
    public abstract void getUniqueValuesForKeysInFile() throws IOException;
    public abstract void getUniqueValuesForKeysInFile(int lineNumber) throws IOException;
    public abstract void keyFrequencyInFile();
    public abstract void discardRedundantKeys();
    public abstract void valueReport();

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
