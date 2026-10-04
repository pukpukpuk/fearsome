package one.autremel.fearsome.input;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.network.ping.ModPingController;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

public class KeyBindings {
    static final KeyBinding.Category GENERAL = KeyBinding.Category.create(FearsomeMod.identifier("general"));
    static final KeyBinding.Category UI = KeyBinding.Category.create(FearsomeMod.identifier("ui"));

    private static boolean initialized;

    private static final HashMap<BindingId, BindingData> bindingDataMap = new HashMap<>();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        createBindings();

        VanillaActionsSuppressor.initialize();
    }

    public static BindingData getData(BindingId id) {
        return bindingDataMap.get(id);
    }

    private static void createBindings() {
        createKeyboardBinding(BindingId.EXPAND_SURVIVOR_LIST, GLFW.GLFW_KEY_LEFT_ALT, UI);

        createMouseBinding(BindingId.ATTACK, GLFW.GLFW_MOUSE_BUTTON_LEFT, GENERAL);
        createMouseBinding(BindingId.FIRST_ABILITY, GLFW.GLFW_MOUSE_BUTTON_RIGHT, GENERAL);

        createKeyboardBinding(BindingId.SECOND_ABILITY, GLFW.GLFW_KEY_Q, GENERAL);
        createKeyboardBinding(BindingId.INTERACTION, GLFW.GLFW_KEY_E, GENERAL);

        ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
            createBinding(BindingId.SNEAK_KEY, client.options.sneakKey);
        });
    }

    private static void createBinding(BindingId id, KeyBinding keyBinding) {
        var data = new BindingData(id, keyBinding);
        bindingDataMap.put(id, data);

        if (id.getByteId() != -1) {
            KeyEvents.onToggle(id, isPressed -> {
                if (VanillaActionsSuppressor.isVanillaThingsMustBeSuppressed()) {
                    NetworkController.send(new InputPayload(id, isPressed));
                }
            });
        }
    }

    private static void createBinding(BindingId id, InputUtil.Type type, int code, KeyBinding.Category category) {
        var textId = id.getId();
        Preconditions.checkNotNull(textId);

        var keyBinding = new KeyBinding(FearsomeMod.bindingTextIdentifier(textId), type, code, category);
        KeyBindingHelper.registerKeyBinding(keyBinding);

        createBinding(id, keyBinding);
    }

    private static void createMouseBinding(BindingId id, int code, KeyBinding.Category category) {
        createBinding(id, InputUtil.Type.MOUSE, code, category);
    }

    private static void createKeyboardBinding(BindingId id, int code, KeyBinding.Category category) {
        createBinding(id, InputUtil.Type.KEYSYM, code, category);
    }
}
