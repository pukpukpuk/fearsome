package one.autremel.fearsome.ui.personal_info;

import io.wispforest.owo.ui.component.LabelComponent;
import lombok.RequiredArgsConstructor;
import one.autremel.fearsome.ui.DefaultBlurredSurface;
import one.autremel.fearsome.ui.components.HideableFlowLayout;
import one.autremel.fearsome.ui.components.LinearBar;
import one.autremel.fearsome.ui.helpers.TimeLabelHelper;
import one.autremel.fearsome.ui.survivor_list.DeathTimersController;

@RequiredArgsConstructor
class DeathTimerPart {
    private final HideableFlowLayout root;
    private final LinearBar bar;
    private final TimeLabelHelper timeLabel;

    static DeathTimerPart initialize(HideableFlowLayout root) {
        DefaultBlurredSurface.setAsSurfaceFor(root, 1);

        var bar = root.childById(LinearBar.class, "death_timer_bar");
        var timeLabel = new TimeLabelHelper(root.childById(LabelComponent.class, "death_timer_label"));
        return new DeathTimerPart(root, bar, timeLabel);
    }

    void setVisible(boolean isVisible) {
        root.setVisible(isVisible);
    }

    void update(int characterIndex) {
        var progress = DeathTimersController.getProgress(characterIndex);
        bar.setProgressColor(DeathTimersController.getColor(progress));
        bar.setProgress(progress);

        var remainingSeconds = Math.ceilDiv(DeathTimersController.getRemainingTime(characterIndex), 1000);
        timeLabel.setRemainingTime(remainingSeconds * 1000);
    }
}
