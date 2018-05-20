package exercise4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

// Consumer
public class Automat implements Runnable {

    private static Logger LOG = LoggerFactory.getLogger(Automat.class);
    private Supermarket supermarket;

    private volatile boolean isRunning = true;

    private ThreadLocalRandom generator;
    private Costumer costumer;

    public Automat(Supermarket supermarket) {
        this.supermarket = supermarket;
        this.generator = ThreadLocalRandom.current();
    }

    public void close() {
        cancel();
        if (supermarket != null) {
            this.supermarket = null;
        }
    }

    public void cancel() {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            costumer = supermarket.acquireACostumer();

            if (costumer != null) {
                LOG.debug("Costumer " + costumer.getId() + " aquired machine.");
                // costumer does his stuff
                LOG.debug("Costumer " + costumer.getId() + " uses machine.");
                for (int i = 1; i <= costumer.getNumberOfBaskets(); i++) {
                    try {
                        int usageTime = getRandomNumberInRange(3, 7);
                        LOG.debug(
                                "Costumer "
                                        + costumer.getId()
                                        + " uses the machine for "
                                        + usageTime
                                        + " Seconds for basket "
                                        + i
                                        + "/"
                                        + costumer.getNumberOfBaskets()
                                        + ".");
                        Thread.sleep(Duration.ofSeconds(usageTime).toMillis());
                    } catch (InterruptedException ie) {
                        LOG.error("InterruptedException", ie);
                    }
                }
                LOG.debug("Costumer " + costumer.getId() + " finished.");
                costumer = null;
            }
        }
    }

    private int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return this.generator.nextInt((max - min) + 1) + min;
    }

    public boolean isInUse() {
        return costumer != null;
    }
}
