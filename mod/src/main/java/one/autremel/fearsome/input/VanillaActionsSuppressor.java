package one.autremel.fearsome.input;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import one.autremel.fearsome.network.ping.ModPingController;
import org.jspecify.annotations.Nullable;

import java.util.Set;

public class VanillaActionsSuppressor {
    private static final Set<String> BLOCKS_ALLOWED_FOR_INTERACTION = Set.of(
            "minecraft:birch_door",
            "minecraft:dark_oak_door",
            "minecraft:pale_oak_trapdoor",
            "minecraft:oak_door"
    );
    public static final Set<Class> DISALLOWED_SCREEN_TYPES = Set.of(
            AdvancementsScreen.class,
            InventoryScreen.class
    );


    private static boolean initialized;

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        AttackBlockCallback.EVENT.register(VanillaActionsSuppressor::onTryingToBreak);
        AttackEntityCallback.EVENT.register(VanillaActionsSuppressor::onTryingToAttack);
        UseBlockCallback.EVENT.register(VanillaActionsSuppressor::onTryingToUseBlock);
        UseEntityCallback.EVENT.register(VanillaActionsSuppressor::onTryingToUseEntity);
    }

    private static ActionResult onTryingToBreak(PlayerEntity player, World world, Hand hand, BlockPos blockPos, Direction direction) {
        if (!ModPingController.isOnFearServer()) return ActionResult.PASS;
        return ActionResult.FAIL;
    }

    private static ActionResult onTryingToAttack(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult result) {
        if (!ModPingController.isOnFearServer()) return ActionResult.PASS;
        return ActionResult.FAIL;
    }

    private static ActionResult onTryingToUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult result) {
        if (!ModPingController.isOnFearServer()) return ActionResult.PASS;

        var id = world.getBlockState(result.getBlockPos()).getBlock().asItem().toString();
        return BLOCKS_ALLOWED_FOR_INTERACTION.contains(id) ? ActionResult.PASS : ActionResult.FAIL;
    }

    private static ActionResult onTryingToUseEntity(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult result) {
        if (!ModPingController.isOnFearServer()) return ActionResult.PASS;

        return ActionResult.FAIL;
    }
}
