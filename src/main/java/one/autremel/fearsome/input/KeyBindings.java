package one.autremel.fearsome.input;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import one.autremel.fearsome.FearsomeMod;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private static final KeyBinding.Category GENERAL_CATEGORY = KeyBinding.Category.create(FearsomeMod.identifier("general"));

    public static final KeyBinding EXPAND_SURVIVOR_LIST = new KeyBinding(
            FearsomeMod.keyTextIdentifier("expand_survivor_list"),
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            GENERAL_CATEGORY
    );

    private static boolean initialized;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        KeyBindingHelper.registerKeyBinding(EXPAND_SURVIVOR_LIST);
    }
}
