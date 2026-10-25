package one.autremel.fearsome.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import one.autremel.fearsome.Easings;
import one.autremel.fearsome.illusions.NameDisplayer;
import one.autremel.fearsome.input.VanillaActionsSuppressor;
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
            method = "getDisplayName(Lnet/minecraft/entity/Entity;)Lnet/minecraft/text/Text;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void decorateDisplayName(Entity entity, CallbackInfoReturnable<Text> cir) {
        if (!ModPingController.isOnFearServer()) return;

        var decorated = NameDisplayer.decorate(entity.getId(), entity.getName());
        if (decorated != null) cir.setReturnValue(decorated);
    }

    @WrapOperation(
            method = "renderLabelIfPresent",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitLabel(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Vec3d;ILnet/minecraft/text/Text;ZIDLnet/minecraft/client/render/state/CameraRenderState;)V"
            )
    )
    private void submitLabelThroughWallsIfGlowing(OrderedRenderCommandQueue queue, MatrixStack matrices, Vec3d pos, int y, Text label,
                                                  boolean notSneaking, int light, double squaredDistance, CameraRenderState cameraState,
                                                  Operation<Void> original, @Local(argsOnly = true) EntityRenderState state) {
        if (VanillaActionsSuppressor.isVanillaThingsMustBeSuppressed()) notSneaking = state.hasOutline();
        original.call(queue, matrices, pos, y, label, notSneaking, light, squaredDistance, cameraState);
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
