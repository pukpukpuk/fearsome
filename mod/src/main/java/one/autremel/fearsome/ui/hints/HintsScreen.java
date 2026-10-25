package one.autremel.fearsome.ui.hints;

import com.google.common.base.Preconditions;
import io.wispforest.owo.ui.component.LabelComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import net.minecraft.util.Identifier;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.input.BindingId;
import one.autremel.fearsome.ui.DefaultBlurredSurface;
import one.autremel.fearsome.ui.components.HideableFlowLayout;
import one.autremel.fearsome.ui.components.binding.ActionControlLabel;
import one.autremel.fearsome.ui.components.LinearBar;
import one.autremel.fearsome.ui.OverlayScreen;
import one.autremel.fearsome.ui.helpers.CollapseHelper;

import java.util.ArrayList;
import java.util.List;

public class HintsScreen extends OverlayScreen {
    public static final String NAME = "hints_screen";
    public static final Identifier ID = FearsomeMod.uiIdentifier(NAME);

    private HideableFlowLayout actionBarRoot;
    private LabelComponent actionBarLabel;
    private LinearBar actionBar;
    private io.wispforest.owo.ui.container.FlowLayout actionControlsList;

    private CollapseHelper actionBarCollapseHelper;
    private Language progressBarTextLanguage;

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

    public void setProgressVisible(boolean isVisible) {
        actionBarRoot.setVisible(isVisible);
    }

    public void setProgressBarCollapsed(boolean collapsed) {
        actionBarCollapseHelper.setCollapsed(collapsed);
    }

    public void setProgressBarText(Text text) {
        var language = Language.getInstance();
        if (text.equals(actionBarLabel.text()) && language == progressBarTextLanguage) return;

        progressBarTextLanguage = language;
        actionBarLabel.text(text);
    }

    public void setActionProgressAmount(float amount) {
        actionBar.setProgress(amount);
    }

    public void setProgressBarColor(int color) {
        actionBar.setProgressColor(color | 0xFF000000);
    }

    public void prepareForControlListRedraw() {
        for (var label : actionControlLabels) {
            label.setVisible(false);
        }
    }

    @Override
    protected void buildInternal(io.wispforest.owo.ui.container.FlowLayout rootComponent) {
        actionBarRoot = rootComponent.childById(HideableFlowLayout.class, "action_bar_root");
        DefaultBlurredSurface.setAsSurfaceFor(actionBarRoot);

        actionBarLabel = actionBarRoot.childById(LabelComponent.class, "action_bar_label");
        actionBar = actionBarRoot.childById(LinearBar.class, "action_bar");

        actionControlsList = rootComponent.childById(io.wispforest.owo.ui.container.FlowLayout.class, "action_controls");

        actionBarCollapseHelper = CollapseHelper.of(actionBar, true);
    }

}
