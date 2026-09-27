package one.autremel.fearsome.ui.hints.payloads;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import one.autremel.fearsome.network.ping.TimeSynchronizer;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetActionControlCooldownPayload(
        int controlId,
        long onCooldownUntil
) implements IServerPayload<SetActionControlCooldownPayload> {
    public static final ServerPayloadId<SetActionControlCooldownPayload> ID = new ServerPayloadId<>(
            SetActionControlCooldownPayload.class,
            SetActionControlCooldownPayload::decode
    );

    private static SetActionControlCooldownPayload decode(PacketByteBuf buf) {
        var controlId = buf.readVarInt();
        var onCooldownUntil = TimeSynchronizer.toLocalTime(buf.readLong());
        return new SetActionControlCooldownPayload(controlId, onCooldownUntil);
    }

    @Override
    public @NotNull ServerPayloadId<SetActionControlCooldownPayload> getPayloadId() {
        return ID;
    }
}
