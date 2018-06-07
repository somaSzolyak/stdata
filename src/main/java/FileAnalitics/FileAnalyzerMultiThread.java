package FileAnalitics;

import Model.KeyContainer;
import Model.KeyHolder;
import Model.KeyRegex;
import Model.TextFileReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FileAnalyzerMultiThread extends FileAnalyzer {

    public FileAnalyzerMultiThread(KeyContainer keyContainer, KeyRegex keyRegex, TextFileReader textFileReader,
                                   List<KeyHolder> keyHolderList, List<String> stringOnlyKeys,
                                   Map<String, Double> keyFrequency) {
        super(keyContainer, keyRegex, textFileReader, keyHolderList, stringOnlyKeys, keyFrequency);
    }

    @Override
    public void getKeysInFile() throws IOException {

    }

    @Override
    public void getKeysInFile(int lineNumber) throws IOException {

    }

    @Override
    public void getUniqueValuesForKeysInFile() throws IOException {

    }

    @Override
    public void getUniqueValuesForKeysInFile(int lineNumber) throws IOException {

    }

    @Override
    public void keyFrequencyInFile() {

    }

    @Override
    public void discardRedundantKeys() {

    }

    @Override
    public void valueReport() {

    }
}
