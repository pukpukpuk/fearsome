package one.autremel.fearsome.network.ping;

import one.autremel.fearsome.network.ClientPayloadId;
import one.autremel.fearsome.network.IClientPayload;
import one.autremel.fearsome.network.Payload;
import org.jetbrains.annotations.NotNull;

@Payload
public record PingPayload() implements IClientPayload<PingPayload> {
    public static final ClientPayloadId<PingPayload> ID = new ClientPayloadId<>(
            PingPayload.class,
            (b, p) -> {}
    );
    public static final PingPayload INSTANCE = new PingPayload();

    @Override
    public @NotNull ClientPayloadId<PingPayload> getPayloadId() {
        return ID;
    }
}
