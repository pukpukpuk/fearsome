package one.autremel.fearsome.builds;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Payload
public record GameBuildsPayload(List<Entry> entries) implements IServerPayload<GameBuildsPayload> {
    private static final CharacterTeam[] TEAMS = CharacterTeam.values();

    public static final ServerPayloadId<GameBuildsPayload> ID = new ServerPayloadId<>(
            GameBuildsPayload.class,
            GameBuildsPayload::decode
    );

    private static GameBuildsPayload decode(PacketByteBuf buf) {
        var count = Byte.toUnsignedInt(buf.readByte());
        var entries = new ArrayList<Entry>(count);
        for (int i = 0; i < count; i++) {
            var characterIndex = buf.readVarInt();
            var team = TEAMS[buf.readByte()];
            var playerName = buf.readString();
            var characterId = buf.readVarInt();

            var perksCount = Byte.toUnsignedInt(buf.readByte());
            var perkIds = new ArrayList<Integer>(perksCount);
            for (int j = 0; j < perksCount; j++) perkIds.add(buf.readVarInt());

            entries.add(new Entry(characterIndex, team, playerName, characterId, perkIds));
        }
        return new GameBuildsPayload(entries);
    }

    @Override
    public @NotNull ServerPayloadId<GameBuildsPayload> getPayloadId() {
        return ID;
    }

    public record Entry(int characterIndex, CharacterTeam team, String playerName, int characterId, List<Integer> perkIds) {}
}
