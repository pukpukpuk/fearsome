package one.autremel.fearsome.render;

import lombok.Getter;
import net.minecraft.client.render.OverlayTexture;

public enum CustomOverlay {
    GHOST_EDGE(0, 0, 0x4952E5E5),
    ;

    @Getter
    private final int column, row;
    @Getter
    private final int color;
    @Getter
    private final int uv;

    CustomOverlay(int column, int row, int color) {
        this.column = column;
        this.row = row;
        this.color = color;
        this.uv = OverlayTexture.packUv(column, row);
    }
}
