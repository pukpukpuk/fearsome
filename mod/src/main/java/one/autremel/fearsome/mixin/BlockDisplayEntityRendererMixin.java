package one.autremel.fearsome.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.DisplayEntityRenderer;
import net.minecraft.client.render.entity.state.BlockDisplayEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import one.autremel.fearsome.illusions.glowing.GlowingBlocks;
import one.autremel.fearsome.network.ping.ModPingController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DisplayEntityRenderer.BlockDisplayEntityRenderer.class)
public class BlockDisplayEntityRendererMixin {
    @WrapOperation(
            method = "render(Lnet/minecraft/client/render/entity/state/BlockDisplayEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitBlock(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/block/BlockState;III)V"
            )
    )
    private void submitBlock(OrderedRenderCommandQueue queue, MatrixStack matrices, BlockState blockState,
                             int light, int overlay, int outlineColor, Operation<Void> original,
                             @Local(argsOnly = true) BlockDisplayEntityRenderState renderState) {
        if (renderState.invisible && ModPingController.isOnFearServer()) {
            light |= GlowingBlocks.OUTLINE_ONLY_LIGHT_MARKER;
        }

        original.call(queue, matrices, blockState, light, overlay, outlineColor);
    }
}
