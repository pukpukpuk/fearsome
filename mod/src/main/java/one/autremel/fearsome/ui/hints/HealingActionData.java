package one.autremel.fearsome.ui.hints;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import one.autremel.fearsome.ui.hints.payloads.SetActionProgressParametersPayload;

public class HealingActionData extends ActionDataWithSpeedIndicator {
    public static final byte ID = 3;

    private static final int RECEIVER_COLOR = 0x77ab6d;
    private static final int WAITING_FOR_HELP_COLOR = ColorHelper.lerp(0.7f, RECEIVER_COLOR, 0x808080);
    private static final Role[] ROLES = Role.values();

    private final Text baseText;
    private final Text waitingForHelpText;

    private Role role = Role.HEALER;
    private Text text;

    public HealingActionData(int actionId, int priority, String text) {
        super(actionId, priority);
        this.baseText = Text.translatable(text);
        this.waitingForHelpText = Text.translatable(text + ".waiting_for_help");
        this.text = baseText;
    }

    @Override
    protected void handle(SetActionProgressParametersPayload payload) {
        var buf = new PacketByteBuf(Unpooled.wrappedBuffer(payload.data()));

        var flags = buf.readByte(); ///____ssrr
        role = ROLES[flags & 0b11];
        speedState = ((flags >> 2) & 1) == 0 ? null : ((flags >> 3) & 1) == 1;
        var targetName = buf.readString();

        text = switch (role) {
            case WAITING_FOR_HELP -> waitingForHelpText;
            case RECEIVER -> baseText;
            case HEALER -> {
                var healerText = Text.empty().append(baseText);
                if (!targetName.isEmpty()) healerText.append(": " + targetName);
                appendSpeedArrow(healerText);
                yield healerText;
            }
        };
    }

    @Override
    protected Text getText() {
        return text;
    }

    @Override
    protected void onUpdateInternal(HintsScreen screen) {
        super.onUpdateInternal(screen);

        switch (role) {
            case RECEIVER -> screen.setProgressBarColor(RECEIVER_COLOR);
            case WAITING_FOR_HELP -> screen.setProgressBarColor(WAITING_FOR_HELP_COLOR);
        }
    }

    private enum Role {
        HEALER,
        RECEIVER,
        WAITING_FOR_HELP
    }
}
