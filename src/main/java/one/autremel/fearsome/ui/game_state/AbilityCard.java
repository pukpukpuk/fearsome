package one.autremel.fearsome.ui.game_state;

import io.wispforest.owo.ui.component.LabelComponent;
import net.minecraft.text.Text;
import one.autremel.fearsome.ui.HideableStackLayout;
import one.autremel.fearsome.ui.RadialProgressBar;

public record AbilityCard(
        RadialProgressBar progressBar,
        HideableStackLayout hideableCountRoot,
        LabelComponent countLabel
) {
    public void setProgress(float progress) {
        progressBar.setProgress(progress);
    }

    public void setCount(int count) {
        countLabel.text(Text.of(count+""));
        hideableCountRoot.setVisible(count >= 0);
    }
}
