package one.autremel.fearsome.ui.hints;

import com.google.common.base.Preconditions;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.network.PayloadId;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.UiController;
import one.autremel.fearsome.ui.hints.payloads.CreateHintsScreenPayload;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class HintsScreenController {
    private static boolean initialized;

    private static CompletableFuture<Data> dataInitTask;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;
        resetScreenInitTask();

        NetworkController.register(PayloadId.CREATE_HINTS_SCREEN, HintsScreenController::handleCreatePayload);
        UiController.registerForScreensRemoval(HintsScreenController::handleScreensRemove, true);
    }


    public static void acceptData(Consumer<Data> consumer) {
        dataInitTask.thenAccept(consumer);
    }


    private static void handleCreatePayload(CreateHintsScreenPayload payload) {
        var screen = new HintsScreen();
        OverlayScreenRegistrar.create(HintsScreen.ID, screen);

        dataInitTask.complete(new Data(screen));
    }

    private static void handleScreensRemove() {
        OverlayScreenRegistrar.remove(HintsScreen.ID);
        resetScreenInitTask();
    }

    private static void resetScreenInitTask() {
        if (dataInitTask != null && !dataInitTask.isDone()) {
            dataInitTask.cancel(true);
        }
        dataInitTask = new CompletableFuture<>();
    }

    public record Data(HintsScreen screen) {}
}
