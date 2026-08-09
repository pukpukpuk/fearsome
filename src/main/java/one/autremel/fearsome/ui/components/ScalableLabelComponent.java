package one.autremel.fearsome.ui.components;

import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.parsing.UIModel;
import io.wispforest.owo.ui.parsing.UIParsing;
import lombok.Setter;
import net.minecraft.text.Text;
import org.w3c.dom.Element;

import java.util.Map;

public class ScalableLabelComponent extends LabelComponent {
    @Setter
    private float scale;

    public ScalableLabelComponent(Text text) {
        super(text);
    }

    @Override
    public void draw(OwoUIGraphics graphics, int mouseX, int mouseY, float partialTicks, float delta) {
        graphics.push().translate(x(), y()).scale(scale, scale).translate(-x(), -y());

        super.draw(graphics, mouseX, mouseY, partialTicks, delta);

        graphics.pop();
    }

    @Override
    public int width() {
        return Math.round(super.width() * scale);
    }

    @Override
    public int height() {
        return Math.round(super.height() * scale);
    }

    @Override
    public void parseProperties(UIModel model, Element element, Map<String, Element> children) {
        super.parseProperties(model, element, children);
        UIParsing.apply(children, "scale", UIParsing::parseFloat, this::setScale);
    }
}
