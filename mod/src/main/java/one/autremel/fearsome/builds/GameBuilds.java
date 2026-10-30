package one.autremel.fearsome.builds;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import one.autremel.fearsome.catalog.CatalogContainer;
import one.autremel.fearsome.input.BindingEvents;
import one.autremel.fearsome.input.BindingId;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.ui.UiController;
import one.autremel.fearsome.ui.menus.BuildsBrowserScreen;
import one.autremel.fearsome.ui.menus.MenuNavigator;

import java.util.ArrayList;
import java.util.List;

public class GameBuilds {
    private static boolean initialized;

    @Getter
    private static final List<GameBuildsPayload.Entry> entries = new ArrayList<>();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(GameBuildsPayload.ID, GameBuilds::handleBuildsPayload);
        NetworkController.register(RemoveGameBuildEntryPayload.ID, GameBuilds::handleRemovePayload);
        UiController.registerForScreensRemoval(GameBuilds::reset, true);

        BindingEvents.onToggle(BindingId.OPEN_BUILDS_BROWSER, isPressed -> {
            if (isPressed) tryOpen();
        });
    }

    public static void reset() {
        entries.clear();
        if (MinecraftClient.getInstance().currentScreen instanceof BuildsBrowserScreen) MenuNavigator.closeAll();
    }

    private static void tryOpen() {
        if (entries.isEmpty() || !CatalogContainer.isLoaded()) return;
        if (MinecraftClient.getInstance().currentScreen != null) return;

        MenuNavigator.openRoot(new BuildsBrowserScreen(entries.size()));
    }

    private static void handleBuildsPayload(GameBuildsPayload payload) {
        entries.clear();
        entries.addAll(payload.entries());
        MenuNavigator.refreshCurrent();
    }

    private static void handleRemovePayload(RemoveGameBuildEntryPayload payload) {
        entries.removeIf(entry -> entry.characterIndex() == payload.characterIndex());

        if (MinecraftClient.getInstance().currentScreen instanceof BuildsBrowserScreen) {
            MenuNavigator.closeAll();
            if (!entries.isEmpty()) MenuNavigator.openRoot(new BuildsBrowserScreen(entries.size()));
        }
    }
}
