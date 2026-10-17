package one.autremel.fearsome.ui.components.binding;

import io.wispforest.owo.ui.core.OwoUIGraphics;

public abstract class AbstractBindingDrawer {
    public static final int BASE_HEIGHT = 11;

    public abstract void draw(OwoUIGraphics graphics, int x, int y);

    public abstract int determineHorizontalSize();
}
