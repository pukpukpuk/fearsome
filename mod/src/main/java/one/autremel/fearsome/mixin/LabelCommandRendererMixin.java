package one.autremel.fearsome.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.BatchingRenderCommandQueue;
import net.minecraft.client.render.command.LabelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import net.minecraft.text.Style;
import one.autremel.fearsome.input.VanillaActionsSuppressor;
import one.autremel.fearsome.mixinhelpers.ICommandsAccess;
import one.autremel.fearsome.network.ping.ModPingController;
import org.spongepowered.asm.mixin.Mixin;
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
        for (OrderedRenderCommandQueueImpl.LabelCommand labelCommand : commands.getNormalLabels()) {
            var text = labelCommand.text();
            var color = text.getStyle().getColor();
            if (color != null && color.getRgb() != -1) text = text.copy().setStyle(Style.EMPTY);

            renderer.draw(text, labelCommand.x(), labelCommand.y(), -1, true, labelCommand.matricesEntry(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, labelCommand.lightCoords());
        }
    }

}

