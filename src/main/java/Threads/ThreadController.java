package Threads;

import FileAnalytics.FileAnalyzer;
import Model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class ThreadController {
    // to find everything: "\"[\\s\\-\\w]+\":\""
    //String keyObjectRegex = "\"[a-zA-Z_0-9]+\":\\{";
    // '~' is the predefined (by me) character that's going to be replaced with the specific key I search values for
    private String keyValueRegex = "\"[\\w]+\":\"";
    private String keyWithValueRegex = "\"~\":\"[a-zA-Z_0-9]+\"";

    private TextFileReader textFileReader;

    private ArrayList<String> redundantKeyList;
    private List<String> stringOnlyKeys;

    private ValueRegex valueRegex;
    private KeyRegex keyRegexForValue;

    private KeyContainer keyContainer;
    private ArrayList<KeyHolder> keyHolders;

    public ThreadController () throws FileNotFoundException {
        String path = "telekom_anonym";
        File file = new File(path);
        textFileReader = new TextFileReader(file);
        redundantKeyList = new ArrayList<>();
        stringOnlyKeys = new ArrayList<String>();

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

        stringOnlyKeys.add("gender");
        stringOnlyKeys.add("locale");
    }

    private Callable<KeyContainer> gettingKeys = () -> {
        System.out.println(Thread.currentThread().getName());

        valueRegex = new ValueRegex(keyWithValueRegex, 1);
        keyRegexForValue = new KeyRegex(keyValueRegex, 1, 3, valueRegex);
        KeyContainer keyContainer = new KeyContainer(new ArrayList<>(), redundantKeyList, stringOnlyKeys);
        List<KeyHolder> keyHolders = new ArrayList<>();
        FileAnalyzer fileAnalyzerForValues = new FileAnalyzer(keyContainer, keyRegexForValue, textFileReader,
                keyHolders, stringOnlyKeys, new HashMap<>());

        fileAnalyzerForValues.getKeysInFile(1000000);
        fileAnalyzerForValues.keyFrequencyInFile();
        fileAnalyzerForValues.discardRedundantKeys();
        //timer(startTime, "1st file reading time for fileAnalyzerSingleThread: ");

        return fileAnalyzerForValues.getData();
    };

    private Callable<List<KeyHolder>> gettingUniqueValues = () -> {
        System.out.println(Thread.currentThread().getName() + " in gettingUniqueValues");

        valueRegex = new ValueRegex(keyWithValueRegex, 1);
        keyRegexForValue = new KeyRegex(keyValueRegex, 1, 3, valueRegex);
        KeyContainer keyContainer = this.keyContainer;
        ArrayList<KeyHolder> keyHolders = new ArrayList<>();
        FileAnalyzer fileAnalyzerForValues = new FileAnalyzer(keyContainer, keyRegexForValue, textFileReader,
                keyHolders, stringOnlyKeys, new HashMap<>());
        fileAnalyzerForValues.getUniqueValuesForKeysInFile(1000000);
        //fileAnalyzerForValues.valueReport();
        return fileAnalyzerForValues.getKeyHolderList();
    };

    public void fileAnalytics(int poolNumber) throws ExecutionException, InterruptedException, IOException {
        long startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(poolNumber);

        ArrayList<Future<KeyContainer>> futuresForKeys = new ArrayList<>();

        futuresForKeys.add(executor.submit(gettingKeys));
        futuresForKeys.add(executor.submit(gettingKeys));

        KeyContainer keyResultGatherer = new KeyContainer(new ArrayList<>(), redundantKeyList, stringOnlyKeys);
        for (Future<KeyContainer> future:futuresForKeys) {
            fillingKeyResultGatherer(keyResultGatherer, future);
        }

        keyContainer = keyResultGatherer;
        textFileReader.close();
        textFileReader.reset();

        ArrayList<Future<List<KeyHolder>>> futureForValues = new ArrayList<>();

        futureForValues.add(executor.submit(gettingUniqueValues));
        futureForValues.add(executor.submit(gettingUniqueValues));

        List<KeyHolder> resultGatherer = new ArrayList<>();

        for (Future<List<KeyHolder>> future: futureForValues){
            fillingResultGatherer(resultGatherer, future);
        }

        System.out.print("\nresult:\n\n");
        for (KeyHolder keyHolder : resultGatherer) {
            System.out.println(keyHolder.getKeyName());
            System.out.println(keyHolder.getValueMap().getUniqueValueOccurrence().toString());
            System.out.println("\n");
        }

        timer(startTime, "program execution time for fileAnalytics: ");

        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }
    }

    private synchronized void fillingKeyResultGatherer(KeyContainer keyResultGatherer, Future<KeyContainer> future) throws InterruptedException, ExecutionException {
        KeyContainer result = future.get();
        keyResultGatherer.add(result);
    }

    private synchronized void fillingResultGatherer(List<KeyHolder> resultGatherer, Future<List<KeyHolder>> future) throws InterruptedException, ExecutionException {
        List<KeyHolder> result = future.get();
        resultGatherer.addAll(result);
    }

    private static void timer(long startTime, String s) {
        long endTime;
        long totalTime;
        endTime = System.nanoTime();
        totalTime = (endTime - startTime)/(long) 1000000000;
        System.out.println(s + totalTime +"-seconds or " + totalTime/60 + "-minute(s)\n");
    }
    // TODO: 2018.06.07. this creates multiple fileAnalyzers and those read the file
    // TODO: 2018.06.07. all fileAnalyzer instances must use the same TextFileReader
    // TODO: 2018.06.07. need some function that copies the results of the threads together
    // TODO: 2018.06.07. print the results
    // TODO: 2018.06.07. stop and restart individual threads
}