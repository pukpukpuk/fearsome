package one.autremel.fearsome.ui.components;

import com.google.common.base.Preconditions;
import io.wispforest.owo.ui.base.BaseUIComponent;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.parsing.UIModel;
import io.wispforest.owo.ui.parsing.UIParsing;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import one.autremel.fearsome.FearsomeMod;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

public class KeyBindLabel extends BaseUIComponent {
    private static final int BASE_HEIGHT = 11;
    private static final String KEYBOARD_KEY_PREFIX = "key.keyboard.";
    private static final String MOUSE_KEY_PREFIX = "key.mouse.";
    private static final Map<String, BindingDrawer> CACHED_DRAWERS = new HashMap<>(Map.of(
            "key.mouse.left",   new MouseBindingDrawer(FearsomeMod.textureIdentifier("ui/icons/left_mouse")),
            "key.mouse.right",  new MouseBindingDrawer(FearsomeMod.textureIdentifier("ui/icons/right_mouse")),
            "key.mouse.middle", new MouseBindingDrawer(FearsomeMod.textureIdentifier("ui/icons/middle_mouse"))
    ));

    private BindingDrawer bindingDrawer;
    private boolean isVisible = true;

    public KeyBindLabel() {
        verticalSizing(Sizing.fixed(BASE_HEIGHT));
    }

    public void setBindingKey(String bindingKey) {
        bindingDrawer = CACHED_DRAWERS.computeIfAbsent(bindingKey, KeyboardBindingDrawer::new);
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
    }

    @Override
    protected int determineHorizontalContentSize(Sizing sizing) {
        return bindingDrawer.determineHorizontalSize();
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

    private class KeyboardBindingDrawer extends BindingDrawer {
        private static final int BACKGROUND_COLOR = 0xFF252A30;
        private static final int OUTLINE_COLOR = 0xFF41424F;
        private static final int TEXT_COLOR = 0xFFFFFFFF;
        private static final int PADDING = 1;

        private final Text text;
        private final int width;

        private KeyboardBindingDrawer(String bindingKey) {
            text = Text.of(tinifyBindingKey(bindingKey));
            width = MinecraftClient.getInstance().textRenderer.getWidth(text) + 1 + PADDING*2;
        }

        private static String tinifyBindingKey(String bindingKey) {
            if (bindingKey.startsWith(MOUSE_KEY_PREFIX)) {
                return "M" + bindingKey.substring(MOUSE_KEY_PREFIX.length()).toUpperCase();
            }

            Preconditions.checkState(bindingKey.startsWith(KEYBOARD_KEY_PREFIX));
            return tinifyKeyboardBindingKey(bindingKey);
        }

        private static String tinifyKeyboardBindingKey(String bindingKey) {
            if (bindingKey.length() == KEYBOARD_KEY_PREFIX.length()+1) {
                return bindingKey.substring(KEYBOARD_KEY_PREFIX.length()).toUpperCase();
            }

            final String translated = Language.getInstance().get(bindingKey);
            if (translated.length() == 1 || (translated.length() <= 3 && translated.startsWith("F"))) {
                return translated;
            }
            
            var words = translated.split(" ");

            if (words.length == 2) {
                if (words[1].equals("Arrow")) {
                    return switch (words[0]) {
                        case "Left" -> "←";
                        case "Right" -> "→";
                        case "Up" -> "↑";
                        case "Down" -> "↓";
                        default -> throw new IllegalStateException("Unexpected value: " + words[0]);
                    };
                }

                if (words[0].equals("Keypad")) {
                    return "NUM " + words[1].toUpperCase();
                }

                if (words[1].equals("Control")) words[1] = "CTRL";
                if (words[0].equals("Left")) return words[1].toUpperCase();
                if (words[0].equals("Right")) return "R" + words[1].toUpperCase();

            }
            words = null;

            return switch (translated) {
                case "Caps Lock" -> "CAPS";
                case "Delete" -> "DEL";
                case "Escape" -> "ESC";
                case "Insert" -> "INSRT";
                case "Page Up" -> "PAGEUP";
                case "Page Down" -> "PAGEDOWN";
                case "Num Lock" -> "NUMLOCK";
                case "Scroll Lock" -> "SCLOCK";
                case "Print Screen" -> "PRTSCR";
                default -> translated.toUpperCase();
            };
        }



        @Override
        void draw(OwoUIGraphics graphics, int x, int y) {
            graphics.fill(x, y, x+width, y+height, BACKGROUND_COLOR);
            graphics.drawRectOutline(x, y, width, height, OUTLINE_COLOR);

            graphics.drawText(
                    text,
                    x + 1 + PADDING,
                    y + 1 + PADDING,
                    1,
                    TEXT_COLOR
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
