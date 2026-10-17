package one.autremel.fearsome.ui.components.binding;

import io.wispforest.owo.ui.base.BaseUIComponent;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

public class ActionControlLabel extends BaseUIComponent {
    private static final int GAP = 2;
    private static final int BOTTOM_MARGIN = 3;

    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    private AbstractBindingDrawer bindingDrawer = BindingDrawers.LEFT_MOUSE_DRAWER;
    private Text labelText = Text.empty();
    private boolean isVisible = true;

    public ActionControlLabel() {
        verticalSizing(Sizing.fixed(AbstractBindingDrawer.BASE_HEIGHT + BOTTOM_MARGIN));
    }

    public void setKeyId(KeyBinding binding) {
        bindingDrawer = BindingDrawers.get(binding);
    }

    public void setText(Text text) {
        labelText = text;
    }

    public void setVisible(boolean isVisible) {
        if (this.isVisible == isVisible) return;
        this.isVisible = isVisible;

        if (isVisible) horizontalSizing(Sizing.content());
        else horizontalSizing(Sizing.fixed(0));
    }

    @Override
    public void draw(OwoUIGraphics graphics, int mouseX, int mouseY, float partialTicks, float delta) {
        if (!isVisible) return;
        bindingDrawer.draw(graphics, x, y);
        graphics.drawText(
                textRenderer,
                labelText,
                x + bindingDrawer.determineHorizontalSize() + GAP,
                y + (AbstractBindingDrawer.BASE_HEIGHT - textRenderer.fontHeight) / 2 + 1,
                0xFFFFFFFF,
                true
        );
    }

    @Override
    protected int determineHorizontalContentSize(Sizing sizing) {
        return bindingDrawer.determineHorizontalSize() + GAP + textRenderer.getWidth(labelText);
    }
}
