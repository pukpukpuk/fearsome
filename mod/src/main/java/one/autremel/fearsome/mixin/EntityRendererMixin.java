package one.autremel.fearsome.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import one.autremel.fearsome.Easings;
import one.autremel.fearsome.illusions.ghost.GhostManifestation;
import one.autremel.fearsome.mixinhelpers.IManifestationProgressHolder;
import one.autremel.fearsome.network.ping.ModPingController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(
            method = "updateRenderState(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/entity/state/EntityRenderState;F)V",
            at = @At("TAIL")
    )
    private void updateManifestationProgress(Entity entity, EntityRenderState state, float tickProgress, CallbackInfo ci) {
        var progress = ModPingController.isOnFearServer() ? GhostManifestation.getProgress(entity.getId()) : Float.NaN;
        ((IManifestationProgressHolder) state).setManifestationProgress(progress);
    }

    @Inject(
            method = "getShadowOpacity(Lnet/minecraft/client/render/entity/state/EntityRenderState;)F",
            at = @At("RETURN"),
            cancellable = true
    )
    private void fadeShadowWithManifestation(EntityRenderState state, CallbackInfoReturnable<Float> cir) {
        var progress = ((IManifestationProgressHolder) state).getManifestationProgress();
        if (Float.isNaN(progress)) return;

        cir.setReturnValue(cir.getReturnValue() * Easings.easeOut(progress));
    }
}
