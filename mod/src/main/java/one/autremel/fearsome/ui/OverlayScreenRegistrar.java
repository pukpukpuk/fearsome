package one.autremel.fearsome.ui;

import io.wispforest.owo.ui.event.WindowResizeCallback;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OverlayScreenRegistrar {
    public static final Identifier ID = FearsomeMod.uiIdentifier("overlay_screens_container");

    private static final HashMap<Identifier, ScreenData> visibleMap = new HashMap<>();

    public static void create(Identifier identifier, OverlayScreen screen) {
        remove(identifier);

        var window = MinecraftClient.getInstance().getWindow();
        screen.init(window.getScaledWidth(), window.getScaledHeight());

        screen.setVisible(true);

        get(identifier).screen = screen;
    }

    public static void registerBeforeScreenDrawing(Identifier identifier, Runnable runnable) {
        get(identifier).beforeDrawingSubscribers.add(runnable);
    }

    public static void remove(Identifier identifier) {
        var data = visibleMap.get(identifier);
        if (data == null || data.screen == null) return;

        data.screen.setVisible(false);
        data.screen = null;
    }

    private static ScreenData get(Identifier identifier) {
        return visibleMap.computeIfAbsent(identifier, i -> new ScreenData());
    }

    static {
        WindowResizeCallback.EVENT.register((client, window) -> {
            for (var data : visibleMap.values()) {
                var screen = data.screen;
                if (screen == null || !screen.isVisible()) continue;

                data.screen.getAdapter().moveAndResize(0, 0, window.getScaledWidth(), window.getScaledHeight());
            }
        });

        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, ID, (context, tickCounter) -> {
            for (var data : visibleMap.values()) {
                var screen = data.screen;
                if (screen == null || !screen.isVisible()) continue;

                data.beforeDrawingSubscribers.forEach(Runnable::run);
                screen.getAdapter().render(context, -100, -100, tickCounter.getTickProgress(false));
            }
        });
    }

    @RequiredArgsConstructor
    private static class ScreenData {
        private OverlayScreen screen;
        private final List<Runnable> beforeDrawingSubscribers = new ArrayList<>();
    }
}
