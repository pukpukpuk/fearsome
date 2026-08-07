package one.autremel.fearsome.ui.game_state;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.network.PayloadId;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.UiController;
import one.autremel.fearsome.ui.game_state.payloads.SetAbilityCountPayload;
import one.autremel.fearsome.ui.game_state.payloads.PlayAbilityProgressAnimationPayload;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AbilitiesPartController {
    private static boolean initialized;

    private static final List<AbilityCardData> abilityCardData = new ArrayList<>();

    static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(PayloadId.PLAY_ABILITY_PROGRESS_ANIMATION, AbilitiesPartController::handlePlayAnimation);
        NetworkController.register(PayloadId.SET_ABILITY_COUNT, AbilitiesPartController::handleSetCount);

        OverlayScreenRegistrar.registerBeforeScreenDrawing(GameStateScreen.ID, AbilitiesPartController::update);
        UiController.registerForScreensRemoval(AbilitiesPartController::handleScreenRemoval, true);
    }

    private static void handlePlayAnimation(PlayAbilityProgressAnimationPayload payload) {
        while (payload.index() >= abilityCardData.size()) {
            abilityCardData.add(new AbilityCardData());
        }
        var cardData = abilityCardData.get(payload.index());
        var start = payload.start() != null ? payload.start() : cardData.progress;

        cardData.currentAnimation = new AnimationData(
                start,
                payload.target(), payload.duration(),
                payload.isLinear(),
                System.currentTimeMillis());
    }

    private static void handleSetCount(SetAbilityCountPayload payload) {
        GameStateScreenController.acceptData(data -> {
            var abilitiesPart = data.screen().getAbilitiesPart();
            if (abilitiesPart == null) return;

            var card = abilitiesPart.getCard(payload.index());
            if (card == null) return;

            card.setCount(payload.count());
        });
    }

    private static void update() {
        var screenData = GameStateScreenController.getDataNow();
        if (screenData == null) return;

        var abilitiesPart = screenData.screen().getAbilitiesPart();
        if (abilitiesPart == null) return;

        for (int i = 0; i < abilityCardData.size(); i++) {
            var cardData = abilityCardData.get(i);
            var animation = cardData.currentAnimation;

            var card = abilitiesPart.getCard(i);
            if (animation == null || card == null) continue;

            var currentProgress = animation.getCurrentValue();
            card.setProgress(currentProgress);
            cardData.progress = currentProgress;
        }
    }

    private static void handleScreenRemoval() {
        for (var cardData : abilityCardData) {
            cardData.progress = 0;
            cardData.currentAnimation = null;
        }
    }



    private static class AbilityCardData {
        private float progress;
        @Nullable
        private AnimationData currentAnimation;
    }

    private record AnimationData(
            float start,
            float target,
            int duration,
            boolean isLinear,
            long startTime
    ) {
        public static float easeOut(float x) {
            return (float)(1d - Math.pow(1 - x, 2));
        }

        public float getCurrentValue() {
            return (target - start) * getProgress() + start;
        }

        public boolean isEnded() {
            return getProgress() >= 0.99f;
        }

        private float getProgress() {
            var passed = System.currentTimeMillis() - startTime;
            if (passed >= duration) return 1;
            return isLinear ?
                    (float) passed / duration :
                    easeOut((float) (passed) / duration);
        }

    }
}
