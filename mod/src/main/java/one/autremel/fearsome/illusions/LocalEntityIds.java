package one.autremel.fearsome.illusions;

import java.util.concurrent.atomic.AtomicInteger;

public class LocalEntityIds {
    private static final AtomicInteger counter = new AtomicInteger(-1000);

    public static int next() {
        return counter.getAndDecrement();
    }
}
