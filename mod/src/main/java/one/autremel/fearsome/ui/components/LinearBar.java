package one.autremel.fearsome.ui.components;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import io.wispforest.owo.ui.base.BaseUIComponent;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.parsing.UIModel;
import io.wispforest.owo.ui.parsing.UIParsing;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;
import org.jspecify.annotations.Nullable;
import org.w3c.dom.Element;

import java.util.Map;

@Setter
public class LinearBar extends BaseUIComponent {
    public static final int DEFAULT_PROGRESS_COLOR = 0xFF839FAF;

    private boolean isVertical;
    private int backgroundColor;
    private int progressColor = DEFAULT_PROGRESS_COLOR;
    private float progress;

    public LinearBar() {
        this.sizing(Sizing.fill(), Sizing.fill());
    }

    @Override
    public void draw(OwoUIGraphics graphics, int mouseX, int mouseY, float partialTicks, float delta) {
        fill(graphics, x, y, x+width, y+height, backgroundColor);

        var clamped = Math.min(Math.max(progress, 0), 1);

        if (isVertical) {
            var progressEndPoint = y + height * (1-clamped);
            fill(graphics, x, progressEndPoint, x+width, y+height, progressColor);
        } else {
            var progressEndPoint = x + width * clamped;
            fill(graphics, x, y, progressEndPoint, y+height, progressColor);
        }
    }

    @Override
    public void parseProperties(UIModel model, Element element, Map<String, Element> children) {
        super.parseProperties(model, element, children);

        UIParsing.expectChildren(element, children, "is-vertical");
        UIParsing.apply(children, "is-vertical", UIParsing::parseBool, this::setVertical);

        UIParsing.apply(children, "background-color", Color::parseAndPack, this::setBackgroundColor);
        UIParsing.apply(children, "progress-color", Color::parseAndPack, this::setProgressColor);
        UIParsing.apply(children, "progress", UIParsing::parseFloat, this::setProgress);
    }

    private static void fill(OwoUIGraphics graphics, float x0, float y0, float x1, float y1, int color) {
        graphics.state.addSimpleElement(new RenderState(
                new Matrix3x2f(graphics.getMatrices()),
                graphics.scissorStack.peekLast(),
                x0, y0,
                x1, y1,
                color
        ));
    }

    private record RenderState(
            Matrix3x2fc pose,
            ScreenRect scissorArea,
            float x0, float y0,
            float x1, float y1,
            int color
    ) implements SimpleGuiElementRenderState {

        public void setupVertices(VertexConsumer vertices) {
            vertices.vertex(pose, x0, y0).color(color);
            vertices.vertex(pose, x0, y1).color(color);
            vertices.vertex(pose, x1, y1).color(color);
            vertices.vertex(pose, x1, y0).color(color);
        }

        @Override
        public RenderPipeline pipeline() {
            return RenderPipelines.GUI;
        }

        @Override
        public TextureSetup textureSetup() {
            return TextureSetup.empty();
        }

        @Override
        public ScreenRect bounds() {
            return new ScreenRect(
                    (int) x0,
                    (int) y0,
                    (int) Math.ceil(x1-x0),
                    (int) Math.ceil(y1-y0)
            ).transformEachVertex(this.pose);
        }
    }

}
