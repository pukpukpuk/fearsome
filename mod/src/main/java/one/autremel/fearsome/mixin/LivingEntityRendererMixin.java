package one.autremel.fearsome.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import one.autremel.fearsome.illusions.NameDisplayer;
import one.autremel.fearsome.input.VanillaActionsSuppressor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
    @Unique
    public boolean hasLabelImpl(T livingEntity, double d) {
        var client = MinecraftClient.getInstance();
        return NameDisplayer.isNameVisible(livingEntity.getId())
                && MinecraftClient.isHudEnabled()
                && livingEntity != client.getCameraEntity()
                && !livingEntity.isInvisibleTo(client.player)
                && !livingEntity.hasPassengers();
    }

    @Inject(
            method = "hasLabel(Lnet/minecraft/entity/LivingEntity;D)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void hasLabelLivingEntity(T livingEntity, double d, CallbackInfoReturnable<Boolean> cir) {
        if (!VanillaActionsSuppressor.isVanillaThingsMustBeSuppressed()) return;

        cir.setReturnValue(hasLabelImpl(livingEntity, d));
    }
}
