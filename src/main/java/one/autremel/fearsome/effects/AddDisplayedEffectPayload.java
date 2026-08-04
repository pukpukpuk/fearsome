package one.autremel.fearsome.effects;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record AddDisplayedEffectPayload(
        int effectId,
        int characterIndex,
        Optional<String> timerKey,
        Optional<String> name,
        Optional<Character> iconSymbol
) implements IPayload {
    private static final byte TIMER_KEY_MASK = 1;
    private static final byte NAME_MASK = 1 << 1;
    private static final byte ICON_MASK = 1 << 2;

    public static final PacketCodec<RegistryByteBuf, AddDisplayedEffectPayload> CODEC
            = PacketCodec.ofStatic(AddDisplayedEffectPayload::encode, AddDisplayedEffectPayload::decode);

    private static void encode(RegistryByteBuf buf, AddDisplayedEffectPayload payload) {
        throw new UnsupportedOperationException();
    }

    private static AddDisplayedEffectPayload decode(RegistryByteBuf buf) {
        var uniqueId = buf.readVarInt();
        var characterIndex = buf.readVarInt();

        var mask = buf.readByte();
        var timerKey = (mask & TIMER_KEY_MASK) != 0 ? Optional.of(buf.readString()) : Optional.<String>empty();
        var name = (mask & NAME_MASK) != 0 ? Optional.of(buf.readString()) : Optional.<String>empty();
        var iconSymbol = (mask & ICON_MASK) != 0 ? Optional.of(buf.readChar()) : Optional.<Character>empty();
        return new AddDisplayedEffectPayload(uniqueId, characterIndex, timerKey, name, iconSymbol);
    }

    @Override
    public @NotNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.ADD_DISPLAYED_EFFECT;
    }
}
