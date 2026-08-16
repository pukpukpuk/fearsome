package one.autremel.fearsome.ui.hints.payloads;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetActionProgressAnimationPayload(
        int actionId,
        Float start,
        float target,
        int duration
) implements IServerPayload<SetActionProgressAnimationPayload> {
    public static final ServerPayloadId<SetActionProgressAnimationPayload> ID = new ServerPayloadId<>(
            SetActionProgressAnimationPayload.class,
            SetActionProgressAnimationPayload::decode
    );

    public static SetActionProgressAnimationPayload decode(PacketByteBuf buf) {
        return new SetActionProgressAnimationPayload(
                buf.readInt(),
                IPayload.readOptionalFloatFromShort(buf),
                IPayload.readFloatFromShort(buf),
                buf.readVarInt()
        );
    }

    @Override
    public @NotNull ServerPayloadId<SetActionProgressAnimationPayload> getPayloadId() {
        return ID;
    }
}
