package one.autremel.fearsome.ui.components;

import io.wispforest.owo.ui.container.StackLayout;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.Sizing;
import lombok.Getter;
import one.autremel.fearsome.input.BindingId;
import one.autremel.fearsome.input.KeyBindings;
import one.autremel.fearsome.ui.components.binding.AbstractBindingDrawer;
import one.autremel.fearsome.ui.components.binding.BindingDrawers;
import org.w3c.dom.Element;

public class WithBindingStackLayout extends StackLayout {
    private final static int SHIFT_X = -5;
    private final static int SHIFT_Y = 1;

    @Getter
    private final BindingId bindingId;

    protected WithBindingStackLayout(BindingId bindingId) {
        super(Sizing.content(), Sizing.content());
        this.bindingId = bindingId;
    }

    public static WithBindingStackLayout parse(Element element) {
        var bindingIdString = element.getAttributeNode("binding_id").getTextContent();
        return new WithBindingStackLayout(BindingId.get(bindingIdString));
    }

    @Override
    public void draw(OwoUIGraphics graphics, int mouseX, int mouseY, float partialTicks, float delta) {
        super.draw(graphics, mouseX, mouseY, partialTicks, delta);

        getDrawer().draw(graphics, x + getRelativeX(), y + getRelativeY());
    }

    @Override
    protected int determineHorizontalContentSize(Sizing sizing) {
        var content = super.determineHorizontalContentSize(sizing);
        var drawerWidth = getDrawer().determineHorizontalSize();
        return content + Math.max(drawerWidth + SHIFT_X, 0);
    }

    private AbstractBindingDrawer getDrawer() {
        return BindingDrawers.get(KeyBindings.getData(bindingId).keyBinding());
    }

    private int getRelativeX() {
        return padding().get().left() + contentSize.width() + SHIFT_X;
    }

    private int getRelativeY() {
        return padding().get().top() + contentSize.height() + SHIFT_Y - AbstractBindingDrawer.BASE_HEIGHT;
    }
}
