package one.autremel.fearsome.ui.hints;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public abstract class ActionDataWithSpeedIndicator extends ActionData {
    protected static final int YELLOW_COLOR = 0xF2F27B;
    protected static final int RED_COLOR = 0xE29083;
    protected static final Style YELLOW = Style.EMPTY.withColor(YELLOW_COLOR);
    protected static final Style RED = Style.EMPTY.withColor(RED_COLOR);

    @Nullable
    protected Boolean speedState;

    public ActionDataWithSpeedIndicator(int actionId, int priority) {
        super(actionId, priority);
    }

    protected void appendSpeedArrow(MutableText text) {
        if (speedState == null) return;

        if (speedState) text.append(Text.literal(" ⏩").setStyle(YELLOW));
        else text.append(Text.literal(" ⏪").setStyle(RED));
    }

    @Override
    protected void onUpdateInternal(HintsScreen screen) {
        super.onUpdateInternal(screen);
        if (speedState != null) screen.setProgressBarColor(speedState ? YELLOW_COLOR : RED_COLOR);
    }
}
