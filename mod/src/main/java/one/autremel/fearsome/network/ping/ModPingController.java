package one.autremel.fearsome.network.ping;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.ui.WrongVersionDisconnectScreen;

@RequiredArgsConstructor
public class ModPingController {
    private static ModPingController INSTANCE;

    private final Runnable successfulHandshakeCallback, disconnectFromGameCallback;

    private boolean isOnFearServer;

    private boolean isWaitingServerResponse;

    public static void initialize(Runnable successfulHandshakeCallback, Runnable disconnectFromGameCallback) {
        Preconditions.checkState(INSTANCE == null);
        INSTANCE = new ModPingController(successfulHandshakeCallback, disconnectFromGameCallback);

        ClientPlayConnectionEvents.JOIN.register(INSTANCE::onJoin);
        ClientPlayConnectionEvents.DISCONNECT.register(INSTANCE::onDisconnect);

        NetworkController.register(PongPayload.ID, INSTANCE::handlePongPayload);
    }

    public static boolean isOnFearServer() {
        return INSTANCE.isOnFearServer;
    }


    private void onJoin(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        if (client.isIntegratedServerRunning()) {
            return;
        }

        TimeSynchronizer.setT1(System.currentTimeMillis());

        NetworkController.send(PingPayload.INSTANCE);
        isOnFearServer = false;
        isWaitingServerResponse = true;
    }

    private void onDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
        isWaitingServerResponse = false;

        if (isOnFearServer) {
            isOnFearServer = false;
            client.execute(disconnectFromGameCallback);
        }
    }


    private void handlePongPayload(PongPayload payload) {
        if (!isWaitingServerResponse) return;
        isWaitingServerResponse = false;

        var server = payload.version();
        var own = FearsomeMod.VERSION;

        var client = MinecraftClient.getInstance();
        if (!own.equals(server)) {
            var reason = Text.translatable("gui.mod_version_mismatch", server, own);

            client.disconnect(reason);
            var screen = new WrongVersionDisconnectScreen(reason);
            client.execute(() -> client.setScreen(screen));
            return;
        }

        TimeSynchronizer.calculateDelta(payload);

        isOnFearServer = true;
        client.execute(successfulHandshakeCallback);
    }
}
