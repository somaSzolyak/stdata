import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppMain {
    public static void main(String[] args) throws IOException {

        String path = "telekom_anonym";
        File file = new File(path);
        //String keySearcherRegex = "\"[a-zA-Z_0-9]+\":";
        // to find everything: "\"[\\s\\-\\w]+\":\""
        String keyValueRegex = "\"[\\w]+\":\"";
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
        redundantKeyList.add("age_range");
        redundantKeyList.add("238");
        redundantKeyList.add("209");
        redundantKeyList.add("crchash");
        redundantKeyList.add("181");
        redundantKeyList.add("facebook");
        redundantKeyList.add("e");
        redundantKeyList.add("10 - Konkurencia - Telenort likeol");
        // redundant keys just for not till I figure out how to work with number values
        // probably will have to bind it to keys manually unfortunately
//        redundantKeyList.add("product_smartphone");
//        redundantKeyList.add("ugyfel_lakossagi");
//        redundantKeyList.add("akcio_irant_aktualisan_erdeklodo");
//        redundantKeyList.add("min");

        List<String> stringOnlyKeys = new ArrayList<String>();
        stringOnlyKeys.add("gender");
        stringOnlyKeys.add("locale");

        KeyContainer keyContainer = new KeyContainer(new HashMap<String, Integer>(), redundantKeyList);
        ValueRegex valueRegex = new ValueRegex(keyWithValueRegex, 1);
        KeyRegex keyRegexForValue = new KeyRegex(keyValueRegex, 1, 3, valueRegex);
        ArrayList<UniqueValueListForKey> uniqueValueListForKeys = new ArrayList<UniqueValueListForKey>();
        FileAnalyzer fileAnalyzerForValues = new FileAnalyzer(keyContainer, keyRegexForValue, textFileReader, uniqueValueListForKeys, stringOnlyKeys);

        fileAnalyzerForValues.getKeysInFile(1000000);
        fileAnalyzerForValues.keyFrequencyInFile();
        fileAnalyzerForValues.discardRedundantKeys();
        fileAnalyzerForValues.report();

        fileAnalyzerForValues.getUniqueValuesForKeysInFile(1000000);
        fileAnalyzerForValues.valueReport();
    }
}
