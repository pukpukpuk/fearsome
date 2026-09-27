package one.autremel.fearsome.ui.hints;

import com.google.common.base.Preconditions;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import lombok.Getter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.input.BindingId;
import one.autremel.fearsome.ui.DefaultBlurredSurface;
import one.autremel.fearsome.ui.components.HideableFlowLayout;
import one.autremel.fearsome.ui.components.ActionControlLabel;
import one.autremel.fearsome.ui.components.LinearBar;
import one.autremel.fearsome.ui.OverlayScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HintsScreen extends OverlayScreen {
    public static final String NAME = "hints_screen";
    public static final Identifier ID = FearsomeMod.uiIdentifier(NAME);

    private FlowLayout rootComponent;
    private HideableFlowLayout actionBarRoot;
    private LabelComponent actionBarLabel;
    private LinearBar actionBar;
    private FlowLayout actionControlsList;

    private final List<ActionControlLabel> actionControlLabels = new ArrayList<>();

    public HintsScreen() {
        super(NAME);
    }

    public ActionControlLabel getLabel(int index) {
        Preconditions.checkArgument(index < BindingId.BINDINGS_COUNT);

        var toBeCreatedAmount = index - (actionControlLabels.size()-1);
        while (toBeCreatedAmount-- > 0) {
            var label = new ActionControlLabel();
            actionControlsList.child(label);

            label.setVisible(false);
            actionControlLabels.add(label);
        }

        return actionControlLabels.get(index);
    }

    public void setProgressBarVisible(boolean isVisible) {
        actionBarRoot.setVisible(isVisible);
    }

    public void setProgressBarText(Text text) {
        if (!text.equals(actionBarLabel.text())) {
            actionBarLabel.text(text);
        }
    }

    public void setActionProgressAmount(float amount) {
        actionBar.setProgress(amount);
    }

    public void prepareForControlListRedraw() {
        for (var label : actionControlLabels) {
            label.setVisible(false);
        }
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        super.init();
        if (this.uiAdapter == null) return;

        this.rootComponent = rootComponent;
        actionBarRoot = rootComponent.childById(HideableFlowLayout.class, "action_bar_root");
        DefaultBlurredSurface.setAsSurfaceFor(actionBarRoot);

        actionBarLabel = actionBarRoot.childById(LabelComponent.class, "action_bar_label");
        actionBar = actionBarRoot.childById(LinearBar.class, "action_bar");

        actionControlsList = rootComponent.childById(FlowLayout.class, "action_controls");
    }

}
