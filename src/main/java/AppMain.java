import Threads.ThreadContoller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AppMain {
    public static void main(String[] args) throws IOException {
        ThreadContoller threadContoller = new ThreadContoller();
        try {
            threadContoller.fileAnalytics(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
