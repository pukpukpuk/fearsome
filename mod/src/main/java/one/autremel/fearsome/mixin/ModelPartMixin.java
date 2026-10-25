package one.autremel.fearsome.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import one.autremel.fearsome.mixinhelpers.IClippingPassesHolder;
import one.autremel.fearsome.render.clipping.ClippingVertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModelPart.class)
public class ModelPartMixin {
    @WrapOperation(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ModelPart;renderCuboids(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;III)V"
            )
    )
    private void clipCuboids(ModelPart part, MatrixStack.Entry entry, VertexConsumer vertexConsumer,
                             int light, int overlay, int color, Operation<Void> original) {
        var passes = ((IClippingPassesHolder) (Object) entry).getClippingPasses();
        if (passes == null) {
            original.call(part, entry, vertexConsumer, light, overlay, color);
            return;
        }

        for (var pass : passes) {
            var clipping = new ClippingVertexConsumer(vertexConsumer, pass);
            original.call(part, entry, clipping, light, overlay, color);
            clipping.commitVertices();
        }
    }
}
