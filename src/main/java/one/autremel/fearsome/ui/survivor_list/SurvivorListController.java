package one.autremel.fearsome.ui.survivor_list;

import com.google.common.base.Preconditions;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.input.KeyBindings;
import one.autremel.fearsome.input.KeyEvents;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.network.PayloadId;
import one.autremel.fearsome.network.RemoveScreensPayload;
import one.autremel.fearsome.network.ping.TimeSynchronizer;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.survivor_list.payloads.CreateSurvivorListPayload;
import one.autremel.fearsome.ui.survivor_list.payloads.SetSurvivorDeadStatePayload;
import one.autremel.fearsome.ui.survivor_list.payloads.SetSurvivorHealthAmountPayload;
import org.jetbrains.annotations.Nullable;

public class SurvivorListController {
    private static boolean initialized;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(PayloadId.CREATE_SURVIVOR_LIST, SurvivorListController::handleCreatePayload);
        NetworkController.register(PayloadId.SET_SURVIVOR_HEALTH_AMOUNT, SurvivorListController::handleSetSurvivorHealthPayload);
        NetworkController.register(PayloadId.SET_SURVIVOR_DEAD_STATE, SurvivorListController::handleSetSurvivorDeadStatePayload);
        DeathTimersController.initialize();

        NetworkController.register(PayloadId.REMOVE_SCREENS, SurvivorListController::handleRemoveScreens);

        KeyEvents.onToggle(KeyBindings.EXPAND_SURVIVOR_LIST, SurvivorListController::onExpandKeyToggle);
    }

    private static void handleCreatePayload(CreateSurvivorListPayload payload) {
        OverlayScreenRegistrar.create(SurvivorList.ID, new SurvivorList(payload.entries()));
    }

    private static void handleSetSurvivorHealthPayload(SetSurvivorHealthAmountPayload payload) {
        var list = getList();
        if (list == null) return;

        var entry = list.getEntry(payload.index());
        entry.setHealth(payload.amount());
    }

    private static void handleSetSurvivorDeadStatePayload(SetSurvivorDeadStatePayload payload) {
        var list = getList();
        if (list == null) return;

        var entry = list.getEntry(payload.index());
        entry.setDeadState(payload.isDead());
    }

    private static void handleRemoveScreens(RemoveScreensPayload payload) {
        if (payload.isForGameScreens()) {
            OverlayScreenRegistrar.remove(SurvivorList.ID);
        }
    }


    private static void onExpandKeyToggle(boolean currentState) {
        var list = getList();
        if (list == null) return;

        switch (FearsomeMod.CONFIG.survivorListExpandBy()) {
            case HOLD -> list.setCollapsed(currentState);
            case TOGGLE -> {
                if (currentState) {
                    list.setCollapsed(!list.isCollapsed());
                }
            }
            default -> throw new RuntimeException();
        }
    }

    @Nullable
    static SurvivorList getList() {
        return (SurvivorList)OverlayScreenRegistrar.get(SurvivorList.ID);
    }
}
