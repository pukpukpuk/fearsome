package one.autremel.fearsome.ui.lobby;

import com.google.common.base.Preconditions;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.timer.TimerSignature;
import one.autremel.fearsome.timer.TimerUpdater;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.UiController;
import one.autremel.fearsome.ui.game_state.GameStateScreen;
import org.jetbrains.annotations.Nullable;

public class LobbyScreenController {
    private static final TimerSignature TIMER_SIGNATURE = new TimerSignature("lobby_timer", -1);

    private static boolean initialized;

    @Nullable
    private static LobbyScreen screen;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(CreateLobbyScreenPayload.ID, LobbyScreenController::handleCreatePayload);
        NetworkController.register(SetLobbyPlayerCountPayload.ID, LobbyScreenController::handleSetPlayerCount);

        UiController.registerForScreensRemoval(LobbyScreenController::handleScreensRemove, false);

        OverlayScreenRegistrar.registerBeforeScreenDrawing(LobbyScreen.ID, LobbyScreenController::updateTimerLabel);
    }

    private static void handleCreatePayload(CreateLobbyScreenPayload payload) {
        OverlayScreenRegistrar.create(LobbyScreen.ID, screen = new LobbyScreen());
    }

    private static void handleSetPlayerCount(SetLobbyPlayerCountPayload payload) {
        if (screen == null) return;

        screen.setPlayerCount(payload.playerCount());
        screen.setInQueueCount(payload.inQueueCount());
    }

    private static void handleScreensRemove() {
        OverlayScreenRegistrar.remove(LobbyScreen.ID);
        screen = null;
    }

    private static void updateTimerLabel() {
        if (screen == null) return;

        var timer = TimerUpdater.getTimer(TIMER_SIGNATURE);
        if (timer == null || !timer.isCounting()) {
            screen.setTimerLineCollapsed(true);
            return;
        }

        screen.setTimerLineCollapsed(false);
        screen.setRemainingTime(timer.getRemainingTime());
    }
}
