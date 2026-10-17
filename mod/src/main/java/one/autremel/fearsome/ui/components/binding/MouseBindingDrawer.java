package one.autremel.fearsome.ui.components.binding;

import io.wispforest.owo.ui.core.OwoUIGraphics;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.util.Identifier;

@RequiredArgsConstructor
public class MouseBindingDrawer extends AbstractBindingDrawer {
    private final Identifier textureId;

    @Override
    public void draw(OwoUIGraphics graphics, int x, int y) {
        graphics.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                textureId,
                x, y, 0, 0,
                9, BASE_HEIGHT, 9, BASE_HEIGHT
        );
    }

    @Override
    public int determineHorizontalSize() {
        return 9;
    }
}