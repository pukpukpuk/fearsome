package one.autremel.fearsome.effects;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Payload
public record SetDisplayedEffectArgsPayload(
        int effectId,
        int characterIndex,
        List<Integer> args
) implements IServerPayload<SetDisplayedEffectArgsPayload> {
    public static final PacketCodec<ByteBuf, List<Integer>> ARGS_CODEC = PacketCodecs.VAR_INT.collect(PacketCodecs.toList());

    public static final ServerPayloadId<SetDisplayedEffectArgsPayload> ID = new ServerPayloadId<>(
            SetDisplayedEffectArgsPayload.class,
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, SetDisplayedEffectArgsPayload::effectId,
                    PacketCodecs.VAR_INT, SetDisplayedEffectArgsPayload::characterIndex,
                    ARGS_CODEC, SetDisplayedEffectArgsPayload::args,
                    SetDisplayedEffectArgsPayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<SetDisplayedEffectArgsPayload> getPayloadId() {
        return ID;
    }
}
