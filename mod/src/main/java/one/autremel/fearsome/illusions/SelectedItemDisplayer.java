package one.autremel.fearsome.illusions;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import one.autremel.fearsome.network.NetworkController;

public class SelectedItemDisplayer {
    private static boolean initialized;

    private static final Int2ObjectOpenHashMap<Data> dataMap = new Int2ObjectOpenHashMap<>();

    static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(SetSelectedItemPayload.ID, SelectedItemDisplayer::handleItemSetPayload);
    }

    static void reset() {
        dataMap.clear();
    }


    public static ItemStack getItemFor(int entityId) {
        var data = dataMap.get(entityId);
        if (data == null) return ItemStack.EMPTY;

        return data.getShownItem();
    }

    private static void handleItemSetPayload(SetSelectedItemPayload payload) {
        var data = dataMap.computeIfAbsent(payload.entityId(), i -> new Data());

        var itemStack = new ItemStack(payload.item());
        if (payload.shownUntil() != null) {
            data.tempItem = itemStack;
            data.tempShownUntil = payload.shownUntil();
        } else {
            data.item = itemStack;
        }
    }

    private static class Data {
        public ItemStack
                item = ItemStack.EMPTY,
                tempItem = ItemStack.EMPTY;
        public long tempShownUntil;

        public ItemStack getShownItem() {
            return System.currentTimeMillis() < tempShownUntil
                    ? tempItem
                    : item;
        }
    }
}
