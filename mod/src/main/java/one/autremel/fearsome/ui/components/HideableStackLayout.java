package one.autremel.fearsome.ui.components;

import io.wispforest.owo.ui.container.StackLayout;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.Sizing;
import lombok.Setter;

public class HideableStackLayout extends StackLayout {

    @Setter
    private boolean isVisible;

    public HideableStackLayout() {
        super(Sizing.content(), Sizing.content());
    }

    @Override
    public void draw(OwoUIGraphics graphics, int mouseX, int mouseY, float partialTicks, float delta) {
        if (isVisible) {
            super.draw(graphics, mouseX, mouseY, partialTicks, delta);
        }
    }
}
