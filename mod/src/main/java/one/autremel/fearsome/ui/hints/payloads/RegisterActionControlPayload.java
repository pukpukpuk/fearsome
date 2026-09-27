package one.autremel.fearsome.ui.hints.payloads;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.input.BindingId;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record RegisterActionControlPayload(
        int controlId,
        String translationKey,
        BindingId bindingId,
        short priority
) implements IServerPayload<RegisterActionControlPayload> {
    public static final ServerPayloadId<RegisterActionControlPayload> ID = new ServerPayloadId<>(
            RegisterActionControlPayload.class,
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, RegisterActionControlPayload::controlId,
                    PacketCodecs.STRING, RegisterActionControlPayload::translationKey,
                    BindingId.CODEC, RegisterActionControlPayload::bindingId,
                    PacketCodecs.SHORT, RegisterActionControlPayload::priority,
                    RegisterActionControlPayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<RegisterActionControlPayload> getPayloadId() {
        return ID;
    }
}
