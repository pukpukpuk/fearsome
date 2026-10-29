package one.autremel.fearsome.input;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.network.NetworkController;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class KeyBindings {
    static final KeyBinding.Category GENERAL = KeyBinding.Category.create(FearsomeMod.identifier("general"));
    static final KeyBinding.Category UI = KeyBinding.Category.create(FearsomeMod.identifier("ui"));
    static final KeyBinding.Category TOOLS = KeyBinding.Category.create(FearsomeMod.identifier("tools"));

    private static boolean initialized;

    private static final HashMap<BindingId, BindingData> bindingDataMap = new HashMap<>();
    private static final HashMap<KeyBinding, BindingId> keyBindingToBindingIdMap = new HashMap<>();
    private static final List<Consumer<BindingId>> boundKeyChangeSubscribers = new ArrayList<>();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        createBindings();

        VanillaActionsSuppressor.initialize();
    }

    public static BindingData getData(BindingId id) {
        return bindingDataMap.get(id);
    }

    public static void registerForBoundKeyChange(Consumer<BindingId> bindingIdConsumer) {
        boundKeyChangeSubscribers.add(bindingIdConsumer);
    }

    public static void onKeyBindingChanged(KeyBinding keyBinding) {
        var bindingId = keyBindingToBindingIdMap.get(keyBinding);
        if (bindingId == null) return;

        for (Consumer<BindingId> consumer : boundKeyChangeSubscribers) {
            consumer.accept(bindingId);
        }
    }


    private static void createBindings() {
        createKeyboardBinding(BindingId.EXPAND_SURVIVOR_LIST, GLFW.GLFW_KEY_LEFT_ALT, UI);
        createKeyboardBinding(BindingId.OPEN_BUILDS_BROWSER, GLFW.GLFW_KEY_Y, UI);

        createKeyboardBinding(BindingId.HIGHLIGHT_GENERATORS, GLFW.GLFW_KEY_H, TOOLS);


        createMouseBinding(BindingId.ATTACK, GLFW.GLFW_MOUSE_BUTTON_LEFT, GENERAL);
        createMouseBinding(BindingId.FIRST_ABILITY, GLFW.GLFW_MOUSE_BUTTON_RIGHT, GENERAL);

        createKeyboardBinding(BindingId.SECOND_ABILITY, GLFW.GLFW_KEY_Q, GENERAL);
        createKeyboardBinding(BindingId.THIRD_ABILITY, GLFW.GLFW_KEY_F, GENERAL);
        createKeyboardBinding(BindingId.INTERACTION, GLFW.GLFW_KEY_E, GENERAL);

        ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
            createBinding(BindingId.SNEAK_KEY, client.options.sneakKey);
        });
    }

    private static void createMouseBinding(BindingId id, int code, KeyBinding.Category category) {
        createBinding(id, InputUtil.Type.MOUSE, code, category);
    }

    private static void createKeyboardBinding(BindingId id, int code, KeyBinding.Category category) {
        createBinding(id, InputUtil.Type.KEYSYM, code, category);
    }

    private static void createBinding(BindingId id, InputUtil.Type type, int code, KeyBinding.Category category) {
        var textId = id.getId();
        Preconditions.checkNotNull(textId);

        var keyBinding = new KeyBinding(FearsomeMod.bindingTextIdentifier(textId), type, code, category);
        KeyBindingHelper.registerKeyBinding(keyBinding);

        createBinding(id, keyBinding);
    }

    private static void createBinding(BindingId id, KeyBinding keyBinding) {
        var data = new BindingData(id, keyBinding);
        bindingDataMap.put(id, data);
        keyBindingToBindingIdMap.put(keyBinding, id);

        if (id.getByteId() != -1) {
            BindingEvents.onToggle(id, isPressed -> {
                if (VanillaActionsSuppressor.isVanillaThingsMustBeSuppressed()) {
                    NetworkController.send(new InputPayload(id, isPressed));
                }
            });
        }
    }
}
