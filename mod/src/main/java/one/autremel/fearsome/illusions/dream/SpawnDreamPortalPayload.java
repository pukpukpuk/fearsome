package one.autremel.fearsome.illusions.dream;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SpawnDreamPortalPayload(
        int portalId,
        double x,
        double y,
        double z,
        float yaw
) implements IServerPayload<SpawnDreamPortalPayload> {
    public static final ServerPayloadId<SpawnDreamPortalPayload> ID = new ServerPayloadId<>(
            SpawnDreamPortalPayload.class,
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, SpawnDreamPortalPayload::portalId,
                    PacketCodecs.DOUBLE, SpawnDreamPortalPayload::x,
                    PacketCodecs.DOUBLE, SpawnDreamPortalPayload::y,
                    PacketCodecs.DOUBLE, SpawnDreamPortalPayload::z,
                    PacketCodecs.FLOAT, SpawnDreamPortalPayload::yaw,
                    SpawnDreamPortalPayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<SpawnDreamPortalPayload> getPayloadId() {
        return ID;
    }
}
