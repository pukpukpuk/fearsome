package one.autremel.fearsome.ui.terror;

import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.ui.OverlayScreen;

public class TerrorIndicator extends OverlayScreen {
    public static final String NAME = "terror_indicator";
    public static final Identifier ID = FearsomeMod.uiIdentifier(NAME);

    private static final Text[] FRAMES = new Text[] {
            Text.of("❤"),
            Text.of(" ❤"),
            Text.of("❤ ")
    };

    private LabelComponent heartLabel;

    public TerrorIndicator() {
        super(NAME);
    }

    public void setColor(Color color) {
        heartLabel.color(color);
    }

    public void setFrame(int index) {
        heartLabel.text(FRAMES[index]);
    }


    @Override
    protected void buildInternal(FlowLayout rootComponent) {
        heartLabel = rootComponent.childById(LabelComponent.class, "heart_label");
        setFrame(0);
    }
}
