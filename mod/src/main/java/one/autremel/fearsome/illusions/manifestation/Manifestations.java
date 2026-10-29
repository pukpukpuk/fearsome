package one.autremel.fearsome.illusions.manifestation;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.math.MathHelper;
import one.autremel.fearsome.Easings;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.render.CustomOverlay;
import org.jetbrains.annotations.Nullable;

public class Manifestations {
    public static final BottomUpManifestation GHOST = new BottomUpManifestation(
            0xFFC1FFFF,
            0xFF6CFFFF,
            0x31FFFFFF,
            CustomOverlay.GHOST_THIRD_RING
    );
    public static final BottomUpManifestation DREAM = new BottomUpManifestation(
            0xFFF0C1FF,
            0xFFC36CFF,
            0x31FFFFFF,
            CustomOverlay.DREAM_THIRD_RING
    );
    private static final BottomUpManifestation[] BY_ID = {GHOST, DREAM};

    private static boolean initialized;

    private static final Int2ObjectOpenHashMap<State> states = new Int2ObjectOpenHashMap<>();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(SetManifestationPayload.ID, Manifestations::handleSetPayload);
    }

    @Nullable
    public static BottomUpManifestation getManifestation(int entityId) {
        var state = getActualState(entityId);
        return state != null ? state.manifestation() : null;
    }

    public static float getProgress(int entityId) {
        var state = getActualState(entityId);
        if (state == null) return Float.NaN;

        return state.getProgress(System.currentTimeMillis());
    }

    public static void reset() {
        states.clear();
    }

    @Nullable
    private static State getActualState(int entityId) {
        var state = states.get(entityId);
        if (state == null) return null;

        if (System.currentTimeMillis() >= state.getKeepUntil()) {
            states.remove(entityId);
            return null;
        }

        return state;
    }

    private static void handleSetPayload(SetManifestationPayload payload) {
        var manifestation = BY_ID[payload.manifestationId()];
        var startTime = payload.startTime();
        var startValue = payload.start() != null
                ? payload.start()
                : getCurrentProgress(payload.entityId(), startTime);

        states.put(payload.entityId(), new State(manifestation, startTime, payload.endTime(), startValue, payload.target()));
    }

    private static float getCurrentProgress(int entityId, long now) {
        var state = states.get(entityId);
        if (state == null || now >= state.getKeepUntil()) return 1;

        return state.getProgress(now);
    }

    private record State(BottomUpManifestation manifestation, long startTime, long endTime, float startValue, float target) {
        public long getKeepUntil() {
            return target >= 1 ? endTime : Long.MAX_VALUE;
        }

        public float getProgress(long now) {
            var duration = endTime - startTime;
            var timeProgress = duration > 0
                    ? MathHelper.clamp((float) (now - startTime) / duration, 0, 1)
                    : 1;
            return MathHelper.lerp(Easings.easeOut(timeProgress), startValue, target);
        }
    }
}
