package one.autremel.fearsome.ui.personal_info;

import lombok.RequiredArgsConstructor;
import one.autremel.fearsome.ui.DefaultBlurredSurface;
import one.autremel.fearsome.ui.components.HideableFlowLayout;
import one.autremel.fearsome.ui.components.LinearBar;
import one.autremel.fearsome.ui.survivor_list.DeathTimersController;

@RequiredArgsConstructor
class DeathTimerPart {
    private final LinearBar bar;

    static DeathTimerPart initialize(HideableFlowLayout root) {
        DefaultBlurredSurface.setAsSurfaceFor(root, 1);

        var bar = root.childById(LinearBar.class, "death_timer_bar");
        return new DeathTimerPart(bar);
    }

    void setProgress(float progress) {
        bar.setProgressColor(DeathTimersController.getColor(progress));
        bar.setProgress(progress);
    }
}
