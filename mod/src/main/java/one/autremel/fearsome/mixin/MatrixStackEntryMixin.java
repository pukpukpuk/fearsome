package one.autremel.fearsome.mixin;

import net.minecraft.client.util.math.MatrixStack;
import one.autremel.fearsome.mixinhelpers.IClippingPassesHolder;
import one.autremel.fearsome.render.clipping.ClippingPass;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(MatrixStack.Entry.class)
public class MatrixStackEntryMixin implements IClippingPassesHolder {
    @Unique
    @Nullable
    private List<ClippingPass> clippingPasses;

    @Override
    public @Nullable List<ClippingPass> getClippingPasses() {
        return clippingPasses;
    }

    @Override
    public void setClippingPasses(@Nullable List<ClippingPass> passes) {
        clippingPasses = passes;
    }

    @Inject(
            method = "copy(Lnet/minecraft/client/util/math/MatrixStack$Entry;)V",
            at = @At("TAIL")
    )
    private void copyClippingPassesFrom(MatrixStack.Entry other, CallbackInfo ci) {
        clippingPasses = ((IClippingPassesHolder) (Object) other).getClippingPasses();
    }

    @Inject(
            method = "copy()Lnet/minecraft/client/util/math/MatrixStack$Entry;",
            at = @At("RETURN")
    )
    private void copyClippingPassesTo(CallbackInfoReturnable<MatrixStack.Entry> cir) {
        ((IClippingPassesHolder) (Object) cir.getReturnValue()).setClippingPasses(clippingPasses);
    }
}
