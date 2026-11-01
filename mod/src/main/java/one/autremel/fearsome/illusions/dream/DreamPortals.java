package one.autremel.fearsome.illusions.dream;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import one.autremel.fearsome.network.NetworkController;

public class DreamPortals {
    private static boolean initialized;

    private static final Int2ObjectOpenHashMap<DreamPortal> portals = new Int2ObjectOpenHashMap<>();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(SpawnDreamPortalPayload.ID, DreamPortals::handleSpawnPayload);
        NetworkController.register(RemoveDreamPortalPayload.ID, DreamPortals::handleRemovePayload);
        NetworkController.register(SetDreamPortalChargePayload.ID, DreamPortals::handleChargePayload);
        NetworkController.register(SetDreamPortalGlowingPayload.ID, DreamPortals::handleGlowingPayload);

        ClientTickEvents.END_CLIENT_TICK.register(client -> tick());
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((client, world) -> reset());
    }

    public static void reset() {
        for (var portal : portals.values()) {
            portal.forget();
        }
        portals.clear();
    }

    public static boolean isCharged(int portalId) {
        var portal = portals.get(portalId);
        return portal != null && portal.isChargeCompleted();
    }

    public static float getChargeProgress(int portalId) {
        var portal = portals.get(portalId);
        return portal != null ? portal.getChargeProgress(System.currentTimeMillis()) : 0;
    }

    private static void tick() {
        if (portals.isEmpty()) return;

        var now = System.currentTimeMillis();
        for (var portal : portals.values()) {
            portal.tick(now);
        }
    }

    private static void handleSpawnPayload(SpawnDreamPortalPayload payload) {
        var world = MinecraftClient.getInstance().world;
        if (world == null) return;

        var previous = portals.remove(payload.portalId());
        if (previous != null) previous.remove();

        var origin = new Vec3d(payload.x(), payload.y(), payload.z());
        portals.put(payload.portalId(), new DreamPortal(world, origin, payload.yaw()));
    }

    private static void handleRemovePayload(RemoveDreamPortalPayload payload) {
        var portal = portals.remove(payload.portalId());
        if (portal != null) portal.remove();
    }

    private static void handleChargePayload(SetDreamPortalChargePayload payload) {
        var portal = portals.get(payload.portalId());
        if (portal == null) return;

        if (payload.chargeEndTime() == null) portal.setCharged();
        else portal.setCharging(payload.chargeStartTime(), payload.chargeEndTime());
    }

    private static void handleGlowingPayload(SetDreamPortalGlowingPayload payload) {
        var portal = portals.get(payload.portalId());
        if (portal != null) portal.setGlowing(payload.isGlowing(), payload.isFocused());
    }
}
