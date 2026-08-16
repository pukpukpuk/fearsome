package one.autremel.fearsome.ui.hints.payloads;

import net.minecraft.network.codec.PacketCodec;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record CreateHintsScreenPayload() implements IServerPayload<CreateHintsScreenPayload> {
    public static final ServerPayloadId<CreateHintsScreenPayload> ID = new ServerPayloadId<>(
            CreateHintsScreenPayload.class,
            PacketCodec.unit(new CreateHintsScreenPayload())
    );

    @Override
    public @NotNull ServerPayloadId<CreateHintsScreenPayload> getPayloadId() {
        return ID;
    }
}
