package one.autremel.fearsome.network.ping;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Setter;

public class TimeSynchronizer {
    private static long delta;
    private static boolean isDeltaCalculated;

    @Setter(AccessLevel.PACKAGE)
    private static long t1;

    public static long toLocalTime(long serverTimestamp) {
        Preconditions.checkState(isDeltaCalculated);
        return serverTimestamp - delta;
    }

    static void calculateDelta(PingPayload payload) {
        var t2 = System.currentTimeMillis();
        delta = payload.senderTimestamp() - (t1 + t2) / 2;
        isDeltaCalculated = true;
    }

    public static void reset() {
        isDeltaCalculated = false;
    }
}
