package one.autremel.fearsome.network;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import one.autremel.fearsome.network.ping.ModPingController;
import org.apache.logging.log4j.util.Lazy;

import java.util.function.Consumer;

public class NetworkController {
    private static NetworkController INSTANCE;

    private final Lazy<PayloadHandler[]> registeredHandlers = Lazy.lazy(() -> new PayloadHandler[PayloadId.getMaxId() + 1]);

    public static void initialize() {
        Preconditions.checkState(INSTANCE == null);
        INSTANCE = new NetworkController();

        PayloadTypeRegistry.playC2S().register(PayloadCodecsContainer.ID, PayloadCodecsContainer.MAIN_CODEC);
        PayloadTypeRegistry.playS2C().register(PayloadCodecsContainer.ID, PayloadCodecsContainer.MAIN_CODEC);

        ClientPlayNetworking.registerGlobalReceiver(PayloadCodecsContainer.ID, INSTANCE::handleReceivedPacket);
    }

    public static <T extends IPayload> void register(PayloadId<T> payloadId, Consumer<T> consumer) {
        var index = payloadId.getIntId();
        INSTANCE.registeredHandlers.get()[index] = new PayloadHandler<>(consumer);
    }

    private static void call(IPayload payload) {
        var payloadId = payload.getPayloadId();
        var handler = INSTANCE.registeredHandlers.get()[payloadId.getIntId()];
        Preconditions.checkState(handler != null, "There is no handler for payload " + payloadId.getClass().getName());

        handler.call(payload);
    }


    private void handleReceivedPacket(IPayload payload, ClientPlayNetworking.Context context) {
        if (!payload.getPayloadId().isOnlyWhileGameIsActive() || ModPingController.isGameActive()) {
            call(payload);
        }
    }
}
