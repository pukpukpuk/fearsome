package one.autremel.fearsome.catalog;

import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.text.RomanNumerals;

import java.util.*;
import java.util.function.Function;

class CatalogReader {
    static Item readItem(PacketByteBuf buf) {
        return Registries.ITEM.get(Identifier.of(buf.readString()));
    }

    static Map<String, Object> readParams(PacketByteBuf buf) {
        var count = Byte.toUnsignedInt(buf.readByte());
        var params = new HashMap<String, Object>(count);
        for (int i = 0; i < count; i++) {
            var name = buf.readString();
            var type = buf.readByte();
            var value = buf.readDouble();
            params.put(name, type == 1 ? RomanNumerals.toRoman((int) Math.round(value)) : toNumber(value));
        }
        return Collections.unmodifiableMap(params);
    }

    static <T> List<T> readList(PacketByteBuf buf, int count, Function<PacketByteBuf, T> reader) {
        var list = new ArrayList<T>(count);
        for (int i = 0; i < count; i++) list.add(reader.apply(buf));
        return Collections.unmodifiableList(list);
    }

    private static Object toNumber(double value) {
        return value == Math.rint(value) ? (Object) (long) value : value;
    }
}
