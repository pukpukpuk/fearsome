package one.autremel.fearsome.ui.components;

import io.wispforest.owo.ui.base.BaseUIComponent;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.parsing.UIModel;
import io.wispforest.owo.ui.parsing.UIParsing;
import lombok.Setter;
import org.w3c.dom.Element;

import java.util.Map;

@Setter
public class LinearBar extends BaseUIComponent {
    private boolean isVertical;
    private int backgroundColor;
    private int progressColor = 0xFFFFFFFF;
    private float progress;

    public LinearBar() {
        this.sizing(Sizing.fill(), Sizing.fill());
    }

    @Override
    public void draw(OwoUIGraphics graphics, int mouseX, int mouseY, float partialTicks, float delta) {
        graphics.fill(x, y, x+width, y+height, backgroundColor);

        var clamped = Math.min(Math.max(progress, 0), 1);

        if (isVertical) {
            var progressEndPoint = y + (int)Math.floor(height * (1-clamped));
            graphics.fill(x, y+height, x+width, progressEndPoint, progressColor);
        } else {
            var progressEndPoint = x + (int)Math.ceil(width * clamped);
            graphics.fill(x, y, progressEndPoint, y+height, progressColor);
        }
    }

    @Override
    public void parseProperties(UIModel model, Element element, Map<String, Element> children) {
        super.parseProperties(model, element, children);

        UIParsing.expectChildren(element, children, "is-vertical");
        UIParsing.apply(children, "is-vertical", UIParsing::parseBool, this::setVertical);

        UIParsing.apply(children, "background-color", Color::parseAndPack, this::setBackgroundColor);
        UIParsing.apply(children, "progress-color", Color::parseAndPack, this::setProgressColor);
        UIParsing.apply(children, "progress", UIParsing::parseFloat, this::setProgress);
    }
}
