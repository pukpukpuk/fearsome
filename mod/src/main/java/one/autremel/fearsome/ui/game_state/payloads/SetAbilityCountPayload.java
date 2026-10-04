package one.autremel.fearsome.ui.game_state.payloads;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Payload
public record SetAbilityCountPayload(
        int index, @Nullable Integer count
) implements IServerPayload<SetAbilityCountPayload> {
    public static final ServerPayloadId<SetAbilityCountPayload> ID = new ServerPayloadId<>(
            SetAbilityCountPayload.class,
            SetAbilityCountPayload::decode
    );

    private static SetAbilityCountPayload decode(PacketByteBuf buf) {
        byte b1 = buf.readByte();
        var index = b1 & 0b111;

        var isCountPresent = ((b1 >> 3) & 1) == 1;
        var count = isCountPresent ? Byte.toUnsignedInt(buf.readByte()) : null;

        return new SetAbilityCountPayload(index, count);
    }

    @Override
    public @NotNull ServerPayloadId<SetAbilityCountPayload> getPayloadId() {
        return ID;
    }
}
