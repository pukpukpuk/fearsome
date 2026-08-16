package one.autremel.fearsome.input;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import java.util.HashMap;

public class KeyEvents {
    private static boolean initialized;

    private static final HashMap<BindingData, PressingState> stateMap = new HashMap<>();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        ClientTickEvents.END_CLIENT_TICK.register(KeyEvents::onTick);
    }

    public static void onPressing(BindingId id, Runnable runnable) {
        getState(id).registerOnPressing(runnable);
    }

    public static void onToggle(BindingId id, BooleanConsumer consumer) {
        getState(id).registerOnToggle(consumer);
    }

    private static PressingState getState(BindingId id) {
        var data = KeyBindings.getData(id);
        return stateMap.computeIfAbsent(data, d -> new PressingState());
    }


    private static void onTick(MinecraftClient client) {
        for (var entry : stateMap.entrySet()) {
            var bindingData = entry.getKey();
            var state = entry.getValue();

            var isPressed = bindingData.keyBinding().isPressed();
            if (state.isPressedLastTick() != isPressed) {
                state.setPressedLastTick(isPressed);
                state.callOnToggle(isPressed);
            }

            if (isPressed) state.callOnPressing();
        }
    }
}
