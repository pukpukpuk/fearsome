package one.autremel.fearsome.mixin;

import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import one.autremel.fearsome.FearsomeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ToastManager.class)
public class ToastManagerMixin {
    @Inject(
            method = "add",
            at = @At("HEAD"),
            cancellable = true
    )
    public void add(Toast toast, CallbackInfo ci) {
        if (!FearsomeMod.CONFIG.allowMessageVerifications()
                && Objects.equals(toast.getType(), SystemToast.Type.UNSECURE_SERVER_WARNING)) {
            ci.cancel();
        }
    }
}
