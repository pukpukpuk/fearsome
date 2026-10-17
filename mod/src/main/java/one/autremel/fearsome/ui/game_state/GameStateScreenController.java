package one.autremel.fearsome.ui.game_state;

import com.google.common.base.Preconditions;
import lombok.Getter;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.UiController;
import one.autremel.fearsome.ui.game_state.payloads.CreateGameStateScreenPayload;
import org.jetbrains.annotations.Nullable;

public class GameStateScreenController {
    private static boolean initialized;

    @Getter @Nullable
    private static Data data;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(CreateGameStateScreenPayload.ID, GameStateScreenController::handleCreatePayload);
        UiController.registerForScreensRemoval(GameStateScreenController::handleScreensRemove, true);

        AbilitiesPartController.initialize();
        GlobalInfoPartController.initialize();
        ToolsPartController.initialize();
    }

    private static void handleCreatePayload(CreateGameStateScreenPayload payload) {
        var ownIndex = payload.ownCharacterIndex();

        var team = ownIndex == 0 ? CharacterTeam.MURDERERS : CharacterTeam.SURVIVORS;
        var screen = new GameStateScreen(team, payload.ownCharacterKey());
        OverlayScreenRegistrar.create(GameStateScreen.ID, screen);

        GlobalInfoPartController.setTotalGeneratorsCount(payload.generatorsCount());

        data = new Data(screen, ownIndex);
    }

    private static void handleScreensRemove() {
        OverlayScreenRegistrar.remove(GameStateScreen.ID);
        data = null;
    }

    public record Data(GameStateScreen screen, int ownCharacterIndex) {}
}
