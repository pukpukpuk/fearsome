package one.autremel.fearsome.ui.hints;

import io.netty.buffer.Unpooled;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import one.autremel.fearsome.ui.components.LinearBar;
import one.autremel.fearsome.ui.hints.payloads.SetActionProgressParametersPayload;

public class GeneratorFixActionData extends ActionData {
    public static final byte ID = 1;

    private static final int YELLOW_COLOR = 0xF2F27B;
    private static final int RED_COLOR = 0xE29083;
    private static final Style LIGHT_BLUE = Style.EMPTY.withColor(0x7FE1FF);
    private static final Style YELLOW = Style.EMPTY.withColor(YELLOW_COLOR);
    private static final Style RED = Style.EMPTY.withColor(RED_COLOR);

    private final String prefix;

    private int displayedPercents;
    private boolean isSurvivorInfoDrawn;
    private Boolean isFixingSpeeded;
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

            isFixingSpeeded = (data & 1) == 0 ? null : ((data & 2) != 0) ? Boolean.TRUE : Boolean.FALSE;
            ownAccuracyPoints = (data >> 2) & 127;
            usedAccuracyPoints = (data >> 9) & 127;
        } else {
            isSurvivorInfoDrawn = false;
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

    @Override
    protected void onUpdateInternal(HintsScreen screen) {
        super.onUpdateInternal(screen);
        if (!isSurvivorInfoDrawn) return;

        if (isFixingSpeeded != null) {
            if (isFixingSpeeded) {
                screen.setProgressBarColor(YELLOW_COLOR);
            } else {
                screen.setProgressBarColor(RED_COLOR);
            }
        }
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

        if (isFixingSpeeded != null) {
            if (isFixingSpeeded) {
                text.append(Text.literal(" ⏩").setStyle(YELLOW));
            } else {
                text.append(Text.literal(" ⏪").setStyle(RED));
            }
        }

    }
}
