package one.autremel.fearsome.ui.survivor_list;

import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.network.PayloadId;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;

public class SurvivorListController {
    public static void initialize() {
        NetworkController.register(PayloadId.CREATE_SURVIVOR_LIST, SurvivorListController::onCreatePayload);
    }

    private static void onCreatePayload(CreateSurvivorListPayload payload) {
        OverlayScreenRegistrar.create(SurvivorList.ID, new SurvivorList(payload.entries()));
    }
}
