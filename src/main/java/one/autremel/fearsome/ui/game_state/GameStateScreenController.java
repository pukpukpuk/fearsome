package one.autremel.fearsome.ui.game_state;

import com.google.common.base.Preconditions;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.network.PayloadId;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.UiController;
import one.autremel.fearsome.ui.game_state.payloads.CreateGameStateScreenPayload;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class GameStateScreenController {
    private static boolean initialized;

    private static CompletableFuture<Data> dataInitTask;


    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;
        resetScreenInitTask();

        NetworkController.register(PayloadId.CREATE_GAME_STATE_SCREEN, GameStateScreenController::handleCreatePayload);
        UiController.registerForScreensRemoval(GameStateScreenController::handleScreensRemove, true);

        AbilitiesPartController.initialize();
        GlobalInfoPartController.initialize();
    }

    public static void acceptData(Consumer<Data> consumer) {
        dataInitTask.thenAccept(consumer);
    }

    @Nullable
    public static Data getDataNow() {
        return dataInitTask != null ? dataInitTask.getNow(null) : null;
    }

    private static void handleCreatePayload(CreateGameStateScreenPayload payload) {
        var ownIndex = payload.ownCharacterIndex();

        var team = ownIndex == 0 ? CharacterTeam.MURDERERS : CharacterTeam.SURVIVORS;
        var screen = new GameStateScreen(team, payload.ownCharacterKey());
        OverlayScreenRegistrar.create(GameStateScreen.ID, screen);

        GlobalInfoPartController.setTotalGeneratorsCount(payload.generatorsCount());

        var data = new Data(screen, ownIndex);
        dataInitTask.complete(data);
    }

    private static void handleScreensRemove() {
        OverlayScreenRegistrar.remove(GameStateScreen.ID);
        resetScreenInitTask();
    }

    private static void resetScreenInitTask() {
        if (dataInitTask != null && !dataInitTask.isDone()) {
            dataInitTask.cancel(true);
        }
        dataInitTask = new CompletableFuture<>();
    }

    public record Data(GameStateScreen screen, int ownCharacterIndex) {}
}
