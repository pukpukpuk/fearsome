package one.autremel.fearsome.mixin;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import one.autremel.fearsome.mixinhelpers.IClippingPassesHolder;
import one.autremel.fearsome.mixinhelpers.IManifestationProgressHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderManager.class)
public class EntityRenderManagerMixin {
    @Inject(
            method = "render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/render/state/CameraRenderState;DDDLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;)V",
            at = @At("HEAD")
    )
    private void applyManifestationClipping(EntityRenderState state, CameraRenderState cameraState, double x, double y, double z,
                                            MatrixStack matrices, OrderedRenderCommandQueue queue, CallbackInfo ci) {
        var holder = (IManifestationProgressHolder) state;
        var manifestation = holder.getManifestation();
        var progress = holder.getManifestationProgress();
        if (manifestation == null || Float.isNaN(progress)) return;

        var entry = matrices.peek();
        var passes = manifestation.createClippingPasses(entry.getPositionMatrix(), (float) y, state.height, progress);
        ((IClippingPassesHolder) (Object) entry).setClippingPasses(passes);
    }

    @Inject(
            method = "render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/render/state/CameraRenderState;DDDLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;)V",
            at = @At("RETURN")
    )
    private void resetManifestationClipping(EntityRenderState state, CameraRenderState cameraState, double x, double y, double z,
                                            MatrixStack matrices, OrderedRenderCommandQueue queue, CallbackInfo ci) {
        ((IClippingPassesHolder) (Object) matrices.peek()).setClippingPasses(null);
    }
}
