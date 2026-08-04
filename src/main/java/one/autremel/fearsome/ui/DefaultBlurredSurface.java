package one.autremel.fearsome.ui;

import com.google.common.base.Preconditions;
import io.wispforest.owo.ui.base.BaseParentUIComponent;
import io.wispforest.owo.ui.core.*;
import io.wispforest.owo.ui.renderstate.BlurQuadElementRenderState;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.gui.ScreenRect;
import org.joml.Matrix3x2f;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultBlurredSurface implements Surface {
    public static final int DEFAULT_COLOR = 0x90181818;
    public static final int MARGIN = 2;
    private static final DefaultBlurredSurface INSTANCE = new DefaultBlurredSurface(true);

    private final boolean isUnmodifiable;

    private int color = DEFAULT_COLOR;

    public static DefaultBlurredSurface setAsSurfaceFor(BaseParentUIComponent component, boolean newInstance) {
        var surface = newInstance
                ? new DefaultBlurredSurface(false)
                : INSTANCE;

        component.surface(surface);
        component.padding(Insets.of(MARGIN));

        return surface;
    }

    public static void setAsSurfaceFor(BaseParentUIComponent component) {
        setAsSurfaceFor(component, false);
    }

    public void setColor(int color) {
        Preconditions.checkState(!isUnmodifiable);
        this.color = color;
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

        context.fill(x, y, component.x() + component.width(), component.y() + component.height(), color);
    }
}
