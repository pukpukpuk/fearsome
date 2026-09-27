package one.autremel.fearsome.ui.hints;

import net.minecraft.text.Text;
import one.autremel.fearsome.input.BindingId;

public record ActionControlMeta(
        BindingId bindingId,
        Text text,
        short priority
) {}
