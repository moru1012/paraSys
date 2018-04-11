package exercise1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Exercise1 {

    private static Logger LOG = LoggerFactory.getLogger(Exercise1.class);

    private static int NUMBER_OF_COSTUMERS = 30;
    private static int NUMBER_OF_MACHINES = 3;
    private static int TIME_BETWEEN_KUNDE = 5;

    private final Supermarket supermarket;
    public Exercise1() {
        // one Supermarket
        this.supermarket = new Supermarket(NUMBER_OF_MACHINES);
        this.startRun();
    }

    private synchronized void startRun() {
        // Costumer generation
        int i = 1;
        while (i <= NUMBER_OF_COSTUMERS) {
            // New Costumer gets generated
            Costumer newCostumer = new Costumer(i);
            DepositRun depositRun = new DepositRun(this.supermarket, newCostumer);
            new Thread(depositRun).start();
            // Wait for next Ccstumer
            try {
                wait(Duration.ofSeconds(TIME_BETWEEN_KUNDE).toMillis());
            } catch (InterruptedException ie) {
                LOG.error("InterruptedException", ie);
            }
            i++;
        }
    }
}
