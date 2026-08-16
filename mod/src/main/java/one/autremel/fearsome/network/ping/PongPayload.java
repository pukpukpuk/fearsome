package one.autremel.fearsome.network.ping;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record PongPayload(
        String version,
        long senderTimestamp
) implements IServerPayload<PongPayload> {
    public static final ServerPayloadId<PongPayload> ID = new ServerPayloadId<>(
            PongPayload.class,
            PacketCodec.tuple(
                    PacketCodecs.STRING, PongPayload::version,
                    PacketCodecs.LONG, PongPayload::senderTimestamp,
                    PongPayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<PongPayload> getPayloadId() {
        return ID;
    }
}
