package one.autremel.fearsome.ui.game_state.payloads;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record CreateGameStateScreenPayload(
        String mapKey,
        int generatorsCount,
        int ownCharacterIndex,
        String ownCharacterKey
) implements IServerPayload<CreateGameStateScreenPayload> {
    public static final ServerPayloadId<CreateGameStateScreenPayload> ID = new ServerPayloadId<>(
            CreateGameStateScreenPayload.class,
            CreateGameStateScreenPayload::decode
    );

    private static CreateGameStateScreenPayload decode(PacketByteBuf buf) {
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
    public @NotNull ServerPayloadId<CreateGameStateScreenPayload> getPayloadId() {
        return ID;
    }
}
