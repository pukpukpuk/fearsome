package one.autremel.fearsome.ui.components;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import io.wispforest.owo.ui.base.BaseUIComponent;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.parsing.UIModel;
import io.wispforest.owo.ui.parsing.UIParsing;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import org.joml.Matrix3x2f;
import org.w3c.dom.Element;

import java.util.Map;

public class RadialProgressBar extends BaseUIComponent {
    @Getter @Setter
    private float progress;
    @Setter
    private int color;
    @Setter
    private float borderWidth;

    @Override
    public void draw(OwoUIGraphics graphics, int mouseX, int mouseY, float partialTicks, float delta) {
        graphics.state.addSimpleElement(new RenderState(
                new Matrix3x2f(graphics.getMatrices()),
                graphics.scissorStack.peekLast(),
                x(), y(), width(),
                borderWidth,
                color,
                progress
        ));
    }

    @Override
    public void parseProperties(UIModel model, Element element, Map<String, Element> children) {
        super.parseProperties(model, element, children);

        UIParsing.apply(children, "border-width", UIParsing::parseFloat, this::setBorderWidth);
    }

    private record RenderState(
            Matrix3x2f pose,
            ScreenRect scissorArea,
            int x0,
            int y0,
            int width,
            float borderWidth,
            int color,
            float progress
    ) implements SimpleGuiElementRenderState {
        private static float clamp(float v, float upperBound) {
            return Math.min(v, upperBound);
        }

        private static float clamp(float v, float lowerBound, float upperBound) {
            return Math.min(Math.max(v, lowerBound), upperBound);
        }

        @Override
        public void setupVertices(VertexConsumer vertices) {
            if (progress <= 0) return;
            if (isFull()) {
                setupAsFull(vertices);
                return;
            }

            setupNormally(vertices);
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
            return new ScreenRect(x0, y0, width, width).transformEachVertex(this.pose);
        }

        private boolean isFull() {
            return progress >= 1;
        }

        private void setupAsFull(VertexConsumer vertices) {
            vertices.vertex(this.pose, x0, y0).color(color);
            vertices.vertex(this.pose, x0, y0 + width).color(color);
            vertices.vertex(this.pose, x0 + width, y0 + width).color(color);
            vertices.vertex(this.pose, x0 + width, y0).color(color);
        }

        private void setupNormally(VertexConsumer vertices) {
            var p8 = progress * 8;

            if (drawUpperRightPart(vertices, p8)) return;
            if (drawRightPart(vertices, p8)) return;
            if (drawBottomPart(vertices, p8)) return;
            if (drawLeftPart(vertices, p8)) return;
            drawUpperLeftPart(vertices, p8);
        }

        private boolean drawUpperRightPart(VertexConsumer vertices, float p8) {
            var isFinal = p8 <= 1;

            var apothem = width / 2f;
            var xc = x0 + apothem;

            vertices.vertex(this.pose, xc, y0).color(color);
            vertices.vertex(this.pose, xc, y0+borderWidth).color(color);

            var x = xc + apothem * clamp(p8, 1);
            addSide(
                    vertices,
                    clamp(x, xc + apothem - borderWidth), y0+borderWidth,
                    x, y0,
                    isFinal
            );

            return isFinal;
        }

        private boolean drawRightPart(VertexConsumer vertices, float p8) {
            var isFinal = p8 <= 3;

            var y = y0 + width * clamp((p8 - 1) / 2f, 1);
            addSide(
                    vertices,
                    x0+width-borderWidth, clamp(y, y0+borderWidth, y0+width-borderWidth),
                    x0+width, y,
                    isFinal
            );

            return isFinal;
        }

        private boolean drawBottomPart(VertexConsumer vertices, float p8) {
            var isFinal = p8 <= 5;

            var x = x0 + width * Math.max(1 - ((p8 - 3) / 2f), 0);
            addSide(
                    vertices,
                    clamp(x, x0+borderWidth, x0+width-borderWidth), y0+width-borderWidth,
                    x, y0+width,
                    isFinal
            );

            return isFinal;
        }

        private boolean drawLeftPart(VertexConsumer vertices, float p8) {
            var isFinal = p8 <= 7;

            var y = y0 + width * Math.max(1 - (p8 - 5) / 2f, 0);
            addSide(
                    vertices,
                    x0+borderWidth, clamp(y, y0+borderWidth, y0+width-borderWidth),
                    x0, y,
                    isFinal
            );

            return isFinal;
        }

        private void drawUpperLeftPart(VertexConsumer vertices, float p8) {
            var apothem = width / 2f;
            var x = x0 + apothem * (p8 - 7);

            addSide(
                    vertices,
                    clamp(x, x0+borderWidth, x0 + apothem), y0+borderWidth,
                    x, y0,
                    true
            );
        }

        private void addSide(VertexConsumer vertices, float x1, float y1, float x2, float y2, boolean isFinal) {
            vertices.vertex(this.pose, x1, y1).color(color);
            vertices.vertex(this.pose, x2, y2).color(color);

            if (!isFinal) addSide(vertices, x2, y2, x1, y1, true);
        }
    }
}
