package one.autremel.fearsome.effects;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import org.jetbrains.annotations.NotNull;

public record RemoveDisplayedEffectPayload(int effectId, int characterIndex) implements IPayload {
    public static final PacketCodec<RegistryByteBuf, RemoveDisplayedEffectPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, RemoveDisplayedEffectPayload::effectId,
            PacketCodecs.VAR_INT, RemoveDisplayedEffectPayload::characterIndex,
            RemoveDisplayedEffectPayload::new
    );

    @Override
    public @NotNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.REMOVE_DISPLAYED_EFFECT;
    }
}
