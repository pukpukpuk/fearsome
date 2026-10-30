package one.autremel.fearsome.builds;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.network.ClientPayloadId;
import one.autremel.fearsome.network.IClientPayload;
import one.autremel.fearsome.network.Payload;
import org.jetbrains.annotations.NotNull;

@Payload
public record TogglePerkPayload(CharacterTeam team, int characterId, int perkId) implements IClientPayload<TogglePerkPayload> {
    public static final ClientPayloadId<TogglePerkPayload> ID = new ClientPayloadId<>(
            TogglePerkPayload.class,
            TogglePerkPayload::encode
    );

    private static void encode(PacketByteBuf buf, TogglePerkPayload payload) {
        buf.writeByte(payload.team().ordinal());
        buf.writeVarInt(payload.characterId());
        buf.writeVarInt(payload.perkId());
    }

    @Override
    public @NotNull ClientPayloadId<TogglePerkPayload> getPayloadId() {
        return ID;
    }
}
