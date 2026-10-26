package one.autremel.fearsome.mixin;

import io.wispforest.owo.ui.component.LabelComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LabelComponent.class, remap = false)
public abstract class LabelComponentMixin {
    @Unique
    @Nullable
    private Language wrappedLanguage;

    @Shadow
    public abstract Text text();

    @Shadow
    public abstract LabelComponent text(Text text);

    @Inject(
            method = "update",
            at = @At("HEAD")
    )
    private void rewrapOnLanguageChange(float delta, int mouseX, int mouseY, CallbackInfo ci) {
        var language = Language.getInstance();
        if (language == wrappedLanguage) return;

        var isFirstUpdate = wrappedLanguage == null;
        wrappedLanguage = language;
        if (!isFirstUpdate) text(text());
    }
}
