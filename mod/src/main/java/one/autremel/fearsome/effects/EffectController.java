package one.autremel.fearsome.effects;

import com.google.common.base.Preconditions;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.ui.UiController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class EffectController {
    private static boolean initialized;

    private static final List<EffectHolder> holders = new ArrayList<>();

    private static final List<Consumer<DisplayedEffect>> onEffectAddition = new ArrayList<>();
    private static final List<Consumer<DisplayedEffect>> onEffectRemoval = new ArrayList<>();
    private static final List<Consumer<DisplayedEffect>> onEffectDisplayNameChange = new ArrayList<>();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(AddDisplayedEffectPayload.ID, EffectController::handleEffectAdd);
        NetworkController.register(RemoveDisplayedEffectPayload.ID, EffectController::handleEffectRemove);
        NetworkController.register(SetDisplayedEffectNamePayload.ID, EffectController::handleEffectDisplayNameChange);

        UiController.registerForScreensRemoval(EffectController::handleScreensRemove, true);
    }

    public static Collection<DisplayedEffect> getEffects(int characterIndex, EffectVisiblePart requiredPart) {
        if (characterIndex == -1) return Collections.emptyList();

        var holder = getHolder(characterIndex);
        return requiredPart == EffectVisiblePart.DISPLAY_NAME
                ? holder.getWithDisplayName()
                : holder.getWithIcon();
    }

    public static void subscribeToEffectAddition(Consumer<DisplayedEffect> consumer) {
        onEffectAddition.add(consumer);
    }

    public static void subscribeToEffectRemoval(Consumer<DisplayedEffect> consumer) {
        onEffectRemoval.add(consumer);
    }

    public static void subscribeToEffectDisplayNameChange(Consumer<DisplayedEffect> consumer) {
        onEffectDisplayNameChange.add(consumer);
    }


    private static EffectHolder getHolder(int characterIndex) {
        while (characterIndex >= holders.size()) {
            holders.add(new EffectHolder());
        }
        return holders.get(characterIndex);
    }

    private static void handleScreensRemove() {
        for (var holder : holders) {
            holder.clear();
        }
    }

    private static void handleEffectAdd(AddDisplayedEffectPayload payload) {
        var effect = DisplayedEffect.of(payload);
        var holder = getHolder(payload.characterIndex());

        holder.add(effect);
        onEffectAddition.forEach(c -> c.accept(effect));
    }

    private static void handleEffectRemove(RemoveDisplayedEffectPayload payload) {
        var holder = getHolder(payload.characterIndex());
        var removedEffect = holder.remove(payload.effectId());

        if (removedEffect != null) {
            onEffectRemoval.forEach(c -> c.accept(removedEffect));
        }
    }

    private static void handleEffectDisplayNameChange(SetDisplayedEffectNamePayload payload) {
        var holder = getHolder(payload.characterIndex());
        var effect = holder.get(payload.effectId());
        if (effect == null) return;

        effect.setName(payload.newName());
        onEffectDisplayNameChange.forEach(c -> c.accept(effect));
    }
}
