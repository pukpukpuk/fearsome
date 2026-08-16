package one.autremel.fearsome.effects;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record RemoveDisplayedEffectPayload(
        int effectId,
        int characterIndex
) implements IServerPayload<RemoveDisplayedEffectPayload> {
    public static final ServerPayloadId<RemoveDisplayedEffectPayload> ID = new ServerPayloadId<>(
            RemoveDisplayedEffectPayload.class,
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, RemoveDisplayedEffectPayload::effectId,
                    PacketCodecs.VAR_INT, RemoveDisplayedEffectPayload::characterIndex,
                    RemoveDisplayedEffectPayload::new
            )
    );

    @Override
    public @NotNull ServerPayloadId<RemoveDisplayedEffectPayload> getPayloadId() {
        return ID;
    }
}
