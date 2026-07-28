package one.autremel.fearsome.ui;

import io.wispforest.owo.ui.base.BaseUIComponent;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.Sizing;
import lombok.Setter;

@Setter
public class LinearBar extends BaseUIComponent {
    private final int backgroundColor;
    private final boolean isVertical;
    private int progressColor = 0xFFFFFFFF;
    private float progress;

    public LinearBar(int backgroundColor, boolean isVertical) {
        this.sizing(Sizing.fill(), Sizing.fill());
        this.backgroundColor = backgroundColor;
        this.isVertical = isVertical;
    }


    @Override
    public void draw(OwoUIGraphics graphics, int mouseX, int mouseY, float partialTicks, float delta) {
        graphics.fill(x, y, x+width, y+height, backgroundColor);

        if (isVertical) {
            var progressEndPoint = y + (int)Math.floor(height * (1-progress));
            graphics.fill(x, y+height, x+width, progressEndPoint, progressColor);
        } else {
            var progressEndPoint = x + (int)Math.ceil(width * progress);
            graphics.fill(x, y, progressEndPoint, y+height, progressColor);
        }
    }
}
