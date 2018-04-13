package exercise3;

public class DepositRun implements Runnable{

    private final Supermarket supermarket;
    private final Costumer costumer;

    public DepositRun(Supermarket supermarket, Costumer costumer) {
        this.supermarket = supermarket;
        this.costumer = costumer;
    }

    @Override
    public void run() {
        // costumer goes in
        supermarket.enter(this.costumer);
        // costumer leaves
        supermarket.leave(this.costumer);
    }
}
