package one.autremel.fearsome.ui.game_state;

import com.google.common.base.Preconditions;
import net.minecraft.util.math.BlockPos;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.config.ViewMethodType;
import one.autremel.fearsome.illusions.glowing.GlowingBlocks;
import one.autremel.fearsome.illusions.glowing.Layer;
import one.autremel.fearsome.input.BindingEvents;
import one.autremel.fearsome.input.BindingId;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.ui.game_state.payloads.MarkBlocksAsGeneratorsPayload;
import one.autremel.fearsome.ui.game_state.payloads.UnmarkGeneratorBlockPayload;

import java.util.HashSet;
import java.util.Set;

class GeneratorsHighlighter {
    private static final int GENERATOR_COLOR = 0xFFFFAA00;

    private static boolean initialized;

    private static boolean isEnabled;
    private static final Set<BlockPos> shownGenerators = new HashSet<>();

    static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(MarkBlocksAsGeneratorsPayload.ID, GeneratorsHighlighter::handleMarkPayload);
        NetworkController.register(UnmarkGeneratorBlockPayload.ID, GeneratorsHighlighter::handleUnmarkPayload);

        BindingEvents.onToggle(BindingId.HIGHLIGHT_GENERATORS, GeneratorsHighlighter::onBindingToggle);
    }

    static void reset() {
        if (isEnabled) setBlocksGlowingState(false);
        isEnabled = false;
        shownGenerators.clear();
    }

    private static void setBlocksGlowingState(boolean mustBeGlowed) {
        if (mustBeGlowed) shownGenerators.forEach(GeneratorsHighlighter::setGlowingGenerator);
        else shownGenerators.forEach(GeneratorsHighlighter::removeGeneratorGlowing);
    }

    private static void setEnabledState(boolean state) {
        var data = GameStateScreenController.getData();
        if (data == null) return;

        if (isEnabled == state) return;
        isEnabled = state;

        var icon = data.screen().getToolsPart().getGeneratorHighlighterIcon();
        icon.setTileX(isEnabled ? 1 : 0);

        setBlocksGlowingState(isEnabled);
    }

    private static void onBindingToggle(boolean currentState) {
        switch (FearsomeMod.CONFIG.generatorsHighlightMethod()) {
            case ViewMethodType.HOLD -> setEnabledState(currentState);
            case ViewMethodType.TOGGLE -> {
                if (currentState) setEnabledState(!isEnabled);
            }
            default -> throw new RuntimeException();
        }
    }

    private static void handleMarkPayload(MarkBlocksAsGeneratorsPayload payload) {
        if (payload.blockPositions() == null) {
            setEnabledState(false);
            reset();
            return;
        }

        if (isEnabled) setBlocksGlowingState(false);
        shownGenerators.clear();

        shownGenerators.addAll(payload.blockPositions());
        if (isEnabled) setBlocksGlowingState(true);
    }

    private static void handleUnmarkPayload(UnmarkGeneratorBlockPayload payload) {
        var pos = payload.blockPosition();
        shownGenerators.remove(pos);
        if (isEnabled) removeGeneratorGlowing(pos);
    }

    private static void setGlowingGenerator(BlockPos pos) {
        GlowingBlocks.setGlowing(Layer.LOCAL, pos, GENERATOR_COLOR);
    }

    private static void removeGeneratorGlowing(BlockPos pos) {
        GlowingBlocks.removeGlowing(Layer.LOCAL, pos);
    }
}
