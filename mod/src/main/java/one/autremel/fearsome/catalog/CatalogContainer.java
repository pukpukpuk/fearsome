package one.autremel.fearsome.catalog;

import com.google.common.base.Preconditions;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.network.NetworkController;
import org.jetbrains.annotations.Nullable;

public class CatalogContainer {
    private static boolean initialized;

    @Nullable
    private static CatalogPayload current;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(CatalogPayload.ID, CatalogContainer::handlePayload);
    }

    public static boolean isLoaded() {
        return current != null;
    }

    public static TeamCatalog get(CharacterTeam team) {
        Preconditions.checkState(current != null, "Catalog is not loaded yet!");
        return team == CharacterTeam.SURVIVORS ? current.survivors() : current.murderers();
    }

    public static void reset() {
        current = null;
    }

    private static void handlePayload(CatalogPayload payload) {
        current = payload;
        FearsomeMod.LOGGER.info("Catalog received: {} survivors, {} survivor perks, {} murderers, {} murderer perks",
                payload.survivors().characters().size(),
                payload.survivors().perks().size(),
                payload.murderers().characters().size(),
                payload.murderers().perks().size());
    }
}
