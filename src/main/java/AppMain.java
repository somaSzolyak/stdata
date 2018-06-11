import Threads.ThreadController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AppMain {
    public static void main(String[] args) throws IOException {
        ThreadController threadController = new ThreadController();
        try {
            threadController.fileAnalytics(2);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
