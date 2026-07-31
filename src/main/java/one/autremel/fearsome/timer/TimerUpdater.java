package one.autremel.fearsome.timer;

import com.google.common.base.Preconditions;
import one.autremel.fearsome.network.*;
import one.autremel.fearsome.network.ping.TimeSynchronizer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class TimerUpdater {
    private static boolean initialized;

    private static final HashMap<TimerSignature, Timer> registeredTimerMap = new HashMap<>();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(PayloadId.UPDATE_TIMER, TimerUpdater::handleTimerUpdate);
        NetworkController.register(PayloadId.DELETE_TIMER, TimerUpdater::handleTimerDelete);
    }


    @Nullable
    public static Timer getTimer(TimerSignature signature) {
        return registeredTimerMap.get(signature);
    }

    private static void handleTimerUpdate(TimerUpdatePayload payload) {
        var signature = new TimerSignature(payload.key(), payload.characterIndex());
        var timer = registeredTimerMap.computeIfAbsent(signature, s -> new Timer(payload));

        timer.setEndTime(TimeSynchronizer.toLocalTime(payload.endTime()));
        timer.setPausedAt(TimeSynchronizer.toLocalTime(payload.pausedAt()));
        timer.setCounting(payload.isCounting());
    }

    private static void handleTimerDelete(TimerDeletePayload payload) {
        var signature = new TimerSignature(payload.key(), payload.characterIndex());
        registeredTimerMap.remove(signature);
    }
}
