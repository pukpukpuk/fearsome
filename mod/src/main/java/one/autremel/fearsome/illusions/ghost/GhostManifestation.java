package one.autremel.fearsome.illusions.ghost;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.math.MathHelper;
import one.autremel.fearsome.Easings;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.render.CustomOverlay;
import one.autremel.fearsome.render.clipping.ClipPlane;
import one.autremel.fearsome.render.clipping.ClippingPass;
import org.joml.Matrix4fc;

import java.util.List;

public class GhostManifestation {
    private static final float CLIP_TOP_MARGIN = 0.25f;
    private static final float RING_HEIGHT = 1/16f;
    private static final int
            FIRST_RING_COLOR = 0xFFC1FFFF,
            SECOND_RING_COLOR = 0xFF6CFFFF,
            THIRD_RING_COLOR = 0x31FFFFFF,
            FIRST_RING_LIGHT = 3,
            SECOND_RING_LIGHT = 7,
            THIRD_RING_LIGHT = 10;

    private static boolean initialized;

    private static final Int2ObjectOpenHashMap<State> states = new Int2ObjectOpenHashMap<>();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(SetGhostManifestationPayload.ID, GhostManifestation::handleSetPayload);
    }

    public static float getProgress(int entityId) {
        var state = states.get(entityId);
        if (state == null) return Float.NaN;

        var now = System.currentTimeMillis();
        if (now >= state.getKeepUntil()) {
            states.remove(entityId);
            return Float.NaN;
        }

        return Easings.easeOut(state.getLinearProgress(now));
    }

    public static List<ClippingPass> createClippingPasses(Matrix4fc basePositionMatrix, float entityY, float entityHeight, float progress) {
        var cutY = entityY + progress * (entityHeight + CLIP_TOP_MARGIN);
        var firstRingStartY = cutY - RING_HEIGHT * 2;
        var secondRingStartY = cutY - RING_HEIGHT;
        var thirdRingEndY = cutY + RING_HEIGHT;

        return List.of(
                ClippingPass.of(ClipPlane.keepBelow(basePositionMatrix, firstRingStartY)),
                ClippingPass.of(
                        FIRST_RING_COLOR,
                        FIRST_RING_LIGHT, FIRST_RING_LIGHT,
                        ClipPlane.keepAbove(basePositionMatrix, firstRingStartY),
                        ClipPlane.keepBelow(basePositionMatrix, secondRingStartY)
                ),
                ClippingPass.of(
                        SECOND_RING_COLOR,
                        SECOND_RING_LIGHT, SECOND_RING_LIGHT,
                        ClipPlane.keepAbove(basePositionMatrix, secondRingStartY),
                        ClipPlane.keepBelow(basePositionMatrix, cutY)
                ),
                ClippingPass.of(
                        THIRD_RING_COLOR,
                        CustomOverlay.GHOST_THIRD_RING,
                        THIRD_RING_LIGHT, THIRD_RING_LIGHT,
                        ClipPlane.keepAbove(basePositionMatrix, cutY),
                        ClipPlane.keepBelow(basePositionMatrix, thirdRingEndY)
                )
        );
    }

    public static void reset() {
        states.clear();
    }

    private static void handleSetPayload(SetGhostManifestationPayload payload) {
        var startTime = payload.startTime();
        var startValue = payload.start() != null
                ? payload.start()
                : getCurrentLinearProgress(payload.entityId(), startTime);

        states.put(payload.entityId(), new State(startTime, payload.endTime(), startValue, payload.target()));
    }

    private static float getCurrentLinearProgress(int entityId, long now) {
        var state = states.get(entityId);
        if (state == null || now >= state.getKeepUntil()) return 1;

        return state.getLinearProgress(now);
    }

    private record State(long startTime, long endTime, float startValue, float target) {
        public long getKeepUntil() {
            return target >= 1 ? endTime : Long.MAX_VALUE;
        }

        public float getLinearProgress(long now) {
            var duration = endTime - startTime;
            var timeProgress = duration > 0
                    ? MathHelper.clamp((float) (now - startTime) / duration, 0, 1)
                    : 1;
            return MathHelper.lerp(timeProgress, startValue, target);
        }
    }
}
