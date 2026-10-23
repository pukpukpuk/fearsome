package one.autremel.fearsome.illusions.glowing;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import one.autremel.fearsome.network.NetworkController;
import org.jetbrains.annotations.Nullable;

public class GlowingEntities {
    private static boolean initialized;

    private static final Int2IntOpenHashMap localGlowColors = new Int2IntOpenHashMap();
    private static final Int2IntOpenHashMap serverGlowColors = new Int2IntOpenHashMap();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(SetEntityGlowingPayload.ID, GlowingEntities::handleSetPayload);
    }

    public static void setGlowing(int entityId, int color) {
        localGlowColors.put(entityId, color);
    }

    public static void unsetGlowing(int entityId) {
        localGlowColors.remove(entityId);
    }

    public static boolean isGlowed(int entityId) {
        return localGlowColors.containsKey(entityId) || serverGlowColors.containsKey(entityId);
    }

    @Nullable
    public static Integer getColor(int entityId) {
        if (localGlowColors.containsKey(entityId)) return localGlowColors.get(entityId);
        if (serverGlowColors.containsKey(entityId)) return serverGlowColors.get(entityId);
        return null;
    }

    public static void reset() {
        localGlowColors.clear();
        serverGlowColors.clear();
    }

    private static void handleSetPayload(SetEntityGlowingPayload payload) {
        if (payload.isCleanupRequest()) {
            serverGlowColors.clear();
            return;
        }

        if (payload.color() == null) serverGlowColors.remove(payload.entityId());
        else serverGlowColors.put(payload.entityId(), (int) payload.color());
    }
}
