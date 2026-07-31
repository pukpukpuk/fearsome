package one.autremel.fearsome.timer;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import org.jetbrains.annotations.NotNull;

public record TimerDeletePayload(String key, int characterIndex) implements IPayload {
    public static final PacketCodec<RegistryByteBuf, TimerDeletePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, TimerDeletePayload::key,
            PacketCodecs.VAR_INT, TimerDeletePayload::characterIndex,
            TimerDeletePayload::new);

    @Override
    public @NotNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.DELETE_TIMER;
    }
}
