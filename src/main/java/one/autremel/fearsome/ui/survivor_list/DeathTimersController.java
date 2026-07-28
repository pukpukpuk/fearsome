package one.autremel.fearsome.ui.survivor_list;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.network.PayloadId;
import one.autremel.fearsome.network.RemoveScreensPayload;
import one.autremel.fearsome.network.ping.TimeSynchronizer;

import java.util.HashMap;
import java.util.Map;

class DeathTimersController {
    private static final Map<Entry, RunningTimerData> dataMap = new HashMap<>();

    static void initialize() {
        NetworkController.register(PayloadId.SET_SURVIVOR_DEATH_TIMER_STATE, DeathTimersController::handleSetSurvivorDeathTimerState);
        NetworkController.register(PayloadId.REMOVE_SCREENS, DeathTimersController::handleRemoveScreens);

        ClientTickEvents.END_CLIENT_TICK.register(DeathTimersController::onTick);
    }

    private static void handleRemoveScreens(RemoveScreensPayload payload) {
        dataMap.clear();
    }

    private static void handleSetSurvivorDeathTimerState(SetSurvivorDeathTimerStatePayload payload) {
        var list = SurvivorListController.getList();
        if (list == null) return;

        var entry = list.getEntry(payload.index());

        if (!payload.isRunning()) {
            var progress = (float) payload.value() / getInitialValue();
            entry.setDeathTimerProgress(progress);
            dataMap.remove(entry);
        } else {
            var startTime = TimeSynchronizer.toLocalTime(payload.serverTime());
            dataMap.put(entry, new RunningTimerData(startTime, payload.value()));
        }
    }

    private static void onTick(MinecraftClient client) {
        var currentTime = System.currentTimeMillis();

        for (var dataEntry : dataMap.entrySet()) {
            var data = dataEntry.getValue();

            var elapsed = currentTime - data.startTime;
            var currentValue = data.startValue + elapsed * (-1);
            var progress = (float)currentValue / getInitialValue();

            dataEntry.getKey().setDeathTimerProgress(progress);
        }
    }



    private static int getInitialValue() {
        return FearsomeMod.GAME_CONFIG.deathTimerInitialValue();
    }

    private record RunningTimerData(long startTime, long startValue) {}
}
