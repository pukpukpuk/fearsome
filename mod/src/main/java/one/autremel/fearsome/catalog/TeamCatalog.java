package one.autremel.fearsome.catalog;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.CharacterTeam;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record TeamCatalog(
        CharacterTeam team,
        MiddleValues middleValues,
        List<CharacterEntry> characters,
        List<PerkEntry> perks
) {
    static TeamCatalog read(PacketByteBuf buf, CharacterTeam team) {
        var middleValues = new MiddleValues(buf.readFloat(), buf.readFloat(), buf.readFloat());
        var characters = CatalogReader.readList(buf, buf.readVarInt(), CharacterEntry::read);
        var perks = CatalogReader.readList(buf, buf.readVarInt(), PerkEntry::read);
        return new TeamCatalog(team, middleValues, characters, perks);
    }

    @Nullable
    public CharacterEntry getCharacter(int id) {
        for (var character : characters) {
            if (character.id() == id) return character;
        }
        return null;
    }

    @Nullable
    public PerkEntry getPerk(int id) {
        for (var perk : perks) {
            if (perk.id() == id) return perk;
        }
        return null;
    }

    public record MiddleValues(float speed, float teamStat, float slots) {}
}
