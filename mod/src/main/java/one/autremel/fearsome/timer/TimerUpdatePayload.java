package one.autremel.fearsome.timer;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record TimerUpdatePayload(
        String key,
        int characterIndex,
        long endTime,
        long pausedAt,
        boolean isCounting
) implements IServerPayload<TimerUpdatePayload> {
    public static final ServerPayloadId<TimerUpdatePayload> ID = new ServerPayloadId<>(
            TimerUpdatePayload.class,
            PacketCodec.tuple(
                    PacketCodecs.STRING, TimerUpdatePayload::key,
                    PacketCodecs.VAR_INT, TimerUpdatePayload::characterIndex,
                    PacketCodecs.LONG, TimerUpdatePayload::endTime,
                    PacketCodecs.LONG, TimerUpdatePayload::pausedAt,
                    PacketCodecs.BOOLEAN, TimerUpdatePayload::isCounting,
                    TimerUpdatePayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<TimerUpdatePayload> getPayloadId() {
        return ID;
    }
}
