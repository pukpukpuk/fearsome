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
    private static final DefaultBlurredSurface INSTANCE = new DefaultBlurredSurface(true, MARGIN);

    private final boolean isUnmodifiable;

    private final int margin;
    private int color = DEFAULT_COLOR;

    public static void setAsSurfaceFor(BaseParentUIComponent component) {
        setAsSurfaceFor(component, false);
    }

    public static DefaultBlurredSurface setAsSurfaceFor(BaseParentUIComponent component, int marginOverride) {
        var surface = new DefaultBlurredSurface(false, marginOverride);
        set(surface, component);
        return surface;
    }

    public static DefaultBlurredSurface setAsSurfaceFor(BaseParentUIComponent component, boolean newInstance) {
        var surface = newInstance
                ? new DefaultBlurredSurface(false, MARGIN)
                : INSTANCE;
        set(surface, component);
        return surface;
    }

    private static void set(DefaultBlurredSurface surface, BaseParentUIComponent component) {
        var p = component.padding().get();
        var margin = surface.margin;
        component.padding(Insets.of(
                p.top() + margin,
                p.bottom() + margin,
                p.left() + margin,
                p.right() + margin
        ));
        component.surface(surface);
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
                component.x() - margin,
                component.y() - margin,
                component.width() + margin*2,
                component.height() + margin*2
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
