package one.autremel.fearsome.ui.components;

import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.parsing.UIModel;
import io.wispforest.owo.ui.parsing.UIParsing;
import lombok.Setter;
import org.w3c.dom.Element;

import java.util.Map;

public class HideableFlowLayout extends FlowLayout {

    @Setter
    private boolean isVisible = true;

    protected HideableFlowLayout(Algorithm algorithm) {
        super(Sizing.content(), Sizing.content(), algorithm);
    }

    public static HideableFlowLayout parse(Element element) {
        UIParsing.expectAttributes(element, "direction");

        var algorithm = switch (element.getAttribute("direction")) {
            case "horizontal" -> Algorithm.HORIZONTAL;
            case "ltr-text-flow" -> Algorithm.LTR_TEXT;
            default -> Algorithm.VERTICAL;
        };

        return new HideableFlowLayout(algorithm);
    }

    @Override
    public void draw(OwoUIGraphics graphics, int mouseX, int mouseY, float partialTicks, float delta) {
        if (isVisible) {
            super.draw(graphics, mouseX, mouseY, partialTicks, delta);
        }
    }

    @Override
    public void parseProperties(UIModel model, Element element, Map<String, Element> children) {
        super.parseProperties(model, element, children);

        UIParsing.apply(children, "is-visible", UIParsing::parseBool, this::setVisible);
    }
}
