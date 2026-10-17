package one.autremel.fearsome.ui.components.binding;

import net.minecraft.client.option.KeyBinding;
import one.autremel.fearsome.FearsomeMod;

import java.util.HashMap;
import java.util.Map;

public class BindingDrawers {
    public static final AbstractBindingDrawer LEFT_MOUSE_DRAWER = new MouseBindingDrawer(FearsomeMod.textureIdentifier("ui/icons/left_mouse"));
    private static final Map<String, AbstractBindingDrawer> CACHED_DRAWERS = new HashMap<>(Map.of(
            "key.mouse.left",   LEFT_MOUSE_DRAWER,
            "key.mouse.right",  new MouseBindingDrawer(FearsomeMod.textureIdentifier("ui/icons/right_mouse")),
            "key.mouse.middle", new MouseBindingDrawer(FearsomeMod.textureIdentifier("ui/icons/middle_mouse"))
    ));

    public static AbstractBindingDrawer get(KeyBinding keyBinding) {
        return get(keyBinding.getBoundKeyTranslationKey());
    }

    public static AbstractBindingDrawer get(String keyId) {
        return CACHED_DRAWERS.computeIfAbsent(keyId, KeyboardBindingDrawer::new);
    }
}
