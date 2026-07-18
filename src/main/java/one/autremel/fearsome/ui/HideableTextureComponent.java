package one.autremel.fearsome.ui;

import io.wispforest.owo.ui.component.TextureComponent;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import lombok.Setter;
import net.minecraft.util.Identifier;

public class HideableTextureComponent extends TextureComponent {
    @Setter
    private boolean isVisible = true;

    public HideableTextureComponent(Identifier texture, int u, int v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        super(texture, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
    }

    @Override
    public void draw(OwoUIGraphics graphics, int mouseX, int mouseY, float partialTicks, float delta) {
        if (!isVisible) return;
        super.draw(graphics, mouseX, mouseY, partialTicks, delta);
    }
}
