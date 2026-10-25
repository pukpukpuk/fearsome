package one.autremel.fearsome.illusions.ghost;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import one.autremel.fearsome.network.ping.TimeSynchronizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Payload
public record SetGhostManifestationPayload(
        int entityId,
        long startTime,
        long endTime,
        @Nullable Float start,
        float target
) implements IServerPayload<SetGhostManifestationPayload> {
    public static final ServerPayloadId<SetGhostManifestationPayload> ID = new ServerPayloadId<>(
            SetGhostManifestationPayload.class,
            SetGhostManifestationPayload::decode
    );

    private static SetGhostManifestationPayload decode(PacketByteBuf buf) {
        var startTime = System.currentTimeMillis();
        var entityId = buf.readVarInt();
        var endTime = TimeSynchronizer.toLocalTime(buf.readLong());
        var start = IPayload.readOptional01FloatFromShort(buf);
        var target = IPayload.read01FloatFromShort(buf);

        return new SetGhostManifestationPayload(entityId, startTime, endTime, start, target);
    }

    @Override
    public @NotNull ServerPayloadId<SetGhostManifestationPayload> getPayloadId() {
        return ID;
    }
}
