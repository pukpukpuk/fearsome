package one.autremel.fearsome.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import one.autremel.fearsome.input.VanillaActionsSuppressor;
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

    @WrapOperation(
            method = "renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitLabel(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Vec3d;ILnet/minecraft/text/Text;ZIDLnet/minecraft/client/render/state/CameraRenderState;)V"
            )
    )
    private void submitPlayerLabelThroughWallsIfGlowing(OrderedRenderCommandQueue queue, MatrixStack matrices, Vec3d pos, int y, Text label,
                                                        boolean notSneaking, int light, double squaredDistance, CameraRenderState cameraState,
                                                        Operation<Void> original, @Local(argsOnly = true) PlayerEntityRenderState state) {
        if (VanillaActionsSuppressor.isVanillaThingsMustBeSuppressed()) notSneaking = state.hasOutline();
        original.call(queue, matrices, pos, y, label, notSneaking, light, squaredDistance, cameraState);
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
