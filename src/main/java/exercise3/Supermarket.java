package exercise3;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.Semaphore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Supermarket {

    private Semaphore semaphore;

    private static Logger LOG = LoggerFactory.getLogger(Supermarket.class);

    private int machines;
    private static Random generator;

    public Supermarket(int min, int max) {
        this(getRandomNumberInRange(min, max));
    }

    public Supermarket(int machines) {
        this.machines = machines;
        this.semaphore = new Semaphore(this.machines, true);
        generator = new Random();
    }

    public void enter(Costumer costumer) {
        // Check condition
        LOG.debug("Costumer " + costumer.getId() + " entered.");
        try {
            this.semaphore.acquire();
            LOG.debug("Costumer " + costumer.getId() + " aquired machine.");
        } catch (InterruptedException ie) {
            LOG.error("InterruptedException", ie);
        }
        // costumer does his stuff
        LOG.debug("Costumer " + costumer.getId() + " uses machine.");
        for (int i = 1; i <= costumer.getNumberOfBaskets(); i++) {
            try {
                int usageTime = getRandomNumberInRange(3, 7);
                LOG.debug("Costumer " + costumer.getId() + " uses the machine for " + usageTime + " Seconds for basket " + i + "/" + costumer.getNumberOfBaskets() + ".");
                Thread.sleep(Duration.ofSeconds(usageTime).toMillis());
            } catch (InterruptedException ie) {
                LOG.error("InterruptedException", ie);
            }
        }
    }

    public void leave(Costumer costumer) {
        LOG.debug("Costumer " + costumer.getId() + " releases machine.");
        semaphore.release();
        LOG.debug("Costumer " + costumer.getId() + " finished.");
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return generator.nextInt((max - min) + 1) + min;
    }
}
