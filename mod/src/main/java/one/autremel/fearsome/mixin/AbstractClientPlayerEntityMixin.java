package one.autremel.fearsome.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import one.autremel.fearsome.input.VanillaActionsSuppressor;
import one.autremel.fearsome.network.ping.ModPingController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {
    @Unique
    private static final float SPRINT_FOV_MULTIPLIER = 1.15f;

    @Inject(
            method = "getFovMultiplier",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getFovMultiplier(boolean firstPerson, float fovEffectScale, CallbackInfoReturnable<Float> cir) {
        if (!VanillaActionsSuppressor.isVanillaThingsMustBeSuppressed()) return;

        var player = (AbstractClientPlayerEntity) (Object) this;
        var multiplier = player.isSprinting() ? SPRINT_FOV_MULTIPLIER : 1f;
        cir.setReturnValue(MathHelper.lerp(fovEffectScale, 1f, multiplier));
    }
}
