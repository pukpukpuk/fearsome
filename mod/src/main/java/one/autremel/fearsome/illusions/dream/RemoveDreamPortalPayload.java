package one.autremel.fearsome.illusions.dream;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record RemoveDreamPortalPayload(int portalId) implements IServerPayload<RemoveDreamPortalPayload> {
    public static final ServerPayloadId<RemoveDreamPortalPayload> ID = new ServerPayloadId<>(
            RemoveDreamPortalPayload.class,
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, RemoveDreamPortalPayload::portalId,
                    RemoveDreamPortalPayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<RemoveDreamPortalPayload> getPayloadId() {
        return ID;
    }
}
