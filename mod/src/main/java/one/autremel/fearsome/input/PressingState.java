package one.autremel.fearsome.input;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

class PressingState {
    private final List<Runnable> onPressing = new ArrayList<>();
    private final List<BooleanConsumer> onToggle = new ArrayList<>();

    @Getter @Setter
    private boolean isPressedLastTick;

    public void registerOnPressing(Runnable runnable) {
        onPressing.add(runnable);
    }

    public void registerOnToggle(BooleanConsumer consumer) {
        onToggle.add(consumer);
    }

    public void callOnPressing() {
        onPressing.forEach(Runnable::run);
    }

    public void callOnToggle(boolean currentState) {
        onToggle.forEach(c -> c.accept(currentState));
    }
}
