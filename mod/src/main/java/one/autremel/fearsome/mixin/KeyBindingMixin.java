package one.autremel.fearsome.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import one.autremel.fearsome.input.KeyBindings;
import one.autremel.fearsome.network.ping.ModPingController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
    @Shadow
    protected InputUtil.Key boundKey;

    @Inject(
            method = "setBoundKey",
            at = @At("HEAD")
    )
    public void setBoundKey(InputUtil.Key boundKey, CallbackInfo ci) {
        if (!ModPingController.isOnFearServer()) return;

        if (this.boundKey != boundKey) {
            this.boundKey = boundKey;
            KeyBindings.onKeyBindingChanged((KeyBinding) (Object)this);
        }
    }
}
