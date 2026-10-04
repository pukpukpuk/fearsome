package one.autremel.fearsome.ui.hints;

import com.google.common.base.Preconditions;
import lombok.Getter;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.UiController;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class HintsScreenController {
    private static boolean initialized;

    @Getter @Nullable
    private static HintsScreen screen;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        ActionBarController.initialize();
        ActionControlsListController.initialize();
    }

    public static void onSuccessfulHandshake() {
        screen = new HintsScreen();
        OverlayScreenRegistrar.create(HintsScreen.ID, screen);
    }

    public static void reset() {
        OverlayScreenRegistrar.remove(HintsScreen.ID);
        screen = null;

        ActionControlsListController.resetControls();
    }

}
