package one.autremel.fearsome.ui.game_state;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.network.PayloadId;
import one.autremel.fearsome.timer.TimerSignature;
import one.autremel.fearsome.timer.TimerUpdater;

class GlobalInfoPartController {
    public static final TimerSignature GAME_TIMER_SIGNATURE = new TimerSignature("global_game_timer", -1);

    private static boolean initialized;

    private static int totalGeneratorsCount;

    static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        ClientTickEvents.END_CLIENT_TICK.register(GlobalInfoPartController::onTick);

        NetworkController.register(PayloadId.SET_GENERATORS_COUNT, GlobalInfoPartController::handleSetGeneratorsCount);
        NetworkController.register(PayloadId.SET_QUESTS_COUNT, GlobalInfoPartController::handleSetQuestsCount);
    }

    static void setTotalGeneratorsCount(int count) {
        totalGeneratorsCount = count;
        GameStateScreenController.acceptScreen(screen -> {
            screen.getGlobalInfoPart().setGeneratorsTotalCount(totalGeneratorsCount);
        });
    }


    private static void updateGameTimerLabel() {
        GameStateScreenController.acceptScreen(screen -> {
            var timer = TimerUpdater.getTimer(GAME_TIMER_SIGNATURE);
            var remaining = timer != null ? timer.getRemainingTime() : 0;
            screen.getGlobalInfoPart().setRemainingTime(remaining);
        });
    }

    private static void onTick(MinecraftClient client) {
        updateGameTimerLabel();
        //TODO statuses
    }


    private static void handleSetGeneratorsCount(SetGeneratorsCountPayload payload) {
        GameStateScreenController.acceptScreen(screen -> {
            screen.getGlobalInfoPart().setGeneratorsRemainingCount(payload.count());
        });
    }

    private static void handleSetQuestsCount(SetQuestsCountPayload payload) {
        GameStateScreenController.acceptScreen(screen -> {
            screen.getGlobalInfoPart().setQuestsCount(payload.count());
        });
    }

}
