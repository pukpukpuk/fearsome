package one.autremel.fearsome.ui.helpers;

import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.UIComponent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CollapseHelper {
    private final UIComponent component;
    private final Sizing
            initialHorizontalSizing,   initialVerticalSizing,
            collapsedHorizontalSizing, collapsedVerticalSizing;

    private boolean isCollapsed;

    public static CollapseHelper of(UIComponent component, boolean vertical) {
        if (!(component.parent() instanceof FlowLayout parent)) {
            throw new IllegalArgumentException("FlowLayout parent is required!");
        }

        var collapsedSizing = Sizing.fixed(-parent.gap());
        var zeroSizing = Sizing.fixed(0);
        var collapsedHorizontalSizing = vertical ? zeroSizing      : collapsedSizing;
        var collapsedVerticalSizing   = vertical ? collapsedSizing : zeroSizing;

        return new CollapseHelper(component,
                component.horizontalSizing().get(), component.verticalSizing().get(),
                collapsedHorizontalSizing, collapsedVerticalSizing);
    }

    public void setCollapsed(boolean collapsed) {
        if (isCollapsed == collapsed) return;
        isCollapsed = collapsed;

        if (collapsed) {
            component.sizing(collapsedHorizontalSizing, collapsedVerticalSizing);
        } else {
            component.sizing(initialHorizontalSizing, initialVerticalSizing);
        }
    }
}
