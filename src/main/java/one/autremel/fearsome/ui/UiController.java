package one.autremel.fearsome.ui;

import one.autremel.fearsome.effects.EffectController;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.network.PayloadId;
import one.autremel.fearsome.network.RemoveScreensPayload;
import one.autremel.fearsome.ui.game_state.GameStateScreenController;
import one.autremel.fearsome.ui.survivor_list.SurvivorList;
import one.autremel.fearsome.ui.survivor_list.SurvivorListController;

import java.util.ArrayList;
import java.util.List;

public class UiController {

    private static final List<Runnable> onGameScreensRemove = new ArrayList<>();
    private static final List<Runnable> onLobbyScreensRemove = new ArrayList<>();

    public static void initialize() {
        NetworkController.register(PayloadId.REMOVE_SCREENS, UiController::handleScreensRemove);

        SurvivorListController.initialize();
        GameStateScreenController.initialize();
    }

    public static void reset() {
        invoke(true);
        invoke(false);
    }

    public static void registerForScreensRemoval(Runnable runnable, boolean asGameScreen) {
        getList(asGameScreen).add(runnable);
    }


    private static List<Runnable> getList(boolean isForGameScreens) {
        return isForGameScreens ? onGameScreensRemove : onLobbyScreensRemove;
    }

    private static void invoke(boolean isForGameScreens) {
        getList(isForGameScreens).forEach(Runnable::run);
    }

    private static void handleScreensRemove(RemoveScreensPayload payload) {
        invoke(payload.isForGameScreens());
    }
}
