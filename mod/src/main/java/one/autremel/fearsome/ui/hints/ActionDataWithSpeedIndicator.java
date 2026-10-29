package one.autremel.fearsome.ui.hints;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import one.autremel.fearsome.Palette;
import org.jetbrains.annotations.Nullable;

public abstract class ActionDataWithSpeedIndicator extends ActionData {
    protected static final Style YELLOW = Palette.WARNING_STYLE;
    protected static final Style RED = Palette.ERROR_STYLE;

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
        if (speedState != null) screen.setProgressBarColor(speedState ? Palette.WARNING : Palette.ERROR);
    }
}
