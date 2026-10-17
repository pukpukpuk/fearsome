package one.autremel.fearsome.ui.helpers;

import io.wispforest.owo.ui.component.LabelComponent;
import lombok.RequiredArgsConstructor;
import net.minecraft.text.Text;
import one.autremel.fearsome.Utils;

@RequiredArgsConstructor
public class TimeLabelHelper {
    private final LabelComponent label;

    private int displayedRemainingMillis = Integer.MIN_VALUE;

    public void setRemainingTime(int remainingMillis) {
        if (displayedRemainingMillis == remainingMillis) return;
        displayedRemainingMillis = remainingMillis;

        label.text(Text.of(Utils.convertTimeToText(remainingMillis)));
    }
}
