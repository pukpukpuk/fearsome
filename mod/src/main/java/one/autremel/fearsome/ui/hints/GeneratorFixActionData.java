package one.autremel.fearsome.ui.hints;

import io.netty.buffer.Unpooled;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import one.autremel.fearsome.ui.hints.payloads.SetActionProgressParametersPayload;

public final class GeneratorFixActionData extends ActionDataWithSpeedIndicator {
    public static final byte ID = 1;

    private static final Style LIGHT_BLUE = Style.EMPTY.withColor(0x7FE1FF);

    private final String prefix;

    private int displayedPercents;
    private boolean isSurvivorInfoDrawn;
    private int usedAccuracyPoints;
    private int ownAccuracyPoints;

    private boolean isTextUpdateRequired = true;
    private MutableText text;

    public GeneratorFixActionData(int actionId, int priority, String text) {
        super(actionId, priority);
        prefix = text;
    }

    @Override
    protected void handle(SetActionProgressParametersPayload payload) {
        var buf = Unpooled.wrappedBuffer(payload.data());
        displayedPercents = Byte.toUnsignedInt(buf.readByte());
        if (buf.isReadable()) {
            isSurvivorInfoDrawn = true;
            var data = buf.readShort();

            speedState = (data & 1) == 0 ? null : ((data & 2) != 0) ? Boolean.TRUE : Boolean.FALSE;
            ownAccuracyPoints = (data >> 2) & 127;
            usedAccuracyPoints = (data >> 9) & 127;
        } else {
            isSurvivorInfoDrawn = false;
            speedState = null;
        }

        isTextUpdateRequired = true;
    }

    @Override
    protected Text getText() {
        if (isTextUpdateRequired) {
            updateText();
            isTextUpdateRequired = false;
        }
        return text;
    }


    private void updateText() {
        text = Text.translatable(prefix);
        text.append(String.format(" (%s%%)", displayedPercents));
        if (!isSurvivorInfoDrawn) return;

        if (displayedPercents < 100 && usedAccuracyPoints > 0) {
            var remains = ownAccuracyPoints - usedAccuracyPoints;
            var accuracyStyle = remains <= 1 ? RED : (remains <= 5 ? YELLOW : LIGHT_BLUE);
            text.append(Text.literal(String.format(" [%s]", usedAccuracyPoints)).setStyle(accuracyStyle));
        }

        appendSpeedArrow(text);
    }
}
