package one.autremel.fearsome.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import one.autremel.fearsome.input.VanillaActionsSuppressor;
import one.autremel.fearsome.network.ping.ModPingController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(
            method = "setScreen",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onSetScreen(Screen screen, CallbackInfo ci) {
        if (!VanillaActionsSuppressor.isVanillaThingsMustBeSuppressed()) return;

        if (screen != null && VanillaActionsSuppressor.DISALLOWED_SCREEN_TYPES.contains(screen.getClass())) {
            ci.cancel();
        }
    }
}
