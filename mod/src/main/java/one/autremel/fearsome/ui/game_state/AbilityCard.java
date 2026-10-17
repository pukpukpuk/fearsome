package one.autremel.fearsome.ui.game_state;

import io.wispforest.owo.ui.component.LabelComponent;
import net.minecraft.text.Text;
import one.autremel.fearsome.ui.components.ColorableTextureComponent;
import one.autremel.fearsome.ui.components.HideableStackLayout;
import one.autremel.fearsome.ui.components.RadialProgressBar;

public record AbilityCard(
        RadialProgressBar progressBar,
        HideableStackLayout hideableCountRoot,
        LabelComponent countLabel,
        ColorableTextureComponent iconTexture
) {
    private static final int ACTIVE_COLOR = 0xFFFFFFFF;
    private static final int INACTIVE_COLOR = 0xFF707070;

    public void setProgress(float progress) {
        progressBar.setProgress(progress);
    }

    public void setCount(int count) {
        countLabel.text(Text.of(String.valueOf(count)));
        hideableCountRoot.setVisible(count >= 0);
    }

    public void hideCountLabel() {
        hideableCountRoot.setVisible(false);
    }

    public void setActiveState(boolean isActive) {
        iconTexture.setColor(isActive ? ACTIVE_COLOR : INACTIVE_COLOR);
    }
}
