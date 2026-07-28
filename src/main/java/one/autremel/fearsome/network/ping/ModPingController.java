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
import one.autremel.fearsome.network.PayloadId;
import one.autremel.fearsome.ui.WrongVersionDisconnectScreen;

@RequiredArgsConstructor
public class ModPingController {
    private static ModPingController INSTANCE;

    private final Runnable successfulHandshakeCallback, disconnectFromGameCallback;

    private boolean isGameActive;

    private boolean isWaitingServerResponse;

    public static void initialize(Runnable successfulHandshakeCallback, Runnable disconnectFromGameCallback) {
        Preconditions.checkState(INSTANCE == null);
        INSTANCE = new ModPingController(successfulHandshakeCallback, disconnectFromGameCallback);

        ClientPlayConnectionEvents.JOIN.register(INSTANCE::onJoin);
        ClientPlayConnectionEvents.DISCONNECT.register(INSTANCE::onDisconnect);

        NetworkController.register(PayloadId.PING, INSTANCE::handlePingPayload);
    }

    public static boolean isGameActive() {
        return INSTANCE.isGameActive;
    }


    private void onJoin(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        var t1 = System.currentTimeMillis();
        TimeSynchronizer.setT1(t1);

        var payload = new PingPayload(FearsomeMod.VERSION, t1);
        NetworkController.send(payload);
        isWaitingServerResponse = true;
    }

    private void onDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
        isWaitingServerResponse = false;

        if (isGameActive) {
            isGameActive = false;
            client.execute(disconnectFromGameCallback);
        }
    }


    private void handlePingPayload(PingPayload payload) {
        if (!isWaitingServerResponse) return;
        isWaitingServerResponse = false;

        var server = payload.version();
        var own = FearsomeMod.VERSION;

        var client = MinecraftClient.getInstance();
        if (!own.equals(server)) {
            client.disconnect(Text.of(String.format("Mod version mismatch! Server: %s, Client: %s", server, own)));

            var reason = Text.translatable("gui.mod_version_mismatch", server, own);
            var screen = new WrongVersionDisconnectScreen(reason);
            client.execute(() -> client.setScreen(screen));
            return;
        }

        TimeSynchronizer.calculateDelta(payload);

        isGameActive = true;
        client.execute(successfulHandshakeCallback);
    }
}
