package one.autremel.fearsome.ui;

public record AnimationData(
        float start,
        float target,
        int duration,
        boolean isLinear,
        long startTime
) {
    public static float easeOut(float x) {
        return (float)(1d - Math.pow(1 - x, 2));
    }

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
                easeOut((float) (passed) / duration);
    }
}
