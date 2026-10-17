package one.autremel.fearsome.ui.components;

import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.parsing.UIParsing;
import lombok.Setter;
import net.minecraft.util.Identifier;
import org.w3c.dom.Element;

public class TiledTextureComponent extends ColorableTextureComponent {
    @Setter
    private int tileX, tileY;

    public TiledTextureComponent(Identifier texture, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        super(texture, 0, 0, regionWidth, regionHeight, textureWidth, textureHeight);
        sizing(Sizing.fixed(regionWidth), Sizing.fixed(regionHeight));
    }

    public static TiledTextureComponent parse(Element element) {
        var sideLength = UIParsing.parseSignedInt(element.getAttributeNode("side_length"));

        return new TiledTextureComponent(
                UIParsing.parseIdentifier(element.getAttributeNode("texture")),
                sideLength, sideLength,
                UIParsing.parseSignedInt(element.getAttributeNode("texture_width")),
                UIParsing.parseSignedInt(element.getAttributeNode("texture_height"))
        );
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
