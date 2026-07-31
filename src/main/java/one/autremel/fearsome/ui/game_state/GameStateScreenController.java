package one.autremel.fearsome.ui.game_state;

import com.google.common.base.Preconditions;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.network.PayloadId;
import one.autremel.fearsome.network.RemoveScreensPayload;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class GameStateScreenController {
    private static boolean initialized;

    private static CompletableFuture<GameStateScreen> screenInitTask;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        resetScreenInitTask();

        NetworkController.register(PayloadId.CREATE_GAME_STATE_SCREEN, GameStateScreenController::handleCreatePayload);
        NetworkController.register(PayloadId.REMOVE_SCREENS, GameStateScreenController::handleScreensRemove);

        GlobalInfoPartController.initialize();
    }

    public static void acceptScreen(Consumer<GameStateScreen> consumer) {
        screenInitTask.thenAccept(consumer);
    }

    private static void handleCreatePayload(CreateGameStateScreenPayload payload) {
        var screen = new GameStateScreen();
        OverlayScreenRegistrar.create(GameStateScreen.ID, screen);

        GlobalInfoPartController.setTotalGeneratorsCount(payload.generatorsCount());

        screenInitTask.complete(screen);
    }

    private static void handleScreensRemove(RemoveScreensPayload payload) {
        if (!payload.isForGameScreens()) return;
        OverlayScreenRegistrar.remove(GameStateScreen.ID);
        resetScreenInitTask();
    }

    private static void resetScreenInitTask() {
        if (screenInitTask != null && !screenInitTask.isDone()) {
            screenInitTask.cancel(true);
        }
        screenInitTask = new CompletableFuture<>();
    }
}
