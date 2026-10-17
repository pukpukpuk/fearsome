package one.autremel.fearsome.illusions;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import one.autremel.fearsome.network.NetworkController;

public class NameDisplayer {
    private static boolean initialized;

    private static final IntOpenHashSet entitiesWithDisplayedName = new IntOpenHashSet();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(SetDisplayNameVisibilityPayload.ID, NameDisplayer::handleSetPayload);
    }

    public static boolean isNameVisible(int entityId) {
        return entitiesWithDisplayedName.contains(entityId);
    }

    private static void handleSetPayload(SetDisplayNameVisibilityPayload payload) {
        if (payload.isCleanupRequest()) {
            entitiesWithDisplayedName.clear();
            return;
        }

        if (payload.isVisible()) entitiesWithDisplayedName.add(payload.entityId());
        else entitiesWithDisplayedName.remove(payload.entityId());
    }
}
