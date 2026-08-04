package one.autremel.fearsome.ui.survivor_list;

import com.google.common.base.Preconditions;
import io.wispforest.owo.ui.core.Easing;
import io.wispforest.owo.ui.core.Sizing;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class TextBlocksResizer {
    private static final Sizing COLLAPSED_SIZING = Sizing.fixed(0);

    private final Entry[] entries;
    private Sizing expandedSizing;

    void calculateMaxWidth() {
        Preconditions.checkState(expandedSizing == null);

        var maxWidth = 0;
        for (var entry : entries) {
            maxWidth = Math.max(maxWidth, entry.textBlock().width());
        }
//        expandedSizing = Sizing.fixed(maxWidth);
        expandedSizing = Sizing.content();
    }

    void setExpandedState(boolean isExpanded) {
        var sizing = isExpanded ? expandedSizing : COLLAPSED_SIZING;
        for (var entry : entries) {
            entry.textBlock().horizontalSizing(sizing);
        }
    }
}
