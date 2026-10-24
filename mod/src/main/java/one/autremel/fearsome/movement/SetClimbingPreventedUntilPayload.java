package one.autremel.fearsome.movement;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import one.autremel.fearsome.network.ping.TimeSynchronizer;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetClimbingPreventedUntilPayload(
        long preventedUntil
) implements IServerPayload<SetClimbingPreventedUntilPayload> {
    public static final ServerPayloadId<SetClimbingPreventedUntilPayload> ID = new ServerPayloadId<>(
            SetClimbingPreventedUntilPayload.class,
            SetClimbingPreventedUntilPayload::decode
    );

    private static SetClimbingPreventedUntilPayload decode(PacketByteBuf buf) {
        var preventedUntil = TimeSynchronizer.toLocalTime(buf.readLong());
        return new SetClimbingPreventedUntilPayload(preventedUntil);
    }

    @Override
    public @NotNull ServerPayloadId<SetClimbingPreventedUntilPayload> getPayloadId() {
        return ID;
    }
}
