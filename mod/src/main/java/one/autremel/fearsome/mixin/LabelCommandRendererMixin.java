package one.autremel.fearsome.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.BatchingRenderCommandQueue;
import net.minecraft.client.render.command.LabelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import one.autremel.fearsome.mixinhelpers.ICommandsAccess;
import one.autremel.fearsome.network.ping.ModPingController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LabelCommandRenderer.class)
public class LabelCommandRendererMixin {
    @Inject(
            method = "render",
            at = @At("HEAD"),
            cancellable = true
    )
    public void render(
            BatchingRenderCommandQueue queue, VertexConsumerProvider.Immediate vertexConsumers, TextRenderer renderer,
            CallbackInfo ci) {
        if (!ModPingController.isOnFearServer()) return;
        ci.cancel();

        var commands = (ICommandsAccess) queue.getLabelCommands();
        for (var labelCommand : commands.getSeethroughLabels()) {
            draw(renderer, labelCommand, vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH);
        }
        for (var labelCommand : commands.getNormalLabels()) {
            draw(renderer, labelCommand, vertexConsumers, TextRenderer.TextLayerType.NORMAL);
        }
    }

    @Unique
    private static void draw(TextRenderer renderer, OrderedRenderCommandQueueImpl.LabelCommand labelCommand,
                             VertexConsumerProvider vertexConsumers, TextRenderer.TextLayerType layerType) {
        renderer.draw(labelCommand.text(), labelCommand.x(), labelCommand.y(), -1, true,
                labelCommand.matricesEntry(), vertexConsumers, layerType, 0, labelCommand.lightCoords());
    }

}

