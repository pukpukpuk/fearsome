package one.autremel.fearsome.catalog;

import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;

import java.util.List;

public record CharacterEntry(
        int id,
        String key,
        int slots,
        boolean isPlayable,
        Item icon,
        float speed,
        float teamStat,
        float soundsRadius,
        List<DescriptionPage> abilityPages,
        List<DescriptionPage> passivePages
) {
    static CharacterEntry read(PacketByteBuf buf) {
        var id = buf.readVarInt();
        var key = buf.readString();
        var slots = Byte.toUnsignedInt(buf.readByte());
        var isPlayable = buf.readBoolean();
        var icon = CatalogReader.readItem(buf);

        var speed = buf.readFloat();
        var teamStat = buf.readFloat();
        var soundsRadius = buf.readFloat();

        var abilityPages = CatalogReader.readList(buf, Byte.toUnsignedInt(buf.readByte()), DescriptionPage::read);
        var passivePages = CatalogReader.readList(buf, Byte.toUnsignedInt(buf.readByte()), DescriptionPage::read);

        return new CharacterEntry(id, key, slots, isPlayable, icon, speed, teamStat, soundsRadius, abilityPages, passivePages);
    }
}
