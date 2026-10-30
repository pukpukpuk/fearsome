package one.autremel.fearsome.catalog;

import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;

import java.util.Map;

public record PerkEntry(int id, String key, int slots, boolean isEnabled, Item icon, Map<String, Object> params) {
    static PerkEntry read(PacketByteBuf buf) {
        var id = buf.readVarInt();
        var key = buf.readString();
        var slots = Byte.toUnsignedInt(buf.readByte());
        var isEnabled = buf.readBoolean();
        var icon = CatalogReader.readItem(buf);
        var params = CatalogReader.readParams(buf);
        return new PerkEntry(id, key, slots, isEnabled, icon, params);
    }
}
