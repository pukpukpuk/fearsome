package one.autremel.fearsome.ui.game_state.payloads;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetQuestsCountPayload(
        int count
) implements IServerPayload<SetQuestsCountPayload> {
    public static final ServerPayloadId<SetQuestsCountPayload> ID = new ServerPayloadId<>(
            SetQuestsCountPayload.class,
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, SetQuestsCountPayload::count,
                    SetQuestsCountPayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<SetQuestsCountPayload> getPayloadId() {
        return ID;
    }
}
