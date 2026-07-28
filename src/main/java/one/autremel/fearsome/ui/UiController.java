package one.autremel.fearsome.ui;

import one.autremel.fearsome.ui.survivor_list.SurvivorList;
import one.autremel.fearsome.ui.survivor_list.SurvivorListController;

public class UiController {
    public static void initialize() {
        SurvivorListController.initialize();
    }

    public static void reset() {
        OverlayScreenRegistrar.remove(SurvivorList.ID);
    }
}
