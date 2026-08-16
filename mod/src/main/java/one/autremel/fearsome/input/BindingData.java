package one.autremel.fearsome.input;

import net.minecraft.client.option.KeyBinding;

import java.util.Objects;

public record BindingData(BindingId id, KeyBinding keyBinding) {

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BindingData that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
