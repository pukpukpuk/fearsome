package one.autremel.fearsome.mixin;

import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.PlayerLikeEntity;
import one.autremel.fearsome.network.ping.ModPingController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin<AvatarlikeEntity extends PlayerLikeEntity> extends LivingEntityRendererMixin<AvatarlikeEntity>{

    @Inject(
            method = "updateRenderState(Lnet/minecraft/entity/PlayerLikeEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V",
            at = @At("TAIL")
    )
    public void updateRenderState(AvatarlikeEntity playerLikeEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo ci) {
        if (!ModPingController.isOnFearServer()) return;
        playerEntityRenderState.playerName = null;
    }

    @Inject(
            method = "hasLabel(Lnet/minecraft/entity/PlayerLikeEntity;D)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void hasLabelPlayerEntity(AvatarlikeEntity playerLikeEntity, double d, CallbackInfoReturnable<Boolean> cir) {
        if (!ModPingController.isOnFearServer()) return;
        cir.setReturnValue(super.hasLabelImpl(playerLikeEntity, d));
    }
}
