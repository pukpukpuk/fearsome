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
    private static final float ROUGH_EDGE_LENGTH = 1/16f;
    private static final int
            EDGE1_COLOR = 0x73FFFFFF,
            EDGE2_COLOR = 0x31FFFFFF;

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
        var edge1StartY = cutY - ROUGH_EDGE_LENGTH*2;
        var edge2StartY = edge1StartY + ROUGH_EDGE_LENGTH;

        return List.of(
                ClippingPass.of(ClipPlane.keepBelow(basePositionMatrix, edge1StartY)),
                ClippingPass.of(
                        EDGE1_COLOR,
                        CustomOverlay.GHOST_EDGE,
                        15, 15,
                        ClipPlane.keepAbove(basePositionMatrix, edge1StartY),
                        ClipPlane.keepBelow(basePositionMatrix, edge2StartY)
                ),
                ClippingPass.of(
                        EDGE2_COLOR,
                        CustomOverlay.GHOST_EDGE,
                        15, 15,
                        ClipPlane.keepAbove(basePositionMatrix, edge2StartY),
                        ClipPlane.keepBelow(basePositionMatrix, cutY)
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
