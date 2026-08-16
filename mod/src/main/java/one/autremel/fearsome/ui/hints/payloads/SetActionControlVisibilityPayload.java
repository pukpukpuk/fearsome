package one.autremel.fearsome.ui.hints.payloads;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetActionControlVisibilityPayload(
        int controlId,
        boolean isVisible
) implements IServerPayload<SetActionControlVisibilityPayload> {
    public static final ServerPayloadId<SetActionControlVisibilityPayload> ID = new ServerPayloadId<>(
            SetActionControlVisibilityPayload.class,
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, SetActionControlVisibilityPayload::controlId,
                    PacketCodecs.BOOLEAN, SetActionControlVisibilityPayload::isVisible,
                    SetActionControlVisibilityPayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<SetActionControlVisibilityPayload> getPayloadId() {
        return ID;
    }
}
