package one.autremel.fearsome.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.decoration.MannequinEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MannequinEntity.class)
public class MannequinMixin extends LivingEntityMixin {
    @Override
    public void tickMovement(Operation<Void> original) {
        super.tickMovement(original);
        tickHandSwing();
    }
}
