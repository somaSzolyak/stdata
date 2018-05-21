import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AppMain {
    public static void main(String[] args) throws IOException {

        String path = "telekom_anonym";
        File file = new File(path);
        //String keySearcherRegex = "\"[a-zA-Z_0-9]+\":";
        String keyValueRegex = "\"[a-zA-Z_0-9]+\":\"";
        //String keyObjectRegex = "\"[a-zA-Z_0-9]+\":\\{";

        // '~' is the predefined (by me) character that's going to be replaced with the specific key I search values for
        String keyWithValueRegex = "\"~\":\"[a-zA-Z_0-9]+\"";
        TextFileReader textFileReader = new TextFileReader(file);
        ArrayList<String> redundantKeyList = new ArrayList<String>();
        redundantKeyList.add("fbunique");
        redundantKeyList.add("email");
        redundantKeyList.add("ucounted");
        redundantKeyList.add("id");
        redundantKeyList.add("last_name");
        redundantKeyList.add("first_name");
        redundantKeyList.add("parent");
        KeyContainer keyContainer = new KeyContainer(new HashMap<String, Integer>(), redundantKeyList);
        ValueRegex valueRegex = new ValueRegex(keyWithValueRegex, 1);
        KeyRegex keyRegexForValue = new KeyRegex(keyValueRegex, 1, 3, valueRegex);
        ArrayList<UniqueValueListForKey> uniqueValueListForKeys = new ArrayList<UniqueValueListForKey>();
        FileAnalyzer fileAnalyzerForValues = new FileAnalyzer(keyContainer, keyRegexForValue, textFileReader, uniqueValueListForKeys);

        fileAnalyzerForValues.getKeysInFile();
        fileAnalyzerForValues.keyFrequencyInFile();
        fileAnalyzerForValues.report();

        fileAnalyzerForValues.getUniqueValuesForKeysInFile();
        fileAnalyzerForValues.valueReport();
    }
}
