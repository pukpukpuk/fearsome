package one.autremel.fearsome.ui.hints.payloads;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetActionVisibilityPayload(
        int actionId,
        boolean isVisible
) implements IServerPayload<SetActionVisibilityPayload> {
    public static final ServerPayloadId<SetActionVisibilityPayload> ID = new ServerPayloadId<>(
            SetActionVisibilityPayload.class,
            PacketCodec.tuple(
                    PacketCodecs.INTEGER, SetActionVisibilityPayload::actionId,
                    PacketCodecs.BOOLEAN, SetActionVisibilityPayload::isVisible,
                    SetActionVisibilityPayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<SetActionVisibilityPayload> getPayloadId() {
        return ID;
    }
}
