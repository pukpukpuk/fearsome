package one.autremel.fearsome.ui.hints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.text.Text;
import one.autremel.fearsome.ui.AnimationData;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
@Getter @Setter
public class ActionData {
    private final int actionId;
    private final int priority;
    private final Text text;

    @Nullable
    private AnimationData animation;
    private boolean isVisible;
    private float currentProgress;

    public void updateAnimation() {
        if (animation == null) return;

        currentProgress = animation.getCurrentValue();
        if (animation.isEnded()) {
            currentProgress = animation.target();
            animation = null;
        }
    }
}
