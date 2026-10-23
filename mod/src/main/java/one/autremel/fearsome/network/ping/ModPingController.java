package one.autremel.fearsome.network.ping;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.network.NetworkController;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class ModPingController {
    private static final Identifier MOD_CHECK_CHANNEL = FearsomeMod.identifier("mod_check");

    private static ModPingController INSTANCE;

    private final Runnable successfulHandshakeCallback, disconnectFromGameCallback;

    private volatile boolean
            isModCheckedOnLogin,
            isOnFearServer;

    private boolean isWaitingServerResponse;

    public static void initialize(Runnable successfulHandshakeCallback, Runnable disconnectFromGameCallback) {
        Preconditions.checkState(INSTANCE == null);
        INSTANCE = new ModPingController(successfulHandshakeCallback, disconnectFromGameCallback);

        ClientLoginConnectionEvents.INIT.register((handler, client) -> INSTANCE.isModCheckedOnLogin = false);
        ClientLoginNetworking.registerGlobalReceiver(
                MOD_CHECK_CHANNEL,
                (client, handler, buf, callbacksConsumer) -> INSTANCE.onModCheckQuery()
        );

        ClientPlayConnectionEvents.JOIN.register(INSTANCE::onJoin);
        ClientPlayConnectionEvents.DISCONNECT.register(INSTANCE::onDisconnect);

        NetworkController.register(PongPayload.ID, INSTANCE::handlePongPayload);
    }

    public static boolean isOnFearServer() {
        return INSTANCE.isOnFearServer;
    }


    private CompletableFuture<PacketByteBuf> onModCheckQuery() {
        isModCheckedOnLogin = true;

        var response = PacketByteBufs.create();
        response.writeBytes(FearsomeMod.VERSION.getBytes(StandardCharsets.UTF_8));
        return CompletableFuture.completedFuture(response);
    }

    private void onJoin(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        isOnFearServer = isModCheckedOnLogin;
        isModCheckedOnLogin = false;
        if (!isOnFearServer) return;

        TimeSynchronizer.setT1(System.currentTimeMillis());

        NetworkController.send(PingPayload.INSTANCE);
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

        TimeSynchronizer.calculateDelta(payload);

        MinecraftClient.getInstance().execute(successfulHandshakeCallback);
    }
}
