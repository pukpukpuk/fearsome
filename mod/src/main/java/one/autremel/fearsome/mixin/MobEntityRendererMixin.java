package one.autremel.fearsome.mixin;

import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.mob.MobEntity;
import one.autremel.fearsome.network.ping.ModPingController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntityRenderer.class)
public abstract class MobEntityRendererMixin<T extends MobEntity> extends LivingEntityRendererMixin<T>{
    @Inject(
            method = "hasLabel(Lnet/minecraft/entity/mob/MobEntity;D)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void hasLabelPlayerEntity(T playerLikeEntity, double d, CallbackInfoReturnable<Boolean> cir) {
        if (!ModPingController.isOnFearServer()) return;
        cir.setReturnValue(super.hasLabelImpl(playerLikeEntity, d));
    }
}
