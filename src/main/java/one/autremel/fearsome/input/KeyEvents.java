package one.autremel.fearsome.input;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.util.HashMap;

public class KeyEvents {
    private static boolean initialized;

    private static final HashMap<KeyBinding, KeyData> dataMap = new HashMap<>();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        ClientTickEvents.END_CLIENT_TICK.register(KeyEvents::onTick);
    }

    public static void onToggle(KeyBinding binding, BooleanConsumer consumer) {
        getData(binding).setOnToggle(consumer);
    }

    public static void onPressing(KeyBinding binding, Runnable runnable) {
        getData(binding).setOnPressing(runnable);
    }

    private static KeyData getData(KeyBinding binding) {
        return dataMap.computeIfAbsent(binding, b -> new KeyData());
    }


    private static void onTick(MinecraftClient client) {
        for (var entry : dataMap.entrySet()) {
            var keyBinding = entry.getKey();
            var data = entry.getValue();

            var isPressed = keyBinding.isPressed();
            if (data.isPressedLastTick() != isPressed) {
                data.setPressedLastTick(isPressed);
                if (data.getOnToggle() != null) data.getOnToggle().accept(isPressed);
            }

            if (isPressed) {
                if (data.getOnPressing() != null) data.getOnPressing().run();
            }
        }
    }
}
