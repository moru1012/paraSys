package exercise4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

// Management
public class Supermarket {

    private final int max_costumers;
    private final int machines;
    private volatile BlockingQueue<Costumer> queue;
    private volatile List<Automat> automats;

    private static Logger LOG = LoggerFactory.getLogger(Supermarket.class);

    public Supermarket(ExecutorService executorService, int machines, int max_costumers) {
        this.machines = machines;
        this.max_costumers = max_costumers;
        this.queue = new PriorityBlockingQueue<>();
        automats = new ArrayList<>();
        for (int i = 0; i < this.machines; i++) {
            Automat automat = new Automat(this);
            automats.add(automat);
            executorService.submit(automat);
        }
    }

    public void enter(Costumer costumer) {
        // Check condition
        LOG.debug("Costumer " + costumer.getId() + " entered.");
        if (costumer.isGold()) {
            LOG.debug("Costumer " + costumer.getId() + " with gold arrived and gets gold treatment.");
        }
        queue.offer(costumer);
    }

    public Costumer acquireACostumer() {
        try {
            return queue.poll(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ie) {
            LOG.error("InterruptedException", ie);
        }
        return null;
    }

    public boolean close() {
        if (!queue.isEmpty()) {
            return false;
        } else {
            for (Automat a : automats) {
                if (a.isInUse()) {
                    return false;
                } else {
                    a.close();
                }
            }
        }
        return true;
    }
}
