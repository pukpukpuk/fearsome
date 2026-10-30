package one.autremel.fearsome.ui.menus;

import net.minecraft.client.MinecraftClient;

import java.util.ArrayDeque;

public class MenuNavigator {
    private static final ArrayDeque<InventoryMenuScreen> history = new ArrayDeque<>();

    public static void openRoot(InventoryMenuScreen screen) {
        history.clear();
        open(screen);
    }

    public static void open(InventoryMenuScreen screen) {
        history.push(screen);
        MinecraftClient.getInstance().setScreen(screen);
    }

    public static void back() {
        if (history.size() <= 1) {
            closeAll();
            return;
        }

        history.pop();
        MinecraftClient.getInstance().setScreen(history.peek());
    }

    public static void toRoot() {
        while (history.size() > 1) history.pop();

        var root = history.peek();
        if (root == null) closeAll();
        else MinecraftClient.getInstance().setScreen(root);
    }

    public static void refreshCurrent() {
        if (MinecraftClient.getInstance().currentScreen instanceof InventoryMenuScreen screen) screen.refresh();
    }

    public static void closeAll() {
        history.clear();
        MinecraftClient.getInstance().setScreen(null);
    }

    static void clear() {
        history.clear();
    }
}
