package one.autremel.fearsome.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @WrapMethod(method = "tickMovement")
    public void tickMovement(Operation<Void> original) {
        original.call();
    }

    @Shadow
    protected void tickHandSwing() {
    }
}
