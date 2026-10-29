package one.autremel.fearsome;

import net.minecraft.util.math.MathHelper;

public class InterpolatedFloat {
    private final int durationMillis;
    private float start, target;
    private long interpolationStartTime;

    public InterpolatedFloat(int durationMillis, float start) {
        this.durationMillis = durationMillis;
        this.start = target = start;
    }

    public float get() {
        var passed = System.currentTimeMillis() - interpolationStartTime;
        var progress = MathHelper.clamp((float) passed / durationMillis, 0, 1);
        return MathHelper.lerp(progress, start, target);
    }

    public void setValue(float newTarget) {
        start = get();
        target = newTarget;
        interpolationStartTime = System.currentTimeMillis();
    }
}
