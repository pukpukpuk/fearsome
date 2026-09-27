package one.autremel.fearsome.ui.hints.payloads;

import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public class UnregisterAllActionControlsPayload implements IServerPayload<UnregisterAllActionControlsPayload> {
    public static final UnregisterAllActionControlsPayload INSTANCE = new UnregisterAllActionControlsPayload();
    public static final ServerPayloadId<UnregisterAllActionControlsPayload> ID = new ServerPayloadId<>(
            UnregisterAllActionControlsPayload.class,
            b -> INSTANCE
    );

    @Override
    public @NotNull ServerPayloadId<UnregisterAllActionControlsPayload> getPayloadId() {
        return ID;
    }
}
