package one.autremel.fearsome.movement;

import com.google.common.base.Preconditions;
import one.autremel.fearsome.network.NetworkController;

public class ClimbingPreventer {
    private static boolean initialized;

    private static long preventedUntil;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(SetClimbingPreventedUntilPayload.ID, ClimbingPreventer::handleSetPayload);
    }

    public static boolean isPrevented() {
        return System.currentTimeMillis() < preventedUntil;
    }

    public static void reset() {
        preventedUntil = 0;
    }

    private static void handleSetPayload(SetClimbingPreventedUntilPayload payload) {
        preventedUntil = payload.preventedUntil();
    }
}
