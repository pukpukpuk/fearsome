package one.autremel.fearsome.input;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import lombok.Data;

@Data
class KeyData {
    private Runnable onPressing;
    private BooleanConsumer onToggle;

    private boolean isPressedLastTick;
}
