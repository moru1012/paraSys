package exercise4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Exercise4 {

    private static Logger LOG = LoggerFactory.getLogger(Exercise4.class);

    private static ExecutorService executorService;

    private static int NUMBER_OF_COSTUMERS = 30;
    private static int NUMBER_OF_MACHINES = 3;
    private static int TIME_BETWEEN_KUNDE = 5;

    private final Supermarket supermarket;

    public Exercise4(int executor_Type) {
        initExecutorService(executor_Type);
        this.supermarket = new Supermarket(executorService, NUMBER_OF_MACHINES, NUMBER_OF_COSTUMERS);
        this.startRun();
    }

    // Producer
    private void startRun() {
        // Costumer generation
        int i = 1;
        while (i <= NUMBER_OF_COSTUMERS) {
            // New Costumer gets generated
            Costumer newCostumer = new Costumer(i);
            Runnable depositRun = () -> { // costumer goes in
                this.supermarket.enter(newCostumer);
            };
            executorService.submit(depositRun);
            // Wait for next Ccstumer
            try {
                Thread.sleep(Duration.ofSeconds(TIME_BETWEEN_KUNDE).toMillis());
            } catch (InterruptedException ie) {
                LOG.error("InterruptedException", ie);
            }
            i++;
        }
        LOG.debug("Supermarket closing...");
        while (!supermarket.close());
        LOG.debug("Supermarket closed.");
        executorService.shutdown();
    }

    private void initExecutorService(int executor_Type) {
        switch (executor_Type) {
            case 0:
                executorService = Executors.newCachedThreadPool();
                LOG.debug("Create Cached Thread Pool.");
                break;
            case 1:
                executorService = Executors.newFixedThreadPool(NUMBER_OF_MACHINES + NUMBER_OF_COSTUMERS);
                LOG.debug("Create Fixed Thread Pool.");
                break;
            case 2:
                executorService = Executors.newSingleThreadExecutor();
                LOG.debug("Create Single Thread Pool.");
                break;
            case 3:
                executorService = Executors.newWorkStealingPool();
                LOG.debug("Create Work Stealing Thread Pool.");
                break;
            default:
                executorService = Executors.newCachedThreadPool();
                LOG.debug("Create Cached Thread Pool.");
        }
    }
}
