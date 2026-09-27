package one.autremel.fearsome.ui.hints.payloads;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetActionControlActiveStatePayload(
        int controlId,
        boolean isActive
) implements IServerPayload<SetActionControlActiveStatePayload> {
    public static final ServerPayloadId<SetActionControlActiveStatePayload> ID = new ServerPayloadId<>(
            SetActionControlActiveStatePayload.class,
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, SetActionControlActiveStatePayload::controlId,
                    PacketCodecs.BOOLEAN, SetActionControlActiveStatePayload::isActive,
                    SetActionControlActiveStatePayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<SetActionControlActiveStatePayload> getPayloadId() {
        return ID;
    }
}
