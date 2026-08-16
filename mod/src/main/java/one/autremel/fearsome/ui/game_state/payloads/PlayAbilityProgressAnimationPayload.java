package one.autremel.fearsome.ui.game_state.payloads;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record PlayAbilityProgressAnimationPayload(
        int index,
        Float start,
        float target,
        int duration,
        boolean isLinear
) implements IServerPayload<PlayAbilityProgressAnimationPayload> {
    public static final ServerPayloadId<PlayAbilityProgressAnimationPayload> ID = new ServerPayloadId<>(
            PlayAbilityProgressAnimationPayload.class,
            PlayAbilityProgressAnimationPayload::decode
    );

    private static PlayAbilityProgressAnimationPayload decode(PacketByteBuf buf) {
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
    public @NotNull ServerPayloadId<PlayAbilityProgressAnimationPayload> getPayloadId() {
        return ID;
    }
}
