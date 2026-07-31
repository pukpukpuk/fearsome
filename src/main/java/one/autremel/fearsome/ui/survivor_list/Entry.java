package one.autremel.fearsome.ui.survivor_list;

import com.google.common.base.Preconditions;
import io.wispforest.owo.ui.container.FlowLayout;
import lombok.*;
import lombok.experimental.Accessors;
import one.autremel.fearsome.ui.ColorableTextureComponent;
import one.autremel.fearsome.ui.LinearBar;
import one.autremel.fearsome.ui.TiledTextureComponent;

@Accessors(fluent = true)
@Data
public class Entry {
    private static final int HEALTH_BAR_HEALTHY = 0xFF67FF67;
    private static final int HEALTH_BAR_INJURED = 0xFFBBFF56;
    private static final int HEALTH_BAR_FATAL = 0xFFF24A3E;

    private static final int DEATH_TIMER_FULL = 0xFF6DCE5F;
    private static final int DEATH_TIMER_HIGH = 0xFFF2F27B;
    private static final int DEATH_TIMER_MEDIUM = 0xFFFFA538;
    private static final int DEATH_TIMER_LOW = 0xFFE8634E;

    private final String name;
    private final FlowLayout baseElement;
    private final TiledTextureComponent healthBarProgress;
    private final ColorableTextureComponent deadMarkTexture;
    private final FlowLayout textBlock;
    private final LinearBar deathTimerBar;

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
        progress = Math.min(Math.max(progress, 0f), 1f);

        var color = DEATH_TIMER_LOW;
        var multiplied = progress * 3;
        if (multiplied >= 3) color = DEATH_TIMER_FULL;
        else if (multiplied >= 2) color = DEATH_TIMER_HIGH;
        else if (multiplied >= 1) color = DEATH_TIMER_MEDIUM;

        deathTimerBar.setProgressColor(color);
        deathTimerBar.setProgress(progress);
    }
}
