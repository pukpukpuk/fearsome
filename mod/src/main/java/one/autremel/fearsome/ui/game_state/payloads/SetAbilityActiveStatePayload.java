package one.autremel.fearsome.ui.game_state.payloads;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record SetAbilityActiveStatePayload(
        int index, boolean isActive
) implements IServerPayload<SetAbilityActiveStatePayload> {
    public static final ServerPayloadId<SetAbilityActiveStatePayload> ID = new ServerPayloadId<>(
            SetAbilityActiveStatePayload.class,
            SetAbilityActiveStatePayload::decode
    );

    private static SetAbilityActiveStatePayload decode(PacketByteBuf buf) {
        byte b1 = buf.readByte();
        var index = b1 & 0b111;
        var isActive = ((b1 >> 3) & 1) == 1;
        return new SetAbilityActiveStatePayload(index, isActive);
    }

    @Override
    public @NotNull ServerPayloadId<SetAbilityActiveStatePayload> getPayloadId() {
        return ID;
    }
}
