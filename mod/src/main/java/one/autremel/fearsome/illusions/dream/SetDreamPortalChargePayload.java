package one.autremel.fearsome.illusions.dream;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import one.autremel.fearsome.network.ping.TimeSynchronizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Payload
public record SetDreamPortalChargePayload(
        int portalId,
        @Nullable Long chargeStartTime,
        @Nullable Long chargeEndTime
) implements IServerPayload<SetDreamPortalChargePayload> {
    public static final ServerPayloadId<SetDreamPortalChargePayload> ID = new ServerPayloadId<>(
            SetDreamPortalChargePayload.class,
            SetDreamPortalChargePayload::decode
    );

    private static final byte CHARGING_MASK = 1;
    private static final byte START_TIME_MASK = 1 << 1;

    private static SetDreamPortalChargePayload decode(PacketByteBuf buf) {
        var portalId = buf.readVarInt();
        var mask = buf.readByte();
        var chargeStartTime = (mask & START_TIME_MASK) != 0 ? TimeSynchronizer.toLocalTime(buf.readLong()) : null;
        var chargeEndTime = (mask & CHARGING_MASK) != 0 ? TimeSynchronizer.toLocalTime(buf.readLong()) : null;
        return new SetDreamPortalChargePayload(portalId, chargeStartTime, chargeEndTime);
    }

    @Override
    public @NotNull ServerPayloadId<SetDreamPortalChargePayload> getPayloadId() {
        return ID;
    }
}
