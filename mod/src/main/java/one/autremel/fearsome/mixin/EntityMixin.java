package one.autremel.fearsome.mixin;

import net.minecraft.entity.Entity;
import one.autremel.fearsome.illusions.glowing.GlowingEntities;
import one.autremel.fearsome.network.ping.ModPingController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(
            method = "isGlowing",
            at = @At("HEAD"),
            cancellable = true
    )
    public void isGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (!ModPingController.isOnFearServer()) return;

        var entity = (Entity) (Object) this;
        var id = entity.getId();
        cir.setReturnValue(GlowingEntities.isGlowed(id));
    }

    @Inject(
            method = "getTeamColorValue",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getTeamColorValue(CallbackInfoReturnable<Integer> cir) {
        if (!ModPingController.isOnFearServer()) return;

        var color = GlowingEntities.getColor(((Entity) (Object) this).getId());
        if (color != null) cir.setReturnValue(color);
    }
}
