package one.autremel.fearsome.builds;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.network.ClientPayloadId;
import one.autremel.fearsome.network.IClientPayload;
import one.autremel.fearsome.network.Payload;
import org.jetbrains.annotations.NotNull;

@Payload
public record SelectCharacterPayload(CharacterTeam team, int characterId) implements IClientPayload<SelectCharacterPayload> {
    public static final ClientPayloadId<SelectCharacterPayload> ID = new ClientPayloadId<>(
            SelectCharacterPayload.class,
            SelectCharacterPayload::encode
    );

    private static void encode(PacketByteBuf buf, SelectCharacterPayload payload) {
        buf.writeByte(payload.team().ordinal());
        buf.writeVarInt(payload.characterId());
    }

    @Override
    public @NotNull ClientPayloadId<SelectCharacterPayload> getPayloadId() {
        return ID;
    }
}
