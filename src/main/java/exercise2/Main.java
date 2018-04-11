package exercise2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        int type = 3;
        LOG.debug("Starting Exercise 2." + (type+1));
        new Exercise2(type);
    }
}
