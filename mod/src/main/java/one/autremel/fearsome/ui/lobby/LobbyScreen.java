package one.autremel.fearsome.ui.lobby;

import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.ui.DefaultBlurredSurface;
import one.autremel.fearsome.ui.OverlayScreen;
import one.autremel.fearsome.ui.helpers.CollapseHelper;
import one.autremel.fearsome.ui.helpers.TimeLabelHelper;

public class LobbyScreen extends OverlayScreen {
    public static final String NAME = "lobby_screen";
    public static final Identifier ID = FearsomeMod.uiIdentifier(NAME);

    private LabelComponent
            playerCountLabel,
            inQueuePlayerCountLabel;

    private TimeLabelHelper timerLabel;
    private CollapseHelper timerLine;

    public LobbyScreen() {
        super(NAME);
    }

    public void setPlayerCount(int count) {
        playerCountLabel.text(Text.of(String.valueOf(count)));
    }

    public void setInQueueCount(int count) {
        inQueuePlayerCountLabel.text(Text.of(String.valueOf(count)));
    }

    public void setRemainingTime(int remainingMillis) {
        timerLabel.setRemainingTime(remainingMillis);
    }

    public void setTimerLineCollapsed(boolean collapsed) {
        timerLine.setCollapsed(collapsed);
    }

    @Override
    protected void buildInternal(FlowLayout rootComponent) {
        var root = rootComponent.childById(FlowLayout.class, "root");
        DefaultBlurredSurface.setAsSurfaceFor(root);

        playerCountLabel = root.childById(LabelComponent.class, "player_count_label");
        inQueuePlayerCountLabel = root.childById(LabelComponent.class, "in_queue_count_label");

        timerLabel = new TimeLabelHelper(root.childById(LabelComponent.class, "timer_label"));
        timerLine = CollapseHelper.of(root.childById(FlowLayout.class, "timer_line"), true);

        setTimerLineCollapsed(true);
    }
}
