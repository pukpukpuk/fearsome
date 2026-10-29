package one.autremel.fearsome.illusions;

import com.google.common.base.Preconditions;
import net.minecraft.client.MinecraftClient;
import one.autremel.fearsome.network.NetworkController;

public class GameEndCauseDisplayer {
    private static boolean initialized;

    static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(DisplayGameEndCausePayload.ID, GameEndCauseDisplayer::handleDisplayPayload);
    }

    private static void handleDisplayPayload(DisplayGameEndCausePayload payload) {
        var cause = payload.cause();

        var hud = MinecraftClient.getInstance().inGameHud;
        hud.setTitleTicks(10, 70, 20);
        hud.setTitle(cause.getOutcome(payload.ownTeam()));
        hud.setSubtitle(cause.getCause());
    }
}
