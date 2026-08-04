package one.autremel.fearsome.effects;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import org.jetbrains.annotations.NotNull;

public record SetDisplayedEffectNamePayload(int effectId, int characterIndex, String newName) implements IPayload {
    public static final PacketCodec<RegistryByteBuf, SetDisplayedEffectNamePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, SetDisplayedEffectNamePayload::effectId,
            PacketCodecs.VAR_INT, SetDisplayedEffectNamePayload::characterIndex,
            PacketCodecs.STRING, SetDisplayedEffectNamePayload::newName,
            SetDisplayedEffectNamePayload::new
    );

    @Override
    public @NotNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.SET_DISPLAYED_EFFECT_NAME;
    }
}
