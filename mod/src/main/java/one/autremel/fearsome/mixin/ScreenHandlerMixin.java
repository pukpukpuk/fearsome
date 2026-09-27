package one.autremel.fearsome.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import one.autremel.fearsome.network.ping.ModPingController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Inject(
            method = "onSlotClick",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (ModPingController.isOnFearServer()) {
            ci.cancel();
        }
    }
}
