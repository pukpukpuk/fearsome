package one.autremel.fearsome.ui.survivor_list;

import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.network.PayloadId;
import one.autremel.fearsome.network.RemoveScreensPayload;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;

public class SurvivorListController {
    public static void initialize() {
        NetworkController.register(PayloadId.CREATE_SURVIVOR_LIST, SurvivorListController::handleCreatePayload);
        NetworkController.register(PayloadId.SET_SURVIVOR_HEALTH_AMOUNT, SurvivorListController::handleSetSurvivorHealthPayload);
        NetworkController.register(PayloadId.SET_SURVIVOR_DEAD_STATE, SurvivorListController::handleSetSurvivorDeadStatePayload);
        NetworkController.register(PayloadId.REMOVE_SCREENS, SurvivorListController::handleRemoveScreens);
    }

    private static void handleCreatePayload(CreateSurvivorListPayload payload) {
        OverlayScreenRegistrar.create(SurvivorList.ID, new SurvivorList(payload.entries()));
    }

    private static void handleSetSurvivorHealthPayload(SetSurvivorHealthAmountPayload payload) {
        var list = (SurvivorList)OverlayScreenRegistrar.get(SurvivorList.ID);
        var entry = list.getEntry(payload.index());

        entry.setHealth(payload.amount());
    }

    private static void handleSetSurvivorDeadStatePayload(SetSurvivorDeadStatePayload payload) {
        var list = (SurvivorList)OverlayScreenRegistrar.get(SurvivorList.ID);
        var entry = list.getEntry(payload.index());

        entry.setDeadState(payload.isDead());
    }

    private static void handleRemoveScreens(RemoveScreensPayload payload) {
        if (payload.isForGameScreens()) {
            OverlayScreenRegistrar.remove(SurvivorList.ID);
        }
    }
}
