import FileAnalitics.FileAnalyzer;
import FileAnalitics.FileAnalyzerSingleThread;
import Model.*;
import FileAnaliticsTemplate.FileAnalyzerTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppMain {
    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();

        String path = "telekom_anonym";
        File file = new File(path);
        //String keySearcherRegex = "\"[a-zA-Z_0-9]+\":";
        // to find everything: "\"[\\s\\-\\w]+\":\""
        String keyValueRegex = "\"[\\w]+\":\"";
        //String keyObjectRegex = "\"[a-zA-Z_0-9]+\":\\{";

        // '~' is the predefined (by me) character that's going to be replaced with the specific key I search values for
        String keyWithValueRegex = "\"~\":\"[a-zA-Z_0-9]+\"";
        TextFileReader textFileReader = new TextFileReader(file);
        TextFileReader textFileReader2 = new TextFileReader(file);
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
        // redundant keys just for now till I figure out how to work with number values
        // probably will have to bind it to keys manually unfortunately
//        redundantKeyList.add("product_smartphone");
//        redundantKeyList.add("ugyfel_lakossagi");
//        redundantKeyList.add("akcio_irant_aktualisan_erdeklodo");
//        redundantKeyList.add("min");

        List<String> stringOnlyKeys = new ArrayList<String>();
        stringOnlyKeys.add("gender");
        stringOnlyKeys.add("locale");

        KeyContainer keyContainer = new KeyContainer(new ArrayList<>(), redundantKeyList, stringOnlyKeys);
        KeyContainer keyContainer2 = new KeyContainer(new ArrayList<>(), redundantKeyList, stringOnlyKeys);
        ValueRegex valueRegex = new ValueRegex(keyWithValueRegex, 1);
        KeyRegex keyRegexForValue = new KeyRegex(keyValueRegex, 1, 3, valueRegex);
        KeyRegex keyRegexForValue2 = new KeyRegex(keyValueRegex, 1, 3, valueRegex);
        ArrayList<KeyHolder> keyHolders = new ArrayList<>();
        ArrayList<KeyHolder> keyHolders2 = new ArrayList<>();
        FileAnalyzerTemplate fileAnalyzerTemplateForValues = new FileAnalyzerTemplate(keyContainer, keyRegexForValue,
                textFileReader, keyHolders, stringOnlyKeys);
        FileAnalyzer fileAnalyzerForValues = new FileAnalyzerSingleThread(keyContainer2, keyRegexForValue2, textFileReader,
                keyHolders2, stringOnlyKeys, new HashMap<>());

        fileAnalyzerTemplateForValues.getKeysInFile(1000000);
        fileAnalyzerTemplateForValues.keyFrequencyInFile();
        fileAnalyzerTemplateForValues.discardRedundantKeys();
        //fileAnalyzerTemplateForValues.report();
        timer(startTime, "1st file reading time: ");

        fileAnalyzerTemplateForValues.getUniqueValuesForKeysInFile(1000000);
        fileAnalyzerTemplateForValues.valueReport();
        timer(startTime, "program execution time: ");

        textFileReader.reset();
        startTime = System.nanoTime();

        fileAnalyzerForValues.getKeysInFile(1000000);
        fileAnalyzerForValues.keyFrequencyInFile();
        fileAnalyzerForValues.discardRedundantKeys();
        timer(startTime, "1st file reading time for fileAnalyzerSingleThread: ");

        fileAnalyzerForValues.getUniqueValuesForKeysInFile(1000000);
        fileAnalyzerForValues.valueReport();
        timer(startTime, "program execution time for fileAnalyzerSingleThread: ");
    }

    private static void timer(long startTime, String s) {
        long endTime;
        long totalTime;
        endTime = System.nanoTime();
        totalTime = (endTime - startTime)/(long) 1000000000;
        System.out.println(s + totalTime +"-seconds or " + totalTime/60 + "-minute(s)\n");
    }
    // TODO: 2018.05.31. make threads to be able to stop the flow of the program
}
