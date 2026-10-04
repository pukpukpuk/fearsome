package one.autremel.fearsome.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import one.autremel.fearsome.input.VanillaActionsSuppressor;
import one.autremel.fearsome.network.ping.ModPingController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(
            method = "dropSelectedItem",
            at = @At("HEAD"),
            cancellable = true
    )
    public void dropSelectedItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        if (VanillaActionsSuppressor.isVanillaThingsMustBeSuppressed()) {
            cir.setReturnValue(false);
        }
    }
}
