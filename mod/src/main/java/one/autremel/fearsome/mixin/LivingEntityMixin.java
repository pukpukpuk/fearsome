package one.autremel.fearsome.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import one.autremel.fearsome.movement.ClimbingPreventer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @WrapMethod(method = "tickMovement")
    public void tickMovement(Operation<Void> original) {
        original.call();
    }

    @Inject(
            method = "applyClimbingSpeed",
            at = @At("RETURN"),
            cancellable = true
    )
    private void applyClimbingSpeed(Vec3d motion, CallbackInfoReturnable<Vec3d> cir) {
        var self = (LivingEntity) (Object) this;
        if (self != MinecraftClient.getInstance().player) return;
        if (!self.isClimbing() || !ClimbingPreventer.isPrevented()) return;

        var velocity = cir.getReturnValue();
        cir.setReturnValue(new Vec3d(velocity.x, 0, velocity.z));
    }

    @Shadow
    protected void tickHandSwing() {
    }
}
