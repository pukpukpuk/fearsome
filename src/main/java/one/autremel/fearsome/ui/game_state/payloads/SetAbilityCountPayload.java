package one.autremel.fearsome.ui.game_state.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import org.jetbrains.annotations.NotNull;

public record SetAbilityCountPayload(int count, int index) implements IPayload {
    public static final PacketCodec<RegistryByteBuf, SetAbilityCountPayload> CODEC
            = PacketCodec.ofStatic(SetAbilityCountPayload::encode, SetAbilityCountPayload::decode);

    private static void encode(RegistryByteBuf buf, SetAbilityCountPayload payload) {
        throw new UnsupportedOperationException();
    }

    private static SetAbilityCountPayload decode(RegistryByteBuf buf) {
        byte data = buf.readByte();
        var progress = data >> 2;
        var index = data & 0b11;

        return new SetAbilityCountPayload(progress, index);
    }

    @Override
    public @NotNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.PLAY_ABILITY_PROGRESS_ANIMATION;
    }
}
