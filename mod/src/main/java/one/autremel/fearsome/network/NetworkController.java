package one.autremel.fearsome.network;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import one.autremel.fearsome.network.ping.ModPingController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("rawtypes")
public class NetworkController {
    private static boolean initialized;

    private static final HashMap<ServerPayloadId<?>, List<Consumer<? extends IServerPayload>>> subscribersMap = new HashMap<>();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        registerPayloads();
    }

    public static <T extends IServerPayload<T>> void register(ServerPayloadId<T> id, Consumer<T> subscriber) {
        getSubscribersList(id).add(subscriber);
    }

    public static void send(IClientPayload<?> payload) {
        ClientPlayNetworking.send(payload);
    }


    private static <T extends IServerPayload<T>> List<Consumer<? extends IServerPayload>> getSubscribersList(ServerPayloadId<T> id) {
        return subscribersMap.computeIfAbsent(id, i -> new ArrayList<>());
    }

    private static <T extends IServerPayload<T>> void register(ServerPayloadId<T> payloadId) {
        PayloadTypeRegistry.playS2C().register(payloadId.getId(), payloadId.getCodec());

        var list = getSubscribersList(payloadId);
        ClientPlayNetworking.registerGlobalReceiver(payloadId.getId(), (payload, context) -> {
            if (payloadId.isOnlyWhileGameActive() && !ModPingController.isGameActive()) return;

            list.forEach(consumer -> ((Consumer<T>)consumer).accept(payload));
        });
    }

    private static <T extends IClientPayload<T>> void register(ClientPayloadId<T> payloadId) {
        PayloadTypeRegistry.playC2S().register(payloadId.getId(), payloadId.getCodec());
    }

    private static void registerPayloads() {
        for (var payloadId : one.autremel.fearsome.network.Payloads.CLIENT_PAYLOAD_IDS) {
            register(payloadId);
        }

        for (var payloadId : one.autremel.fearsome.network.Payloads.SERVER_PAYLOAD_IDS) {
            register(payloadId);
        }
    }
}
