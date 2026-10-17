package one.autremel.fearsome.mixin;

import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.network.ping.ModPingController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatHudLine.Visible.class)
public class ChatHudLineVisibleMixin {
    @Inject(
            method = "indicator",
            at = @At("HEAD"),
            cancellable = true
    )
    public void indicator(CallbackInfoReturnable<MessageIndicator> cir) {
        if (ModPingController.isOnFearServer() && !FearsomeMod.CONFIG.allowMessageVerifications()) {
            cir.setReturnValue(null);
        }
    }
}
