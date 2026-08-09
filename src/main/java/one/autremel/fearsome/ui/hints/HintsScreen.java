package one.autremel.fearsome.ui.hints;

import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.ui.DefaultBlurredSurface;
import one.autremel.fearsome.ui.components.HideableFlowLayout;
import one.autremel.fearsome.ui.components.LinearBar;
import one.autremel.fearsome.ui.OverlayScreen;

public class HintsScreen extends OverlayScreen {
    public static final String NAME = "hints_screen";
    public static final Identifier ID = FearsomeMod.uiIdentifier(NAME);

    private HideableFlowLayout actionBarRoot;
    private LabelComponent actionBarLabel;
    private LinearBar actionBar;

    public HintsScreen() {
        super(NAME);
    }

    public void setProgressBarVisible(boolean isVisible) {
        actionBarRoot.setVisible(isVisible);
    }

    public void setProgressBarText(Text text) {
        actionBarLabel.text(text);
    }

    public void setActionProgressAmount(float amount) {
        actionBar.setProgress(amount);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        super.init();
        if (this.uiAdapter == null) return;

        actionBarRoot = rootComponent.childById(HideableFlowLayout.class, "action_bar_root");
        DefaultBlurredSurface.setAsSurfaceFor(actionBarRoot);

        actionBarLabel = actionBarRoot.childById(LabelComponent.class, "action_bar_label");
        actionBar = actionBarRoot.childById(LinearBar.class, "action_bar");
    }
}
