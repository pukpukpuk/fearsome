package one.autremel.fearsome.ui.game_state;

import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.text.Text;
import one.autremel.fearsome.Utils;
import one.autremel.fearsome.ui.components.ColorableTextureComponent;
import one.autremel.fearsome.ui.DefaultBlurredSurface;

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

    @Getter
    private final EffectTable effectTable;

    private int remainingCount, totalCount;

    static GlobalInfoPart initialize(FlowLayout root) {
        DefaultBlurredSurface.setAsSurfaceFor(root);

        var timerLabel = root.childById(LabelComponent.class, "until_end_timer");

        var generatorsCountLabel = root.childById(LabelComponent.class, "generators_count");

        var questsCountLabel = root.childById(LabelComponent.class, "quests_count");
        var questsIcon = root.childById(ColorableTextureComponent.class, "quests_icon");

        var nameColumn = root.childById(FlowLayout.class, "effect_name_column");
        var timeColumn = root.childById(FlowLayout.class, "effect_time_column");
        var effectTable = new EffectTable(nameColumn, timeColumn);

        return new GlobalInfoPart(timerLabel, generatorsCountLabel, questsCountLabel, questsIcon, effectTable);
    }

    public void setRemainingTime(int remainingMillis) {
        timerLabel.text(Text.of(Utils.convertTimeToText(remainingMillis)));
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
