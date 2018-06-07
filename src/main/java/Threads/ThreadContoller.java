package Threads;

import FileAnalytics.FileAnalyzer;
import FileAnalyticsTemplate.FileAnalyzerTemplate;
import Model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class ThreadContoller {
    private Callable<List<KeyHolder>> task = () -> {
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
        ArrayList<String> redundantKeyList = new ArrayList<>();
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
        FileAnalyzer fileAnalyzerForValues = new FileAnalyzer(keyContainer2, keyRegexForValue2, textFileReader,
                keyHolders2, stringOnlyKeys, new HashMap<>());

//        fileAnalyzerTemplateForValues.getKeysInFile(1000000);
//        fileAnalyzerTemplateForValues.keyFrequencyInFile();
//        fileAnalyzerTemplateForValues.discardRedundantKeys();
//        //fileAnalyzerTemplateForValues.report();
//        timer(startTime, "1st file reading time: ");
//
//        fileAnalyzerTemplateForValues.getUniqueValuesForKeysInFile(1000000);
//        //fileAnalyzerTemplateForValues.valueReport();
//        timer(startTime, "program execution time: ");
//
//        textFileReader.reset();
//        startTime = System.nanoTime();

        fileAnalyzerForValues.getKeysInFile(1000000);
        fileAnalyzerForValues.keyFrequencyInFile();
        fileAnalyzerForValues.discardRedundantKeys();
        timer(startTime, "1st file reading time for fileAnalyzerSingleThread: ");

        fileAnalyzerForValues.getUniqueValuesForKeysInFile(1000000);
        //fileAnalyzerForValues.valueReport();
        timer(startTime, "program execution time for fileAnalyzerSingleThread: ");

        return fileAnalyzerForValues.getKeyHolderList();
    };

    public void fileAnalytics(int threadNumber) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);
        Future<List<KeyHolder>> future = executor.submit(task);

        System.out.println("future done? " + future.isDone());

        List<KeyHolder> result = future.get();

        System.out.println("future done? " + future.isDone());
        System.out.print("result: " + result);
        executor.shutdownNow();
    }

    private static void timer(long startTime, String s) {
        long endTime;
        long totalTime;
        endTime = System.nanoTime();
        totalTime = (endTime - startTime)/(long) 1000000000;
        System.out.println(s + totalTime +"-seconds or " + totalTime/60 + "-minute(s)\n");
    }
    // TODO: 2018.06.07. this creates multiple FAMTs and those read the file
    // TODO: 2018.06.07. all FAMT instances must use the same TextFileReader
    // TODO: 2018.06.07. need some function that copies the results of the threads together
    // TODO: 2018.06.07. print the results
    // TODO: 2018.06.07. stop and restart individual threads
}
