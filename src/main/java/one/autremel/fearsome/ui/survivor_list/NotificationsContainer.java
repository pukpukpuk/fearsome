package one.autremel.fearsome.ui.survivor_list;

import com.google.common.collect.Comparators;
import io.wispforest.owo.ui.base.BaseUIComponent;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.render.state.TextGuiElementRenderState;
import net.minecraft.text.Text;
import one.autremel.fearsome.ui.DefaultBlurredSurface;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.NonNull;

import java.util.SortedSet;
import java.util.TreeSet;

public class NotificationsContainer extends BaseUIComponent {
    private static final int GAP = 5;
    private static final int RIGHT_MARGIN = DefaultBlurredSurface.MARGIN * 3;

    private static final int TOTAL_DURATION = 5000;
    private static final int FADE_OUT_DURATION = 1000;
    private static final int FULL_ALPHA_DURATION = TOTAL_DURATION-FADE_OUT_DURATION;

    private final SortedSet<Notification> notifications = new TreeSet<>();

    private static int multiplyAlpha(int color, float m) {
        var a = (color >> 24) & 0xFF;
        var multipliedA = Math.round(a * m);
        multipliedA = Math.min(Math.max(multipliedA, 0), 0xFF);

        return (color & 0x00FFFFFF) | (multipliedA << 24);
    }

    public void add(Text text, int color) {
        var startTime = System.currentTimeMillis();
        notifications.add(new Notification(text, startTime, color));
    }

    public void update() {
        notifications.removeIf(notification -> {
            var passed = System.currentTimeMillis() - notification.startTime();
            return passed > TOTAL_DURATION;
        });
    }

    @Override
    public void draw(OwoUIGraphics graphics, int mouseX, int mouseY, float partialTicks, float delta) {
        var renderer = MinecraftClient.getInstance().textRenderer;

        var cursor = -RIGHT_MARGIN;
        for (var notification : notifications) {
            var width = renderer.getWidth(notification.text());
            cursor -= width;

            var passed = System.currentTimeMillis() - notification.startTime();
            var m = 1f;
            if (passed >= FULL_ALPHA_DURATION) {
                var k = (float)(passed - FULL_ALPHA_DURATION) / FADE_OUT_DURATION;
                m = -k*k + 1;
            }

            var color = multiplyAlpha(notification.color, m);

            graphics.state.addText(new TextGuiElementRenderState(
                    renderer,
                    notification.text().asOrderedText(),
                    new Matrix3x2f(graphics.getMatrices()),
                    this.x + cursor, this.y,
                    color,
                    0,
                    true,
                    false,
                    null
            ));

            cursor -= GAP;
        }
    }


    @Override
    protected int determineHorizontalContentSize(Sizing sizing) {
        return 0;
    }

    @Override
    protected int determineVerticalContentSize(Sizing sizing) {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }


    private record Notification(Text text, long startTime, int color) implements Comparable<Notification> {
        @Override
        public int compareTo(@NonNull Notification other) {
            var byTime = (int)(other.startTime - startTime);
            if (byTime != 0) return byTime;

            return text.hashCode() - other.hashCode();
        }
    }
}
