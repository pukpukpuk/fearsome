package one.autremel.fearsome.ui.game_state.payloads;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetGeneratorsCountPayload(
        int count
) implements IServerPayload<SetGeneratorsCountPayload> {
    public static final ServerPayloadId<SetGeneratorsCountPayload> ID = new ServerPayloadId<>(
            SetGeneratorsCountPayload.class,
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, SetGeneratorsCountPayload::count,
                    SetGeneratorsCountPayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<SetGeneratorsCountPayload> getPayloadId() {
        return ID;
    }
}
