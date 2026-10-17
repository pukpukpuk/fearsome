package one.autremel.fearsome.illusions;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;

import java.util.concurrent.atomic.AtomicInteger;

public class GlowingBlocks {
    private static final float DISPLAY_BLOCK_SCALE = 0.99f;
    private static final float OFFSET = (1 - DISPLAY_BLOCK_SCALE) / 2f;
    private static final AffineTransformation TRANSFORMATION = new AffineTransformation(
            null,
            null,
            new Vector3f(DISPLAY_BLOCK_SCALE, DISPLAY_BLOCK_SCALE, DISPLAY_BLOCK_SCALE),
            null
    );

    private static final AtomicInteger idCounter = new AtomicInteger(-1000);
    private static final Object2IntOpenHashMap<BlockPos> activeGlowingBlocks = new Object2IntOpenHashMap<>();

    public static void setGlowingGenerator(BlockPos pos) {
        setGlowing(pos, 0xFFFFAA00);
    }

    public static void setGlowing(BlockPos pos, int color) {
        var world = MinecraftClient.getInstance().world;
        if (world == null) return;

        removeGlowing(pos);

        var entityId = idCounter.getAndDecrement();
        var blockDisplay = new DisplayEntity.BlockDisplayEntity(EntityType.BLOCK_DISPLAY, world);
        blockDisplay.setId(entityId);
        blockDisplay.setPosition(pos.getX() + OFFSET, pos.getY() + OFFSET, pos.getZ() + OFFSET);
        blockDisplay.setBlockState(world.getBlockState(pos));
        blockDisplay.setGlowColorOverride(color);
        blockDisplay.setTransformation(TRANSFORMATION);
        world.addEntity(blockDisplay);

        GlowingEntities.setGlowing(entityId);

        activeGlowingBlocks.put(pos, entityId);
    }

    public static void removeGlowing(BlockPos pos) {
        var world = MinecraftClient.getInstance().world;
        if (world == null) return;

        var entityId = activeGlowingBlocks.removeInt(pos);
        if (entityId == activeGlowingBlocks.defaultReturnValue()) return;

        GlowingEntities.unsetGlowing(entityId);

        var entity = world.getEntityById(entityId);
        if (entity != null) entity.remove(Entity.RemovalReason.KILLED);
    }

    public static void reset() {
        activeGlowingBlocks.clear();
    }
}
