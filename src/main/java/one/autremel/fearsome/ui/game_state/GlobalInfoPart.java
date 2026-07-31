package one.autremel.fearsome.ui.game_state;

import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.core.Color;
import lombok.RequiredArgsConstructor;
import net.minecraft.text.Text;
import one.autremel.fearsome.ui.ColorableTextureComponent;

@RequiredArgsConstructor
class GlobalInfoPart {
    public static final int WITHOUT_QUESTS_COLOR = 0xFF5E5E5E;
    public static final int WITH_QUESTS_COLOR = 0xFFFFFFFF;
    public static final Color WITHOUT_QUESTS_TEXT_COLOR = Color.ofArgb(WITHOUT_QUESTS_COLOR);
    public static final Color WITH_QUESTS_TEXT_COLOR = Color.ofArgb(WITH_QUESTS_COLOR);

    private final LabelComponent timerLabel;

    private final LabelComponent generatorsCountLabel;

    private final LabelComponent questsCountLabel;
    private final ColorableTextureComponent questsIcon;

    private int remainingCount, totalCount;

    public void setRemainingTime(int remainingMillis) {
        var totalSeconds = Math.ceilDiv(remainingMillis, 1000);

        var seconds = totalSeconds % 60;
        var secondsText = seconds < 10 ? ("0" + seconds) : seconds;

        var str = (totalSeconds / 60) + ":" + secondsText;
        timerLabel.text(Text.of(str));
    }

    public void setGeneratorsRemainingCount(int count) {
        remainingCount = count;
        updateGeneratorsText();
    }

    public void setGeneratorsTotalCount(int count) {
        totalCount = count;
        updateGeneratorsText();
    }

    public void setQuestsCount(int count) {
        if (count < 0) count = 0;
        questsCountLabel.text(Text.of(count+""));

        if (count == 0) {
            questsIcon.setColor(WITHOUT_QUESTS_COLOR);
            questsCountLabel.color(WITHOUT_QUESTS_TEXT_COLOR);
        } else {
            questsIcon.setColor(WITH_QUESTS_COLOR);
            questsCountLabel.color(WITH_QUESTS_TEXT_COLOR);
        }
    }

    private void updateGeneratorsText() {
        var str = remainingCount + "/" + totalCount;
        generatorsCountLabel.text(Text.of(str));
    }
}
