package one.autremel.fearsome.ui.hints;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import one.autremel.fearsome.ui.hints.payloads.SetActionProgressParametersPayload;

public class HealingActionData extends ActionData {
    public static final byte ID = 3;

    private final Text baseText;
    private Text text;

    public HealingActionData(int actionId, int priority, String text) {
        super(actionId, priority);
        this.baseText = Text.translatable(text);
        this.text = baseText;
    }

    @Override
    protected void handle(SetActionProgressParametersPayload payload) {
        var buf = new PacketByteBuf(Unpooled.wrappedBuffer(payload.data()));
        var targetName = buf.readString();

        text = targetName.isEmpty()
                ? baseText
                : Text.empty().append(baseText).append(": " + targetName);
    }

    @Override
    protected Text getText() {
        return text;
    }
}
