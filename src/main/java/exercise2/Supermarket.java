package exercise2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Supermarket {

    final Lock lock = new ReentrantLock();
    final Condition notAvailable = lock.newCondition();

    private static Logger LOG = LoggerFactory.getLogger(Supermarket.class);

    private int machines;
    private static Random generator;

    public Supermarket(int min, int max) {
        this(getRandomNumberInRange(min, max));
    }

    public Supermarket(int machines) {
        this.machines = machines;
        generator = new Random();
    }

    public void enter(Costumer costumer) {
        // Check condition
        LOG.debug("Costumer " + costumer.getId() + " entered.");
        lock.lock();
        try {
            while (machines == 0) {
                try {
                    LOG.debug("Costumer " + costumer.getId() + " waits.");
                    notAvailable.await();
                    LOG.debug("Costumer " + costumer.getId() + " resumes.");
                } catch (InterruptedException ie) {
                    LOG.error("InterruptedException", ie);
                }
            }
            // Take resource
            machines--;
            // just to be sure
            assert machines >= 0;
        } finally {
            lock.unlock();
        }
        LOG.debug("Costumer " + costumer.getId() + " takes machine.");
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
        lock.lock();
        try {
            // free resource
            LOG.debug("Costumer " + costumer.getId() + " releases machine.");
            machines++;
            // wake others up
            LOG.debug("Costumer " + costumer.getId() + " finished.");
            notAvailable.signal();
        } finally {
            lock.unlock();
        }
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return generator.nextInt((max - min) + 1) + min;
    }
}
