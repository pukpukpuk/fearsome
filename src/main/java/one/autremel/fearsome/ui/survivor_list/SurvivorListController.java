package one.autremel.fearsome.ui.survivor_list;

import com.google.common.base.Preconditions;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.input.KeyBindings;
import one.autremel.fearsome.input.KeyEvents;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.network.PayloadId;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.UiController;
import one.autremel.fearsome.ui.survivor_list.payloads.CreateSurvivorListPayload;
import one.autremel.fearsome.ui.survivor_list.payloads.SetSurvivorDeadStatePayload;
import one.autremel.fearsome.ui.survivor_list.payloads.SetSurvivorHealthAmountPayload;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SurvivorListController {
    private static boolean initialized;

    private static CompletableFuture<SurvivorList> listInitTask;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(PayloadId.CREATE_SURVIVOR_LIST, SurvivorListController::handleCreatePayload);
        NetworkController.register(PayloadId.SET_SURVIVOR_HEALTH_AMOUNT, SurvivorListController::handleSetSurvivorHealthPayload);
        NetworkController.register(PayloadId.SET_SURVIVOR_DEAD_STATE, SurvivorListController::handleSetSurvivorDeadStatePayload);

        UiController.registerForScreensRemoval(SurvivorListController::handleScreensRemove, true);
        resetListInitTask();

        KeyEvents.onToggle(KeyBindings.EXPAND_SURVIVOR_LIST, SurvivorListController::onExpandKeyToggle);

        DeathTimersController.initialize();
        EffectsPartController.initialize();
    }

    public static void acceptList(Consumer<SurvivorList> consumer) {
        listInitTask.thenAccept(consumer);
    }

    @Nullable
    public static SurvivorList getListNow() {
        return listInitTask != null ? listInitTask.getNow(null) : null;
    }


    private static void handleCreatePayload(CreateSurvivorListPayload payload) {
        var list = new SurvivorList(payload.entries());
        OverlayScreenRegistrar.create(SurvivorList.ID, list);

        listInitTask.complete(list);
    }

    private static void handleSetSurvivorHealthPayload(SetSurvivorHealthAmountPayload payload) {
        acceptList(survivorList -> {
            var entry = survivorList.getEntry(payload.index());
            entry.setHealth(payload.amount());
        });
    }

    private static void handleSetSurvivorDeadStatePayload(SetSurvivorDeadStatePayload payload) {
        acceptList(survivorList -> {
            var entry = survivorList.getEntry(payload.index());
            entry.setDeadState(payload.isDead());
        });
    }

    private static void handleScreensRemove() {
        OverlayScreenRegistrar.remove(SurvivorList.ID);
        resetListInitTask();
    }

    private static void onExpandKeyToggle(boolean currentState) {
        var survivorList = getListNow();
        if (survivorList == null) return;

        switch (FearsomeMod.CONFIG.survivorListExpandBy()) {
            case HOLD -> survivorList.setCollapsed(currentState);
            case TOGGLE -> {
                if (currentState) {
                    survivorList.setCollapsed(!survivorList.isCollapsed());
                }
            }
            default -> throw new RuntimeException();
        }
    }

    private static void resetListInitTask() {
        if (listInitTask != null && !listInitTask.isDone()) {
            listInitTask.cancel(true);
        }
        listInitTask = new CompletableFuture<>();
    }
}
