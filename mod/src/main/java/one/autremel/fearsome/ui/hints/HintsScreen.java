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
    private static final String CONTROL_CARD_ID = "control_card@fearsome:"+NAME;

    private HideableFlowLayout actionBarRoot;
    private LabelComponent actionBarLabel;
    private LinearBar actionBar;
    private FlowLayout actionControlsList;

    public HintsScreen() {
        super(NAME);
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

    @Override
    protected void build(FlowLayout rootComponent) {
        super.init();
        if (this.uiAdapter == null) return;

        actionBarRoot = rootComponent.childById(HideableFlowLayout.class, "action_bar_root");
        DefaultBlurredSurface.setAsSurfaceFor(actionBarRoot);

        actionBarLabel = actionBarRoot.childById(LabelComponent.class, "action_bar_label");
        actionBar = actionBarRoot.childById(LinearBar.class, "action_bar");

        actionControlsList = rootComponent.childById(FlowLayout.class, "action_controls");

    }

//            model.expandTemplate(
//    FlowLayout.class,
//    CONTROL_CARD_ID,
//            Map.of("action_name", "Текст")
//            ),

//    private void zalupa(FlowLayout list, FlowLayout flowLayout, String bindingKey) { TODO
//        var label = flowLayout.childById(KeyBindLabel.class, "key_bind_label");
//        label.setBindingKey(bindingKey);
//        list.child(flowLayout);
//    }
}
