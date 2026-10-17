package one.autremel.fearsome.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.network.ping.ModPingController;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Shadow public abstract boolean isChatFocused();

    @Unique
    private static final int WIDTH = 140;
    @Unique
    private static final double
            SCALE = 0.75d,
            UNFOCUSED_HEIGHT = 0.3,
            FOCUSED_HEIGHT = 0.671;

    @Unique
    private static boolean isActive() {
        return ModPingController.isOnFearServer() && FearsomeMod.CONFIG.useChatSizePreset();
    }


    @Inject(
            method = "getWidth(D)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getWidth(double widthOption, CallbackInfoReturnable<Integer> cir) {
        if (isActive()) {
            cir.setReturnValue(WIDTH);
        }
    }

    @Inject(
            method = "getChatScale()D",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getChatScale(CallbackInfoReturnable<Double> cir) {
        if (isActive()) {
            cir.setReturnValue(SCALE);
        }
    }

    @Inject(
            method = "getHeight()I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getHeight(CallbackInfoReturnable<Integer> cir) {
        if (isActive()) {
            cir.setReturnValue(
                    ChatHud.getHeight(isChatFocused()
                            ? FOCUSED_HEIGHT
                            : UNFOCUSED_HEIGHT)
            );
        }
    }
}
