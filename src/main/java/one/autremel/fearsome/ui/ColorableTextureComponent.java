package one.autremel.fearsome.ui;

import io.wispforest.owo.ui.base.BaseUIComponent;
import io.wispforest.owo.ui.component.TextureComponent;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.OwoUIPipelines;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.UIComponent;
import io.wispforest.owo.ui.parsing.UIParsing;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.util.Identifier;
import org.w3c.dom.Element;

@RequiredArgsConstructor
public class ColorableTextureComponent extends BaseUIComponent {
    private final Identifier texture;
    protected final int u, v;
    protected final int regionWidth, regionHeight;
    protected final int textureWidth, textureHeight;

    @Setter
    private boolean blend = false;
    @Setter
    private boolean isVisible = true;
    @Setter
    private int color = -1;

    public ColorableTextureComponent(Identifier texture, int sideLength) {
        this.texture = texture;
        u = v = 0;
        regionWidth = regionHeight = textureWidth = textureHeight = sideLength;
        sizing(Sizing.fixed(sideLength));
    }

    public static ColorableTextureComponent parse(Element element) {
        var texture = UIParsing.parseIdentifier(element.getAttributeNode("texture"));
        var sideLength = UIParsing.parseSignedInt(element.getAttributeNode("side-length"));
        return new ColorableTextureComponent(texture, sideLength);
    }

    @Override
    public void draw(OwoUIGraphics graphics, int mouseX, int mouseY, float partialTicks, float delta) {
        if (!isVisible) return;
        var matrices = graphics.getMatrices();
        matrices.pushMatrix();
        matrices.translate(x, y);
        matrices.scale(this.width / (float) this.regionWidth, this.height / (float) this.regionHeight);

        graphics.drawTexture(this.blend ? RenderPipelines.GUI_TEXTURED : OwoUIPipelines.GUI_TEXTURED_NO_BLEND,
                this.texture,
                0,
                0,
                getU(),
                getV(),
                regionWidth,
                regionHeight,
                regionWidth,
                regionHeight,
                this.textureWidth, this.textureHeight,
                color
        );

        matrices.popMatrix();
    }

    protected int getU() {
        return u;
    }

    protected int getV() {
        return v;
    }


}
