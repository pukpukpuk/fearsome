package one.autremel.fearsome.illusions.dream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalConnectingBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import one.autremel.fearsome.Easings;
import one.autremel.fearsome.illusions.LocalEntityIds;
import one.autremel.fearsome.illusions.glowing.GlowingEntities;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

class DreamPortal {
    private static final double MIN_ABS = 0.4;
    private static final Quaternionf LEFT_ROTATION = new Quaternionf(-0.5f, -0.5f, -0.5f, 0.5f);
    private static final Quaternionf RIGHT_ROTATION = new Quaternionf();
    private static final List<AffineTransformation> TRANSFORMS = List.of(
            createTransform(new Vector3f(-0.5f, 0, -0.5f),       new Vector3f(1)),
            createTransform(new Vector3f(-0.5f, -1.345f, -0.5f), new Vector3f(1, 1, 4.5f)),
            createTransform(new Vector3f(-0.5f, 0.8125f, -0.5f), new Vector3f(1))
    );
    private static final List<BlockState> CHARGED_BLOCK_STATES = createBlockStates(Blocks.GRAY_STAINED_GLASS_PANE);
    private static final List<BlockState> UNCHARGED_BLOCK_STATES = createBlockStates(Blocks.GLASS_PANE);

    private static final long DISCHARGE_DURATION = 700;

    private static final double
            RANDOM_SHIFT_HORIZONTAL = 0.4,
            RANDOM_SHIFT_VERTICAL = 0.275,
            RANDOM_SHIFT_PERPENDICULAR_HORIZONTAL = 0.2;
    private static final double
            MIN_SHAKE_STRENGTH = 0.002,
            MAX_SHAKE_STRENGTH = 0.005,
            PASSIVE_SHAKE_STRENGTH = 0.008;
    private static final long PASSIVE_SHAKE_PERIOD = 750;
    private static final long
            CHARGE_PARTICLES_PERIOD_MIN = 1000,
            CHARGE_PARTICLES_PERIOD_MAX = 50,
            CHARGE_PARTICLES_PERIOD_PASSIVE = 600;

    private static final int
            CHARGED_GLOW_COLOR = 0xFFAA00AA,
            CHARGED_FOCUSED_GLOW_COLOR = 0xFFFF55FF,
            UNCHARGED_GLOW_COLOR = 0xFFAAAAAA,
            UNCHARGED_FOCUSED_GLOW_COLOR = 0xFFFFFFFF;

    private final ClientWorld world;
    private final Vec3d origin;
    private final double yawCos, yawSin;
    private final List<DisplayEntity.BlockDisplayEntity> displays;

    private final Vec3d[] animationFrom, animationTo;

    private boolean isCharged;
    private boolean isCharging;
    private long dischargeEndTime;
    private long chargeStartTime, chargeEndTime;
    private long chargingSince;

    private long lastTimeChargeParticlesPlayed;
    private long lastTimePassiveShake;

    private boolean isGlowing, isFocused;

    DreamPortal(ClientWorld world, Vec3d origin, float yaw) {
        this.world = world;
        this.origin = origin;

        var radians = Math.toRadians((-yaw + 360f) % 360f);
        yawCos = Math.cos(radians);
        yawSin = Math.sin(radians);

        displays = new ArrayList<>(TRANSFORMS.size());
        for (var transform : TRANSFORMS) {
            var display = new DisplayEntity.BlockDisplayEntity(EntityType.BLOCK_DISPLAY, world);
            display.setId(LocalEntityIds.next());

            var position = randomShift(origin);
            display.refreshPositionAndAngles(position.x, position.y, position.z, yaw, 0);
            display.setTransformation(transform);

            world.addEntity(display);
            displays.add(display);
        }

        animationFrom = new Vec3d[displays.size()];
        animationTo = new Vec3d[displays.size()];

        updateBlockStates();
    }

    void setCharging(@Nullable Long chargeStartTime, long chargeEndTime) {
        var now = System.currentTimeMillis();
        this.chargeEndTime = chargeEndTime;
        if (isCharging) return;
        isCharging = true;
        chargingSince = chargeStartTime != null ? chargeStartTime : now;

        if (isCharged) {
            dischargeEndTime = now + DISCHARGE_DURATION;
            setupAnimation(false);
        } else {
            beginChargingAnimation(chargingSince);
        }
    }

    void setCharged() {
        var wasCharging = isCharging;
        isCharging = false;

        for (var display : displays) {
            display.setPosition(origin);
        }

        if (isCharged) return;
        setChargedState(true);
        if (wasCharging) spawnParticles(ParticleTypes.TRIAL_SPAWNER_DETECTION_OMINOUS, 0.3, 15, 0.3, 0.3, 0.3, 0);
    }

    boolean isChargeCompleted() {
        return isCharged && !isCharging;
    }

    float getChargeProgress(long currentTime) {
        if (!isCharging) return isCharged ? 1 : 0;

        var duration = Math.max(chargeEndTime - chargingSince, 1);
        return MathHelper.clamp((float) (currentTime - chargingSince) / duration, 0, 1);
    }

    void setGlowing(boolean isGlowing, boolean isFocused) {
        this.isGlowing = isGlowing;
        this.isFocused = isFocused;
        updateGlow();
    }

    void tick(long currentTime) {
        if (isCharging) tickCharging(currentTime);
        else if (isCharged) tickPassive(currentTime);
    }

    void remove() {
        forget();
        for (var display : displays) {
            display.remove(Entity.RemovalReason.KILLED);
        }
    }

    void forget() {
        for (var display : displays) {
            GlowingEntities.unsetGlowing(display.getId());
        }
    }


    private void tickCharging(long currentTime) {
        if (isCharged) {
            if (currentTime < dischargeEndTime) {
                var linearProgress = 1 - (float) (dischargeEndTime - currentTime) / DISCHARGE_DURATION;
                var progress = Easings.easeOut(linearProgress);
                setAnimationProgress(progress);
                shake(MathHelper.lerp(progress, MIN_SHAKE_STRENGTH, MAX_SHAKE_STRENGTH));
                return;
            }

            setAnimationProgress(1);
            setChargedState(false);
            beginChargingAnimation(currentTime);
        }

        var duration = Math.max(chargeEndTime - chargeStartTime, 1);
        var linearProgress = MathHelper.clamp((float) (currentTime - chargeStartTime) / duration, 0, 1);
        var progress = linearProgress * linearProgress;

        setAnimationProgress(progress);
        shake(MathHelper.lerp(1 - progress, MIN_SHAKE_STRENGTH, MAX_SHAKE_STRENGTH));

        var particlesPeriod = (long) MathHelper.lerp(linearProgress, CHARGE_PARTICLES_PERIOD_MIN, CHARGE_PARTICLES_PERIOD_MAX);
        tryPlayChargeParticles(currentTime, particlesPeriod);
    }

    private void tickPassive(long currentTime) {
        if (currentTime - lastTimePassiveShake >= PASSIVE_SHAKE_PERIOD) {
            lastTimePassiveShake = currentTime;
            for (var display : displays) {
                display.setPosition(addRandomShift(origin, PASSIVE_SHAKE_STRENGTH, PASSIVE_SHAKE_STRENGTH, PASSIVE_SHAKE_STRENGTH));
            }
        }

        tryPlayChargeParticles(currentTime, CHARGE_PARTICLES_PERIOD_PASSIVE);
    }

    private void beginChargingAnimation(long now) {
        chargeStartTime = now;
        setupAnimation(true);
    }

    private void setupAnimation(boolean charging) {
        for (int i = 0; i < displays.size(); i++) {
            animationFrom[i] = displays.get(i).getEntityPos();
            animationTo[i] = charging ? origin : randomShift(origin);
        }
    }

    private void setAnimationProgress(float progress) {
        for (int i = 0; i < displays.size(); i++) {
            displays.get(i).setPosition(animationFrom[i].lerp(animationTo[i], progress));
        }
    }

    private void shake(double strength) {
        if (strength < 0.001) return;

        for (var display : displays) {
            display.setPosition(addRandomShift(display.getEntityPos(), strength, strength, strength));
        }
    }

    private void tryPlayChargeParticles(long now, long period) {
        if (now - lastTimeChargeParticlesPlayed < period) return;
        lastTimeChargeParticlesPlayed = now;

        spawnParticles(ParticleTypes.MYCELIUM, 1, 3, 0.25, 0.45, 0.25, 1);
    }

    private void spawnParticles(ParticleEffect effect, double yOffset, int count, double dx, double dy, double dz, double speed) {
        var random = world.getRandom();
        for (int i = 0; i < count; i++) {
            world.addParticleClient(effect,
                    origin.x + random.nextGaussian() * dx,
                    origin.y + yOffset + random.nextGaussian() * dy,
                    origin.z + random.nextGaussian() * dz,
                    random.nextGaussian() * speed,
                    random.nextGaussian() * speed,
                    random.nextGaussian() * speed);
        }
    }

    private void setChargedState(boolean state) {
        isCharged = state;
        updateBlockStates();
        updateGlow();
    }

    private void updateBlockStates() {
        var blockStates = isCharged ? CHARGED_BLOCK_STATES : UNCHARGED_BLOCK_STATES;
        for (int i = 0; i < displays.size(); i++) {
            displays.get(i).setBlockState(blockStates.get(i));
        }
    }

    private void updateGlow() {
        var color = isCharged
                ? (isFocused ? CHARGED_FOCUSED_GLOW_COLOR : CHARGED_GLOW_COLOR)
                : (isFocused ? UNCHARGED_FOCUSED_GLOW_COLOR : UNCHARGED_GLOW_COLOR);

        for (var display : displays) {
            if (!isGlowing) {
                GlowingEntities.unsetGlowing(display.getId());
                continue;
            }

            display.setGlowColorOverride(color);
            GlowingEntities.setGlowing(display.getId(), color);
        }
    }

    private Vec3d randomShift(Vec3d position) {
        return addRandomShift(position, RANDOM_SHIFT_HORIZONTAL, RANDOM_SHIFT_VERTICAL, RANDOM_SHIFT_PERPENDICULAR_HORIZONTAL);
    }

    private Vec3d addRandomShift(Vec3d position, double sx, double sy, double sz) {
        var x = sx * random();
        var y = sy * randomPositive();
        var z = sz * random();

        var rotatedX = yawCos * x + yawSin * z;
        var rotatedZ = -yawSin * x + yawCos * z;

        return position.add(rotatedX, y, rotatedZ);
    }

    private static double random() {
        var r = Math.random() * 2 - 1;
        var sign = r >= 0 ? 1 : -1;

        return r * (1 - MIN_ABS) + MIN_ABS * sign;
    }

    private static double randomPositive() {
        return Math.random() * (1 - MIN_ABS) + MIN_ABS;
    }

    private static AffineTransformation createTransform(Vector3f translation, Vector3f scale) {
        return new AffineTransformation(translation, LEFT_ROTATION, scale, RIGHT_ROTATION);
    }

    private static List<BlockState> createBlockStates(Block pane) {
        var state = pane.getDefaultState();
        return List.of(
                state.with(HorizontalConnectingBlock.NORTH, true),
                state,
                state.with(HorizontalConnectingBlock.SOUTH, true)
        );
    }
}
