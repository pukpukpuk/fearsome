package one.autremel.fearsome.ui.game_state;

import com.google.common.base.Preconditions;
import one.autremel.fearsome.effects.DisplayedEffect;
import one.autremel.fearsome.effects.EffectController;
import one.autremel.fearsome.effects.EffectVisiblePart;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.timer.TimerSignature;
import one.autremel.fearsome.timer.TimerUpdater;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.game_state.payloads.SetGeneratorsCountPayload;
import one.autremel.fearsome.ui.game_state.payloads.SetQuestsCountPayload;
class GlobalInfoPartController {
    public static final TimerSignature GAME_TIMER_SIGNATURE = new TimerSignature("global_game_timer", -1);
    private static final int TIME_UPDATE_PERIOD = 1000;

    private static boolean initialized;
    private static int totalGeneratorsCount;

    private static long lastUpdateTime;

    static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        OverlayScreenRegistrar.registerBeforeScreenDrawing(GameStateScreen.ID, GlobalInfoPartController::update);

        NetworkController.register(SetGeneratorsCountPayload.ID, GlobalInfoPartController::handleSetGeneratorsCount);
        NetworkController.register(SetQuestsCountPayload.ID, GlobalInfoPartController::handleSetQuestsCount);

        EffectController.subscribeToEffectAddition(GlobalInfoPartController::handleEffectsCollectionChange);
        EffectController.subscribeToEffectRemoval(GlobalInfoPartController::handleEffectsCollectionChange);
        EffectController.subscribeToEffectDisplayNameChange(GlobalInfoPartController::handleEffectDisplayNameChange);
    }

    static void setTotalGeneratorsCount(int count) {
        totalGeneratorsCount = count;
        GameStateScreenController.acceptData(data -> {
            data.screen().getGlobalInfoPart().setGeneratorsTotalCount(totalGeneratorsCount);
        });
    }


    private static void handleSetGeneratorsCount(SetGeneratorsCountPayload payload) {
        GameStateScreenController.acceptData(data -> {
            data.screen().getGlobalInfoPart().setGeneratorsRemainingCount(payload.count());
        });
    }

    private static void handleSetQuestsCount(SetQuestsCountPayload payload) {
        GameStateScreenController.acceptData(data -> {
            data.screen().getGlobalInfoPart().setQuestsCount(payload.count());
        });
    }

    private static void updateGameTimerLabel(GameStateScreenController.Data data) {
        var timer = TimerUpdater.getTimer(GAME_TIMER_SIGNATURE);
        var remaining = timer != null ? timer.getRemainingTime() : 0;
        data.screen().getGlobalInfoPart().setRemainingTime(remaining);
    }

    private static void updateStatusTimeLabels(GameStateScreenController.Data data) {
        var table = data.screen().getGlobalInfoPart().getEffectTable();
        var characterIndex = data.ownCharacterIndex();
        var effects = EffectController.getEffects(characterIndex, EffectVisiblePart.DISPLAY_NAME);

        table.updateEffectTimeColumn(effects);
    }

    private static void update() {
        var currentTime = System.currentTimeMillis();
        if ((currentTime - lastUpdateTime) < TIME_UPDATE_PERIOD) return;
        lastUpdateTime = currentTime;

        var data = GameStateScreenController.getDataNow();
        if (data == null) return;

        updateGameTimerLabel(data);
        updateStatusTimeLabels(data);
    }

    private static void handleEffectsCollectionChange(DisplayedEffect effect) {
        GameStateScreenController.acceptData(data -> {
            var ownIndex = data.ownCharacterIndex();
            if (ownIndex != effect.getCharacterIndex()) return;

            var table = data.screen().getGlobalInfoPart().getEffectTable();
            var effects = EffectController.getEffects(data.ownCharacterIndex(), EffectVisiblePart.DISPLAY_NAME);

            table.updateEffectNameColumn(effects);
            table.updateEffectTimeColumn(effects);
        });
    }

    private static void handleEffectDisplayNameChange(DisplayedEffect effect) {
        GameStateScreenController.acceptData(data -> {
            var ownIndex = data.ownCharacterIndex();
            if (ownIndex != effect.getCharacterIndex()) return;

            var table = data.screen().getGlobalInfoPart().getEffectTable();
            var effects = EffectController.getEffects(data.ownCharacterIndex(), EffectVisiblePart.DISPLAY_NAME);

            table.updateEffectNameColumn(effects);
        });
    }
}
