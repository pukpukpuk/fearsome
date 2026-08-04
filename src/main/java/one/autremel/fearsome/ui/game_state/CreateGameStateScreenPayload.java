package one.autremel.fearsome.ui.game_state;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import one.autremel.fearsome.effects.AddDisplayedEffectPayload;
import one.autremel.fearsome.network.IPayload;
import one.autremel.fearsome.network.PayloadId;
import one.autremel.fearsome.ui.survivor_list.payloads.CreateSurvivorListPayload;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record CreateGameStateScreenPayload(
        String mapKey,
        int generatorsCount,
        int ownCharacterIndex
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

        return new CreateGameStateScreenPayload(mapKey, generatorsCount, ownCharacterIndex);
    }

    @Override
    public @NotNull PayloadId<? extends IPayload> getPayloadId() {
        return PayloadId.CREATE_GAME_STATE_SCREEN;
    }
}
