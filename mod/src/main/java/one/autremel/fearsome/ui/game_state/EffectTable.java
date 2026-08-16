package one.autremel.fearsome.ui.game_state;

import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.component.UIComponents;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.text.Text;
import one.autremel.fearsome.Utils;
import one.autremel.fearsome.effects.DisplayedEffect;
import one.autremel.fearsome.timer.TimerUpdater;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class EffectTable {
    private static final Color DISPLAY_NAME_COLOR = Color.ofRgb(0xAAAAAA);
    private static final Color TIME_COLOR = Color.WHITE;
    private static final Sizing SHOWN_SIZING = Sizing.content();
    private static final Sizing HIDDEN_SIZING = Sizing.fixed(0);
    private static final Text EMPTY_TEXT = Text.of(" ");

    private final FlowLayout nameColumn, timeColumn;
    private final GridLayout table;
    private final Insets shownTableMargin, hiddenTableMargin;

    private final List<LabelComponent> nameLabels = new ArrayList<>();
    private final List<LabelComponent> timeLabels = new ArrayList<>();

    private int labelsUsedByNames, labelsUsedByTimes;

    public EffectTable(FlowLayout nameColumn, FlowLayout timeColumn) {
        this.nameColumn = nameColumn;
        this.timeColumn = timeColumn;
        this.table = (GridLayout) nameColumn.parent();

        shownTableMargin = table.margins().get();
        hiddenTableMargin = Insets.of(
                -((FlowLayout)table.parent()).gap(),
                shownTableMargin.bottom(),
                shownTableMargin.left(),
                shownTableMargin.right()
        );

        handleUsedLabelsCountChange();
    }

    private static Text getRemainingTimeText(DisplayedEffect effect) {
        var signature = effect.getTimerSignature();
        if (signature == null) return EMPTY_TEXT;

        var timer = TimerUpdater.getTimer(signature);
        return timer != null
                ? Text.of(Utils.convertTimeToText(timer.getRemainingTime()))
                : EMPTY_TEXT;
    }

    public void updateEffectNameColumn(Collection<DisplayedEffect> effects) {
        var effectsCount = effects.size();
        ensureLabelsCount(nameLabels, effectsCount, DISPLAY_NAME_COLOR, nameColumn);

        labelsUsedByNames = updateColumn(
                effects,
                nameLabels,
                DisplayedEffect::getDisplayNameText
        );
        handleUsedLabelsCountChange();
    }

    public void updateEffectTimeColumn(Collection<DisplayedEffect> effects) {
        ensureLabelsCount(timeLabels, effects.size(), TIME_COLOR, timeColumn);

        labelsUsedByTimes = updateColumn(
                effects,
                timeLabels,
                EffectTable::getRemainingTimeText
        );
        handleUsedLabelsCountChange();
    }


    private void ensureLabelsCount(List<LabelComponent> list, int minimalCount, Color color, FlowLayout parent) {
        while (list.size() < minimalCount) {
            var label = UIComponents.label(EMPTY_TEXT);
            label.color(color);
            parent.child(label);

            list.add(label);
        }
    }

    private int updateColumn(
            Collection<DisplayedEffect> effects,
            List<LabelComponent> labels,
            Function<DisplayedEffect, Text> textExtractor
    ) {
        var lineIndex = 0;
        for (var effect : effects) {
            var label = labels.get(lineIndex);
            label.text(textExtractor.apply(effect));
            label.verticalSizing(SHOWN_SIZING);

            lineIndex++;
        }

        for (var i = lineIndex; i < labels.size(); i++) {
            var label = labels.get(i);
            label.text(EMPTY_TEXT);
            label.verticalSizing(HIDDEN_SIZING);
        }

        return lineIndex;
    }

    private void handleUsedLabelsCountChange() {
        var anyLabel = labelsUsedByNames > 0 || labelsUsedByTimes > 0;

        table.verticalSizing(anyLabel ? SHOWN_SIZING : HIDDEN_SIZING);
        table.margins(anyLabel ? shownTableMargin : hiddenTableMargin);
    }
}
