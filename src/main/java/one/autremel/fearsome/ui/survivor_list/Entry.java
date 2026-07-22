package one.autremel.fearsome.ui.survivor_list;

import com.google.common.base.Preconditions;
import io.wispforest.owo.ui.container.FlowLayout;
import one.autremel.fearsome.ui.HideableTextureComponent;
import one.autremel.fearsome.ui.TiledTextureComponent;

public record Entry(
        String name,
        FlowLayout baseElement,
        TiledTextureComponent healthBarProgress,
        HideableTextureComponent deadMarkTexture
) {
    public void setHealth(int amount) {
        Preconditions.checkArgument(amount >= 1 && amount <= 20);

        healthBarProgress.setTileX(amount - 1);

        var color = amount == 20 ? 0xFF67FF67 : amount >= 10 ? 0xFFBBFF56 : 0xFFF24A3E;

        healthBarProgress.setColor(color);
    }

    public void setDeadState(boolean isDead) {
        deadMarkTexture.setVisible(isDead);
        healthBarProgress.setVisible(!isDead);
    }
}
