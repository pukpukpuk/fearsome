package one.autremel.fearsome.network;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import org.jetbrains.annotations.NotNull;

@Payload
public record RemoveScreensPayload(
        boolean isForGameScreens
) implements IServerPayload<RemoveScreensPayload> {
    public static final ServerPayloadId<RemoveScreensPayload> ID = new ServerPayloadId<>(
            RemoveScreensPayload.class,
            false,
            PacketCodec.tuple(
                    PacketCodecs.BOOLEAN, RemoveScreensPayload::isForGameScreens,
                    RemoveScreensPayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<RemoveScreensPayload> getPayloadId() {
        return ID;
    }
}
