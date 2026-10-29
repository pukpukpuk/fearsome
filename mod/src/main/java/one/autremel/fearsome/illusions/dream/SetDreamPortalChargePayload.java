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
        @Nullable Long chargeEndTime
) implements IServerPayload<SetDreamPortalChargePayload> {
    public static final ServerPayloadId<SetDreamPortalChargePayload> ID = new ServerPayloadId<>(
            SetDreamPortalChargePayload.class,
            SetDreamPortalChargePayload::decode
    );

    private static SetDreamPortalChargePayload decode(PacketByteBuf buf) {
        var portalId = buf.readVarInt();
        var chargeEndTime = buf.readBoolean() ? TimeSynchronizer.toLocalTime(buf.readLong()) : null;

        return new SetDreamPortalChargePayload(portalId, chargeEndTime);
    }

    @Override
    public @NotNull ServerPayloadId<SetDreamPortalChargePayload> getPayloadId() {
        return ID;
    }
}
