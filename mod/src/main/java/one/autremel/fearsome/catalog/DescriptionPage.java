package one.autremel.fearsome.catalog;

import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;

import java.util.Map;

public record DescriptionPage(String key, Item icon, Map<String, Object> params) {
    static DescriptionPage read(PacketByteBuf buf) {
        var key = buf.readString();
        var icon = CatalogReader.readItem(buf);
        var params = CatalogReader.readParams(buf);
        return new DescriptionPage(key, icon, params);
    }
}
