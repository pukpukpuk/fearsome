package one.autremel.fearsome.illusions.manifestation;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import one.autremel.fearsome.network.ping.TimeSynchronizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Payload
public record SetManifestationPayload(
        int entityId,
        int manifestationId,
        long startTime,
        long endTime,
        @Nullable Float start,
        float target
) implements IServerPayload<SetManifestationPayload> {
    public static final ServerPayloadId<SetManifestationPayload> ID = new ServerPayloadId<>(
            SetManifestationPayload.class,
            SetManifestationPayload::decode
    );

    private static SetManifestationPayload decode(PacketByteBuf buf) {
        var startTime = System.currentTimeMillis();
        var entityId = buf.readVarInt();
        var manifestationId = Byte.toUnsignedInt(buf.readByte());
        var endTime = TimeSynchronizer.toLocalTime(buf.readLong());
        var start = IPayload.readOptional01FloatFromShort(buf);
        var target = IPayload.read01FloatFromShort(buf);

        return new SetManifestationPayload(entityId, manifestationId, startTime, endTime, start, target);
    }

    @Override
    public @NotNull ServerPayloadId<SetManifestationPayload> getPayloadId() {
        return ID;
    }
}
