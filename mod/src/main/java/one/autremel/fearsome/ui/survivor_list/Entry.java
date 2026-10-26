package one.autremel.fearsome.ui.survivor_list;

import com.google.common.base.Preconditions;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import lombok.*;
import lombok.experimental.Accessors;
import one.autremel.fearsome.ui.components.ColorableTextureComponent;
import one.autremel.fearsome.ui.DefaultBlurredSurface;
import one.autremel.fearsome.ui.components.LinearBar;
import one.autremel.fearsome.ui.components.TiledTextureComponent;

@Accessors(fluent = true)
@Data
public class Entry {
    private static final int HEALTH_BAR_HEALTHY = 0xFF67FF67;
    private static final int HEALTH_BAR_INJURED = 0xFFBBFF56;
    private static final int HEALTH_BAR_FATAL = 0xFFF24A3E;

    private final String name;
    private final FlowLayout baseElement;
    private final TiledTextureComponent healthBarProgress;
    private final ColorableTextureComponent deadMarkTexture;
    private final FlowLayout textBlock;
    private final LinearBar deathTimerBar;
    private final LabelComponent effectsLine;
    private final DefaultBlurredSurface surface;
    private final NotificationsContainer notificationsContainer;

    @Setter(AccessLevel.PRIVATE)
    private boolean isDead;

    public void setHealth(int amount) {
        if (isDead) return;

        Preconditions.checkArgument(amount >= 1 && amount <= 20);

        healthBarProgress.setTileX(amount - 1);

        var color = amount == 20 ? HEALTH_BAR_HEALTHY : amount >= 10 ? HEALTH_BAR_INJURED : HEALTH_BAR_FATAL;

        healthBarProgress.setColor(color);
    }

    public void setDeadState(boolean isDead) {
        this.isDead = isDead;

        deadMarkTexture.setVisible(isDead);
        healthBarProgress.setVisible(!isDead);
    }

    public void setDeathTimerProgress(float progress) {
        deathTimerBar.setProgressColor(DeathTimersController.getColor(progress));
        deathTimerBar.setProgress(progress);
    }
}
