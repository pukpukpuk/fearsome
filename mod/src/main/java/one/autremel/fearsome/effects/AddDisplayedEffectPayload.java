package one.autremel.fearsome.effects;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Payload
public record AddDisplayedEffectPayload(
        int effectId,
        int characterIndex,
        Optional<String> timerKey,
        Optional<String> name,
        Optional<Character> iconSymbol
) implements IServerPayload<AddDisplayedEffectPayload> {
    public static final ServerPayloadId<AddDisplayedEffectPayload> ID = new ServerPayloadId<>(
            AddDisplayedEffectPayload.class,
            AddDisplayedEffectPayload::decode
    );

    private static final byte TIMER_KEY_MASK = 1;
    private static final byte NAME_MASK = 1 << 1;
    private static final byte ICON_MASK = 1 << 2;

    private static AddDisplayedEffectPayload decode(PacketByteBuf buf) {
        var uniqueId = buf.readVarInt();
        var characterIndex = buf.readVarInt();

        var mask = buf.readByte();
        var timerKey = (mask & TIMER_KEY_MASK) != 0 ? Optional.of(buf.readString()) : Optional.<String>empty();
        var name = (mask & NAME_MASK) != 0 ? Optional.of(buf.readString()) : Optional.<String>empty();
        var iconSymbol = (mask & ICON_MASK) != 0 ? Optional.of(buf.readChar()) : Optional.<Character>empty();
        return new AddDisplayedEffectPayload(uniqueId, characterIndex, timerKey, name, iconSymbol);
    }

    @Override
    public @NotNull ServerPayloadId<AddDisplayedEffectPayload> getPayloadId() {
        return ID;
    }
}
