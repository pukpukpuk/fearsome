package one.autremel.fearsome.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEquipment;
import net.minecraft.item.ItemStack;
import one.autremel.fearsome.FearsomeMod;
import one.autremel.fearsome.illusions.SelectedItemDisplayer;
import one.autremel.fearsome.input.VanillaActionsSuppressor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEquipment.class)
public class PlayerEquipmentMixin {
    @Shadow @Final private PlayerEntity player;

    @Inject(
            method = "get",
            at = @At("HEAD"),
            cancellable = true
    )
    public void get(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir) {
        if (!VanillaActionsSuppressor.isVanillaThingsMustBeSuppressed()) return;

        if (slot == EquipmentSlot.MAINHAND) {
            cir.setReturnValue(SelectedItemDisplayer.getItemFor(player.getId()));
        }
    }
}
