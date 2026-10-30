package one.autremel.fearsome.ui.menus;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public record MenuSlot(ItemStack icon, List<Text> tooltip) {
    public static MenuSlot of(Item item) {
        return of(item, List.of());
    }

    public static MenuSlot of(Item item, List<Text> tooltip) {
        return new MenuSlot(new ItemStack(item), tooltip);
    }

    public static MenuSlot of(Item item, boolean isGlinted, List<Text> tooltip) {
        var stack = new ItemStack(item);
        if (isGlinted) stack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
        return new MenuSlot(stack, tooltip);
    }
}
