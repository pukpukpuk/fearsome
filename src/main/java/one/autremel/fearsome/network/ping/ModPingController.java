package one.autremel.fearsome.network.ping;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.ui.WrongVersionDisconnectScreen;

@RequiredArgsConstructor
public class ModPingController {
    private final Runnable successfulHandshakeCallback;
    private final Runnable disconnectFromGameCallback;

    @Getter
    private boolean isGameActive;

    private boolean isWaitingServerResponse;

    public void initialize() {
        PayloadTypeRegistry.playC2S().register(PingPayload.ID, PingPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PingPayload.ID, PingPayload.CODEC);

        ClientPlayConnectionEvents.JOIN.register(this::onJoin);
        ClientPlayConnectionEvents.DISCONNECT.register(this::onDisconnect);

        ClientPlayNetworking.registerGlobalReceiver(PingPayload.ID, this::handlePacketFromServer);
    }


    private void onJoin(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        ClientPlayNetworking.send(new PingPayload(FearsomeMod.VERSION));
        isWaitingServerResponse = true;
    }

    private void onDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
        isWaitingServerResponse = false;

        if (isGameActive) {
            isGameActive = false;
            client.execute(disconnectFromGameCallback);
        }
    }


    private void handlePacketFromServer(PingPayload payload, ClientPlayNetworking.Context context) {
        if (!isWaitingServerResponse) return;
        isWaitingServerResponse = false;

        var server = payload.version();
        var own = FearsomeMod.VERSION;

        var client = context.client();
        if (!own.equals(server)) {
            client.disconnect(Text.of(String.format("Mod version mismatch! Server: %s, Client: %s", server, own)));

            var reason = Text.translatable("gui.mod_version_mismatch", server, own);
            var screen = new WrongVersionDisconnectScreen(reason);
            client.execute(() -> client.setScreen(screen));
            return;
        }

        isGameActive = true;
        client.execute(successfulHandshakeCallback);
    }
}
