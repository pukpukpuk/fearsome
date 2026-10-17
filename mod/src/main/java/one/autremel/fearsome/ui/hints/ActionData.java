package one.autremel.fearsome.ui.hints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.text.Text;
import one.autremel.fearsome.ui.AnimationData;
import one.autremel.fearsome.ui.components.LinearBar;
import one.autremel.fearsome.ui.hints.payloads.SetActionProgressParametersPayload;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
@Getter @Setter
public abstract class ActionData {
    private final int actionId;
    private final int priority;

    @Nullable
    private AnimationData animation;
    private boolean isVisible;
    private float currentProgress;

    protected abstract void handle(SetActionProgressParametersPayload payload);

    protected abstract Text getText();

    public void update(HintsScreen screen) {
        updateAnimation();

        screen.setProgressBarText(getText());
        screen.setActionProgressAmount(currentProgress);
        onUpdateInternal(screen);
    }

    protected void onUpdateInternal(HintsScreen screen) {
        screen.setProgressBarColor(LinearBar.DEFAULT_PROGRESS_COLOR);
        screen.setProgressBarCollapsed(false);
    }

    private void updateAnimation() {
        if (animation == null) return;

        currentProgress = animation.getCurrentValue();
        if (animation.isEnded()) {
            currentProgress = animation.target();
            animation = null;
        }
    }
}
