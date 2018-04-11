package exercise2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Exercise2 {

    private static Logger LOG = LoggerFactory.getLogger(Exercise2.class);

    private static ExecutorService executorService;

    private static int NUMBER_OF_COSTUMERS = 30;
    private static int NUMBER_OF_MACHINES = 3;
    private static int TIME_BETWEEN_KUNDE = 5;

    private final Supermarket supermarket;

    public Exercise2(int executor_Type) {
        switch (executor_Type) {
            case 0:
                this.executorService = Executors.newCachedThreadPool();
                LOG.debug("Create Cached Thread Pool.");
                break;
            case 1:
                this.executorService = Executors.newFixedThreadPool(NUMBER_OF_COSTUMERS);
                LOG.debug("Create Fixed Thread Pool.");
                break;
            case 2:
                this.executorService = Executors.newSingleThreadExecutor();
                LOG.debug("Create Single Thread Pool.");
                break;
            case 3:
                this.executorService = Executors.newWorkStealingPool();
                LOG.debug("Create Work Stealing Thread Pool.");
                break;
            default:
                this.executorService = Executors.newCachedThreadPool();
                LOG.debug("Create Cached Thread Pool.");
        }

        // one Supermarket
        this.supermarket = new Supermarket(NUMBER_OF_MACHINES);
        this.startRun();
    }

    private void startRun() {
        // Costumer generation
        int i = 1;
        while (i <= NUMBER_OF_COSTUMERS) {
            // New Costumer gets generated
            Costumer newCostumer = new Costumer(i);
            DepositRun depositRun = new DepositRun(this.supermarket, newCostumer);
            executorService.submit(depositRun);
            // Wait for next Ccstumer
            try {
                Thread.sleep(Duration.ofSeconds(TIME_BETWEEN_KUNDE).toMillis());
            } catch (InterruptedException ie) {
                LOG.error("InterruptedException", ie);
            }
            i++;
        }
        executorService.shutdown();
    }
}
