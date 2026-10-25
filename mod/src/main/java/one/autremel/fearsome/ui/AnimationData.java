package one.autremel.fearsome.ui;

import one.autremel.fearsome.Easings;

public record AnimationData(
        float start,
        float target,
        int duration,
        boolean isLinear,
        long startTime
) {
    public float getCurrentValue() {
        return (target - start) * getProgress() + start;
    }

    public boolean isEnded() {
        return getProgress() >= 0.99f;
    }

    private float getProgress() {
        var passed = System.currentTimeMillis() - startTime;
        if (passed >= duration) return 1;
        return isLinear ?
                (float) passed / duration :
                Easings.easeOut((float) (passed) / duration);
    }
}
