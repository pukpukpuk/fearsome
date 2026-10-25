package one.autremel.fearsome.mixin;

import net.minecraft.entity.decoration.DisplayEntity;
import one.autremel.fearsome.illusions.glowing.GlowingEntities;
import one.autremel.fearsome.network.ping.ModPingController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DisplayEntity.class)
public class DisplayEntityMixin {
    @Inject(
            method = "shouldRender(D)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    public void shouldRender(double distance, CallbackInfoReturnable<Boolean> cir) {
        if (!ModPingController.isOnFearServer()) return;

        var id = ((DisplayEntity) (Object) this).getId();
        if (GlowingEntities.isGlowed(id)) cir.setReturnValue(true);
    }
}
