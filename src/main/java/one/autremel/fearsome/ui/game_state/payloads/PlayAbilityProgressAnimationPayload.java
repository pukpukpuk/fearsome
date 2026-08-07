package one.autremel.fearsome.ui.game_state.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import org.jetbrains.annotations.NotNull;

public record PlayAbilityProgressAnimationPayload(
        int index,
        Float start,
        float target,
        int duration,
        boolean isLinear
) implements IPayload {

    public static final PacketCodec<RegistryByteBuf, PlayAbilityProgressAnimationPayload> CODEC
            = PacketCodec.ofStatic(PlayAbilityProgressAnimationPayload::encode, PlayAbilityProgressAnimationPayload::decode);

    private static void encode(RegistryByteBuf buf, PlayAbilityProgressAnimationPayload payload) {
        throw new UnsupportedOperationException();
    }

    private static PlayAbilityProgressAnimationPayload decode(RegistryByteBuf buf) {
        var data = buf.readByte();
        var index = (data >> 2) & 0b11;
        var isStartPredefined = ((data >> 1) & 0b1) == 1;
        var isLinear = (data & 0b1) == 1;

        var start = isStartPredefined ? IPayload.readFloatFromShort(buf) : null;
        var target = IPayload.readFloatFromShort(buf);
        var duration = buf.readVarInt();

        return new PlayAbilityProgressAnimationPayload(index, start, target, duration, isLinear);
    }

    @Override
    public @NotNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.PLAY_ABILITY_PROGRESS_ANIMATION;
    }
}
