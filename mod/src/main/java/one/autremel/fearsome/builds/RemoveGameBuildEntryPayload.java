package one.autremel.fearsome.builds;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record RemoveGameBuildEntryPayload(int characterIndex) implements IServerPayload<RemoveGameBuildEntryPayload> {
    public static final ServerPayloadId<RemoveGameBuildEntryPayload> ID = new ServerPayloadId<>(
            RemoveGameBuildEntryPayload.class,
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, RemoveGameBuildEntryPayload::characterIndex,
                    RemoveGameBuildEntryPayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<RemoveGameBuildEntryPayload> getPayloadId() {
        return ID;
    }
}
