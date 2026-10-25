package one.autremel.fearsome.mixin;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.texture.NativeImageBackedTexture;
import one.autremel.fearsome.render.CustomOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OverlayTexture.class)
public class OverlayTextureMixin {
    @Shadow
    @Final
    private NativeImageBackedTexture texture;

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/NativeImageBackedTexture;upload()V"
            )
    )
    private void paintCustomOverlays(CallbackInfo ci) {
        var image = texture.getImage();
        if (image == null) return;

        for (var overlay : CustomOverlay.values()) {
            image.setColorArgb(overlay.getColumn(), overlay.getRow(), overlay.getColor());
        }
    }
}
