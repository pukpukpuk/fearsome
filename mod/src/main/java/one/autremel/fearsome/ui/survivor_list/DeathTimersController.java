package one.autremel.fearsome.ui.survivor_list;

import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.timer.TimerSignature;
import one.autremel.fearsome.timer.TimerUpdater;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;

import java.util.ArrayList;
import java.util.List;


public class DeathTimersController {
    private static final String TIMER_KEY = "death_timer";

    private static final int DEATH_TIMER_FULL = 0xFF6DCE5F;
    private static final int DEATH_TIMER_HIGH = 0xFFF2F27B;
    private static final int DEATH_TIMER_MEDIUM = 0xFFFFA538;
    private static final int DEATH_TIMER_LOW = 0xFFE8634E;

    private static final List<TimerSignature> cachedSignatures = new ArrayList<>();

    static void initialize() {
        OverlayScreenRegistrar.registerBeforeScreenDrawing(SurvivorList.ID, DeathTimersController::update);
    }

    public static float getProgress(int characterIndex) {
        var timer = TimerUpdater.getTimer(getSignatureFor(characterIndex - 1));
        var remainingTime = timer != null ? timer.getRemainingTime() : 0;

        var progress = (float) remainingTime / FearsomeMod.GAME_CONFIG.deathTimerInitialValue();
        return Math.min(Math.max(progress, 0f), 1f);
    }

    public static int getColor(float progress) {
        var multiplied = progress * 3;
        if (multiplied >= 3) return DEATH_TIMER_FULL;
        if (multiplied >= 2) return DEATH_TIMER_HIGH;
        if (multiplied >= 1) return DEATH_TIMER_MEDIUM;
        return DEATH_TIMER_LOW;
    }

    private static void update() {
        var survivorList = SurvivorListController.getListNow();
        if (survivorList == null) return;

        var entries = survivorList.getEntries();
        for (int i = 0; i < entries.size(); i++) {
            var entry = entries.get(i);
            var progress = entry.isDead() ? 0f : getProgress(i + 1);

            entry.setDeathTimerProgress(progress);
        }
    }

    private static TimerSignature getSignatureFor(int survivorIndex) {
        while (survivorIndex >= cachedSignatures.size()) {
            var characterIndex = cachedSignatures.size() + 1;
            var signature = new TimerSignature(TIMER_KEY, characterIndex);
            cachedSignatures.add(signature);
        }

        return cachedSignatures.get(survivorIndex);
    }
}
