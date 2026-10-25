package one.autremel.fearsome.illusions.glowing;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.BlockPos;
import one.autremel.fearsome.network.NetworkController;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GlowingBlocks {
    public static final int OUTLINE_ONLY_LIGHT_MARKER = 0b1;

    private static final BlockState FALLBACK_BLOCK_STATE = Blocks.STONE.getDefaultState();

    private static boolean initialized;

    private static final AtomicInteger idCounter = new AtomicInteger(-1000);
    private static final HashMap<BlockPos, GlowingBlock> glowingBlocks = new HashMap<>();
    private static final HashMap<BlockPos, String> names = new HashMap<>();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        NetworkController.register(SetBlockGlowingPayload.ID, GlowingBlocks::handleSetPayload);
        NetworkController.register(SetGlowingBlockNamePayload.ID, GlowingBlocks::handleSetNamePayload);
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((client, world) -> forgetAll());
    }

    public static void setGlowing(Layer layer, BlockPos pos, int color) {
        var block = glowingBlocks.get(pos);
        if (block == null) {
            var display = spawnDisplay(pos);
            if (display == null) return;

            glowingBlocks.put(pos, block = new GlowingBlock(display));
        }

        block.colors.put(layer, color);
        updateDisplay(pos, block);
    }

    public static void removeGlowing(Layer layer, BlockPos pos) {
        var block = glowingBlocks.get(pos);
        if (block == null) return;

        block.colors.remove(layer);
        if (!block.colors.isEmpty()) {
            updateDisplay(pos, block);
            return;
        }

        glowingBlocks.remove(pos);
        GlowingEntities.unsetGlowing(block.display.getId());
        block.display.remove(Entity.RemovalReason.KILLED);
    }

    public static void reset() {
        glowingBlocks.clear();
        names.clear();
    }

    private static void forgetAll() {
        for (var block : glowingBlocks.values()) {
            GlowingEntities.unsetGlowing(block.display.getId());
        }
        reset();
    }

    @Nullable
    private static DisplayEntity.BlockDisplayEntity spawnDisplay(BlockPos pos) {
        var world = MinecraftClient.getInstance().world;
        if (world == null) return null;

        var display = new DisplayEntity.BlockDisplayEntity(EntityType.BLOCK_DISPLAY, world);
        display.setId(idCounter.getAndDecrement());
        display.setPosition(pos.getX(), pos.getY(), pos.getZ());
        display.setInvisible(true);

        var blockState = world.getBlockState(pos);
        display.setBlockState(blockState.getRenderType() == BlockRenderType.INVISIBLE ? FALLBACK_BLOCK_STATE : blockState);
        world.addEntity(display);

        return display;
    }

    private static void updateDisplay(BlockPos pos, GlowingBlock block) {
        var layers = Layer.values();
        for (int i = layers.length - 1; i >= 0; i--) {
            var color = block.colors.get(layers[i]);
            if (color == null) continue;

            block.display.setGlowColorOverride(color);
            GlowingEntities.setGlowing(block.display.getId(), color);
            break;
        }

        var name = names.get(pos);
        block.display.setCustomName(name != null ? Text.literal(name) : null);
        block.display.setCustomNameVisible(name != null);
    }

    private static void handleSetPayload(SetBlockGlowingPayload payload) {
        if (payload.isCleanupRequest()) {
            names.clear();
            for (var pos : new ArrayList<>(glowingBlocks.keySet())) {
                removeGlowing(Layer.SERVER, pos);
            }
            return;
        }

        var pos = payload.blockPosition();
        if (payload.color() != null) {
            setGlowing(Layer.SERVER, pos, payload.color());
            return;
        }

        names.remove(pos);
        removeGlowing(Layer.SERVER, pos);
    }

    private static void handleSetNamePayload(SetGlowingBlockNamePayload payload) {
        var pos = payload.blockPosition();
        if (payload.name() == null) names.remove(pos);
        else names.put(pos, payload.name());

        var block = glowingBlocks.get(pos);
        if (block != null) updateDisplay(pos, block);
    }


    private static class GlowingBlock {
        private final DisplayEntity.BlockDisplayEntity display;
        private final EnumMap<Layer, Integer> colors = new EnumMap<>(Layer.class);

        private GlowingBlock(DisplayEntity.BlockDisplayEntity display) {
            this.display = display;
        }
    }
}
