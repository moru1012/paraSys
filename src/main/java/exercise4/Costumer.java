package exercise4;

import java.util.Random;

public class Costumer implements Comparable<Costumer> {
    private int numberOfBaskets;
    private int id;
    private boolean gold;

    public Costumer(int id) {
        this(id, getRandomNumberInRange(2, 5));
    }

    public Costumer(int id, int numberOfBaskets) {
        this.id = id;
        this.numberOfBaskets = numberOfBaskets;
        if (getRandomNumberInRange(0, 100) > 70) {
            this.gold = true;
        }
    }

    public int getNumberOfBaskets() {
        return numberOfBaskets;
    }

    public int getId() {
        return id;
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random generator = new Random();
        return generator.nextInt((max - min) + 1) + min;
    }

    public boolean isGold() {
        return gold;
    }

    public void setGold(boolean gold) {
        this.gold = gold;
    }

    @Override
    public int compareTo(Costumer other) {
        if (this.isGold() && other.isGold()) {
            return Integer.compare(this.getId(), other.getId());
        } else if (this.isGold()) {
            return -1;
        } else if (other.isGold()) {
            return 1;
        } else {
            return Integer.compare(this.getId(), other.getId());
        }
    }
}
