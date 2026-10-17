package one.autremel.fearsome.ui.game_state;

import io.wispforest.owo.ui.container.FlowLayout;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import one.autremel.fearsome.ui.DefaultBlurredSurface;
import one.autremel.fearsome.ui.components.ColorableTextureComponent;
import one.autremel.fearsome.ui.components.TiledTextureComponent;
import one.autremel.fearsome.ui.components.WithBindingStackLayout;

import java.util.List;

@RequiredArgsConstructor @Getter
class ToolsPart {
    private final FlowLayout root;
    private final ColorableTextureComponent buildsBrowserIcon;
    private final TiledTextureComponent generatorHighlighterIcon;
    private final List<WithBindingStackLayout> withBindingStackLayoutList;

    static ToolsPart initialize(FlowLayout root) {
        DefaultBlurredSurface.setAsSurfaceFor(root);

        var buildsBrowserIcon = root.childById(ColorableTextureComponent.class, "builds_browser_icon");
        var generatorHighlighterIcon = root.childById(TiledTextureComponent.class, "generator_highlighter_icon");

        var withBindingStackLayoutList = root.children().stream()
                .filter(child -> child instanceof WithBindingStackLayout)
                .map(child -> (WithBindingStackLayout)child)
                .toList();

        return new ToolsPart(
                root,
                buildsBrowserIcon,
                generatorHighlighterIcon,
                withBindingStackLayoutList);
    }
}
