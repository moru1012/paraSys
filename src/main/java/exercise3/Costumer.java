package exercise3;

import java.util.Random;

public class Costumer {
    private int numberOfBaskets;
    private int id;

    public Costumer(int id) {
        this(id, getRandomNumberInRange(2,5));
    }

    public Costumer(int id, int numberOfBaskets) {
        this.id = id;
        this.numberOfBaskets = numberOfBaskets;
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
}
