package one.autremel.fearsome.ui.components;

import com.google.common.base.Preconditions;
import io.wispforest.owo.ui.base.BaseUIComponent;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.parsing.UIModel;
import io.wispforest.owo.ui.parsing.UIParsing;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import one.autremel.fearsome.FearsomeMod;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

public class ActionControlLabel extends BaseUIComponent {
    private static final int BASE_HEIGHT = 11;
    private static final int GAP = 2;
    private static final int BOTTOM_MARGIN = 3;

    private static final String KEYBOARD_KEY_PREFIX = "key.keyboard.";
    private static final BindingDrawer LEFT_MOUSE_DRAWER = new MouseBindingDrawer(FearsomeMod.textureIdentifier("ui/icons/left_mouse"));
    private static final Map<String, BindingDrawer> CACHED_DRAWERS = new HashMap<>(Map.of(
            "key.mouse.left",   LEFT_MOUSE_DRAWER,
            "key.mouse.right",  new MouseBindingDrawer(FearsomeMod.textureIdentifier("ui/icons/right_mouse")),
            "key.mouse.middle", new MouseBindingDrawer(FearsomeMod.textureIdentifier("ui/icons/middle_mouse"))
    ));

    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    private BindingDrawer bindingDrawer = LEFT_MOUSE_DRAWER;
    private Text labelText = Text.empty();
    private boolean isVisible = true;

    public ActionControlLabel() {
        verticalSizing(Sizing.fixed(BASE_HEIGHT + BOTTOM_MARGIN));
    }

    public void setBindingKey(String bindingKey) {
        bindingDrawer = CACHED_DRAWERS.computeIfAbsent(bindingKey, KeyboardBindingDrawer::new);
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
                y + (BASE_HEIGHT - textRenderer.fontHeight) / 2 + 1,
                0xFFFFFFFF,
                true
        );
    }

    @Override
    protected int determineHorizontalContentSize(Sizing sizing) {
        return bindingDrawer.determineHorizontalSize() + GAP + textRenderer.getWidth(labelText);
    }

    @Override
    public void parseProperties(UIModel model, Element element, Map<String, Element> children) {
        super.parseProperties(model, element, children);
        UIParsing.apply(children, "binding_key", Node::getNodeValue, this::setBindingKey);
    }


    private abstract static class BindingDrawer {
        abstract void draw(OwoUIGraphics graphics, int x, int y);

        abstract int determineHorizontalSize();
    }

    private static class KeyboardBindingDrawer extends BindingDrawer {
        private static final int BACKGROUND_COLOR = 0xFF252A30;
        private static final int OUTLINE_COLOR = 0xFF41424F;
        private static final int TEXT_COLOR = 0xFFFFFFFF;
        private static final int PADDING = 1;

        private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        private final Text text;
        @Getter
        private final int width;

        private KeyboardBindingDrawer(String bindingKey) {
            text = Text.of(tinify(bindingKey));
            width = textRenderer.getWidth(text) + 1 + PADDING*2;
        }

        private static String tinify(String bindingKey) {
            if (bindingKey.length() == KEYBOARD_KEY_PREFIX.length() + 1) {
                return bindingKey.substring(KEYBOARD_KEY_PREFIX.length()).toUpperCase();
            }

            var lang = Language.getInstance();
            var translated = lang.get("short_"+bindingKey, null);
            if (translated == null) translated = lang.get(bindingKey);

            return translated;
        }

        @Override
        void draw(OwoUIGraphics graphics, int x, int y) {
            graphics.fill(x, y, x+width, y+BASE_HEIGHT, BACKGROUND_COLOR);
            graphics.drawRectOutline(x, y, width, BASE_HEIGHT, OUTLINE_COLOR);

            graphics.drawText(
                    textRenderer,
                    text,
                    x + 1 + PADDING,
                    y + 1 + PADDING,
                    TEXT_COLOR,
                    false
            );
        }

        @Override
        int determineHorizontalSize() {
            return width;
        }

    }

    @RequiredArgsConstructor
    private static class MouseBindingDrawer extends BindingDrawer {
        private final Identifier textureId;

        @Override
        void draw(OwoUIGraphics graphics, int x, int y) {
            graphics.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    textureId,
                    x, y, 0, 0,
                    9, BASE_HEIGHT, 9, BASE_HEIGHT
            );
        }

        @Override
        int determineHorizontalSize() {
            return 9;
        }
    }
}
