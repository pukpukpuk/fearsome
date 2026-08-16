package one.autremel.fearsome.ui.game_state.payloads;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetAbilityCountPayload(
        int count, int index
) implements IServerPayload<SetAbilityCountPayload> {
    public static final ServerPayloadId<SetAbilityCountPayload> ID = new ServerPayloadId<>(
            SetAbilityCountPayload.class,
            SetAbilityCountPayload::decode
    );

    private static SetAbilityCountPayload decode(PacketByteBuf buf) {
        byte data = buf.readByte();
        var progress = data >> 2;
        var index = data & 0b11;

        return new SetAbilityCountPayload(progress, index);
    }

    @Override
    public @NotNull ServerPayloadId<SetAbilityCountPayload> getPayloadId() {
        return ID;
    }
}
