package one.autremel.fearsome.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.ItemCommandRenderer;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import one.autremel.fearsome.mixinhelpers.IClippingPassesHolder;
import one.autremel.fearsome.render.clipping.ClippingVertexConsumerProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ItemCommandRenderer.class)
public class ItemCommandRendererMixin {
    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II[ILjava/util/List;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V"
            )
    )
    private void clipItem(ItemDisplayContext displayContext, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                          int light, int overlay, int[] tintLayers, List<BakedQuad> quads, RenderLayer layer,
                          ItemRenderState.Glint glint, Operation<Void> original) {
        var passes = ((IClippingPassesHolder) (Object) matrices.peek()).getClippingPasses();
        if (passes == null) {
            original.call(displayContext, matrices, vertexConsumers, light, overlay, tintLayers, quads, layer, glint);
            return;
        }

        for (var pass : passes) {
            var clipping = new ClippingVertexConsumerProvider(vertexConsumers, pass);
            original.call(displayContext, matrices, clipping, light, overlay, tintLayers, quads, layer, glint);
            clipping.flush();
        }
    }
}
