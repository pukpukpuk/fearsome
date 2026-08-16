package one.autremel.fearsome.timer;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

public record TimerDeletePayload(
        String key,
        int characterIndex
) implements IServerPayload<TimerDeletePayload> {
    public static final ServerPayloadId<TimerDeletePayload> ID = new ServerPayloadId<>(
            TimerDeletePayload.class,
            PacketCodec.tuple(
                    PacketCodecs.STRING, TimerDeletePayload::key,
                    PacketCodecs.VAR_INT, TimerDeletePayload::characterIndex,
                    TimerDeletePayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<TimerDeletePayload> getPayloadId() {
        return ID;
    }
}
