package one.autremel.fearsome.ui.terror;

import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.core.Color;
import lombok.RequiredArgsConstructor;
import net.minecraft.text.Text;
import one.autremel.fearsome.ui.components.HideableFlowLayout;

@RequiredArgsConstructor
public class TerrorPart {
    private static final Text[] FRAMES = new Text[] {
            Text.of("❤"),
            Text.of(" ❤"),
            Text.of("❤ ")
    };

    private final HideableFlowLayout root;
    private final LabelComponent heartLabel;

    public static TerrorPart initialize(HideableFlowLayout root) {
        var heartLabel = root.childById(LabelComponent.class, "heart_label");

        var part = new TerrorPart(root, heartLabel);
        part.setFrame(0);
        return part;
    }

    void setVisible(boolean isVisible) {
        root.setVisible(isVisible);
    }

    void setColor(Color color) {
        heartLabel.color(color);
    }

    void setFrame(int index) {
        heartLabel.text(FRAMES[index]);
    }
}
