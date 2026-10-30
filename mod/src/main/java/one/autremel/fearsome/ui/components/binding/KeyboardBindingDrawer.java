package one.autremel.fearsome.ui.components.binding;

import io.wispforest.owo.ui.core.OwoUIGraphics;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Language;

public class KeyboardBindingDrawer extends AbstractBindingDrawer {
    private static final String KEYBOARD_KEY_PREFIX = "key.keyboard.";

    private static final int BACKGROUND_COLOR = 0xFF252A30;
    private static final int OUTLINE_COLOR = 0xFF41424F;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int PADDING = 1;

    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    private final Text text;
    @Getter
    private final int width;

    KeyboardBindingDrawer(String keyId) {
        text = Text.of(tinify(keyId));
        width = textRenderer.getWidth(text) + 1 + PADDING*2;
    }

    public static String tinify(String keyId) {
        if (keyId.length() == KEYBOARD_KEY_PREFIX.length() + 1) {
            return keyId.substring(KEYBOARD_KEY_PREFIX.length()).toUpperCase();
        }

        var lang = Language.getInstance();
        var translated = lang.get("short_"+keyId, null);
        if (translated == null) translated = lang.get(keyId);

        return translated;
    }

    @Override
    public void draw(OwoUIGraphics graphics, int x, int y) {
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
    public int determineHorizontalSize() {
        return width;
    }

}