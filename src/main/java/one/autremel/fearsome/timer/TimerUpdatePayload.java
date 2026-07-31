package one.autremel.fearsome.timer;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import org.jetbrains.annotations.NotNull;

public record TimerUpdatePayload(String key, int characterIndex, long endTime, long pausedAt, boolean isCounting) implements IPayload {
    public static final PacketCodec<RegistryByteBuf, TimerUpdatePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, TimerUpdatePayload::key,
            PacketCodecs.VAR_INT, TimerUpdatePayload::characterIndex,
            PacketCodecs.LONG, TimerUpdatePayload::endTime,
            PacketCodecs.LONG, TimerUpdatePayload::pausedAt,
            PacketCodecs.BOOLEAN, TimerUpdatePayload::isCounting,
            TimerUpdatePayload::new);

    @Override
    public @NotNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.UPDATE_TIMER;
    }
}
