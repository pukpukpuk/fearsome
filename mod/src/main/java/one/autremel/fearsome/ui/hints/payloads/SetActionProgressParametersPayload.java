package one.autremel.fearsome.ui.hints.payloads;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetActionProgressParametersPayload(
        int actionId,
        byte[] data
) implements IServerPayload<SetActionProgressParametersPayload> {
    public static final ServerPayloadId<SetActionProgressParametersPayload> ID = new ServerPayloadId<>(
            SetActionProgressParametersPayload.class,
            SetActionProgressParametersPayload::decode
    );

    private static SetActionProgressParametersPayload decode(PacketByteBuf buf) {
        var actionId = buf.readVarInt();
        var data = new byte[buf.readableBytes()];
        buf.readBytes(data);

        return new SetActionProgressParametersPayload(actionId, data);
    }

    @Override
    public @NotNull ServerPayloadId<SetActionProgressParametersPayload> getPayloadId() {
        return ID;
    }
}
