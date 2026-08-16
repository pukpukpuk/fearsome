package one.autremel.fearsome.ui.survivor_list;

import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.timer.TimerSignature;
import one.autremel.fearsome.timer.TimerUpdater;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;

import java.util.ArrayList;
import java.util.List;


class DeathTimersController {
    private static final String TIMER_KEY = "death_timer";
    private static final List<TimerSignature> cachedSignatures = new ArrayList<>();

    static void initialize() {
        OverlayScreenRegistrar.registerBeforeScreenDrawing(SurvivorList.ID, DeathTimersController::update);
    }

    private static void update() {
        var survivorList = SurvivorListController.getListNow();
        if (survivorList == null) return;

        var initialValue = FearsomeMod.GAME_CONFIG.deathTimerInitialValue();

        var entries = survivorList.getEntries();
        for (int i = 0; i < entries.size(); i++) {
            var progress = 0f;
            var entry = entries.get(i);
            if (!entry.isDead()) {
                var timer = TimerUpdater.getTimer(getSignatureFor(i));

                var remainingTime = timer != null ? timer.getRemainingTime() : 0;
                progress = (float) remainingTime / initialValue;
            }

            entry.setDeathTimerProgress(progress);
        }
    }

    private static TimerSignature getSignatureFor(int survivorIndex) {
        while (survivorIndex >= cachedSignatures.size()) {
            var characterIndex = survivorIndex + 1;
            var signature = new TimerSignature(TIMER_KEY, characterIndex);
            cachedSignatures.add(signature);
        }

        return cachedSignatures.get(survivorIndex);
    }
}
