package one.autremel.fearsome.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.command.LabelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.input.VanillaActionsSuppressor;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LabelCommandRenderer.Commands.class)
public class LabelCommandRendererCommandsMixin {
    private static final float VERTICAL_SHIFT = 12;
    private static final float SCALE_FACTOR = 0.65f;

    @Shadow @Final List<OrderedRenderCommandQueueImpl.LabelCommand> normalLabels;

    @Inject(
            method = "add",
            at = @At("HEAD"),
            cancellable = true
    )
    public void add(
            MatrixStack matrices, Vec3d pos, int y, Text label, boolean notSneaking, int light, double squaredDistanceToCamera, CameraRenderState cameraState,
            CallbackInfo ci) {
        if (!VanillaActionsSuppressor.isVanillaThingsMustBeSuppressed()) return;
        ci.cancel();

        if (pos != null) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            matrices.push();
            matrices.translate(pos.x, pos.y + (double)0.5F, pos.z);
            matrices.multiply(cameraState.orientation);
            matrices.scale(0.025F, -0.025F, 0.025F);
            matrices.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
            Matrix4f matrix4f = new Matrix4f(matrices.peek().getPositionMatrix());
            float f = (float)(-minecraftClient.textRenderer.getWidth(label)) / 2.0F;
            int i = (int)(minecraftClient.options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24;

            this.normalLabels.add(new OrderedRenderCommandQueueImpl.LabelCommand(matrix4f, f, (float)y + VERTICAL_SHIFT, label, light, -1, i, squaredDistanceToCamera));

            matrices.pop();
        }
    }
}
