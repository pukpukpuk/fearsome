package one.autremel.fearsome.ui.hints;

import lombok.Getter;
import net.minecraft.text.Text;
import one.autremel.fearsome.ui.hints.payloads.SetActionProgressParametersPayload;

public class SimpleActionData extends ActionData {
    @Getter
    private final Text text;

    public SimpleActionData(int actionId, int priority, String text) {
        super(actionId, priority);
        this.text = Text.translatable(text);
    }

    @Override
    protected void handle(SetActionProgressParametersPayload payload) {
    }
}
