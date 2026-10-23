package one.autremel.fearsome.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.command.FallingBlockCommandRenderer;
import net.minecraft.client.util.math.MatrixStack;
import one.autremel.fearsome.illusions.glowing.GlowingBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FallingBlockCommandRenderer.class)
public class FallingBlockCommandRendererMixin {
    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/BlockRenderManager;renderBlockAsEntity(Lnet/minecraft/block/BlockState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V"
            )
    )
    private void renderBlockAsEntity(BlockRenderManager manager, BlockState blockState, MatrixStack matrices,
                                     VertexConsumerProvider vertexConsumers, int light, int overlay,
                                     Operation<Void> original) {
        if ((light & GlowingBlocks.OUTLINE_ONLY_LIGHT_MARKER) == 0) {
            original.call(manager, blockState, matrices, vertexConsumers, light, overlay);
            return;
        }

        if (!(vertexConsumers instanceof OutlineVertexConsumerProvider)) return;
        original.call(manager, blockState, matrices, vertexConsumers, light & ~GlowingBlocks.OUTLINE_ONLY_LIGHT_MARKER, overlay);
    }
}
