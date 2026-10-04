package one.autremel.fearsome.input;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.MinecraftClient;
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
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import one.autremel.fearsome.network.ping.ModPingController;
import org.jspecify.annotations.Nullable;

import java.util.Set;

public class VanillaActionsSuppressor {
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

    public static boolean isVanillaThingsMustBeSuppressed() {
        if (!ModPingController.isOnFearServer()) return false;

        var gameMode = MinecraftClient.getInstance().player.getGameMode();
        return gameMode != GameMode.CREATIVE && gameMode != GameMode.SPECTATOR;
    }

    private static ActionResult baseFailCondition() {
        return isVanillaThingsMustBeSuppressed() ? ActionResult.FAIL : ActionResult.PASS;
    }

    private static ActionResult onTryingToBreak(PlayerEntity player, World world, Hand hand, BlockPos blockPos, Direction direction) {
        return baseFailCondition();
    }

    private static ActionResult onTryingToAttack(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult result) {
        return baseFailCondition();
    }

    private static ActionResult onTryingToUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult result) {
        return baseFailCondition();
    }

    private static ActionResult onTryingToUseEntity(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult result) {
        return baseFailCondition();
    }
}
