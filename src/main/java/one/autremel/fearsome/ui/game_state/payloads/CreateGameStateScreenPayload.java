package one.autremel.fearsome.ui.game_state.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import org.jetbrains.annotations.NotNull;

public record CreateGameStateScreenPayload(
        String mapKey,
        int generatorsCount,
        int ownCharacterIndex,
        String ownCharacterKey
) implements IPayload {
    public static final PacketCodec<RegistryByteBuf, CreateGameStateScreenPayload> CODEC
            = PacketCodec.ofStatic(CreateGameStateScreenPayload::encode, CreateGameStateScreenPayload::decode);

    private static void encode(RegistryByteBuf buf, CreateGameStateScreenPayload payload) {
        throw new UnsupportedOperationException();
    }

    private static CreateGameStateScreenPayload decode(RegistryByteBuf buf) {
        var mapKey = buf.readString();
        var generatorsCount = buf.readVarInt();
        var ownCharacterIndexRaw = buf.readByte();
        var ownCharacterIndex = (ownCharacterIndexRaw & 0x1) != 0
                ? (ownCharacterIndexRaw >> 1) : -1;
        var ownCharacterKey = buf.readString();
        if (ownCharacterKey.isEmpty()) ownCharacterKey = null;

        return new CreateGameStateScreenPayload(mapKey, generatorsCount, ownCharacterIndex, ownCharacterKey);
    }

    @Override
    public @NotNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.CREATE_GAME_STATE_SCREEN;
    }
}
