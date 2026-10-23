package one.autremel.fearsome.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.text.TranslatableTextContent;
import one.autremel.fearsome.ui.WrongVersionDisconnectScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLoginNetworkHandler.class)
public class ClientLoginNetworkHandlerMixin {
    @Shadow @Final
    private MinecraftClient client;

    @Inject(
            method = "onDisconnected",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onDisconnected(DisconnectionInfo info, CallbackInfo ci) {
        var reason = info.reason();
        if (!(reason.getContent() instanceof TranslatableTextContent content)) return;
        if (!content.getKey().equals(WrongVersionDisconnectScreen.REASON_KEY)) return;

        client.setScreen(new WrongVersionDisconnectScreen(reason));
        ci.cancel();
    }
}
