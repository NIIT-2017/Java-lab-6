import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        ExecutorService exec = Executors.newFixedThreadPool(5);
        int j = 0;

        while (j < 5) {
            j++;
            exec.execute(new Client("127.0.0.1",8081, j));
            Thread.sleep(10);
        }

        exec.shutdown();
    }
}