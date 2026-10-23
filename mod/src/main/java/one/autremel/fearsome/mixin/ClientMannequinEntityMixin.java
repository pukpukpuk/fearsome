package one.autremel.fearsome.mixin;

import net.minecraft.client.network.ClientMannequinEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.SkinTextures;
import one.autremel.fearsome.illusions.SkinOverrides;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientMannequinEntity.class)
public class ClientMannequinEntityMixin {
    @Inject(
            method = "getSkin",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getSkin(CallbackInfoReturnable<SkinTextures> cir) {
        var skin = SkinOverrides.get(((Entity) (Object) this).getId());
        if (skin != null) cir.setReturnValue(skin);
    }
}
