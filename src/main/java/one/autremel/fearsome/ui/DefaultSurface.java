package one.autremel.fearsome.ui;

import io.wispforest.owo.ui.base.BaseParentUIComponent;
import io.wispforest.owo.ui.component.UIComponents;
import io.wispforest.owo.ui.core.*;
import io.wispforest.owo.ui.renderstate.BlurQuadElementRenderState;
import net.minecraft.client.gui.ScreenRect;
import org.joml.Matrix3x2f;

public class DefaultSurface implements Surface {
    private static final DefaultSurface INSTANCE = new DefaultSurface();

    private static final int BACKGROUND_COLOR = 0x90181818;

    public static int MARGIN = 2;

    public static void setAsSurfaceFor(BaseParentUIComponent component) {
        component.surface(INSTANCE);
        component.padding(Insets.of(MARGIN));
    }

    @Override
    public void draw(OwoUIGraphics context, ParentUIComponent component) {
        var x = component.x();
        var y = component.y();

        var rect = new ScreenRect(
                component.x() - MARGIN,
                component.y() - MARGIN,
                component.width() + MARGIN*2,
                component.height() + MARGIN*2
        );

        context.state.addSimpleElement(new BlurQuadElementRenderState(
                new Matrix3x2f(context.getMatrices()),
                rect,
                rect,
                16, 5, 40
        ));

        context.fill(x, y, component.x() + component.width(), component.y() + component.height(), BACKGROUND_COLOR);
    }
}
