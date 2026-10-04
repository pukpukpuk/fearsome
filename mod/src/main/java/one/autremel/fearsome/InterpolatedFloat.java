package one.autremel.fearsome;

import org.joml.Math;

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
        var progress = org.joml.Math.min(org.joml.Math.max(passed / durationMillis, 0), 1);
        return Math.lerp(start, target, progress);
    }

    public void setValue(float newTarget) {
        start = get();
        target = newTarget;
        interpolationStartTime = System.currentTimeMillis();
    }
}
