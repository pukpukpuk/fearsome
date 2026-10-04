package one.autremel.fearsome.ui;

import io.wispforest.owo.ui.parsing.UIParsing;
import net.minecraft.text.Text;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.network.RemoveScreensPayload;
import one.autremel.fearsome.ui.components.*;
import one.autremel.fearsome.ui.game_state.GameStateScreenController;
import one.autremel.fearsome.ui.hints.HintsScreenController;
import one.autremel.fearsome.ui.survivor_list.SurvivorListController;
import one.autremel.fearsome.ui.terror.TerrorIndicatorController;

import java.util.ArrayList;
import java.util.List;

public class UiController {
    private static final List<Runnable> onGameScreensRemove = new ArrayList<>();
    private static final List<Runnable> onLobbyScreensRemove = new ArrayList<>();

    static {
        UIParsing.registerFactory(FearsomeMod.identifier("colorable_texture"), ColorableTextureComponent::parse);
        UIParsing.registerFactory(FearsomeMod.identifier("radial_bar"), element -> new RadialProgressBar());
        UIParsing.registerFactory(FearsomeMod.identifier("scalable_label"), element -> new ScalableLabelComponent(Text.empty()));
        UIParsing.registerFactory(FearsomeMod.identifier("linear_bar"), element -> new LinearBar());
        UIParsing.registerFactory(
                FearsomeMod.identifier("action_control_icon"),
                element -> new ActionControlLabel()
        );

        UIParsing.registerFactory(FearsomeMod.identifier("hideable_stack_layout"), element -> new HideableStackLayout());
        UIParsing.registerFactory(FearsomeMod.identifier("hideable_flow_layout"), HideableFlowLayout::parse);
    }

    public static void initialize() {
        NetworkController.register(RemoveScreensPayload.ID, UiController::handleScreensRemove);

        SurvivorListController.initialize();
        GameStateScreenController.initialize();
        HintsScreenController.initialize();
        TerrorIndicatorController.initialize();
    }

    public static void reset() {
        onGameScreensRemove.forEach(Runnable::run);
        onLobbyScreensRemove.forEach(Runnable::run);
    }

    public static void registerForScreensRemoval(Runnable runnable, boolean asGameScreen) {
        getList(asGameScreen).add(runnable);
    }

    private static List<Runnable> getList(boolean isForGameScreens) {
        return isForGameScreens ? onGameScreensRemove : onLobbyScreensRemove;
    }

    private static void handleScreensRemove(RemoveScreensPayload payload) {
        getList(payload.isForGameScreens()).forEach(Runnable::run);
    }
}
