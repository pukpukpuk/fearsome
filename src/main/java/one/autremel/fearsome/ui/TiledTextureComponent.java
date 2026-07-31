package one.autremel.fearsome.ui;

import io.wispforest.owo.ui.base.BaseUIComponent;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.OwoUIPipelines;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.util.Identifier;

public class TiledTextureComponent extends ColorableTextureComponent {
    @Setter
    private int tileX, tileY;

    public TiledTextureComponent(Identifier texture, int u, int v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        super(texture, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
    }

    @Override
    protected int getU() {
        return u + tileX * regionWidth;
    }

    @Override
    protected int getV() {
        return v + tileY * regionHeight;
    }
}
