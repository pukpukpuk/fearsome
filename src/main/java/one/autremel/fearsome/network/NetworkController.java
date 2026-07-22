package one.autremel.fearsome.network;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import one.autremel.fearsome.network.ping.ModPingController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NetworkController {
    private static NetworkController INSTANCE;

    private final List<List<PayloadHandler>> registeredHandlers = createListForHandlers();

    public static void initialize() {
        Preconditions.checkState(INSTANCE == null);
        INSTANCE = new NetworkController();

        PayloadTypeRegistry.playC2S().register(PayloadCodecsContainer.ID, PayloadCodecsContainer.MAIN_CODEC);
        PayloadTypeRegistry.playS2C().register(PayloadCodecsContainer.ID, PayloadCodecsContainer.MAIN_CODEC);


        ClientPlayNetworking.registerGlobalReceiver(PayloadCodecsContainer.ID, INSTANCE::handleReceivedPacket);
    }

    public static <T extends IPayload> void register(PayloadId<T> payloadId, Consumer<T> consumer) {
        var index = payloadId.getIntId();

        var handlers = INSTANCE.registeredHandlers.get(index);
        handlers.add(new PayloadHandler<>(consumer));
    }

    private static void call(IPayload payload) {
        var payloadId = payload.getPayloadId();

        var index = payloadId.getIntId();
        var handlers = INSTANCE.registeredHandlers.get(index);
        Preconditions.checkState(!handlers.isEmpty(), "There is no handlers for payload " + payloadId.getClass().getName());

        for (var handler : handlers) {
            handler.call(payload);
        }
    }


    private void handleReceivedPacket(IPayload payload, ClientPlayNetworking.Context context) {
        if (!payload.getPayloadId().isOnlyWhileGameIsActive() || ModPingController.isGameActive()) {
            call(payload);
        }
    }


    private static List<List<PayloadHandler>> createListForHandlers() {
        var size = PayloadId.getMaxId() + 1;
        var result = new ArrayList<List<PayloadHandler>>(size);

        for (var i = 0; i < size; i++) {
            var element = PayloadId.get(i) != null ? new ArrayList<PayloadHandler>() : null;
            result.add(element);
        }

        return result;
    }
}
