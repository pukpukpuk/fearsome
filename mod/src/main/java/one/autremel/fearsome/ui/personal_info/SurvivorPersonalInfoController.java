package one.autremel.fearsome.ui.personal_info;

import com.google.common.base.Preconditions;
import one.autremel.fearsome.network.NetworkController;
import one.autremel.fearsome.ui.OverlayScreenRegistrar;
import one.autremel.fearsome.ui.UiController;
import one.autremel.fearsome.ui.survivor_list.DeathTimersController;
import one.autremel.fearsome.ui.terror.TerrorPartController;
import org.jetbrains.annotations.Nullable;

public class SurvivorPersonalInfoController {
    private static boolean initialized;

    @Nullable
    private static Data data;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(CreateSurvivorPersonalInfoScreenPayload.ID, SurvivorPersonalInfoController::handleCreatePayload);
        UiController.registerForScreensRemoval(SurvivorPersonalInfoController::handleScreensRemove, true);
        OverlayScreenRegistrar.registerBeforeScreenDrawing(SurvivorPersonalInfoScreen.ID, SurvivorPersonalInfoController::update);

        TerrorPartController.initialize();
    }

    private static void handleCreatePayload(CreateSurvivorPersonalInfoScreenPayload payload) {
        var screen = new SurvivorPersonalInfoScreen();
        OverlayScreenRegistrar.create(SurvivorPersonalInfoScreen.ID, screen);
        data = new Data(screen, payload.characterIndex());

        TerrorPartController.attach(screen.getTerrorPart(), payload.bounds());
    }

    private static void handleScreensRemove() {
        OverlayScreenRegistrar.remove(SurvivorPersonalInfoScreen.ID);
        data = null;

        TerrorPartController.detach();
    }

    private static void update() {
        if (data == null) return;

        TerrorPartController.update();

        var deathTimerProgress = DeathTimersController.getProgress(data.characterIndex());
        data.screen().getDeathTimerPart().setProgress(deathTimerProgress);
    }

    private record Data(SurvivorPersonalInfoScreen screen, int characterIndex) {}
}
