package one.autremel.fearsome.ui;

import io.wispforest.owo.ui.base.BaseUIComponent;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.OwoUIPipelines;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.util.Identifier;

@RequiredArgsConstructor
public class TiledTextureComponent extends BaseUIComponent {
    private final Identifier texture;
    protected final int u, v;
    protected final int regionWidth, regionHeight;
    protected final int textureWidth, textureHeight;

    @Setter
    private boolean blend = false;
    @Setter
    private int tileX, tileY;
    @Setter
    private int color = -1;


    @Override
    public void draw(OwoUIGraphics graphics, int mouseX, int mouseY, float partialTicks, float delta) {
        var matrices = graphics.getMatrices();
        matrices.pushMatrix();
        matrices.translate(x, y);
        matrices.scale(this.width / (float) this.regionWidth, this.height / (float) this.regionHeight);

        graphics.drawTexture(this.blend ? RenderPipelines.GUI_TEXTURED : OwoUIPipelines.GUI_TEXTURED_NO_BLEND,
                this.texture,
                0,
                0,
                u + tileX * regionWidth,
                v + tileY * regionHeight,
                regionWidth,
                regionHeight,
                regionWidth,
                regionHeight,
                this.textureWidth, this.textureHeight,
                color
        );

        matrices.popMatrix();
    }
}
