package one.autremel.fearsome.ui;

import io.wispforest.owo.ui.event.WindowResizeCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;

import java.util.HashMap;
import java.util.function.Supplier;

public class OverlayScreenRegistrar {
    public static final Identifier ID = FearsomeMod.uiIdentifier("overlay_screens_container");

    private static final HashMap<Identifier, OverlayScreen> visibleMap = new HashMap<>();

    public static void create(Identifier identifier, OverlayScreen screen) {
        remove(identifier);

        var window = MinecraftClient.getInstance().getWindow();

        screen.init(window.getScaledWidth(), window.getScaledHeight());

        screen.setVisible(true);
        visibleMap.put(identifier, screen);
    }

    public static void remove(Identifier identifier) {
        var existing = visibleMap.remove(identifier);
        if (existing != null) {
            existing.setVisible(false);
        }
    }

    static {
        WindowResizeCallback.EVENT.register((client, window) -> {
            for (var screen : visibleMap.values()) {
                if (!screen.isVisible()) return;
                screen.getAdapter().moveAndResize(0, 0, window.getScaledWidth(), window.getScaledHeight());
            }
        });

        HudElementRegistry.addLast(ID, (context, tickCounter) -> {
            for (var screen : visibleMap.values()) {
                if (!screen.isVisible()) return;
                screen.getAdapter().render(context, -100, -100, tickCounter.getTickProgress(false));
            }
        });
    }

}
